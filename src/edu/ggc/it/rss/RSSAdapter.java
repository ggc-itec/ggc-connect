package edu.ggc.it.rss;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.ggc.it.R;

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
    private LayoutInflater inflater;
    private RSSDataContainer container;

    /**
     * This is the ViewHolder pattern.
     * Holds references to TextViews from layout rss_list_row.
     * Instead of reinflating Views each time getView() is called
     * we store Views in this static class
     * This keeps calls to findViewById() to a minimum
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
     */
    public RSSAdapter(Context context)
    {
	inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    

    /**
     * @return the number of rss items (views) this adapter is returning
     */
    @Override
    public int getCount()
    {
	return container.getTitlesSize();
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

	holder.titleText.setText(container.getTitleAt(position));
	holder.dateText.setText(container.getDateAt(position));
	holder.descriptionText.setText(Html.fromHtml(container.getDescriptionAt(position)));

	return view;
    }
    
    public void setContainer(RSSDataContainer container)
    {
	this.container = container;
    }

    public RSSDataContainer getContainer()
    {
	return container;
    }
}
