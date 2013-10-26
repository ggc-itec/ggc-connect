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
	public static final String RSS_URL_EXTRA = "edu.ggc.it.rss.RSS_URL";
	
	private Context context;
	private RSSDataContainer container;
	private RSSTask rssTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss);
		
		context = this;
		container = new RSSDataContainer(getIntent().getStringExtra(RSS_URL_EXTRA));
		
		setRSSActivityTitle();
		
		rssTask = new RSSTask(this, container);
		rssTask.execute();
	}
	
	/**
	 * Set the title of the activity based on the URL passed from Main
	 */
	private void setRSSActivityTitle()
	{
		if(container.getURL().equals(RSS_URL.NEWS.toString()))
		{
			setTitle("GGC News");
		}
		else if(container.getURL().equals(RSS_URL.EVENTS.toString()))
		{
			setTitle("GGC Events");
		}
	}
	
	/**
	 * Called when user clicks on a List item
	 * Opens webpage of item
	 * @param l
	 * @param v
	 * @param position
	 * 			the position of the View item click the only passed argument used in method
	 * @param id
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   Uri uri = Uri.parse(container.getLinkAtIndex(position));
	   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	   startActivity(intent);
	}

	/**
	 * Override from RSSTaskComplete interface
	 * This method is called when RSSTask finishes execution
	 */
	@Override
	public void taskComplete() {
		RSSAdapter adapter = new RSSAdapter(context,container);
		setListAdapter(adapter);	
	}
}
