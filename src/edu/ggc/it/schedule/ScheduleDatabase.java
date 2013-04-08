package edu.ggc.it.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is the Schedule Database class. It contains all the information about
 * the database and methods for adding, editing, retrieving and deleting
 * records in the database.
 * @author Raj Ramsaroop
 *
 */
public class ScheduleDatabase {
	
	/**
	 * The database version
	 */
	public static final int DATABASE_VERSION = 2;
	
	/**
	 * The name of the database
	 */
	public static final String DATABASE_NAME = "my_schedule_db";
	
	/**
	 * The name of the table in the database
	 */
	public static final String DATABASE_TABLE = "classes";

	/**
	 * The key for rowID
	 */
	public static final String KEY_ROWID = "_id";
	
	/**
	 * The index for rowID
	 */
	public static final int INDEX_ROWID = 0;
	
	/**
	 * The key for class name
	 */
	public static final String KEY_NAME = "name";
	
	/**
	 * The index for class name
	 */
	public static final int INDEX_NAME = 1;
	
	/**
	 * The key for class section
	 */
	public static final String KEY_SECTION = "section";
	
	/**
	 * The index for class section
	 */
	public static final int INDEX_SECTION = 2;
	
	/**
	 * The key for the class start time
	 */
	public static final String KEY_START_TIME = "start_time";
	
	/**
	 * The index for the class start time
	 */
	public static final int INDEX_START_TIME = 3;
	
	/**
	 * The key for the class end time
	 */
	public static final String KEY_END_TIME = "end_time";
	
	/**
	 * The index for the class end time
	 */
	public static final int INDEX_END_TIME = 4;
	
	/**
	 * The key for the integer/boolean for a class on mondays
	 */
	public static final String KEY_ON_MONDAY = "on_monday";
	
	/**
	 * The index for the integer/boolean for a class on mondays
	 */
	public static final int INDEX_ON_MONDAY = 5;
	
	/**
	 * The key for the integer/boolean for a class on tuesdays
	 */
	public static final String KEY_ON_TUESDAY = "on_tuesday";
	
	/**
	 * The index for the integer/boolean for a class on tuesdays
	 */
	public static final int INDEX_ON_TUESDAY = 6;
	
	/**
	 * The key for the integer/boolean for a class on wednesdays
	 */
	public static final String KEY_ON_WEDNESDAY = "on_wednesday";
	
	/**
	 * The index for the integer/boolean for a class on wednesdays
	 */
	public static final int INDEX_ON_WEDNESDAY = 7;
	
	/**
	 * The key for the integer/boolean for a class on thursdays
	 */
	public static final String KEY_ON_THURSDAY = "on_thursday";
	
	/**
	 * The index for the integer/boolean for a class on thursdays
	 */
	public static final int INDEX_ON_THURSDAY = 8;
	
	/**
	 * The key for the integer/boolean for a class on fridays
	 */
	public static final String KEY_ON_FRIDAY = "on_friday";
	
	/**
	 * The index for the integer/boolean for a class on fridays
	 */
	public static final int INDEX_ON_FRIDAY = 9;
	
	/**
	 * The key for the integer/boolean for a class on saturdays
	 */
	public static final String KEY_ON_SATURDAY = "on_saturday";
	
	/**
	 * The index for the integer/boolean for a class on saturdays
	 */
	public static final int INDEX_ON_SATURDAY = 10;
	
	/**
	 * The key for class building location
	 */
	public static final String KEY_BUILDING_LOCATION = "building_location";
	
	/**
	 * The index for class building location
	 */
	public static final int INDEX_LOCATION_BUILDING = 11;
	
	/**
	 * The key for the class room location
	 */
	public static final String KEY_ROOM_LOCATION = "room_location";
	
	/**
	 * The index for the class room location
	 */
	public static final int INDEX_LOCATION_ROOM = 12;

	/**
	 * A string array of all the keys
	 */
	public static final String[] KEYS_ALL = { ScheduleDatabase.KEY_ROWID,
			ScheduleDatabase.KEY_NAME, ScheduleDatabase.KEY_SECTION,
			ScheduleDatabase.KEY_START_TIME, ScheduleDatabase.KEY_END_TIME,
			ScheduleDatabase.KEY_ON_MONDAY, ScheduleDatabase.KEY_ON_TUESDAY,
			ScheduleDatabase.KEY_ON_WEDNESDAY,
			ScheduleDatabase.KEY_ON_THURSDAY, ScheduleDatabase.KEY_ON_FRIDAY,
			ScheduleDatabase.KEY_ON_SATURDAY,
			ScheduleDatabase.KEY_BUILDING_LOCATION,
			ScheduleDatabase.KEY_ROOM_LOCATION };

	private Context context;
	private SQLiteDatabase database;
	private ScheduleDatabaseHelper helper;

