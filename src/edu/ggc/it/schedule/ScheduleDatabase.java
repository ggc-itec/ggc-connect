package edu.ggc.it.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDatabase {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "my_schedule_db";
	public static final String DATABASE_TABLE = "classes";

	public static final String KEY_ROWID = "_id";
	public static final int INDEX_ROWID = 0;
	public static final String KEY_NAME = "name";
	public static final int INDEX_NAME = 1;
	public static final String KEY_START_TIME = "start_time";
	public static final int INDEX_START_TIME = 2;
	public static final String KEY_END_TIME = "end_time";
	public static final int INDEX_END_TIME = 3;
	public static final String KEY_DAYS = "days";
	public static final int INDEX_DAYS = 4;
	public static final String KEY_BUILDING_LOCATION = "building_location";
	public static final int INDEX_LOCATION_BUILDING = 5;
	public static final String KEY_ROOM_LOCATION = "room_location";
	public static final int INDEX_LOCATION_ROOM = 6;

	public static final String[] KEYS_ALL = { ScheduleDatabase.KEY_ROWID,
			ScheduleDatabase.KEY_NAME, 
			ScheduleDatabase.KEY_START_TIME,
			ScheduleDatabase.KEY_END_TIME,
			ScheduleDatabase.KEY_DAYS,
			ScheduleDatabase.KEY_BUILDING_LOCATION,
			ScheduleDatabase.KEY_ROOM_LOCATION };

	private Context context;
	private SQLiteDatabase database;
	private StudentDatabaseHelper helper;

	public ScheduleDatabase(Context context) {
		this.context = context;
	}

	public void open() throws SQLException {
		helper = new StudentDatabaseHelper(context);
		database = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
		helper = null;
		database = null;
	}

	public long createRow(ContentValues values) {
		return database.insert(DATABASE_TABLE, null, values);
	}

	public boolean updateRow(long rowId, ContentValues values) {
		return database.update(DATABASE_TABLE, values,
				ScheduleDatabase.KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deleteRow(long rowId) {
		return database.delete(DATABASE_TABLE, ScheduleDatabase.KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	public Cursor queryAll() {
		return database.query(DATABASE_TABLE, KEYS_ALL, null, null, null, null,
				ScheduleDatabase.KEY_NAME + " ASC");
	}

	public Cursor query(long rowId) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE, KEYS_ALL,
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public ContentValues createContentValues(String name, String startTime,
			String endTime, String days, String buildingLocation, String roomLocation) {
		ContentValues values = new ContentValues();
		values.put(ScheduleDatabase.KEY_NAME, name);
		values.put(ScheduleDatabase.KEY_START_TIME, startTime);
		values.put(ScheduleDatabase.KEY_END_TIME, endTime);
		values.put(ScheduleDatabase.KEY_DAYS, days);
		values.put(ScheduleDatabase.KEY_BUILDING_LOCATION, buildingLocation);
		values.put(ScheduleDatabase.KEY_ROOM_LOCATION, roomLocation);
		return values;
	}

	private static class StudentDatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
				+ DATABASE_TABLE + " (" + ScheduleDatabase.KEY_ROWID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ ScheduleDatabase.KEY_NAME + " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_START_TIME + " TEXT NOT NULL, " 
				+ ScheduleDatabase.KEY_END_TIME + " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_DAYS + " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_BUILDING_LOCATION + " TEXT NOT NULL, "
				+ ScheduleDatabase.KEY_ROOM_LOCATION + " TEXT NOT NULL "
				+ ");";

		public StudentDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		}

	}

}
