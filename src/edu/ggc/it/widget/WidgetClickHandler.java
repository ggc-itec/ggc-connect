package edu.ggc.it.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import edu.ggc.it.R;
import edu.ggc.it.rss.RSSFeed;

/**
 * This class handles events when the user clicks one of the buttons on the widget.
 * Think of this class as an ActionListener.
 * The button that was clicked is determined in WidgetProvider.onReceive().
 * @author Derek
 *
 */
public class WidgetClickHandler
{
    private static WidgetData data = WidgetData.getInstance();
    
    private Context context;
    private int widgetID;
    private RemoteViews rv;
    private AppWidgetManager manager;
    
    /**
     * Constructor
     * @param context		The Context the widget is running in.
     * @param widgetID		The ID of the widget.
     */
    public WidgetClickHandler(Context context, int widgetID)
    {
	this.context = context;
	this.widgetID = widgetID;
	this.rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	this.manager = AppWidgetManager.getInstance(context);
    }
    
    /**
     * Shows previous View in the widget.
     */
    public void previousAction()
    {
	rv.showPrevious(R.id.widget_view_flipper);
	manager.partiallyUpdateAppWidget(widgetID, rv);
    }
    
    /**
     * Shows next View in the widget.
     */
    public void nextAction()
    {
	rv.showNext(R.id.widget_view_flipper);
	manager.partiallyUpdateAppWidget(widgetID, rv);
    }
    
    /**
     * Switch the feeds on the widget, changes title of the widget's banner.
     */
    public void switchAction()
    {
	data.switchFeed();
	manager.notifyAppWidgetViewDataChanged(widgetID, R.id.widget_view_flipper);
	
	RSSFeed feed = data.getCurrentFeed();
	rv.setTextViewText(R.id.widget_banner, feed.title());
	manager.updateAppWidget(widgetID, rv);
    }
    
    /**
     * Opens web browser to passed link.
     * @param link		The URL of the widget item clicked.
     */
    public void webAction(String link)
    {
	Uri uri = Uri.parse(link);
	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	context.startActivity(intent);
    }
}
