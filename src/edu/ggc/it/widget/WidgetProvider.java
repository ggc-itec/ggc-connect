package edu.ggc.it.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import edu.ggc.it.rss.RSSFeed;

/**
 * The WidgetProvider class which is a AppWidgetProvider a type of BroadcastReceiver.
 * Makes updates and receives actions based on user interaction with the widget.
 * @author Derek
 * 
 * NOTE:
 * 	If the data for the widget is ever cleared from RAM the widget will crash whenever the Switch button is clicked.
 * 	This was a design oversight on my part, the Android guide recommends using a ContentProvider to store data.
 * 	Instead this widget uses RSSDataContainers, essentially a bunch of ArrayLists, that will potentially get cleared from RAM.
 *
 */
public class WidgetProvider extends AppWidgetProvider
{
    /*
     * The actions associated with this receiver
     */
    public static final String PREVIOUS_ACTION = "edu.ggc.it.widget.WidgetProvider.PREVIOUS";
    public static final String NEXT_ACTION = "edu.ggc.it.widget.WidgetProvider.NEXT";
    public static final String SWITCH_ACTION = "edu.ggc.it.widget.WidgetProvider.SWITCH";
    public static final String WEB_ACTION = "edu.ggc.it.widget.WidgetProvider.WEB";
    /*
     * The fill extra for the widget_items
     */
    public static final String FILL_EXTRA = "edu.ggc.it.widget.WidgetProvider.FILLIN";
    
    @Override
    public void onEnabled(Context context)
    {
	RSSDatabase db = RSSDatabase.getInstance(context);
	db.onUpgrade(db.getWritableDatabase(), 0, 0);
	super.onEnabled(context);
    }
        
    /**
     * Updates the widget everytime it is created and for every update period.
     * The update period is defined in the appwidget-provider xml file which for this widget is located in /res/xml/widget_info.xml
     * Loops through every active widget and creates a WidgetUpdater for each one.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds)
    {
	for(int i = 0; i < widgetIds.length; i++)
	{
	    WidgetUpdater updater = new WidgetUpdater(context, widgetIds[i]);
	    updater.updateWidget();
	}
	super.onUpdate(context, widgetManager, widgetIds);
    } 
    
    /**
     * Receives Intents that are defined as PendingIntents in WidgetUpdater.
     * These PendingIntents each have an button and action associated with them that is received by this method.
     * This method then distinguishes which button was pressed by received action.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
	WidgetData data = WidgetData.getInstance();
	String action = intent.getAction();//the action defined by the received Intent
	AppWidgetManager manager = AppWidgetManager.getInstance(context);
	RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
	
	if (action.equals(PREVIOUS_ACTION))//Previous button clicked
	{
	    rv.showPrevious(R.id.widget_view_flipper);
	    manager.partiallyUpdateAppWidget(widgetID, rv);
	}
	if (action.equals(NEXT_ACTION))//Next button clicked
	{
	    rv.showNext(R.id.widget_view_flipper);
	    manager.partiallyUpdateAppWidget(widgetID, rv);
	}
	if (action.equals(SWITCH_ACTION))//The "switch" button clicked, changes RSSFeed and updates the text on the banner
	{
	    data.switchFeed();
	    manager.notifyAppWidgetViewDataChanged(widgetID, R.id.widget_view_flipper);
	    
	    RSSFeed currentFeed = data.getCurrentFeed();
	    rv.setTextViewText(R.id.widget_banner, currentFeed.title());
	    manager.updateAppWidget(widgetID, rv);
	}
	if(action.equals(WEB_ACTION))//When user clicks the widget_item returned from WidgetService, opens webpage to item clicked
	{
	    int index = intent.getIntExtra(FILL_EXTRA, 0);
	    
	    Cursor cursor = data.getCursor(context);
	    String link = "";
	    if(cursor.moveToPosition(index))
	    {
		link = cursor.getString(cursor.getColumnIndex(RSSTable.COL_LINK));
	    }
	    cursor.close();
	    
	    Uri uri = Uri.parse(link);
	    Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
	    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(webIntent);
	}
	super.onReceive(context, intent);
    }

    @Override
    public void onDisabled(Context context)
    {
	RSSDatabase.getInstance(context).close();
	super.onDisabled(context);
    }
}
