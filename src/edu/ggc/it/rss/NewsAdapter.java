package edu.ggc.it.rss;

import java.util.List;

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
public class NewsAdapter extends BaseAdapter {

	private Context context;
	private List<String> titles;
	private List<String> dates;
	private List<String> descriptions;

	/**
	 * Needs context and the strings for each part of the rss view that it will
	 * be returned
	 * 
	 * @param context
	 *            Context in which the generated views should be place
	 * @param titles
	 *            Strings describing the rss item
	 * @param dates
	 *            Strings describing dates for each rss item
	 * @param descriptions
	 *            Strings describing the body of each rss item
	 */
	public NewsAdapter(Context context, List<String> titles,
			List<String> dates, List<String> descriptions) {
		this.context = context;
		this.titles = titles;
		this.dates = dates;
		this.descriptions = descriptions;
	}

	/**
	 * @return the number of rss items (views) this adapter is returning
	 */
	@Override
	public int getCount() {
		return titles.size();
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

		titleText.setText(titles.get(position));
		dateText.setText(dates.get(position));
		// why change the text to html for this? for some reason, the descriptions are in html!!
		// no big deal though, if they change it back to plain text, it won't break anything
		descriptionText.setText(Html.fromHtml(descriptions.get(position)));

		return row;
	}

}