	/**
	 * The constructor for the Schedule Database
	 * @param context the activity context
	 */
	public ScheduleDatabase(Context context) {
		this.context = context;
	}

	/**
	 * Opens a connection to the database
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		helper = new ScheduleDatabaseHelper(context);
		database = helper.getWritableDatabase();
	}

	/**
	 * Closes the connection to the dtabase
	 */
	public void close() {
		helper.close();
		helper = null;
		database = null;
	}

	/**
	 * Inserts a row into the database with the specified values
	 * @param values the values to be inserted
	 * @return
	 */
	public long createRow(ContentValues values) {
		return database.insert(DATABASE_TABLE, null, values);
	}

	/**
	 * Updates a specified record in the database
	 * @param rowId the row to be updated
	 * @param values the values to be updated with
	 * @return
	 */
	public boolean updateRow(long rowId, ContentValues values) {
		return database.update(DATABASE_TABLE, values,
				ScheduleDatabase.KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Deletes a specified row in the database
	 * @param rowId the rowID
	 * @return
	 */
	public boolean deleteRow(long rowId) {
		return database.delete(DATABASE_TABLE, ScheduleDatabase.KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	/**
	 * Selects all records in the database and returns it
	 * @return A cursor with all the records from the database
	 */
	public Cursor queryAll() {
		return database.query(DATABASE_TABLE, KEYS_ALL, null, null, null, null,
				KEY_NAME + " ASC");
	}

	/**
	 * Selects all the records in the database where the specified day is 1
	 * @param day the day to select
	 * @return a cursor with the record information
	 */
	public Cursor queryByDay(String day) {
		String selection = day + "=" + "1";
		return database.query(DATABASE_TABLE, KEYS_ALL, selection, null, null,
				null, KEY_START_TIME + " ASC");
	}

	/**
	 * Selects a specified record from the database
	 * @param rowId the rowID to be selected
	 * @return a Cursor with the results of the query
	 * @throws SQLException
	 */
	public Cursor query(long rowId) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE, KEYS_ALL,
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * Creates the content values that can be used to insert or update records in the database
	 * @param name the class name
	 * @param section the class section
	 * @param startTime the start time in minutes
	 * @param endTime the end time in minutes
	 * @param monday integer/boolean if the class is on monday
	 * @param tuesday integer/boolean if the class is on tuesday
	 * @param wednesday integer/boolean if the class is on wednesday
	 * @param thursday integer/boolean if the class is on thusday
	 * @param friday integer/boolean if the class is on friday
	 * @param saturday integer/boolean if the class is on saturday
	 * @param buildingLocation the building the class is in
	 * @param roomLocation the room the class is in
	 * @return the ContentValues that can be used in a database
	 */
	public ContentValues createContentValues(String name, String section,
			int startTime, int endTime, int monday, int tuesday,
			int wednesday, int thursday, int friday, int saturday,
			String buildingLocation, String roomLocation) {
		ContentValues values = new ContentValues();
		values.put(ScheduleDatabase.KEY_NAME, name);
		values.put(ScheduleDatabase.KEY_SECTION, section);
		values.put(ScheduleDatabase.KEY_START_TIME, startTime);
		values.put(ScheduleDatabase.KEY_END_TIME, endTime);
		values.put(KEY_ON_MONDAY, monday);
		values.put(KEY_ON_TUESDAY, tuesday);
		values.put(KEY_ON_WEDNESDAY, wednesday);
		values.put(KEY_ON_THURSDAY, thursday);
		values.put(KEY_ON_FRIDAY, friday);
		values.put(KEY_ON_SATURDAY, saturday);
		values.put(ScheduleDatabase.KEY_BUILDING_LOCATION, buildingLocation);
		values.put(ScheduleDatabase.KEY_ROOM_LOCATION, roomLocation);
		return values;
	}
	
	/**
	 * Deletes the table
	 */
	public void deleteTable() {
		database.delete(DATABASE_TABLE, null, null);
	}

	/**
	 * A database helper class to help with performing database related functions
	 * @author Raj Ramsaroop
	 *
	 */
	private static class ScheduleDatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE
				+ " ("
				+ ScheduleDatabase.KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ScheduleDatabase.KEY_NAME
				+ " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_SECTION
				+ " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_START_TIME
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_END_TIME
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_MONDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_TUESDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_WEDNESDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_THURSDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_FRIDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_ON_SATURDAY
				+ " INTEGER NOT NULL, "
				+ ScheduleDatabase.KEY_BUILDING_LOCATION
				+ " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_ROOM_LOCATION + " TEXT NOT NULL " + ");";

		/**
		 * The database helper constructor
		 * @param context The activity context
		 */
		public ScheduleDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS classes");
			onCreate(db);
		}

	}

}
