package edu.ggc.it.schedule;

import java.util.ArrayList;
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
import edu.ggc.it.schedule.helper.ClassItem;
import edu.ggc.it.schedule.helper.SeparatedListAdapter;

/**
 * This is the main activity for the class schedule feature of ggc-connect.
 * @author Raj Ramsaroop
 *
 */
public class ScheduleActivity extends Activity {

	private Context scheduleContext;
	private ScheduleDatabase database;
	private ArrayList<Long> listRowID = new ArrayList<Long>();
	
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

	}

	/**
	 * Pulls the information from the database and sorts it under each
	 * appropriate header.
	 */
	private void populateList() {
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

	/**
	 * Returns a list of mappings for each class and information for that class
	 * @param dayIndex the day to query the database by
	 * @return LinkedList of mappings for each class for the specified day
	 */
	private List<Map<String, ?>> getClassListByDay(String dayIndex) {
		List<Map<String, ?>> day = new LinkedList<Map<String, ?>>();
		Cursor cursor = database.queryByDay(dayIndex);
		if (cursor.getCount() > 0) {
			// set to unreasonable amount to account for header
			listRowID.add(Long.MIN_VALUE);
		}
		
		if (cursor.moveToFirst()) {
			do {
				// rowID for the position of this list item
				long rowID = cursor.getLong(ScheduleDatabase.INDEX_ROWID);
				listRowID.add(rowID);
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

	/**
	 * This creates the text beneath the class name in the list.
	 * @param section the class section
	 * @param startTime start time minutes
	 * @param endTime end time minutes
	 * @param building building location
	 * @param room the room location
	 * @return A formatted string/paragraph based on input information
	 */
	private String getScheduleItemCaption(String section, int startTime,
			int endTime, String building, String room) {
		int startTimeHour = startTime / 60;
		int startTimeMinute = startTime % 60;
		int endTimeHour = endTime / 60;
		int endTimeMinute = endTime % 60;
		String startTimeFormatted = ScheduleUpdateActivity.getFormattedTimeString(
				startTimeHour, startTimeMinute);
		String endTimeFormatted = ScheduleUpdateActivity.getFormattedTimeString(
				endTimeHour, endTimeMinute);
		String duration = getFormattedTimeDuration(startTime, endTime);

		String caption = startTimeFormatted + " - " + endTimeFormatted + " ("
				+ duration + ").\n" + "Section: " + section + "\n"
				+ "Location: " + building + " (Room: " + room + ").";

		return caption;
	}

	/**
	 * Calculates the duration of the class and returns a formatted user-friendly
	 * String version.
	 * @param start the start time minutes
	 * @param end the end time minutes
	 * @return a formatted String
	 */
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

	/**
	 * This creates each list item section
	 * @param adapter the adapter to use
	 * @param sectionHeader which section to add to
	 * @param list the list of classes and descriptions
	 */
	private void createListSection(SeparatedListAdapter adapter,
			String sectionHeader, List<Map<String, ?>> list) {
		adapter.addSection(sectionHeader, new SimpleAdapter(this, list,
				R.layout.activity_schedule, new String[] { ITEM_TITLE,
						ITEM_CAPTION }, new int[] { R.id.list_complex_title,
						R.id.list_complex_caption }));
	}

	/**
	 * Checks if classes are added to the schedule
	 * @return true if classes are in the database
	 */
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
		case R.id.clear_schedule:
			showConfirmClearSchedule();
			return true;
		case R.id.schedule_settings:
			startActivity(new Intent(scheduleContext, SchedulePreferenceActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Deletes the table from the database
	 */
	private void clearSchedule() {
		database.deleteTable();
		refreshList();
	}

	/**
	 * Shows a dialog popup asking the user to confirm clearing the schedule
	 */
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

	/**
	 * This will start the update activity that will allow the user
	 * to edit the selected class.
	 * @param rowId the rowID of the class that corresponds to the database rowID
	 */
	private void updateDatabase(long rowId) {
		Intent intent = new Intent(scheduleContext,
				ScheduleUpdateActivity.class);
		intent.putExtra("rowID", rowId);
		intent.putExtra("action", ScheduleUpdateActivity.ACTION_EDIT);
		startActivity(intent);
	}

	/**
	 * Shows a confirmation alert dialog asking the user to confirm deleting a class
	 * from their schedule.
	 * @param rowID The corresponding rowID of the class in the database
	 */
	private void showDeleteConfirmDialog(final long rowID) {
		// get class name
		Cursor cursor = database.query(rowID);
		String name = cursor.getString(ScheduleDatabase.INDEX_NAME);
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
	
	/**
	 * Delete a class from the database
	 * @param rowID the corresponding rowID of the class in the database
	 */
	private void deleteClass(long rowID) {
		database.deleteRow(rowID);
		refreshList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!classesExist()) {
			CharSequence text = "No courses found on your schedule. Use the menu to add a course.";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(scheduleContext, text, duration);
			toast.show();
		} else {
			refreshList();
		}
	}
	
	private void refreshList() {
		listRowID.clear();
		populateList();
	}

	/**
	 * This listener handles what happens when a list item is clicked.
	 * @author Raj Ramsaroop
	 *
	 */
	public class ScheduleActivityListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> list, View view, int position, long rowID) {
			// get the correct rowID that corresponds with the database
			final long databaseRowID = listRowID.get(position);
			new AlertDialog.Builder(scheduleContext)
					.setTitle("Choose Action")
					.setItems(R.array.schedule_item_options,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										updateDatabase(databaseRowID);
									} else if (which == 1) {
										showDeleteConfirmDialog(databaseRowID);
									}
								}
							}).show();
		}

	}
}
