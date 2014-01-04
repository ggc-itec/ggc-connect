package edu.ggc.it.widget;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import edu.ggc.it.rss.RSSFeed;
import edu.ggc.it.rss.RSSProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**CLASS: WidgetService
 * The WidgetService class which is a RemoteViewsService a type of Service.
 * For a simple collection widget like this one we only need to override the onGetViewFactory() method.
 * 
 * @author Derek
 *
 */
public class WidgetService extends RemoteViewsService
{
    private static final RSSFeed[] FEEDS = RSSFeed.values();
    
    /**
     * The index value of the currently displayed fee in the above RSSFeed[] array.
     * 0 = NEWS
     * 1 = EVENTS
     * 2 = GET INVOLVED
     */
    private static int feedIndex = 0;
    
    /**
     * Increments the value of feedIndex only if the next index value is less than FEEDS.length - 1 else sets index back to 0.
     * This creates a looping effect so when users click the Switch button.
     */
    public static void switchFeed()
    {
	feedIndex = (feedIndex + 1 >= FEEDS.length)? 0 : ++feedIndex;
    }
    
    /**
     * @return the current RSSFeed.
     */
    public static RSSFeed getCurrentFeed()
    {
	return FEEDS[feedIndex];
    }
    
    /**
     * Returns a ViewsFactory object.
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
	return new ViewsFactory(this.getApplicationContext());
    }
    
    /**CLASS: ViewsFactory
     * The ViewsFactory class implements RemoteViewsFactory which is a thin wrapper for an Adapter.
     * This class returns views from data in the RSSProvider.
     * @author Derek
     *
     */
    private class ViewsFactory implements RemoteViewsService.RemoteViewsFactory
    {
	private Context context;
	private Cursor cursor;
	
	/**
	 * Constructor
	 * 
	 * @param context	The Context the widget is running in.
	 */
	public ViewsFactory(Context context)
	{
	    this.context = context;
	}
	
	/**
	 * This method is called when the ViewsFactory is first created and whenever notifyAppWidgetViewDataChanged() is called.
	 */
	@Override
	public void onDataSetChanged()
	{
	    closeCursor();
	    setCursor();
	}
	
	/**
	 * Closes the cursor if it has been instantiated.
	 */
	private void closeCursor()
	{
	    if(cursor != null)
		cursor.close();
	}
		
	/**
	 * Sets the Cursor for this class.
	 */
	public void setCursor()
	{
	    ContentResolver resolver = context.getContentResolver();
	    String[] columns = {RSSTable.COL_TITLE, RSSTable.COL_LINK};
	    this.cursor = resolver.query(RSSProvider.CONTENT_URI,
		    			columns,
		    			RSSTable.COL_FEED + "=?",
		    			new String[] {FEEDS[feedIndex].title()},
		    			null);
	}

	/**
	 * @return the amount of items this Factory will return
	 */
	@Override
	public int getCount()
	{
	    return cursor.getCount();
	}

	/**
	 * The loading view is shown if something is taking a long time to process.
	 * Returning null uses the default loading view.
	 * A custom loading view can be created in xml.
	 */
	@Override
	public RemoteViews getLoadingView()
	{
	    return null;
	}

	/**
	 * Returns RemoteViews that display data at the supplied index.
	 * Also fills in the PendingIntentTemplate with the data's link.
	 */
	@Override
	public RemoteViews getViewAt(int index)
	{
	    if(index < 0 || index >= getCount()) {
		return getLoadingView();
	    }
	    
	    RemoteViews widgetItem = new RemoteViews(context.getPackageName(), R.layout.widget_item);
	    
	    String title = "";
	    String link = "";
	    if(cursor.moveToPosition(index))
	    {
		title = cursor.getString(cursor.getColumnIndex(RSSTable.COL_TITLE));
		link = cursor.getString(cursor.getColumnIndex(RSSTable.COL_LINK));
	    }
	    widgetItem.setTextViewText(R.id.widget_titles_textview, title);
	    
	    widgetItem.setOnClickFillInIntent(R.id.widget_titles_textview, getFillIntent(link));
	    
	    return widgetItem;
	}
	
	/**
	 * Fills in the PendingIntentTemplate with the data's link.
	 * @param link		The URL of the displayed View.
	 * @return fillIntent
	 */
	private Intent getFillIntent(String link)
	{
	    Intent fillIntent = new Intent();
	    fillIntent.putExtra(WidgetProvider.FILL_EXTRA, link);
	    return fillIntent;
	}

	/**
	 * @return the number of types of views returned by this factory
	 */
	@Override
	public int getViewTypeCount()
	{
	    return 1;
	}
	
	/**
	 * Returns true if the same id refers to the same object
	 */
	@Override
	public boolean hasStableIds()
	{
	    return true;
	}
	
	/**
	 * Called when all RemoteViewsAdapters are unbound
	 */
	@Override
	public void onDestroy()
	{
	    closeCursor();
	}
	
	/*
	 * These methods are unused
	 */
	@Override
	public long getItemId(int index){return 0;}

	@Override
	public void onCreate(){}
    }
}
