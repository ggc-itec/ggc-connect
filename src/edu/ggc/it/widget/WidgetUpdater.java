package edu.ggc.it.widget;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDataContainer;
import edu.ggc.it.rss.RSSEnumSets.RSS_URL;
import edu.ggc.it.rss.RSSTask;
import edu.ggc.it.rss.RSSTaskComplete;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class WidgetUpdater implements RSSTaskComplete
{
    private Context context;
    private AppWidgetManager manager;
    private int widgetID;
    private RemoteViews rv;
    private Intent intent;
    
    public WidgetUpdater(Context context, AppWidgetManager manager, int widgetID)
    {
	new RSSTask(this, context, false).execute(RSS_URL.NEWS, RSS_URL.EVENTS);
	
	this.context = context;
	this.manager = manager;
	this.widgetID = widgetID;
	
	rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	intent = new Intent(context, WidgetService.class);
    }
    
    public void updateWidget()
    {
	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
	intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
	rv.setOnClickPendingIntent(R.id.widget_previous_button, getPendingIntent(WidgetProvider.PREVIOUS_ACTION));
	rv.setOnClickPendingIntent(R.id.widget_next_button, getPendingIntent(WidgetProvider.NEXT_ACTION));
	rv.setOnClickPendingIntent(R.id.widget_switch_button, getPendingIntent(WidgetProvider.SWITCH_ACTION));
	rv.setPendingIntentTemplate(R.id.widget_view_flipper, getPendingIntent(WidgetProvider.WEB_ACTION));
    }
    
    private PendingIntent getPendingIntent(String action)
    {
	Intent onClickIntent = new Intent(context, WidgetProvider.class);
	onClickIntent.setAction(action);
	onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
	onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
	return PendingIntent.getBroadcast(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void taskComplete(RSSDataContainer[] containers)
    {
	WidgetData.getInstance().setContainers(containers);
	rv.setRemoteAdapter(widgetID, R.id.widget_view_flipper, intent);
	manager.updateAppWidget(widgetID, rv);
    }
}
