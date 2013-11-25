package edu.ggc.it.widget;

import edu.ggc.it.R;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
	return new ViewsFactory(intent);
    }
    
    private class ViewsFactory implements RemoteViewsService.RemoteViewsFactory
    {
	private Intent intent;
	private RemoteViews rv;
	private WidgetData data = WidgetData.getInstance();
	
	public ViewsFactory(Intent intent)
	{
	    this.intent = intent;
	    rv = new RemoteViews(getPackageName(), R.layout.widget_item);
	}

	@Override
	public int getCount()
	{
	    return data.getCount();
	}

	@Override
	public RemoteViews getLoadingView()
	{
	    return null;
	}

	@Override
	public RemoteViews getViewAt(int index)
	{
	    String title = data.getContainer().getTitleAt(index);
	    rv.setTextViewText(R.id.widget_title_textview, title);
	    
	    Intent fillIntent = new Intent();
	    fillIntent.putExtra(WidgetProvider.FILL_EXTRA, index);
	    rv.setOnClickFillInIntent(R.id.widget_title_textview, fillIntent);
	    return rv;
	}

	@Override
	public int getViewTypeCount()
	{
	    return 1;
	}
	
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
