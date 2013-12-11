package edu.ggc.it.rss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ggc.it.R;
import edu.ggc.it.rss.RSSDatabase.RSSTable;

/**
 * This class serves up Views to RSSListFragment.
 * These Views, defined in rss_list_row, are "filled" in getView().
 * @author crystalist, Derek
 * 
 */
public class RSSListAdapter extends BaseAdapter
{
    private Context context;
    private RSSFeed feed;
    private LayoutInflater inflater;
    private List<CharSequence> links;
    private Cursor cursor;

    /**
     * Holds references to TextViews from layout rss_list_row.
     * Instead of reinflating Views each time getView() is called we store Views in this static class.
     * This keeps calls to findViewById() to a minimum.
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
     * Constructor
     * Needs context to set up LayoutInflater
     * 
     * @param context		the Context of the calling class
     * @param feed		RSSFeed that was selected
     */
    public RSSListAdapter(Context context, RSSFeed feed)
    {
	this.context = context;
	this.feed = feed;
	this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	this.links = new ArrayList<CharSequence>();
	setCursor();
    }
    
    /**
     * Sets the Cursor object by querying the RSSDatabase through the RSSProvider.
     */
    private void setCursor()
    {
	String[] columns = {RSSTable.COL_TITLE, RSSTable.COL_PUB_DATE, RSSTable.COL_DESCRIPTION, RSSTable.COL_LINK};
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
     * 
     * @param position		the current position for which the view is being generated
     * @param convertView	a saved view, is null when method is first called
     * @param parent		where the generated view will be placed
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

	if(cursor.moveToPosition(position))
	{
	    CharSequence title = cursor.getString(cursor.getColumnIndex(RSSTable.COL_TITLE));
	    CharSequence date = cursor.getString(cursor.getColumnIndex(RSSTable.COL_PUB_DATE));
	    CharSequence description = Html.fromHtml(cursor.getString(cursor.getColumnIndex(RSSTable.COL_DESCRIPTION)));
	    CharSequence link = cursor.getString(cursor.getColumnIndex(RSSTable.COL_LINK));
	    holder.titleText.setText(title);
	    holder.dateText.setText(date);
	    holder.descriptionText.setText(description);
	    if(links.size() < getCount())//Keeps from adding duplicate links to ArrayList
		links.add(link);
	}
	return view;
    }
    
    /**
     * @param position		The index in the links ArrayList
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
    public Object getItem(int position) {return null; }

    @Override
    public long getItemId(int position) {return 0;}
}
