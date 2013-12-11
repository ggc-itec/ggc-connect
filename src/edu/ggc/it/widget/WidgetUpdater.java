package edu.ggc.it.widget;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSFeed;
import edu.ggc.it.rss.RSSTask;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * A class that updates the information for each widget.
 * For each widget that is available a new WidgetUpdater is created.
 * Keep in mind there can be multiple of the same widget on the home screen.
 * @author Derek
 *
 */
public class WidgetUpdater implements RSSTask.RSSTaskComplete
{
    private Context context;
    private AppWidgetManager manager;
    private int widgetID;
    
    /**
     * The RemoteViews object that points to widget_layout
     */
    private RemoteViews rv;
    
    /**
     * The Intent that points to the WidgetService class
     */
    private Intent serviceIntent;
    
    /**
     * Constructor
     * @param context		the Context of the created widget
     * @param widgetID		the ID of the widget, corresponds to its index value in the widgetIds[] array
     */
    public WidgetUpdater(Context context, int widgetID)
    {
	//create and execute a new RSSTask
	new RSSTask(this, context, false).execute(RSSFeed.values());
	
	this.context = context;
	this.manager = AppWidgetManager.getInstance(context);
	this.widgetID = widgetID;
	
	rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	serviceIntent = new Intent(context, WidgetService.class);	
    }
    
    /**
     * Called in onUpdate() this method updates the data for the widget
     */
    public void updateWidget()
    {
	embedData();
	setUpPendingIntents();
    }
    
    /**
     * Embeds widgetID into serviceIntent, setData() is needed to embed the data otherwise any extras will be ignored.
     */
    private void embedData()
    {
	serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
	serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
    }
    
    /**
     * Sets up all onClickPendingIntents for each of the buttons in the widget.
     * The PendingIntentTemplate sets up a template that is filled by each of views when they are created.
     */
    private void setUpPendingIntents()
    {
	rv.setOnClickPendingIntent(R.id.widget_previous_button, getPendingIntent(WidgetProvider.PREVIOUS_ACTION));
	rv.setOnClickPendingIntent(R.id.widget_next_button, getPendingIntent(WidgetProvider.NEXT_ACTION));
	rv.setOnClickPendingIntent(R.id.widget_switch_button, getPendingIntent(WidgetProvider.SWITCH_ACTION));
	rv.setPendingIntentTemplate(R.id.widget_view_flipper, getPendingIntent(WidgetProvider.WEB_ACTION));
    }
    
    /**
     * Creates a PendingIntent with the passed String action. This String will be compared in the onReceive() method
     * to determine which button was pressed
     * @param action	the action that will be compared in onReceive()
     * @return 	a PendingIntent that points to the WidgetProvider class
     *		these Intents are captured in the onReceive() method
     */
    private PendingIntent getPendingIntent(String action)
    {
	Intent onClickIntent = new Intent(context, WidgetProvider.class);
	onClickIntent.setAction(action);
	onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
	onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
	return PendingIntent.getBroadcast(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * When RSSTask finishes it calls this method.
     * In this case the Adapter is actually a WidgetService inner class.
     * Finally the widget is updated with the widgetID and RemoteViews object
     */
    @SuppressWarnings("deprecation")
    @Override
    public void taskComplete()
    {
	rv.setRemoteAdapter(widgetID, R.id.widget_view_flipper, serviceIntent);
	manager.updateAppWidget(widgetID, rv);
    }
}
