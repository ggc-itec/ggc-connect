package edu.ggc.it;

import android.app.Activity;
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

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.websites)));

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {

		String selection = getListAdapter().getItem(position).toString();

		if (selection.equals("CNN")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.cnn.com"));
			startActivity(browserIntent);
		}
		else if( selection.equals("FOX")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://m.fox.com"));
			startActivity(browserIntent);
		}
		else if(selection.equals("GGC")) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://www.ggc.edu"));
			startActivity(browserIntent);
		}

	}

}
