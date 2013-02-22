package edu.ggc.it;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class News extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		setTitle("Links");
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.websites)));

	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		String selection = getListAdapter().getItem(position).toString();

		if (selection.equals("Clawmail")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://clawmail.ggc.edu"));
			startActivity(browserIntent);
		} else if (selection.equals("Banner")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("https://ggc.gabest.usg.edu/pls/B400/twbkwbis.P_WWWLogin"));
			startActivity(browserIntent);
		} else if (selection.equals("GGC")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.ggc.edu"));
			startActivity(browserIntent);
		} else if (selection.equals("My.GGC")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://my.ggc.edu"));
			startActivity(browserIntent);
		} else {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.ggc.edu/academics/calendar/index.html"));
			startActivity(browserIntent);
		}

	}

}
