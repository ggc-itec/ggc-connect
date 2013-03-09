package edu.ggc.it.schedule;

import java.util.Calendar;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import edu.ggc.it.R;

public class ScheduleActivity extends Activity {

	private Context scheduleContext;
	private ScheduleDatabase database;
	/**
	 * This is the XML value for the text of each list item
	 */
	public final static String ITEM_TITLE = "title";

	/**
	 * This is the XML value for an optional caption or description of each list
	 * item
	 */
	public final static String ITEM_CAPTION = "caption";

	/**
	 * This creates a list item in the separated list view for the activity
	 * 
	 * @param title
	 *            The main text for the item
	 * @param caption
	 *            A short description of the item
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
		} else {
			populateList();
		}

	}

	public void populateList() {
		// A LinkedList for each day that holds the classes that are on that day
		List<Map<String, ?>> monday = getClassListByDay(ScheduleDatabase.KEY_ON_MONDAY);
		List<Map<String, ?>> tuesday = getClassListByDay(ScheduleDatabase.KEY_ON_TUESDAY);
		List<Map<String, ?>> wednesday = getClassListByDay(ScheduleDatabase.KEY_ON_WEDNESDAY);
		List<Map<String, ?>> thursday = getClassListByDay(ScheduleDatabase.KEY_ON_THURSDAY);
		List<Map<String, ?>> friday = getClassListByDay(ScheduleDatabase.KEY_ON_FRIDAY);
		List<Map<String, ?>> saturday = getClassListByDay(ScheduleDatabase.KEY_ON_SATURDAY);

		// Create header sections and add populated lists
		SeparatedListAdapter adapter = new SeparatedListAdapter(this);
		createListSection(adapter, createDayHeader("Monday"), monday);
		createListSection(adapter, createDayHeader("Tuesday"), tuesday);
		createListSection(adapter, createDayHeader("Wednesday"), wednesday);
		createListSection(adapter, createDayHeader("Thursday"), thursday);
		createListSection(adapter, createDayHeader("Friday"), friday);
		createListSection(adapter, createDayHeader("Saturday"), saturday);

		// Display the list
		ListView list = new ListView(this);
		list.setAdapter(adapter);
		this.setContentView(list);
	}

	private List<Map<String, ?>> getClassListByDay(String dayIndex) {
		List<Map<String, ?>> day = new LinkedList<Map<String, ?>>();
		Cursor cursor = database.queryByDay(dayIndex);
		if (cursor.moveToFirst()) {
	        do {
	        	String className = cursor.getString(ScheduleDatabase.INDEX_NAME);
	        	day.add(createItem(className, "Start/End time, location etc"));
	        } while (cursor.moveToNext());
	    }
		return day;
	}

	/**
	 * This creates the section header for the list view using the day
	 * 
	 * @param day
	 *            the day of the week
	 * @return the day and if that day is today, an indication in parenthesis
	 *         (e.g. Monday (today))
	 */
	private String createDayHeader(String day) {
		Calendar c = Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_WEEK);
		String header = day;
		String[] daysListString = { "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };
		int[] daysListInt = { Calendar.MONDAY, Calendar.TUESDAY,
				Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY,
				Calendar.SATURDAY };
		for (int i = 0; i < daysListString.length; i++) {
			if (day.equalsIgnoreCase(daysListString[i])
					&& today == daysListInt[i]) {
				header += " (today)";
			}
		}
		return header;
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
