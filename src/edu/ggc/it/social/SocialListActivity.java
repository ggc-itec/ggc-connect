package edu.ggc.it.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.ggc.it.R;

/**
 * Activity that displays all the posts from the users. The posts are stored in
 * Parse.com
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
	private Button submitButton;
	
	public static final String REPLY_SUBJECT = "REPLY_SUBJECT";
	public static final String REPLY_BODY = "REPLY_BODY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_listview);
		names = new ArrayList<String>();
		subjects = new ArrayList<String>();
		dates = new ArrayList<String>();
		contents = new ArrayList<String>();
		context = this;
		submitButton = (Button) findViewById(R.id.button_social_addentry);
		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, SocialUpdateActivity.class));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		downloadParseData();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final String name = names.get(position);
		final String subject = subjects.get(position);
		final String body = contents.get(position);

		new AlertDialog.Builder(this)
				.setTitle("\"" + subject + "\"" + " from " + name)
				.setMessage(body)
				.setPositiveButton("Close",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						})
				.setNegativeButton("Reply",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										SocialUpdateActivity.class);
								intent.putExtra(REPLY_SUBJECT, subject);
								intent.putExtra(REPLY_BODY, body);
								startActivity(intent);
							}
						}).show();
	}

	private void downloadParseData() {
		Parse.initialize(this, getString(R.string.parse_app_id),
				getString(R.string.parse_client_key));
		ParseQuery query = new ParseQuery("ggcconnect");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				names.clear();
				subjects.clear();
				dates.clear();
				contents.clear();
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
				Collections.reverse(names);
				Collections.reverse(subjects);
				Collections.reverse(dates);
				Collections.reverse(contents);
				SocialAdapter adapter = new SocialAdapter(context, names,
						subjects, dates, contents);
				setListAdapter(adapter);
			}
		});
	}

}
