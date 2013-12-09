package edu.ggc.it.banner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * CourseDataSource provides indirect access to the information contained in the class search
 * database. Based on the design at {@link http://www.vogella.com/articles/AndroidSQLite/article.html}.
 * 
 * @author Jacob
 *
 */
public class CourseDataSource
{
	private SQLiteDatabase db;
	private CourseDB dbHelper;
	
	private static final String[] CATALOG_ID = new String[]{CourseDB.Catalog.COL_ID};
	private static final String[] INSTRUCTOR_ID = new String[]{CourseDB.Instructors.COL_ID};
	private static final String[] CLASS_TYPES_ID = new String[]{CourseDB.ClassTypes.COL_ID};
	
	private static final String[] COURSE_COLUMNS = new String[]{
		CourseDB.Catalog.COL_ID,
		CourseDB.Catalog.COL_CODE,
		CourseDB.Catalog.COL_DESC,
		CourseDB.Catalog.COL_HOURS,
		CourseDB.Catalog.COL_SUBJ,
		CourseDB.Catalog.COL_TITLE
	};
	private static final String[] SECTION_COLUMNS = new String[]{
		CourseDB.Schedule.COL_ID,
		CourseDB.Schedule.COL_COURSE,
		CourseDB.Schedule.COL_CRN,
		CourseDB.Schedule.COL_SECT,
		CourseDB.Schedule.COL_TERM
	};
	private static final String[] INSTRUCTOR_COLUMNS = new String[]{
		CourseDB.Instructors.COL_ID,
		CourseDB.Instructors.COL_EMAIL,
		CourseDB.Instructors.COL_NAME
	};
	
	public CourseDataSource(Context context)
    {
		dbHelper = CourseDB.getInstance(context);
	}
	
	public void open() throws SQLException
    {
		db = dbHelper.getWritableDatabase();
	}
	
	public void close()
    {
		dbHelper.close();
	}
	
	public boolean hasCourses(String term, String subject)
    {
		final String schdcourse = CourseDB.Schedule.TABLE + "." + CourseDB.Schedule.COL_COURSE;
		final String schdterm = CourseDB.Schedule.TABLE + "." + CourseDB.Schedule.COL_TERM;
		final String ctlgsubj = CourseDB.Catalog.TABLE + "." + CourseDB.Catalog.COL_SUBJ;
		
		Cursor cursor = db.rawQuery("select 1 " +
				"from " + CourseDB.Schedule.TABLE +
				" inner join " + CourseDB.Catalog.TABLE +
				" on " + schdcourse +
				" = " + CourseDB.Catalog.QUAL_ID +
				" where " + schdterm + " = ?" +
				" and " + ctlgsubj + " = ?", new String[]{term, subject});
		boolean result = cursor.getCount() > 0;
		cursor.close();
		
		return result;
	}
	
	public void addCourses(List<Course> courses)
    {
		final String existsQuery = "select " + CourseDB.Catalog.COL_ID +
				" from " + CourseDB.Catalog.TABLE +
				" where " + CourseDB.Catalog.COL_SUBJ + " = ?" +
				" and " + CourseDB.Catalog.COL_CODE + " =  ?";
		
		for (Course course: courses){
			Cursor cursor = db.rawQuery(existsQuery, new String[]{course.getSubject(), course.getId()});
			String[] id = new String[1];
			id[0] = "";
			boolean exists = false;
			if (exists = (cursor.getCount() > 0 && cursor.moveToFirst()))
				id[0] = cursor.getString(0);
			cursor.close();
			
			// build content values
			ContentValues values = new ContentValues(CourseDB.Catalog.NUM_COLUMNS);
			values.put(CourseDB.Catalog.COL_SUBJ, course.getSubject());
			values.put(CourseDB.Catalog.COL_TITLE, course.getName());
			values.put(CourseDB.Catalog.COL_CODE, course.getId());
			values.put(CourseDB.Catalog.COL_DESC, course.getDescription());
			values.put(CourseDB.Catalog.COL_HOURS, course.getCredits());
			
			// update or insert
			if (exists)
				db.update(CourseDB.Catalog.TABLE, values, CourseDB.Catalog.COL_ID + " = ?", id);
			else
				db.insert(CourseDB.Catalog.TABLE, null, values);
		}
	}
	
	public void addSections(List<Section> sections)
    {
		final String existsQuery = "select " + CourseDB.Schedule.COL_ID +
				" from " + CourseDB.Schedule.TABLE +
				" where " + CourseDB.Schedule.COL_TERM + " = ?" +
				" and " + CourseDB.Schedule.COL_CRN + " = ?";
		
		String[] id = new String[1];
		String lastCourse = "";
		long crsId = 0;
		for (Section section: sections){
			Cursor cursor = db.rawQuery(existsQuery, new String[]{section.getTerm(), Integer.toString(section.getCRN())});
			id[0] = "";
			boolean exists = false;
			if (exists = (cursor.getCount() > 0 && cursor.moveToFirst()))
				id[0] = cursor.getString(0);
			cursor.close();
			
			// get course ID
			Course course = section.getCourse();
			String courseCode = course.getSubject() + course.getId();
			if (courseCode != lastCourse){
				crsId = getCourseId(course);
				lastCourse = courseCode;
			}
			
			// build content values
			ContentValues values = new ContentValues(CourseDB.Schedule.NUM_COLUMNS);
			values.put(CourseDB.Schedule.COL_COURSE, crsId);
			values.put(CourseDB.Schedule.COL_TERM, Integer.parseInt(section.getTerm()));
			values.put(CourseDB.Schedule.COL_CRN, section.getCRN());
			values.put(CourseDB.Schedule.COL_SECT, section.getSection());
			
			// update or insert
			if (exists){
				db.update(CourseDB.Schedule.TABLE, values, CourseDB.Schedule.COL_ID + " = ?", id);
			} else{
				long schdid = db.insert(CourseDB.Schedule.TABLE, null, values);
				for (Meeting meeting: section.getMeetings())
					addMeeting(schdid, meeting);
			}
		}
	}
	
	public long getCourseId(Course course)
    {
		String[] args = new String[]{course.getSubject(), course.getId()};
		
		Cursor cursor = db.query(CourseDB.Catalog.TABLE,
				CATALOG_ID,
				CourseDB.Catalog.COL_SUBJ + " = ?"
				+ " and " + CourseDB.Catalog.COL_CODE + " = ?",
				args, null, null, null);
		
		long id = 0;
		if (cursor.moveToFirst())
			id = cursor.getLong(0);
		cursor.close();
		
		return id;
	}
	
	public Course getCourse(long crseId)
    {
		String[] args = new String[]{Long.toString(crseId)};
		Cursor cursor = db.query(CourseDB.Catalog.TABLE, COURSE_COLUMNS, 
				CourseDB.Catalog.COL_ID + " = ?", args, null, null, null);
		
		Course course = null;
		if (cursor.moveToFirst()){
			String code = cursor.getString(1);
			String desc = cursor.getString(2);
			double hours = cursor.getDouble(3);
			String subj = cursor.getString(4);
			String title = cursor.getString(5);
			course = new Course(subj, title, code, desc, hours);
		}
		cursor.close();
		
		return course;
	}
	
	public Instructor getInstructor(long instId)
    {
		String[] args = new String[]{Long.toString(instId)};
		Cursor cursor = db.query(CourseDB.Instructors.TABLE, INSTRUCTOR_COLUMNS, 
				CourseDB.Instructors.COL_ID + " = ?", args, null, null, null);
		
		Instructor instructor = null;
		if (cursor.moveToFirst()){
			String email = cursor.getString(1);
			String name = cursor.getString(2);
			instructor = new Instructor(name, email);
		}
		cursor.close();
		
		return instructor;
	}
	
	public List<Section> getSubjectSections(String term, String subject)
    {
		List<Section> result = null;
		String[] args = new String[]{term, subject};
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		queryBuilder.setTables(CourseDB.Schedule.TABLE +
				" inner join " + CourseDB.Catalog.TABLE +
				" on " + CourseDB.Schedule.COL_COURSE + " = " + CourseDB.Catalog.QUAL_ID);
		Cursor sectcsr = queryBuilder.query(db, SECTION_COLUMNS,
				CourseDB.Schedule.COL_TERM + " = ?" +
				" and " + CourseDB.Catalog.COL_SUBJ + " = ?", args, null, null, null);
		if (sectcsr.moveToFirst())
			result = cursorToSections(sectcsr);
		sectcsr.close();
		
		return result;
	}
	
	public List<Course> getSubjectCourses(String term, String subject)
    {
		List<Course> result = new ArrayList<Course>();
		String[] args = new String[]{subject, term};
		
		Cursor crscsr = db.query(CourseDB.Catalog.TABLE, COURSE_COLUMNS,
				CourseDB.Catalog.COL_SUBJ + " = ?" +
				" and exists (select 1 from " + CourseDB.Schedule.TABLE +
				" where " + CourseDB.Schedule.COL_TERM + " = ?)", args, null, null, null);
		if (crscsr.moveToFirst()){
			while (!crscsr.isAfterLast()){
				String code = crscsr.getString(1);
				String desc = crscsr.getString(2);
				double hours = crscsr.getDouble(3);
				String subj = crscsr.getString(4);
				String title = crscsr.getString(5);
				result.add(new Course(subj, title, code, desc, hours));
				
				crscsr.moveToNext();
			}
		}
		crscsr.close();
		
		return result;
	}
	
	public List<Instructor> getSubjectInstructors(String term, String subject)
    {
		final String[] cols = new String[]{CourseDB.Instructors.COL_NAME, CourseDB.Instructors.COL_EMAIL};
		List<Instructor> result = new ArrayList<Instructor>();
		String[] args = new String[]{term, subject};
		SQLiteQueryBuilder query = new SQLiteQueryBuilder();
		
		query.setDistinct(true);
		query.setTables(CourseDB.Instructors.TABLE +
				" inner join " + CourseDB.Meetings.TABLE +
				" on " + CourseDB.Meetings.COL_INSTRUCTOR + " = " + CourseDB.Instructors.QUAL_ID +
				" inner join " + CourseDB.Schedule.TABLE +
				" on " + CourseDB.Meetings.COL_SCHEDULE + " = " + CourseDB.Schedule.QUAL_ID +
				" inner join " + CourseDB.Catalog.TABLE +
				" on " + CourseDB.Schedule.COL_COURSE + " = " + CourseDB.Catalog.QUAL_ID);
		Cursor instcsr = query.query(db, cols,
				CourseDB.Schedule.COL_TERM + " = ?"
				+ " and " + CourseDB.Catalog.COL_SUBJ + " = ?", args, null, null, null);
		if (instcsr.moveToFirst()){
			while (!instcsr.isAfterLast()){
				String name = instcsr.getString(0);
				String email = instcsr.getString(1);
				result.add(new Instructor(name, email));
				
				instcsr.moveToNext();
			}
		}
		instcsr.close();
		
		return result;
	}
	
	private List<Section> cursorToSections(Cursor sectcsr)
    {
		final String typeid = CourseDB.ClassTypes.TABLE + "." + CourseDB.ClassTypes.COL_ID;
		final String[] meetColumns = new String[]{
			CourseDB.Meetings.COL_BEGIN_DATE,
			CourseDB.Meetings.COL_END_DATE,
			CourseDB.Meetings.COL_BEGIN_TIME,
			CourseDB.Meetings.COL_END_TIME,
			CourseDB.Meetings.COL_DAYS,
			CourseDB.Meetings.COL_LOCATION,
			CourseDB.Meetings.COL_INSTRUCTOR,
			CourseDB.ClassTypes.COL_DESC
		};
		Map<Long, Course> courseCache = new HashMap<Long, Course>();
		Map<Long, Instructor> instCache = new HashMap<Long, Instructor>();
		ArrayList<Section> result = new ArrayList<Section>();
		
		while (!sectcsr.isAfterLast()){
			long crseId = sectcsr.getLong(1);
			Course course = null;
			if (courseCache.containsKey(crseId)){
				course = courseCache.get(crseId);
			} else{
				course = getCourse(crseId);
				courseCache.put(crseId, course);
			}
			
			int crn = sectcsr.getInt(2);
			int section = sectcsr.getInt(3);
			String term = Integer.toString(sectcsr.getInt(4));
			
			// get meetings
			ArrayList<Meeting> meetings = new ArrayList<Meeting>();
			SQLiteQueryBuilder query = new SQLiteQueryBuilder();
			query.setTables(CourseDB.Meetings.TABLE +
					" inner join " + CourseDB.ClassTypes.TABLE +
					" on " + CourseDB.Meetings.COL_TYPE + " = " + typeid);
			Cursor meetcsr = query.query(db, meetColumns,
					CourseDB.Meetings.COL_SCHEDULE + " = ?",
					new String[]{Long.toString(crseId)}, null, null, null);
			if (meetcsr.moveToFirst()){
				while (!meetcsr.isAfterLast()){
					long instId = meetcsr.getLong(6);
					Instructor instructor = null;
					if (instCache.containsKey(instId)){
						instructor = instCache.get(instId);
					} else{
						instructor = getInstructor(instId);
						instCache.put(instId, instructor);
					}
					
					Date beginDate = new Date(meetcsr.getLong(0));
					Date endDate = new Date(meetcsr.getLong(1));
					Date beginTime = new Date(meetcsr.getLong(2));
					Date endTime = new Date(meetcsr.getLong(3));
					String days = meetcsr.getString(4);
					String location = meetcsr.getString(5);
					String type = meetcsr.getString(7);
					Meeting meeting = new Meeting(location, days, type, instructor, beginDate, endDate, beginTime, endTime);
					meetings.add(meeting);
					meetcsr.moveToNext();
				}
			}
			meetcsr.close();
			
			Section sect = new Section(course, term, section, crn, meetings);
			result.add(sect);
			
			sectcsr.moveToNext();
		}
		
		return result;
	}
	
	private void addMeeting(long sectionId, Meeting meeting)
    {
		ContentValues values = new ContentValues(CourseDB.Meetings.NUM_COLUMNS);
		Instructor instructor = meeting.getInstructor();
		long classTypeId = createClassTypeId(meeting.getType());
		
		if (instructor != null)
			values.put(CourseDB.Meetings.COL_INSTRUCTOR, createInstructorId(instructor));
		values.put(CourseDB.Meetings.COL_SCHEDULE, sectionId);
		values.put(CourseDB.Meetings.COL_LOCATION, meeting.getLocation());
		values.put(CourseDB.Meetings.COL_BEGIN_DATE, meeting.getBeginDate().getTime());
		values.put(CourseDB.Meetings.COL_END_DATE, meeting.getEndDate().getTime());
		values.put(CourseDB.Meetings.COL_BEGIN_TIME, meeting.getBeginTime().getTime());
		values.put(CourseDB.Meetings.COL_END_TIME, meeting.getEndTime().getTime());
		values.put(CourseDB.Meetings.COL_DAYS, meeting.getDays());
		values.put(CourseDB.Meetings.COL_TYPE, classTypeId);
		
		db.insert(CourseDB.Meetings.TABLE, null, values);
	}
	
	private long createInstructorId(Instructor instructor)
    {
		String[] args = new String[]{instructor.getEmail()};
		
		Cursor cursor = db.query(CourseDB.Instructors.TABLE, INSTRUCTOR_ID,
				CourseDB.Instructors.COL_EMAIL + " = ?", args, null, null, null);
		
		long id = 0;
		if (cursor.moveToFirst()){
			id = cursor.getLong(0);
		} else{
			ContentValues values = new ContentValues(CourseDB.Instructors.NUM_COLUMNS);
			values.put(CourseDB.Instructors.COL_NAME, instructor.getName());
			values.put(CourseDB.Instructors.COL_EMAIL, instructor.getEmail());
			id = db.insert(CourseDB.Instructors.TABLE, null, values);
		}
		cursor.close();
		
		return id;
	}
	
	private long createClassTypeId(String type)
    {
		String[] args = new String[]{type};
		
		Cursor cursor = db.query(CourseDB.ClassTypes.TABLE, CLASS_TYPES_ID, 
				CourseDB.ClassTypes.COL_DESC + " = ?", args, null, null, null);
		
		long id = 0;
		if (cursor.moveToFirst()){
			id = cursor.getLong(0);
		} else{
			ContentValues values = new ContentValues(CourseDB.ClassTypes.NUM_COLUMNS);
			values.put(CourseDB.ClassTypes.COL_DESC, type);
			id = db.insert(CourseDB.ClassTypes.TABLE, null, values);
		}
		cursor.close();
		
		return id;
	}
}
