package edu.ggc.it.rss;

import java.util.ArrayList;
import java.util.List;

import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase.RSSTable;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** CLASS: RSSListFragment
 * This class inflates and returns a View (specifically ListView) to RSSPagerAdapter.
 * Each ListView sets a RSSListAdapter to get Views from.
 * @author Derek
 *
 */
public class RSSListFragment extends ListFragment
{
    public static final String FEED_INDEX_TAG = "edu.ggc.it.rss.RSSListFragment.FEED_INDEX_TAG";

    private static RSSFeed[] FEEDS = RSSFeed.values();
    private RSSListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
	View list = inflater.inflate(R.layout.rss_list, container, false);
	int index = getArguments().getInt(FEED_INDEX_TAG);
	adapter = new RSSListAdapter(getActivity(), FEEDS[index]);
	setListAdapter(adapter);
	return list;
    }

    /**
     * Called when user clicks on a List item. Opens web page of item clicked.
     * 
     * @param list	ListView where the click happened
     * @param view	View that was clicked within the ListView
     * @param position	position of the View item clicked
     * @param id	row id of the item that was clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
	String link = (String) adapter.getLinkAt(position);
	Uri uri = Uri.parse(link);
	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	startActivity(intent);
    }

    /**
     * Clean up by closing Cursor object in RSSListAdapter.
     */
    @Override
    public void onDestroy()
    {
	adapter.closeCursor();
	super.onDestroy();
    }
    
//==============================================Inner Class RSSListAdpater================================================//

    /**CLASS: RSSListAdapter
     * This class serves up Views to RSSListFragment.
     * These Views, defined in rss_list_row.xml are "filled" in getView().
     * @author Derek
     *
     */
    private static class RSSListAdapter extends BaseAdapter
    {
	private Context context;
	private RSSFeed feed;
	private LayoutInflater inflater;
	private List<CharSequence> links;
	private Cursor cursor;

	/**
	 * Holds references to TextViews from layout rss_list_row. Instead of
	 * reinflating Views each time getView() is called we store Views in
	 * this static class. This keeps calls to findViewById() to a minimum.
	 * 
	 * @author Derek
	 * 
	 */
	private static class ViewHolder
	{
	    TextView titleText;
	    TextView dateText;
	    TextView descriptionText;
	}

	/**
	 * Constructor Needs context to set up LayoutInflater
	 * 
	 * @param context	the Context of the calling class
	 * @param feed	RSSFeed that was selected
	 */
	public RSSListAdapter(Context context, RSSFeed feed)
	{
	    this.context = context;
	    this.feed = feed;
	    this.inflater = (LayoutInflater) context
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    this.links = new ArrayList<CharSequence>();
	    setCursor();
	}

	/**
	 * Sets the Cursor object by querying the RSSDatabase through the RSSProvider.
	 */
	private void setCursor()
	{
	    String[] columns = {RSSTable.COL_TITLE, RSSTable.COL_PUB_DATE, RSSTable.COL_DESCRIPTION, RSSTable.COL_LINK };
	    cursor = context.getContentResolver().query(
		    				RSSProvider.CONTENT_URI,
		    				columns,
		    				RSSTable.COL_FEED + "=?",
		    				new String[] {feed.title()},
		    				null);
	}

	/**
	 * Closes the cursor
	 */
	public void closeCursor()
	{
	    cursor.close();
	}

	/**
	 * @return the number of rss items (views) this adapter is returning
	 */
	@Override
	public int getCount()
	{
	    return cursor.getCount();
	}

	/**
	 * Returns a view for each rss item. Makes use of the ViewHolder pattern.
	 * 
	 * @param position	the current position for which the view is being generated
	 * @param convertView	a saved view, is null when method is first called
	 * @param parent	where the generated view will be placed
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
	    View view = convertView;
	    ViewHolder holder;

	    if (convertView == null)
	    {
		view = inflater.inflate(R.layout.rss_list_row, parent, false);
		
		holder = new ViewHolder();
		holder.titleText = (TextView) view.findViewById(R.id.rss_title);
		holder.dateText = (TextView) view.findViewById(R.id.rss_date);
		holder.descriptionText = (TextView) view.findViewById(R.id.rss_body);
		
		view.setTag(holder);
	    }
	    else
	    {
		holder = (ViewHolder) view.getTag();
	    }

	    setTextViews(holder, position);
	    return view;
	}
	
	/**
	 * Sets the various TextViews in rss_list_row.
	 * @param holder
	 * @param position
	 */
	private void setTextViews(ViewHolder holder, int position)
	{
	    if(cursor.moveToPosition(position))
	    {
		CharSequence title = getStrFromColumn(RSSTable.COL_TITLE);
		CharSequence date = getStrFromColumn(RSSTable.COL_PUB_DATE);
		CharSequence description = Html.fromHtml(getStrFromColumn(RSSTable.COL_DESCRIPTION));
		CharSequence link = getStrFromColumn(RSSTable.COL_LINK);
		
		holder.titleText.setText(title);
		holder.dateText.setText(date);
		holder.descriptionText.setText(description);
		if(links.size() < getCount())//Keep from adding duplicate links to ArrayList
		    links.add(link);
	    }
	}
	
	/**
	 * Gets the String from the specified column name.
	 * @param columnName
	 * @return
	 */
	private String getStrFromColumn(String columnName)
	{
	    return cursor.getString(cursor.getColumnIndex(columnName));
	}

	/**
	 * @param position	The index in the links ArrayList
	 * @return the link at the specified position.
	 */
	public CharSequence getLinkAt(int position)
	{
	    return links.get(position);
	}

	/*
	 * These methods are unused.
	 */
	@Override
	public Object getItem(int position){return null;}

	@Override
	public long getItemId(int position){return 0;}
    }
}
