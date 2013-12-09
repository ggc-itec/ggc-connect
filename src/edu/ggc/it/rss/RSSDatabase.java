package edu.ggc.it.rss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RSSDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME = "rss.db";
    private static final int DB_VERSION = 1;
    private static RSSDatabase instance = null;
    
    private RSSDatabase(Context context)
    {
	super(context, DB_NAME, null, DB_VERSION);
    }
    
    public static RSSDatabase getInstance(Context context)
    {
	if (instance == null)
	    instance = new RSSDatabase(context);
	return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
	RSSTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2)
    {
	RSSTable.onUpgrade(db);
    }
    
    public static class RSSTable
    {
	//The name of the table
	public static final String TABLE_NAME = "rsstable";
	//Column headers in this table
	public static final String COL_ID = "_id";
	public static final String COL_TITLE = "title";
	public static final String COL_LINK = "link";
	public static final String COL_DESCRIPTION = "description";
	public static final String COL_PUB_DATE = "pubDate";
	public static final String COL_FEED = "feed";
	
	public static void onCreate(SQLiteDatabase db)
	{
	    db.execSQL("create table " + TABLE_NAME + " (" +
		    			COL_ID + " integer primary key autoincrement," + 
		    			COL_TITLE + " text not null," +
		    			COL_LINK + " text," +
		    			COL_DESCRIPTION + " text," +
		    			COL_PUB_DATE + " text," +
		    			COL_FEED + " text" + 
		    			")");
	}
	
	public static void onUpgrade(SQLiteDatabase db)
	{
	    drop(db);
	    onCreate(db);
	}
	
	public static void drop(SQLiteDatabase db)
	{
	    db.execSQL("drop table if exists " + TABLE_NAME);
	}
    }
}
