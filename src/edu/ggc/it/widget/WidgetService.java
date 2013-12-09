package edu.ggc.it.widget;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * The WidgetService class which is a RemoteViewsService a type of Service.
 * For a simple collection widget like this one we only need to override the onGetViewFactory() method
 * @author Derek
 *
 */
public class WidgetService extends RemoteViewsService
{
    /**
     * Returns a ViewsFactory object
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
	return new ViewsFactory(this.getApplicationContext());
    }
    
    /**
     * The ViewsFactory class implements RemoteViewsFactory which is a thin wrapper for an Adapter.
     * This class returns views for any underlying data which in this case is stored in the WidgetData singleton
     * @author Derek
     *
     */
    private class ViewsFactory implements RemoteViewsService.RemoteViewsFactory
    {
	private RemoteViews rv;
	private Context context;
	private WidgetData data = WidgetData.getInstance();
	private Cursor cursor;
	
	/**
	 * Constructor
	 * @param intent	the Intent passed from WidgetUpdater(Unused for now)
	 */
	public ViewsFactory(Context context)
	{
	    this.rv = new RemoteViews(getPackageName(), R.layout.widget_item);
	    this.context = context;
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
	 * Also fills in the PendingIntentTemplate with the data's index.
	 */
	@Override
	public RemoteViews getViewAt(int index)
	{
	    if(index < 0 || index >= getCount()) {
		return getLoadingView();
	    }
	    
	    String title = "";
	    if(cursor.moveToPosition(index))
	    {
		title = cursor.getString(cursor.getColumnIndex(RSSTable.COL_TITLE));
	    }
	    rv.setTextViewText(R.id.widget_titles_textview, title);
	    
	    Intent fillIntent = new Intent();
	    fillIntent.putExtra(WidgetProvider.FILL_EXTRA, index);
	    rv.setOnClickFillInIntent(R.id.widget_titles_textview, fillIntent);
	    return rv;
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
	
	@Override
	public void onDataSetChanged()
	{
	    cursor = data.getCursor(context);
	}
	
	@Override
	public void onDestroy()
	{
	    if(cursor != null)
		cursor.close();
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
