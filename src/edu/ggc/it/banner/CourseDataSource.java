package edu.ggc.it.banner;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CourseDataSource {
	private SQLiteDatabase db;
	private CourseDB dbHelper;
	
	public CourseDataSource(Context context){
		dbHelper = new CourseDB(context);
	}
	
	public void open() throws SQLException{
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public boolean hasCourses(String term, String subject){
		String schdcourse = CourseDB.Schedule.TABLE + "." + CourseDB.Schedule.COL_COURSE;
		String schdterm = CourseDB.Schedule.TABLE + "." + CourseDB.Schedule.COL_TERM;
		String ctlgid = CourseDB.Catalog.TABLE + "." + CourseDB.Catalog.COL_ID;
		String ctlgsubj = CourseDB.Catalog.TABLE + "." + CourseDB.Catalog.COL_SUBJ;
		
		Cursor cursor = db.rawQuery("select count(*) " +
				"from " + CourseDB.Schedule.TABLE +
				" inner join " + CourseDB.Catalog.TABLE +
				" on " + schdcourse +
				" = " + ctlgid +
				" where " + schdterm + " = ?" +
				" and " + ctlgsubj + " = ?", new String[]{term, subject});
		if (!cursor.moveToFirst()){
			cursor.close();
			return false;
		}
		
		boolean result = cursor.getInt(0) > 0;
		cursor.close();
		return result;
	}
}
