package edu.ggc.it.rss;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSEnumSets.RSS_URL;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Activity responsible for displaying RSS feeds from ggc.edu
 * 
 * @author crystalist, Derek
 * 
 */
public class RSSActivity extends ListActivity implements RSSTaskComplete
{
    public static final String RSS_URL_EXTRA = "edu.ggc.it.rss.RSS_URL_EXTRA";

    private Context context;
    private RSSTask rssTask;
    private RSSAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rss);

	String rssURL = getIntent().getStringExtra(RSS_URL_EXTRA);
	context = this;
	rssTask = new RSSTask(this, context, true);
	rssTask.execute(getRSS_URL(rssURL));
	
	setRSSActivityTitle(rssURL);
	adapter = new RSSAdapter(context);
    }

    /**
     * Set the title of the activity based on the URL
     * 
     * @param rssURL		the URL passed as an extra 
     * @return url		RSS_URL that matches passed String
     */
    private void setRSSActivityTitle(String rssURL)
    {
	if(rssURL.equals(RSS_URL.NEWS.URL()))
	{
	    setTitle("GGC News");
	}
	else if(rssURL.equals(RSS_URL.EVENTS.URL()))
	{
	    setTitle("GGC Events");
	}
    }
    
    /**
     * Returns an RSS_URL enum with the same URL as the passed String
     * 
     * @param rssURL		the URL passed as an extra
     * @return url		RSS_URL that matches passed String
     */
    private RSS_URL getRSS_URL(String rssURL)
    {
	RSS_URL[] urls = RSS_URL.values();
	int index = 0;
	for(int i = 0; i < urls.length; i++)
	{
	    if(rssURL.equals(urls[i].URL()))
	    {
		index = i;
	    }
	}
	return urls[index];
    }

    /**
     * Called when user clicks on a List item Opens webpage of item
     * 
     * @param list		ListView where the click happened
     * @param view		View that was clicked within the ListView
     * @param position		position of the View item clicked
     * @param id		row id of the item that was clicked 
     */
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
	RSSDataContainer container = adapter.getContainer();
	Uri uri = Uri.parse(container.getLinkAt(position));
	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	startActivity(intent);
    }

    /**
     * Called when RSSTask finishes execution
     * 
     * @param containers	containers filled by RSSTask
     */
    @Override
    public void taskComplete(RSSDataContainer[] containers)
    {
	adapter.setContainer(containers[0]);
	setListAdapter(adapter);
    }
}
