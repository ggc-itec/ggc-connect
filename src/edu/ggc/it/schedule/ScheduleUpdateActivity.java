package edu.ggc.it.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import edu.ggc.it.R;
import edu.ggc.it.banner.Schedule;

/**
 * The update class handles processing of the add/edit form. This class handles
 * adding classes as well as editing the individual course item. This is because
 * the add/edit form is the same layout.
 * 
 * @author Raj Ramsaroop
 * 
 * 
 */
public class ScheduleUpdateActivity extends Activity implements
		TimePickerFragment.OnTimeSetListener {

	private ScheduleDatabase database;
	private long rowID;
	private boolean editingClass = false;

	private Context scheduleContext;

	/**
	 * Cancel button
	 */
	private Button btnCancel;

	/**
	 * Update button. Either says "edit" or "add" depending on action.
	 */
	private Button btnUpdateClass;

	private EditText txtClass;
	private EditText txtSection;
	private Button btnStartTime;
	private Button btnEndTime;
	private CheckBox chkMonday, chkTuesday, chkWednesday, chkThursday,
			chkFriday, chkSaturday;
	private Spinner spnBuildingLocation;
	private EditText txtRoomLocation;

	private int startTimeHour = 24;
	private int startTimeMinute = 60;
	private int endTimeHour = 24;
	private int endTimeMinute = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_update_class);
		scheduleContext = this;
		database = new ScheduleDatabase(scheduleContext);
		database.open();

		// Create listeners for each button
		btnCancel = (Button) findViewById(R.id.btn_schedule_update_cancel);
		btnCancel.setOnClickListener(new ScheduleUpdateListener());

		btnUpdateClass = (Button) findViewById(R.id.btn_schedule_update_submit);
		btnUpdateClass.setOnClickListener(new ScheduleUpdateListener());

		btnStartTime = (Button) findViewById(R.id.btn_schedule_update_start_time);
		btnStartTime.setOnClickListener(new ScheduleUpdateListener());

		btnEndTime = (Button) findViewById(R.id.btn_schedule_update_end_time);
		btnEndTime.setOnClickListener(new ScheduleUpdateListener());

		// Initialize each form element
		txtClass = (EditText) findViewById(R.id.edittext_schedule_update_name);
		txtSection = (EditText) findViewById(R.id.edittext_schedule_update_section);
		spnBuildingLocation = (Spinner) findViewById(R.id.spinner_schedule_update_building_location);
		txtRoomLocation = (EditText) findViewById(R.id.edittext_schedule_update_room_location);
		chkMonday = (CheckBox) findViewById(R.id.chk_schedule_update_monday);
		chkTuesday = (CheckBox) findViewById(R.id.chk_schedule_update_tuesday);
		chkWednesday = (CheckBox) findViewById(R.id.chk_schedule_update_wednesday);
		chkThursday = (CheckBox) findViewById(R.id.chk_schedule_update_thursday);
		chkFriday = (CheckBox) findViewById(R.id.chk_schedule_update_friday);
		chkSaturday = (CheckBox) findViewById(R.id.chk_schedule_update_saturday);

		// check if updating a class
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			rowID = extras.getLong("rowID");
			fillForm();
			editingClass = true;
		}
	}

	/**
	 * Gets the class data based on rowID and fills the form with the
	 * information from the database
	 */
	private void fillForm() {
		
		Cursor cursor = database.query(rowID);
		txtClass.setText(cursor.getString(ScheduleDatabase.INDEX_NAME));
		txtSection.setText(cursor.getString(ScheduleDatabase.INDEX_SECTION));

		Resources res = getResources();

		String[] buildingLocations = res.getStringArray(R.array.buildings);
		String buildingLocation = cursor
				.getString(ScheduleDatabase.INDEX_LOCATION_BUILDING);
		for (int i = 0; i < buildingLocations.length; i++) {
			if (buildingLocation.equals(buildingLocations[i])) {
				spnBuildingLocation.setSelection(i);
			}
		}

		txtRoomLocation.setText(cursor
				.getString(ScheduleDatabase.INDEX_LOCATION_ROOM));

		int monday = cursor.getInt(ScheduleDatabase.INDEX_ON_MONDAY);
		if (monday == 1) {
			chkMonday.setChecked(true);
		}

		int tuesday = cursor.getInt(ScheduleDatabase.INDEX_ON_TUESDAY);
		if (tuesday == 1) {
			chkTuesday.setChecked(true);
		}

		int wednesday = cursor.getInt(ScheduleDatabase.INDEX_ON_WEDNESDAY);
		if (wednesday == 1) {
			chkWednesday.setChecked(true);
		}

		int thursday = cursor.getInt(ScheduleDatabase.INDEX_ON_THURSDAY);
		if (thursday == 1) {
			chkThursday.setChecked(true);
		}

		int friday = cursor.getInt(ScheduleDatabase.INDEX_ON_FRIDAY);
		if (friday == 1) {
			chkFriday.setChecked(true);
		}

		int saturday = cursor.getInt(ScheduleDatabase.INDEX_ON_SATURDAY);
		if (saturday == 1) {
			chkSaturday.setChecked(true);
		}

		int startTime = cursor.getInt(ScheduleDatabase.INDEX_START_TIME);
		startTimeHour = getHour(startTime);
		startTimeMinute = getMinute(startTime);
		btnStartTime.setText(getFormattedTimeString(startTimeHour,
				startTimeMinute));

		int endTime = cursor.getInt(ScheduleDatabase.INDEX_END_TIME);
		endTimeHour = getHour(endTime);
		endTimeMinute = getMinute(endTime);
		btnEndTime.setText(getFormattedTimeString(endTimeHour, endTimeMinute));

		btnUpdateClass.setText("Update Class");

	}

	/**
	 * Listener inner class for the add/update class activity.
	 * 
	 * @author Raj Ramsaroop
	 * 
	 */
	public class ScheduleUpdateListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.btn_schedule_update_cancel) {
				finish();
			} else if (view.getId() == R.id.btn_schedule_update_submit) {
				updateClass();
			} else if (view.getId() == R.id.btn_schedule_update_start_time) {
				showTimePickerDialog(view, startTimeHour, startTimeMinute);
			} else if (view.getId() == R.id.btn_schedule_update_end_time) {
				showTimePickerDialog(view, endTimeHour, endTimeMinute);
			}
		}
	}

	/**
	 * This method processes the form contents and once validated, adds or
	 * updates the record in the database
	 */
	private void updateClass() {

		// Reset form processing flag
		boolean validData = true;

		// Get data from form to be put into the database.
		String className = (String) txtClass.getText().toString().trim();
		String section = (String) txtSection.getText().toString().trim();
		int startTime = (startTimeHour * 60) + startTimeMinute;
		int endTime = (endTimeHour * 60) + endTimeMinute;
		String buildingLocation = spnBuildingLocation.getSelectedItem()
				.toString().trim();
		String roomLocation = txtRoomLocation.getText().toString().trim();
		int monday = (chkMonday.isChecked()) ? 1 : 0;
		int tuesday = (chkTuesday.isChecked()) ? 1 : 0;
		int wednesday = (chkWednesday.isChecked()) ? 1 : 0;
		int thursday = (chkThursday.isChecked()) ? 1 : 0;
		int friday = (chkFriday.isChecked()) ? 1 : 0;
		int saturday = (chkSaturday.isChecked()) ? 1 : 0;

		// Validate data
		if (className.isEmpty()) {
			validData = false;
			showMessageDialog("You need to enter a name for the course.",
					"Empty Class Name");
		} else if (section.isEmpty()) {
			// It's ok if the user doesn't know the section, just use 00 as
			// default
			section = "00";
		} else if (!isValidTime(startTimeHour, startTimeMinute)) {
			validData = false;
			showMessageDialog("Please set the start time for the class.",
					"No Start Time Set");
		} else if (!isValidTime(endTimeHour, endTimeMinute)) {
			validData = false;
			showMessageDialog("Please set the end time for the class.",
					"No End Time Set");
		} else if (endTimeHour <= startTimeHour) {
			if (((endTimeHour == startTimeHour) && (endTimeMinute < startTimeMinute))
					|| (endTimeHour < startTimeHour)) {
				validData = false;
				showMessageDialog(
						"Please choose an end time that is after the start time.",
						"Start/End Time Conflict");
			}
		} else if (roomLocation.isEmpty()) {
			validData = false;
			showMessageDialog("Please enter a room location.",
					"No Room Number Entered");
		} else if (!validateDays(monday, tuesday, wednesday, thursday, friday,
				saturday)) {
			validData = false;
			showMessageDialog("Please select at least one day.",
					"No Day Selected");
		}

		if (validData) {
			ContentValues values = database.createContentValues(className, section,
					startTime, endTime, monday, tuesday, wednesday, thursday,
					friday, saturday, buildingLocation, roomLocation);
			if (editingClass) {
				database.updateRow(rowID, values);
			} else {
				database.createRow(values);
			}
			finish();
		}
	}

	/**
	 * This is to show a simple message dialog with an OK button. This is to be
	 * used when something stronger than a Toast is needed to get the user's
	 * attention.
	 * 
	 * @param message
	 *            The message to display to the user.
	 * @param title
	 *            The title of the pop up.
	 */
	private void showMessageDialog(String message, String title) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete
					}
				}).show();
	}

	private boolean validateDays(int monday, int tuesday, int wednesday,
			int thursday, int friday, int saturday) {
		int[] days = { monday, tuesday, wednesday, thursday, friday, saturday };
		boolean dayChecked = false;
		for (int i = 0; i < days.length; i++) {
			if (days[i] == 1) {
				dayChecked = true;
			}
		}
		return dayChecked;
	}

	private boolean isValidTime(int hour, int minute) {
		return (hour < 24) && (hour >= 0) && (minute < 60) && (minute >= 0);
	}

	private void showTimePickerDialog(View view, int hour, int minute) {
		DialogFragment t = new TimePickerFragment();
		Bundle args = new Bundle();
		args.putInt("buttonSource", view.getId());
		args.putInt("hour", hour);
		args.putInt("minute", minute);
		t.setArguments(args);
		t.show(getFragmentManager(), "timePicker");
	}

	/**
	 * This is called when the user picks a time through the time picker pop up.
	 */
	@Override
	public void onTimeSet(int buttonSource, int hour, int minute) {
		String time = getFormattedTimeString(hour, minute);

		if (buttonSource == R.id.btn_schedule_update_start_time) {
			startTimeHour = hour;
			startTimeMinute = minute;
			btnStartTime.setText(time);
		} else if (buttonSource == R.id.btn_schedule_update_end_time) {
			endTimeHour = hour;
			endTimeMinute = minute;
			btnEndTime.setText(time);
		}
	}

	public static String getFormattedTimeString(int hour, int minute) {
		String time = "";
		Object minuteText = (minute < 10) ? "0" + minute : minute;
		Object hourText = "";
		String ampm = (hour < 12) ? "AM" : "PM";
		if (hour > 12) {
			hourText = (hour - 12);
		} else if (hour == 0) {
			hourText = "1";
		} else {
			hourText = hour;
		}
		time = hourText + ":" + minuteText + " " + ampm;
		return time;
	}

	private int getHour(int minutes) {
		return minutes / 60;
	}

	private int getMinute(int minutes) {
		return minutes % 60;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
