package edu.ggc.it.banner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

// TODO: this duplicates a lot of functionality of CourseDataSource; merge these somehow
public class CourseSearchBuilder implements Serializable {
	private static final long serialVersionUID = -4716765738834007056L;
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
	private static final String[] MEETING_COLUMNS = new String[]{
		CourseDB.Meetings.QUAL_ID,
		CourseDB.Meetings.COL_BEGIN_DATE,
		CourseDB.Meetings.COL_END_DATE,
		CourseDB.Meetings.COL_BEGIN_TIME,
		CourseDB.Meetings.COL_END_TIME,
		CourseDB.Meetings.COL_DAYS,
		CourseDB.Meetings.COL_INSTRUCTOR,
		CourseDB.Meetings.COL_LOCATION,
		CourseDB.ClassTypes.COL_DESC
	};
	private static final String[] INSTRUCTOR_COLUMNS = new String[]{
		CourseDB.Instructors.COL_ID,
		CourseDB.Instructors.COL_EMAIL,
		CourseDB.Instructors.COL_NAME
	};
	
	public static final String KEY = "edu.ggc.it.banner.CourseSearchBuilder";
	
	private String term;
	private String courseNumber;
	private String instructor;
	private double minCredits;
	private long beginTime;
	private long endTime;
	private List<String> subjects;
	private List<String> days;
	
	private Map<Long, Instructor> cachedInstructors;
	
	public CourseSearchBuilder(){
		term = null;
		courseNumber = null;
		instructor = null;
		minCredits = 0.0;
		beginTime = 0L;
		endTime = Long.MAX_VALUE;
		subjects = new ArrayList<String>();
		days = new ArrayList<String>(7);
		cachedInstructors = new HashMap<Long, Instructor>();
	}
	
	public List<Section> searchCourses(Context context){
		List<Section> results = new ArrayList<Section>();
		String[] ctlgargs = new String[]{Double.toString(minCredits)};
		SQLiteDatabase db = CourseDB.getInstance(context).getReadableDatabase();
		
		cachedInstructors.clear();
		// query catalog first
		String selection = "";
		for (String subject: subjects)
			selection += ", '" + subject + "'";
		selection = CourseDB.Catalog.COL_SUBJ + " in (" + selection.substring(1) + ")" +
			" and " + CourseDB.Catalog.COL_HOURS + " >= ?";
		if (courseNumber != null)
			selection += " and " + CourseDB.Catalog.COL_CODE + " like '%" + courseNumber + "%'";
		String orderBy = CourseDB.Catalog.COL_SUBJ + ", " +
			CourseDB.Catalog.COL_CODE;
		Cursor ctlgcsr = db.query(CourseDB.Catalog.TABLE, COURSE_COLUMNS, 
				selection, ctlgargs, null, null, orderBy);
		if (ctlgcsr.moveToFirst()){
			while (!ctlgcsr.isAfterLast()){
				// build Course
				long courseId = ctlgcsr.getLong(0);
				String code = ctlgcsr.getString(1);
				String desc = ctlgcsr.getString(2);
				double hours = ctlgcsr.getDouble(3);
				String subj = ctlgcsr.getString(4);
				String title = ctlgcsr.getString(5);
				Course course = new Course(subj, title, code, desc, hours);
				results.addAll(getCourseSections(db, course, courseId));
				ctlgcsr.moveToNext();
			}
		}
		ctlgcsr.close();
		
		return results;
	}
	
	private List<Section> getCourseSections(SQLiteDatabase db, Course course, long courseId){
		List<Section> results = new ArrayList<Section>();
		String[] args = new String[]{term, Long.toString(courseId), Long.toString(beginTime), Long.toString(endTime)};
		
		String selection = CourseDB.Schedule.COL_TERM + " = ?" +
				" and " + CourseDB.Schedule.COL_COURSE + " = ?" +
				" and exists (select 1 from " + CourseDB.Meetings.TABLE +
				" left join " + CourseDB.Instructors.TABLE +
				" on " + CourseDB.Instructors.QUAL_ID + " = " + CourseDB.Meetings.COL_INSTRUCTOR +
				" where " + CourseDB.Meetings.COL_SCHEDULE + " = " + CourseDB.Schedule.QUAL_ID + 
				" and " + CourseDB.Meetings.COL_BEGIN_TIME + " >= ?" +
				" and " + CourseDB.Meetings.COL_END_TIME + " <= ?";
		for (int i = 0; i < days.size(); i++){
			if (i == 0)
				selection += " and (days like '%";
			else
				selection += " or days like '%";
			selection += days.get(i);
			if (i == days.size()-1)
				selection += "%')";
			else
				selection += "%'";
		}
		if (instructor != null)
			selection += " and " + CourseDB.Instructors.COL_NAME + " like '%" + instructor + "%'";
		selection += ")";
		Cursor sectcsr = db.query(CourseDB.Schedule.TABLE, SECTION_COLUMNS,
				selection, args, null, null, null);
		if (sectcsr.moveToFirst()){
			while (!sectcsr.isAfterLast()){
				// build Section
				long sectionId = sectcsr.getLong(0);
				List<Meeting> meetings = getSectionMeetings(db, sectionId);
				if (meetings.size() > 0){
					int crn = sectcsr.getInt(2);
					int section = sectcsr.getInt(3);
					Section sect = new Section(course, term, section, crn, meetings);
					results.add(sect);
				}
				sectcsr.moveToNext();
			}
		}
		sectcsr.close();
		
		return results;
	}
	
