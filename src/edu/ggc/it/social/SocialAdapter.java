package edu.ggc.it.social;

import java.util.List;

import edu.ggc.it.R;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SocialAdapter extends BaseAdapter {

	private Context context;
	private List<String> names;
	private List<String> titles;
	private List<String> dates;
	private List<String> contents;

	public SocialAdapter(Context context, List<String> names,
			List<String> titles, List<String> dates, List<String> contents) {
		this.context = context;
		this.names = names;
		this.titles = titles;
		this.dates = dates;
		this.contents = contents;
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// inflate view from xml, why not do this with code? i like to keep code
		// to a minimum :)
		LinearLayout row = new LinearLayout(context);
		View.inflate(context, R.layout.rss_list_row, row);
		TextView titleText = (TextView) row.findViewById(R.id.rss_title);
		TextView dateText = (TextView) row.findViewById(R.id.rss_date);
		TextView descriptionText = (TextView) row.findViewById(R.id.rss_body);

		titleText.setText(titles.get(position));
		dateText.setText(dates.get(position)  + "  from " + names.get(position));
		// why change the text to html for this? for some reason, the
		// descriptions are in html!!
		// no big deal though, if they change it back to plain text, it won't
		// break anything
		descriptionText.setText(Html.fromHtml(contents.get(position)));

		return row;
	}

}
