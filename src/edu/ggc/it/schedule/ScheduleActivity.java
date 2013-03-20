package edu.ggc.it.schedule;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
		if (!monday.isEmpty()) {
			createListSection(adapter, createDayHeader("Monday"), monday);
		}
		if (!tuesday.isEmpty()) {
			createListSection(adapter, createDayHeader("Tuesday"), tuesday);
		}
		if (!wednesday.isEmpty()) {
			createListSection(adapter, createDayHeader("Wednesday"), wednesday);
		}
		if (!thursday.isEmpty()) {
			createListSection(adapter, createDayHeader("Thursday"), thursday);
		}
		if (!friday.isEmpty()) {
			createListSection(adapter, createDayHeader("Friday"), friday);
		}
		if (!saturday.isEmpty()) {
			createListSection(adapter, createDayHeader("Saturday"), saturday);
		}

		// Display the list
		ListView list = new ListView(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new ScheduleActivityListener());
		this.setContentView(list);

	}

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

	private List<Map<String, ?>> getClassListByDay(String dayIndex) {
		List<Map<String, ?>> day = new LinkedList<Map<String, ?>>();
		Cursor cursor = database.queryByDay(dayIndex);
		if (cursor.moveToFirst()) {
			do {
				String className = cursor
						.getString(ScheduleDatabase.INDEX_NAME);
				String section = cursor
						.getString(ScheduleDatabase.INDEX_SECTION);
				int startTime = cursor
						.getInt(ScheduleDatabase.INDEX_START_TIME);
				int endTime = cursor.getInt(ScheduleDatabase.INDEX_END_TIME);
				String building = cursor
						.getString(ScheduleDatabase.INDEX_LOCATION_BUILDING);
				String room = cursor
						.getString(ScheduleDatabase.INDEX_LOCATION_ROOM);

				String caption = getScheduleItemCaption(section, startTime,
						endTime, building, room);
				day.add(createItem(className, caption));
			} while (cursor.moveToNext());
		}
		return day;
	}

	private String getScheduleItemCaption(String section, int startTime,
			int endTime, String building, String room) {
		int startTimeHour = startTime / 60;
		int startTimeMinute = startTime % 60;
		int endTimeHour = endTime / 60;
		int endTimeMinute = endTime % 60;
		String startTimeFormatted = ScheduleUpdateActivity.formattedTimeString(
				startTimeHour, startTimeMinute);
		String endTimeFormatted = ScheduleUpdateActivity.formattedTimeString(
				endTimeHour, endTimeMinute);
		String duration = getFormattedTimeDuration(startTime, endTime);

		String caption = startTimeFormatted + " - " + endTimeFormatted + " ("
				+ duration + ").\n" + "Section: " + section + "\n"
				+ "Location: " + building + " (Room: " + room + ").";

		return caption;
	}

	private String getFormattedTimeDuration(int start, int end) {
		String duration = "";
		int durationInt = end - start;
		if (durationInt == 1) {
			duration = "1 minute";
		} else if (durationInt < 60) {
			duration = durationInt + " minutes";
		} else {
			int hours = durationInt / 60;
			int minutes = durationInt % 60;
			if (hours == 1) {
				if (minutes == 1) {
					duration = "1 hour, 1 minute";
				} else {
					duration = "1 hour, " + minutes + " minutes";
				}
			} else {
				if (minutes == 1) {
					duration = hours + " hours, 1 minute";
				} else {
					duration = hours + " hours, " + minutes + " minutes";
				}
			}
		}
		return duration;
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
		case R.id.refresh_schedule:
			populateList();
			return true;
		case R.id.clear_schedule:
			showConfirmClearSchedule();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void clearSchedule() {
		database.deleteTable();
		populateList();
	}

	private void showConfirmClearSchedule() {
		new AlertDialog.Builder(scheduleContext)
		.setTitle("Confirm Clear Schedule")
		.setMessage("WARNING: This will remove all classes from your schedule and cannot be undone, are you sure you wish to continue?")
		.setIcon(android.R.drawable.stat_sys_warning)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clearSchedule();
			}
		})
		.setNegativeButton("No", null)
		.show();
	}

	private void updateDatabase(long rowId) {
		Intent intent = new Intent(scheduleContext,
				ScheduleUpdateActivity.class);
		intent.putExtra("rowID", rowId);
		startActivity(intent);
	}

	private void showDeleteConfirmDialog(final long rowID) {
		// get class name
		Cursor cursor = database.query(rowID);
		Log.d("schedule", "rowID: " + rowID);
		//String name = cursor.getString(ScheduleDatabase.INDEX_NAME);
		String name = "";
		new AlertDialog.Builder(scheduleContext)
		.setTitle("Confirm Class Delete")
		.setMessage("Are you sure you want to delete the class " + name + "? This cannot be undone.")
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteClass(rowID);
			}
		})
		.setNegativeButton("No", null)
		.show();
	}
	
	private void deleteClass(long rowID) {
		database.deleteRow(rowID);
		populateList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

	public class ScheduleActivityListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position,
				final long rowID) {
			new AlertDialog.Builder(scheduleContext)
					.setTitle("Choose Action")
					.setItems(R.array.schedule_item_options,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										updateDatabase(rowID);
									} else if (which == 1) {
										showDeleteConfirmDialog(rowID);
									}
								}
							}).show();
		}

	}
}
