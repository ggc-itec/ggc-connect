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
	public static final String KEY_SECTION = "section";
	public static final int INDEX_SECTION = 2;
	public static final String KEY_START_TIME = "start_time";
	public static final int INDEX_START_TIME = 3;
	public static final String KEY_END_TIME = "end_time";
	public static final int INDEX_END_TIME = 4;
	public static final String KEY_ON_MONDAY = "on_monday";
	public static final int INDEX_ON_MONDAY = 5;
	public static final String KEY_ON_TUESDAY = "on_tuesday";
	public static final int INDEX_ON_TUESDAY = 6;
	public static final String KEY_ON_WEDNESDAY = "on_wednesday";
	public static final int INDEX_ON_WEDNESDAY = 7;
	public static final String KEY_ON_THURSDAY = "on_thursday";
	public static final int INDEX_ON_THURSDAY = 8;
	public static final String KEY_ON_FRIDAY = "on_friday";
	public static final int INDEX_ON_FRIDAY = 9;
	public static final String KEY_ON_SATURDAY = "on_saturday";
	public static final int INDEX_ON_SATURDAY = 10;
	public static final String KEY_BUILDING_LOCATION = "building_location";
	public static final int INDEX_LOCATION_BUILDING = 11;
	public static final String KEY_ROOM_LOCATION = "room_location";
	public static final int INDEX_LOCATION_ROOM = 12;

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

	public ScheduleDatabase(Context context) {
		this.context = context;
	}

	public void open() throws SQLException {
		helper = new ScheduleDatabaseHelper(context);
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
				KEY_NAME + " ASC");
	}

	public Cursor queryByDay(String day) {
		String selection = day + "=" + "1";
		return database.query(DATABASE_TABLE, KEYS_ALL, selection, null, null,
				null, KEY_START_TIME + " ASC");
	}

	public Cursor query(long rowId) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE, KEYS_ALL,
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

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

		public ScheduleDatabaseHelper(Context context) {
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
