package edu.ggc.it.rss;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
public class RSSAdapter extends BaseAdapter {

	private Context context;
	private RSSDataContainer container;

	/**
	 * Needs context and the strings for each part of the rss view that it will
	 * be returned
	 * 
	 * @param context
	 * 			the Context of the calling class
	 * @param container
	 * 			a container object that contains the data from the rss feed
	 */
	public RSSAdapter(Context context, RSSDataContainer container) {
		this.context = context;
		this.container = container;
	}

	/**
	 * @return the number of rss items (views) this adapter is returning
	 */
	@Override
	public int getCount() {
		return container.getTitlesSize();
	}

	/**
	 * TODO: ignored for now
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/**
	 * TODO: ignored for now
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * Returns a view for each rss item
	 * 
	 * @param position
	 *            the current position for which the view is being generated
	 * @param convertView
	 *            a saved view from last time, can't reuse it in this method
	 * @param parent
	 *            where the generated view will be placed
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// inflate view from xml, why not do this with code? i like to keep code to a minimum :)
		LinearLayout row = new LinearLayout(context);
		View.inflate(context, R.layout.rss_list_row, row);
		TextView titleText = (TextView) row.findViewById(R.id.rss_title);
		TextView dateText = (TextView) row.findViewById(R.id.rss_date);
		TextView descriptionText = (TextView) row.findViewById(R.id.rss_body);

		titleText.setText(container.getTitleAtIndex(position));
		dateText.setText(container.getPublishedDatesAtIndex(position));
		// why change the text to html for this? for some reason, the descriptions are in html!!
		// no big deal though, if they change it back to plain text, it won't break anything
		descriptionText.setText(Html.fromHtml(container.getDescriptionsAtIndex(position)));

		return row;
	}

}
