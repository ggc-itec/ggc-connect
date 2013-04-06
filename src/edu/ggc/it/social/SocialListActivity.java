package edu.ggc.it.social;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ggc.it.R;

/**
 * 
 * 
 * @author crystalist
 * 
 */
public class SocialListActivity extends ListActivity {

	private List<String> names;
	private List<String> subjects;
	private List<String> dates;
	private List<String> contents;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_listview);
		names = new ArrayList<String>();
		subjects = new ArrayList<String>();
		dates = new ArrayList<String>();
		contents = new ArrayList<String>();
		context = this;
		downloadParseData();
	}

	private void downloadParseData() {
		Parse.initialize(this, getString(R.string.parse_app_id),
				getString(R.string.parse_client_key));
		ParseQuery query = new ParseQuery("ggcconnect");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// retrieve successful
					for (ParseObject object : objects) {
						String name = object.getString("name");
						String subject = object.getString("subject");
						String date = object.getString("date");
						String body = object.getString("body");
						names.add(name);
						subjects.add(subject);
						dates.add(date);
						contents.add(body);
					}
				} else {
					
				}
				SocialAdapter adapter = new SocialAdapter(context,names, subjects, dates, contents);
				setListAdapter(adapter);
			}
		});
	}


}
