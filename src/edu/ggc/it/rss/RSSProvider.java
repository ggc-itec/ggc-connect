package edu.ggc.it.rss;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import edu.ggc.it.rss.RSSDatabase.RSSTable;

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
    
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
	SQLiteDatabase db = dbHelper.getWritableDatabase();
	db.insert(RSSTable.TABLE_NAME, null, values);
	db.close();
	return uri;
    }

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
