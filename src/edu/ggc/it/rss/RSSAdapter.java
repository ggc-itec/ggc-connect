package edu.ggc.it.rss;

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
 * 
 * NewsAdapter, which is an adapter, serves up the views for AdapterViews. What
 * is an AdapterView? It is view with multiple subviews that are similar. (e.g.
 * ListView, Spinner, Gallery)
 * 
 * This adapter will return views that will show the title and the published
 * date of a feed.
 * 
 * 
 * @author crystalist
 * 
 */
public class RSSAdapter extends BaseAdapter
{
    private Context context;
    private RSSFeed feed;
    private LayoutInflater inflater;
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
    public RSSAdapter(Context context, RSSFeed feed)
    {
	this.context = context;
	this.feed = feed;
	this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    /**
     * Sets the Cursor object by querying the RSSDatabase through the RSSProvider.
     */
    public void setCursor()
    {
	cursor = context.getContentResolver().query(RSSProvider.CONTENT_URI,
				new String[] {RSSTable.COL_TITLE, RSSTable.COL_PUB_DATE, RSSTable.COL_DESCRIPTION},
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
     * TODO: ignored for now
     */
    @Override
    public Object getItem(int position)
    {
	return null;
    }

    /**
     * TODO: ignored for now
     */
    @Override
    public long getItemId(int position)
    {
	return 0;
    }

    /**
     * Returns a view for each rss item. Makes use of the ViewHolder pattern
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
	    holder.titleText.setText(title);
	    holder.dateText.setText(date);
	    holder.descriptionText.setText(description);
	}
	return view;
    }
}
