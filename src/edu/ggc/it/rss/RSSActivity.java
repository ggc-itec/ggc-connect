package edu.ggc.it.rss;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import edu.ggc.it.rss.RSSTask.RSSTaskComplete;

/**
 * Activity responsible for displaying RSS feeds from ggc.edu
 * 
 * @author crystalist, Derek
 * 
 */
public class RSSActivity extends ListActivity implements RSSTaskComplete
{
    public static final String RSS_URL_EXTRA = "edu.ggc.it.rss.RSS_URL_EXTRA";

    private RSSFeed feed;
    private Context context;
    private RSSTask rssTask;
    private RSSAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rss);

	String rssURL = getIntent().getStringExtra(RSS_URL_EXTRA);
	feed = getRSSFeed(rssURL);
	context = this;
	
	setUpDatabase();
	
	rssTask = new RSSTask(this, context, true);
	rssTask.execute(RSSFeed.values());
	
	setTitle(feed.title());
	adapter = new RSSAdapter(context, feed);
    }
    
    /**
     * Simply sets up the database to be used in the rss package
     */
    private void setUpDatabase()
    {
	RSSDatabase db = RSSDatabase.getInstance(context);
	db.onUpgrade(db.getWritableDatabase(), 0, 0);
    }
    
    /**
     * Returns an RSSFeed enum with the same URL as the passed String
     * 
     * @param rssURL		the URL passed as an extra
     * @return RSSFeed that matches passed String
     */
    private RSSFeed getRSSFeed(String rssURL)
    {
	RSSFeed[] feeds = RSSFeed.values();
	int index = 0;
	for(int i = 0; i < feeds.length; i++)
	{
	    if(rssURL.equals(feeds[i].URL()))
	    {
		index = i;
	    }
	}
	return feeds[index];
    }

    /**
     * Called when user clicks on a List item. Opens web page of item clicked.
     * Makes a query to the RSSProvider to get the appropriate link.
     * 
     * @param list		ListView where the click happened
     * @param view		View that was clicked within the ListView
     * @param position		position of the View item clicked
     * @param id		row id of the item that was clicked 
     */
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id)
    {
	ContentResolver resolver = context.getContentResolver();
	Cursor cursor = resolver.query(RSSProvider.CONTENT_URI,
					new String[] {RSSTable.COL_LINK},
					RSSTable.COL_FEED + "=?",
					new String[] {feed.title()},
					null);
	if(cursor.moveToPosition(position))
	{
	    String url = cursor.getString(cursor.getColumnIndex(RSSTable.COL_LINK));
	    Uri uri = Uri.parse(url);
	    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	    cursor.close();
	    startActivity(intent);
	}
    }

    /**
     * Called when RSSTask finishes execution
     */
    @Override
    public void taskComplete()
    {
	adapter.setCursor();
	setListAdapter(adapter);
    }

    @Override
    protected void onDestroy()
    {
	adapter.closeCursor();
	super.onDestroy();
    }
}
