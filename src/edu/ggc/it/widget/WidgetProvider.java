package edu.ggc.it.widget;

import edu.ggc.it.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * The WidgetProvider class which is a AppWidgetProvider a type of BroadcastReceiver.
 * Makes updates and receives actions based on user interaction with the widget.
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
    public static final String FILL_EXTRA = "edu.ggc.it.widget.WidgetProvider.FILLIN";
    
    /**
     * The singleton instance
     */
    private WidgetData data = WidgetData.getInstance();
    
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
	String action = intent.getAction();
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
	if (action.equals(SWITCH_ACTION))//The "switch" button clicked, changes RSSDataContainer and updates the text on the button
	{
	    data.switchContainer();
	    manager.notifyAppWidgetViewDataChanged(widgetID, R.id.widget_view_flipper);
	    
	    rv.setTextViewText(R.id.widget_switch_button, data.getTitle());
	    manager.updateAppWidget(widgetID, rv);
	}
	if(action.equals(WEB_ACTION))//When user clicks the widget_item returned from WidgetService, opens webpage to item clicked
	{
	    int index = intent.getIntExtra(FILL_EXTRA, 0);
	    Uri uri = Uri.parse(data.getCurrentContainer().getLinkAt(index));
	    Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
	    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(webIntent);
	}
	super.onReceive(context, intent);
    }
}
