package edu.ggc.it.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A sqlite database of ToDo tasks
 * 
 * @author crystalist
 * 
 */
public class ToDoDatabase {

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "todoDB";
	public static final String DATABASE_TABLE = "todoTable";

	public static final String KEY_ROWID = "_id";
	public static final int INDEX_ROWID = 0;
	public static final String KEY_TASK = "task";
	public static final int INDEX_TASK = 1;

	public static final String[] KEYS_ALL = { ToDoDatabase.KEY_ROWID,
			ToDoDatabase.KEY_TASK };

	private Context context;
	private SQLiteDatabase database;
	private StudentDatabaseHelper helper;

	public ToDoDatabase(Context context) {
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
		return database.update(DATABASE_TABLE, values, ToDoDatabase.KEY_ROWID
				+ "=" + rowId, null) > 0;
	}

	public boolean deleteRow(long rowId) {
		return database.delete(DATABASE_TABLE, ToDoDatabase.KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	public Cursor queryAllByRowID() {
		return database.query(DATABASE_TABLE, KEYS_ALL, null, null, null, null,
				" ROWID");
	}

	/**
	 * Queries all items from the database, results them in ascending order
	 * 
	 * @see <a
	 *      href="http://www.sqlite.org/queryplanner.html">http://www.sqlite.org/queryplanner.html</a>
	 * @return cursor with the results
	 */
	public Cursor queryAllByAscending() {
		return database.query(DATABASE_TABLE, KEYS_ALL, null, null, null, null,
				ToDoDatabase.KEY_TASK + " ASC");
	}

	public Cursor query(long rowId) throws SQLException {
		Cursor cursor = database.query(true, DATABASE_TABLE, KEYS_ALL,
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public ContentValues createContentValues(String task) {
		ContentValues values = new ContentValues();
		values.put(ToDoDatabase.KEY_TASK, task);
		return values;
	}

	private static class StudentDatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + ToDoDatabase.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ ToDoDatabase.KEY_TASK + " text not null " + ");";

		public StudentDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS todoTable");
			onCreate(db);
		}

	}

}
