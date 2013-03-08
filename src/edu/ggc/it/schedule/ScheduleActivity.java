package edu.ggc.it.schedule;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import edu.ggc.it.R;

public class ScheduleActivity extends Activity {

	private Context scheduleContext;
	private ScheduleDatabase database;
	private SimpleCursorAdapter cursorAdapter;
	private ListView list;
	
	public final static String ITEM_TITLE = "title";
	public final static String ITEM_CAPTION = "caption";
	
	/**
	 * This creates a list item in the separated list view for the activity
	 * @param title The main text for the item
	 * @param caption A short description of the ite,
	 * @return A HashMap with the item title and caption
	 */
	public Map<String,?> createItem(String title, String caption) {
		Map<String,String> item = new HashMap<String,String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scheduleContext = this;
		
		// list stuff
		List<Map<String,?>> security = new LinkedList<Map<String,?>>();
		security.add(createItem("Remember passwords", "Save usernames and passwords for Web sites"));
		security.add(createItem("Clear passwords", "Save usernames and passwords for Web sites"));
		security.add(createItem("Show security warnings", "Show warning if there is a problem with a site's security"));
		
		// create our list and custom adapter
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		adapter.addSection("Array test", new ArrayAdapter<String>(this,
			R.layout.schedule_list_item, new String[] { "First item", "Item two" }));
		adapter.addSection("Security", new SimpleAdapter(this, security, R.layout.activity_schedule, 
			new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
		
		ListView list = new ListView(this);
		list.setAdapter(adapter);
		this.setContentView(list);
		// end list stuff
		
		// open new database
		database = new ScheduleDatabase(scheduleContext);
		database.open();

		if (!classesExist()) {
			CharSequence text = "No courses found on your schedule. Use the menu to add a course.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(scheduleContext, text, duration);
			toast.show();;
		} else {
			populateList();
		}

	}

	private void populateList() {
		//TODO: Use the new list adapter method to populate the list
		/*Cursor cursor = database.queryAll();
		startManagingCursor(cursor);

		String[] from = new String[] { ScheduleDatabase.KEY_NAME, ScheduleDatabase.KEY_BUILDING_LOCATION };
		int[] to = new int[] { R.id.class_name, R.id.building_location };
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.schedule_list_row,
				cursor, from, to);

		list.setAdapter(cursorAdapter);
		registerForContextMenu(list.getRootView());*/
	}

	private boolean classesExist() {
		Cursor c = database.queryAll();
		if (c.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_schedule, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_class:
			startActivity(new Intent(scheduleContext,
					ScheduleUpdateActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void updateDatabase(long rowId, boolean create) {
		Intent intent = new Intent(scheduleContext, ScheduleUpdateActivity.class);
		if (!create) {
			intent.putExtra("rowID", rowId);
		}
		startActivity(intent);
	}
	
	public class ScheduleActivityListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long rowId) {
			updateDatabase(rowId, false);
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

}
