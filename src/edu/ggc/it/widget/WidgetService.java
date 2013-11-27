package edu.ggc.it.widget;

import edu.ggc.it.R;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * The WidgetService class which is a RemoteViewsService is a type of Service.
 * For a simple collection widget like this one we only need to override the onGetViewFactory() method
 * @author Derek
 *
 */
public class WidgetService extends RemoteViewsService
{
    private static final String TAG = "WidgetService";
    /**
     * Returns a ViewsFactory object
     */
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
	return new ViewsFactory(intent);
    }
    
    /**
     * The ViewsFactory class implements RemoteViewsFactory which is a thin wrapper for an Adapter.
     * This class returns views for any underlying data which in this case is stored in the WidgetData singleton
     * @author Derek
     *
     */
    private class ViewsFactory implements RemoteViewsService.RemoteViewsFactory
    {
	private Intent intent;
	private RemoteViews rv;
	private WidgetData data = WidgetData.getInstance();
	
	/**
	 * Constructor
	 * @param intent	the Intent passed from WidgetUpdater(Unused for now)
	 */
	public ViewsFactory(Intent intent)
	{
	    this.intent = intent;
	    rv = new RemoteViews(getPackageName(), R.layout.widget_item);
	}

	/**
	 * @return the amount of items this Factory will return
	 */
	@Override
	public int getCount()
	{
	    return data.getCount();
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
		return null;
	    }
	    String title = data.getCurrentContainer().getTitleAt(index);
	    rv.setTextViewText(R.id.widget_title_textview, title);
	    
	    Intent fillIntent = new Intent();
	    fillIntent.putExtra(WidgetProvider.FILL_EXTRA, index);
	    rv.setOnClickFillInIntent(R.id.widget_title_textview, fillIntent);
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
	
	/*
	 * These methods are unused
	 */
	@Override
	public long getItemId(int index)
	{
	    return 0;
	}

	@Override
	public void onCreate(){}

	@Override
	public void onDataSetChanged(){}

	@Override
	public void onDestroy(){}
    }
}
