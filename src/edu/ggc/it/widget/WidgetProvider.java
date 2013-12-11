package edu.ggc.it.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import edu.ggc.it.rss.RSSDatabase;

/**
 * The WidgetProvider class which is a AppWidgetProvider a type of BroadcastReceiver.
 * Makes updates and receives actions based on user interaction with the widget.
 * 
 * @author Derek
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
    public static final String FILL_EXTRA = "edu.ggc.it.widget.WidgetProvider.FILL_IN";
            
    /**
     * Updates the widget everytime it is created and for every update period.
     * The update period is defined in the appwidget-provider xml file which for this widget is located in /res/xml/widget_info.xml
     * Loops through every active widget and creates a WidgetUpdater for each one.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds)
    {
	RSSDatabase db = RSSDatabase.getInstance(context);
	db.onUpgrade(db.getWritableDatabase(), 0, 0);
	
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
	String action = intent.getAction();//the action defined by the received Intent
	int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
	WidgetClickHandler clickHandler = new WidgetClickHandler(context, widgetID);
	
	if (action.equals(PREVIOUS_ACTION))//Previous button clicked
	{
	    clickHandler.previousAction();
	}
	if (action.equals(NEXT_ACTION))//Next button clicked
	{
	    clickHandler.nextAction();
	}
	if (action.equals(SWITCH_ACTION))//The "switch" button clicked, changes RSSFeed and updates the text on the banner
	{
	    clickHandler.switchAction();
	}
	if(action.equals(WEB_ACTION))//When user clicks the widget_item returned from WidgetService, opens web page to item clicked
	{
	    String link = intent.getStringExtra(FILL_EXTRA);
	    clickHandler.webAction(link);
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
