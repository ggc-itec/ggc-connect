package edu.ggc.it.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import edu.ggc.it.rss.RSSFeed;
import edu.ggc.it.rss.RSSProvider;

/**
 * A singleton class that keeps track of the currently displayed feed.
 * This class's getCursor() method is to return a Cursor with information from RSSDatabase.
 * 
 * @author Derek
 *
 */
public class WidgetData
{
    /**
     * The singleton instance.
     */
    private static WidgetData instance;
        
    /**
     * The index of the current rss feed.
     * 0 = News,
     * 1 = Events.
     * Note: this isn't the best implementation just the easiest at the time
     */
    private int currentFeed = 0;
    
    /**
     * An array with all feeds stored in the RSSFeed enum.
     */
    private RSSFeed[] feeds = RSSFeed.values();
    
    /**
     * The singleton constructor, just leave blank
     */
    private WidgetData(){}
    
    /**
     * Singleton getInstance() method
     * @return instance		the instance of this class
     */
    public static WidgetData getInstance()
    {
	if(instance == null)
	{
	    instance = new WidgetData();
	}
	return instance;
    }
    
    /**
     * Increments the value of currentFeed only if the new value is a valid index of the feeds[] array.
     */
    public void switchFeed()
    {
	currentFeed = (currentFeed + 1 >= feeds.length)? 0 : ++currentFeed;
    }
    
    /**
     * Returns a cursor with the title and link information based on the current feed.
     * 
     * @param context
     * @return cursor
     */
    public Cursor getCursor(Context context)
    {
	ContentResolver resolver = context.getContentResolver();
	String[] columns = {RSSTable.COL_TITLE, RSSTable.COL_LINK};
	Cursor cursor = resolver.query(RSSProvider.CONTENT_URI,
					columns,
					RSSTable.COL_FEED + "=?",
					new String[] {feeds[currentFeed].title()},
					null);
	return cursor;
    }
    
    /**
     * @return the current RSSFeed
     */
    public RSSFeed getCurrentFeed()
    {
	return feeds[currentFeed];
    }
}
