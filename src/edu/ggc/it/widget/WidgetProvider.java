package edu.ggc.it.widget;

import edu.ggc.it.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider
{
    public static final String PREVIOUS_ACTION = "edu.ggc.it.widget.WidgetProvider.PREVIOUS";
    public static final String NEXT_ACTION = "edu.ggc.it.widget.WidgetProvider.NEXT";
    public static final String SWITCH_ACTION = "edu.ggc.it.widget.WidgetProvider.SWITCH";
    public static final String WEB_ACTION = "edu.ggc.it.widget.WidgetProvider.WEB";
    public static final String FILL_EXTRA = "edu.ggc.it.widget.WidgetProvider.FILLIN";
    
    private WidgetData data = WidgetData.getInstance();
    
    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds)
    {
	for(int i = 0; i < widgetIds.length; i++)
	{
	    WidgetUpdater updater = new WidgetUpdater(context, widgetManager, widgetIds[i]);
	    updater.updateWidget();
	}
	super.onUpdate(context, widgetManager, widgetIds);
    } 
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
	String action = intent.getAction();
	AppWidgetManager manager = AppWidgetManager.getInstance(context);
	RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	int widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
	
	if (action.equals(PREVIOUS_ACTION))
	{
	    rv.showPrevious(R.id.widget_view_flipper);
	    manager.partiallyUpdateAppWidget(widgetID, rv);
	}
	if (action.equals(NEXT_ACTION))
	{
	    rv.showNext(R.id.widget_view_flipper);
	    manager.partiallyUpdateAppWidget(widgetID, rv);
	}
	if (action.equals(SWITCH_ACTION))
	{
	    data.setCurrent((data.getCurrent() == 0) ? 1 : 0);
	    manager.notifyAppWidgetViewDataChanged(widgetID, R.id.widget_view_flipper);
	    
	    rv.setTextViewText(R.id.widget_switch_button, data.getTitle());
	    manager.updateAppWidget(widgetID, rv);
	}
	if(action.equals(WEB_ACTION))
	{
//	    Toast.makeText(context, "Flipper clicked", Toast.LENGTH_SHORT).show();
	    int index = intent.getIntExtra(FILL_EXTRA, 0);
	    Uri uri = Uri.parse(data.getContainer().getLinkAt(index));
	    Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
	    webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(webIntent);
	}
	super.onReceive(context, intent);
    }
}
