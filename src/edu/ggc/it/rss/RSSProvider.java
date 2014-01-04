package edu.ggc.it.rss;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import edu.ggc.it.rss.RSSDatabase.RSSTable;

/**
 * The ContentProvider for the RSSDatabase.
 * A ContentProvider acts like as abstraction layer between a database and your application.
 * This Provider is used in both the rss and widget package to query and insert data into the database.
 * 
 * @author Derek
 *
 */
public class RSSProvider extends ContentProvider
{
    private RSSDatabase dbHelper;
    private static final String AUTH = "edu.ggc.it.rss.RSSProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTH + "/" + RSSTable.TABLE_NAME);
    
    @Override
    public boolean onCreate()
    {
	dbHelper = RSSDatabase.getInstance(getContext());
	return true;
    }
    
    /**
     * Inserts data into the RSSDatabase.
     * Since the database has only one table the Uri doesn't need to be matched to a specific table.
     * 
     * @param uri		The Uri identifying where to insert data (not used here).
     * @param values		ContentValues is what represents a row of data in the table.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
	SQLiteDatabase db = dbHelper.getWritableDatabase();
	db.insert(RSSTable.TABLE_NAME, null, values);
	return uri;
    }

    /**
     * Query's the RSSDatabase.
     * 
     * @param uri		The Uri identifying what table to query the data from (not used here).
     * @param columns		Array representing the table columns we want.
     * @param selection		Column we want to compare to selectionArgs.
     * 				NOTE: When comparing selection to selectionArgs you must concatenate "=?" to the end of selection
     * @param selectionArgs	Array of column(s) we want to compare selection with. The combination of selection and sectionArgs is what
     * 				constitutes the WHERE clause in SQL. Example: WHERE COL_FEED = GGC News, etc.
     * @param sortOrder		The sort order you want for the returned Cursor.
     * 
     * @return cursor		A Cursor representing the data queried from the database.
     */
    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder)
    {
	SQLiteDatabase db = dbHelper.getReadableDatabase();
	Cursor cursor = db.query(RSSTable.TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
	cursor.setNotificationUri(getContext().getContentResolver(), uri);
	return cursor;
    }
    
    //These methods are unused
    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {return 0; }

    @Override
    public String getType(Uri arg0){return null;}

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3){return 0;}

}