	private List<Meeting> getSectionMeetings(SQLiteDatabase db, long sectId){
		List<Meeting> results = new ArrayList<Meeting>();
		String[] args = new String[]{Long.toString(sectId)};
		SQLiteQueryBuilder meetingQuery = new SQLiteQueryBuilder();
		
		meetingQuery.setTables(CourseDB.Meetings.TABLE +
				" inner join " + CourseDB.ClassTypes.TABLE +
				" on " + CourseDB.Meetings.COL_TYPE + " = " + CourseDB.ClassTypes.QUAL_ID);
		Cursor meetcsr = meetingQuery.query(db, MEETING_COLUMNS, 
				CourseDB.Meetings.COL_SCHEDULE + " = ?", args, null, null, null);
		if (meetcsr.moveToFirst()){
			while (!meetcsr.isAfterLast()){
				Date beginDate = new Date(meetcsr.getLong(1));
				Date endDate = new Date(meetcsr.getLong(2));
				Date beginTime = new Date(meetcsr.getLong(3));
				Date endTime = new Date(meetcsr.getLong(4));
				String sectDays = meetcsr.getString(5);
				String location = meetcsr.getString(7);
				String type = meetcsr.getString(8);
				Instructor instructor = null;
				
				if (!meetcsr.isNull(6))
					instructor = getInstructor(db, meetcsr.getLong(6));
				
				Meeting meeting = new Meeting(location, sectDays, type, instructor, beginDate, endDate, beginTime, endTime);
				results.add(meeting);
				
				meetcsr.moveToNext();
			}
		}
		meetcsr.close();
		
		return results;
	}
	
	private Instructor getInstructor(SQLiteDatabase db, long instId){
		if (cachedInstructors.containsKey(instId))
			return cachedInstructors.get(instId);
		
		Instructor instructor = null;
		String[] args = new String[]{Long.toString(instId)};
		Cursor instcsr = db.query(CourseDB.Instructors.TABLE, INSTRUCTOR_COLUMNS, 
				CourseDB.Instructors.COL_ID + " = ?", args, null, null, null);
		if (instcsr.moveToFirst()){
			String email = instcsr.getString(1);
			String name = instcsr.getString(2);
			instructor = new Instructor(name, email);
			cachedInstructors.put(instId, instructor);
		}
		instcsr.close();
		
		return instructor;
	}
	
	public void setTerm(String term){
		this.term = term;
	}
	
	public void setCourseNumber(String course){
		courseNumber = course;
	}
	
	public void setInstructor(String instructor){
		this.instructor = instructor;
	}
	
	public void setMinCredits(double credits){
		minCredits = credits;
	}
	
	public void setBeginTime(Date time){
		beginTime = time.getTime();
	}
	
	public void setEndTime(Date time){
		endTime = time.getTime();
	}
	
	public void addSubject(String subject){
		subjects.add(subject);
	}
	
	public void addSubjects(String[] subjects){
		for (String subject: subjects)
			this.subjects.add(subject);
	}
	
	public void addSubjects(Collection<String> subjects){
		this.subjects.addAll(subjects);
	}
	
	public void addDay(String day){
		days.add(day);
	}
	
	public void addDays(String[] days){
		for (String day: days)
			this.days.add(day);
	}
	
	public void addDays(Collection<String> days){
		this.days.addAll(days);
	}
	
	public void clearSubjects(){
		subjects.clear();
	}
	
	public void clearDays(){
		days.clear();
	}
}
