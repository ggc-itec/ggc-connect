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
	 * 
	 * @param title
	 *            The main text for the item
	 * @param caption
	 *            A short description of the ite,
	 * @return A HashMap with the item title and caption
	 */
	public Map<String, ?> createItem(String title, String caption) {
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		return item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scheduleContext = this;

		// open new database
		database = new ScheduleDatabase(scheduleContext);
		database.open();

		if (!classesExist()) {
			CharSequence text = "No courses found on your schedule. Use the menu to add a course.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(scheduleContext, text, duration);
			toast.show();
			// call populate list for testing purposes for now
			populateList();
		} else {
			//populateList();
		}

	}

	private void populateList() {
		// A LinkedList for each day that holds the classes that are on that day
		List<Map<String, ?>> monday = new LinkedList<Map<String, ?>>();
		List<Map<String, ?>> tuesday = new LinkedList<Map<String, ?>>();
		List<Map<String, ?>> wednesday = new LinkedList<Map<String, ?>>();
		List<Map<String, ?>> thursday = new LinkedList<Map<String, ?>>();
		List<Map<String, ?>> friday = new LinkedList<Map<String, ?>>();
		List<Map<String, ?>> saturday = new LinkedList<Map<String, ?>>();
		
		//TODO: Database needs to pull info here to populate lists
		monday.add(createItem("Class Name", "Start/End time, location etc"));

		// Create header sections and add populated lists
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		createListSection(adapter, "Monday", monday);
		createListSection(adapter, "Tuesday", tuesday);
		createListSection(adapter, "Wednesday", wednesday);
		createListSection(adapter, "Thursday", thursday);
		createListSection(adapter, "Friday", friday);
		createListSection(adapter, "Saturday", saturday);
		
		// Display the list
		ListView list = new ListView(this);
		list.setAdapter(adapter);
		this.setContentView(list);

		/*
		 * Cursor cursor = database.queryAll(); startManagingCursor(cursor);
		 * 
		 * String[] from = new String[] { ScheduleDatabase.KEY_NAME,
		 * ScheduleDatabase.KEY_BUILDING_LOCATION }; int[] to = new int[] {
		 * R.id.class_name, R.id.building_location }; cursorAdapter = new
		 * SimpleCursorAdapter(this, R.layout.schedule_list_row, cursor, from,
		 * to);
		 * 
		 * list.setAdapter(cursorAdapter);
		 * registerForContextMenu(list.getRootView());
		 */
	}

	private void createListSection(SeparatedListAdapter adapter,
			String sectionHeader, List<Map<String, ?>> list) {
		adapter.addSection(sectionHeader, new SimpleAdapter(this, list,
				R.layout.activity_schedule, new String[] { ITEM_TITLE,
						ITEM_CAPTION }, new int[] { R.id.list_complex_title,
						R.id.list_complex_caption }));
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
		Intent intent = new Intent(scheduleContext,
				ScheduleUpdateActivity.class);
		if (!create) {
			intent.putExtra("rowID", rowId);
		}
		startActivity(intent);
	}

	public class ScheduleActivityListListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				long rowId) {
			updateDatabase(rowId, false);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

}
