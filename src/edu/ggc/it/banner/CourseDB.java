package edu.ggc.it.banner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class CourseDB extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "course_catalog.db";
	private static final int DATABASE_VERSION = 2;
	private static CourseDB instance = null;

	private CourseDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static CourseDB getInstance(Context context){
		if (instance == null)
			instance = new CourseDB(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Catalog.onCreate(db);
		Schedule.onCreate(db);
		Instructors.onCreate(db);
		ClassTypes.onCreate(db);
		Meetings.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Catalog.onUpgrade(db);
		Schedule.onUpgrade(db);
		Instructors.onUpgrade(db);
		ClassTypes.onUpgrade(db);
		Meetings.onUpgrade(db);
	}

	// inner classes for creating individual tables
	static class Catalog{
		public static final String TABLE = "catalog";
		public static final String COL_ID = "_id";
		public static final String COL_SUBJ = "subj";
		public static final String COL_CODE = "code";
		public static final String COL_TITLE = "title";
		public static final String COL_DESC = "desc";
		public static final String COL_HOURS = "hours";
		public static final String QUAL_ID = TABLE + "." + COL_ID;
		public static final String INDEX_COURSE = "crsidx";
		public static final int NUM_COLUMNS = 5; // excludes rowid; for ContentValues
		
		public static void onCreate(SQLiteDatabase db){
			db.execSQL("create table " + TABLE + " (" +
					COL_ID + " integer primary key," +
					COL_SUBJ + " text not null," +
					COL_CODE + " text not null," +
					COL_TITLE + " text not null," +
					COL_DESC + " text not null," +
					COL_HOURS + " real not null" +
					")");
			db.execSQL("create unique index " + INDEX_COURSE +
					" on " + TABLE + "(" + COL_SUBJ + "," + COL_CODE +
					")");
		}
		
		public static void onUpgrade(SQLiteDatabase db){
			db.execSQL("DROP TABLE IF EXISTS catalog");
			onCreate(db);
		}
	}
	
	static class Schedule{
		public static final String TABLE = "schedule";
		public static final String COL_ID = "_id";
		public static final String COL_COURSE = "courseid";
		public static final String COL_TERM = "term";
		public static final String COL_CRN = "crn";
		public static final String COL_SECT = "section";
		public static final String QUAL_ID = TABLE + "." + COL_ID;
		public static final String INDEX_CRN = "crnidx";
		public static final String INDEX_SECT = "sectidx";
		public static final int NUM_COLUMNS = 4;
		
		public static void onCreate(SQLiteDatabase db){
			db.execSQL("create table " + TABLE + " (" +
					COL_ID + " integer primary key," +
					COL_COURSE + " integer not null," +
					COL_TERM + " integer not null," +
					COL_CRN + " integer not null," + 
					COL_SECT + " integer not null," +
					"foreign key (" + COL_COURSE + ") references " + Catalog.TABLE + "(" + Catalog.COL_ID + ")" +
					")");
			db.execSQL("create unique index " + INDEX_CRN +
					" on " + TABLE + "(" + COL_TERM + "," + COL_CRN +
					")");
			db.execSQL("create unique index " + INDEX_SECT +
					" on " + TABLE + "(" + COL_TERM + "," + COL_COURSE + "," + COL_SECT +
					")");
		}
		
		public static void onUpgrade(SQLiteDatabase db){
			db.execSQL("DROP TABLE IF EXISTS schedule");
			onCreate(db);
		}
	}
	
	static class Instructors{
		public static final String TABLE = "instructors";
		public static final String COL_ID = "_id";
		public static final String COL_NAME = "name";
		public static final String COL_EMAIL = "email";
		public static final String QUAL_ID = TABLE + "." + COL_ID;
		public static final String INDEX_EMAIL = "emlidx";
		public static final int NUM_COLUMNS = 2;
		
		public static void onCreate(SQLiteDatabase db){
			db.execSQL("create table " + TABLE + " (" +
					COL_ID + " integer primary key," +
					COL_NAME + " text not null," +
					COL_EMAIL + " text not null" +
					")");
			db.execSQL("create unique index " + INDEX_EMAIL +
					" on " + TABLE + "(" + COL_EMAIL +
					")");
		}
		
		public static void onUpgrade(SQLiteDatabase db){
			db.execSQL("DROP TABLE IF EXISTS instructors");
			onCreate(db);
		}
	}
	
	static class ClassTypes{
		public static final String TABLE = "classtypes";
		public static final String COL_ID = "_id";
		public static final String COL_DESC = "desc";
		public static final String QUAL_ID = TABLE + "." + COL_ID;
		public static final String INDEX_DESC = "descidx";
		public static final int NUM_COLUMNS = 1;
		
		public static void onCreate(SQLiteDatabase db){
			db.execSQL("create table " + TABLE + " (" +
					COL_ID + " integer primary key," +
					COL_DESC + " text not null" +
					")");
			db.execSQL("create unique index " + INDEX_DESC +
					" on " + TABLE + "(" + COL_DESC +
					")");
		}
		
		public static void onUpgrade(SQLiteDatabase db){
			db.execSQL("DROP TABLE IF EXISTS classtypes");
			onCreate(db);
		}
	}
	
	static class Meetings{
		public static final String TABLE = "meetings";
		public static final String COL_ID = "_id";
		public static final String COL_INSTRUCTOR = "instid";
		public static final String COL_SCHEDULE = "schedid";
		public static final String COL_LOCATION = "location";
		public static final String COL_BEGIN_DATE = "begindate";
		public static final String COL_END_DATE = "enddate";
		public static final String COL_BEGIN_TIME = "begintime";
		public static final String COL_END_TIME = "endtime";
		public static final String COL_DAYS = "days";
		public static final String COL_TYPE = "typeid";
		public static final String QUAL_ID = TABLE + "." + COL_ID;
		public static final String INDEX_INSTRUCTOR = "instidx";
		public static final int NUM_COLUMNS = 9;
		
		public static void onCreate(SQLiteDatabase db){
			db.execSQL("create table " + TABLE + " (" +
					COL_ID + " integer primary key," +
					COL_INSTRUCTOR + " integer," +
					COL_SCHEDULE + " integer not null," +
					COL_LOCATION + " text not null," +
					COL_BEGIN_DATE + " integer not null," +
					COL_END_DATE + " integer not null," +
					COL_BEGIN_TIME + " integer not null," +
					COL_END_TIME + " integer not null," +
					COL_DAYS + " text not null," + 
					COL_TYPE + " integer not null," +
					"foreign key (" + COL_INSTRUCTOR + ") references " + Instructors.TABLE + "(" + Instructors.COL_ID + ") " +
					"foreign key (" + COL_SCHEDULE + ") references " + Schedule.TABLE + "(" + Schedule.COL_ID + ") " +
					"foreign key (" + COL_TYPE + ") references " + ClassTypes.TABLE + "(" + ClassTypes.COL_ID + ") " +
					")");
			db.execSQL("create index " + INDEX_INSTRUCTOR +
					" on " + TABLE + " (" + COL_INSTRUCTOR + 
					")");
		}
		
		public static void onUpgrade(SQLiteDatabase db){
			db.execSQL("DROP TABLE IF EXISTS meetings");
			onCreate(db);
		}
	}
}
