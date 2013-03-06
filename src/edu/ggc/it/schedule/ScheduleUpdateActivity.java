package edu.ggc.it.schedule;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import edu.ggc.it.R;

/**
 * 
 * @author Raj Ramsaroop
 * The update class handles processing of the add/edit form. This class handles
 * adding classes as well as editing the individual course item. This is because
 * the add/edit form is the same layout.
 *
 */
public class ScheduleUpdateActivity extends Activity implements TimePickerFragment.OnTimeSetListener {

	private ScheduleDatabase database;
	private Long rowID;

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
	
	private boolean validData = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_update_class);
		scheduleContext = this;

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
		spnBuildingLocation = (Spinner) findViewById(R.id.spinner_schedule_update_building_location);
		txtRoomLocation = (EditText) findViewById(R.id.edittext_schedule_update_room_location);
		chkMonday = (CheckBox) findViewById(R.id.chk_schedule_update_monday);
		chkTuesday = (CheckBox) findViewById(R.id.chk_schedule_update_tuesday);
		chkWednesday = (CheckBox) findViewById(R.id.chk_schedule_update_wednesday);
		chkThursday = (CheckBox) findViewById(R.id.chk_schedule_update_thursday);
		chkFriday = (CheckBox) findViewById(R.id.chk_schedule_update_friday);
		chkSaturday = (CheckBox) findViewById(R.id.chk_schedule_update_saturday);
	}

	/**
	 * Listener inner class for the add class activity.
	 * 
	 * @author Raj
	 * 
	 */
	public class ScheduleUpdateListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.btn_schedule_update_cancel) {
				finish();
			} else if (view.getId() == R.id.btn_schedule_update_submit) {
				addClass();
			} else if (view.getId() == R.id.btn_schedule_update_start_time) {
				showTimePickerDialog(view, startTimeHour, startTimeMinute);
			} else if (view.getId() == R.id.btn_schedule_update_end_time) {
				showTimePickerDialog(view, endTimeHour, endTimeMinute);
			}
		}
	}

	/**
	 * This method adds a class to the schedule
	 */
	private void addClass() {
		validData = true;
		
		String className = (String) txtClass.getText().toString().trim();
		String startTime = startTimeHour + ":" + startTimeMinute;
		String endTime = endTimeHour + ":" + endTimeMinute;
		String buildingLocation = spnBuildingLocation.getSelectedItem()
				.toString().trim();
		String roomLocation = txtRoomLocation.getText().toString().trim();
		int monday = (chkMonday.isChecked()) ? 1 : 0;
		int tuesday = (chkTuesday.isChecked()) ? 1 : 0;
		int wednesday = (chkWednesday.isChecked()) ? 1 : 0;
		int thursday = (chkThursday.isChecked()) ? 1 : 0;
		int friday = (chkFriday.isChecked()) ? 1 : 0;
		int saturday = (chkSaturday.isChecked()) ? 1 : 0;
		
		Log.i("timepicker", "start Time hour: " + startTimeHour + " min: " + startTimeMinute);
		
		if (className.isEmpty()) {
			validData = false;
			showMessageDialog("You need to enter a name for the course.", "Empty Class Name");
		} else if (!isValidTime(startTimeHour, startTimeMinute)) {
			validData = false;
			showMessageDialog("Please set the start time for the class.", "No Start Time Set");
		} else if (!isValidTime(endTimeHour, endTimeMinute)) {
			validData = false;
			showMessageDialog("Please set the end time for the class.", "No End Time Set");
		} else if (roomLocation.isEmpty()) {
			validData = false;
			showMessageDialog("Please enter a room location.", "No Room Number Entered");
		} else if (!validateDays(monday, tuesday, wednesday, thursday, friday, saturday)) {
			validData = false;
			showMessageDialog("Please select at least one day.", "No Day Selected");
		}
		
		if (validData) {
			database = new ScheduleDatabase(scheduleContext);
			database.open();
			database.createRow(database.createContentValues(className, startTime,
					endTime, monday, tuesday, wednesday, thursday, friday,
					saturday, buildingLocation, roomLocation));
			finish();
		} 
	}
	
	private void showMessageDialog(String message, String title) {
		new AlertDialog.Builder(this)
	    .setTitle(title)
	    .setMessage(message)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	     .show();
	}

	private boolean validateDays(int monday, int tuesday, int wednesday,
			int thursday, int friday, int saturday) {
		int[] days = { monday, tuesday, wednesday, thursday, friday, saturday };
		boolean dayChecked = false;
		for (int i=0;i<days.length;i++) {
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
		String time = formattedTimeString(hour, minute);
		
		if (buttonSource == R.id.btn_schedule_update_start_time) {
			startTimeHour = hour;
			startTimeMinute = minute;
			btnStartTime.setText(time);
		} else if (buttonSource == R.id.btn_schedule_update_end_time) {
			startTimeHour = hour;
			startTimeMinute = minute;
			btnEndTime.setText(time);
		}
	}

	private String formattedTimeString(int hour, int minute) {
		String time = "";
		Object minuteText = (minute < 10) ? "0" + minute : minute;
		Object hourText = "";
		String ampm = (hour < 12) ? "AM" : "PM";
		if (hour > 12) {
			hourText = (hour-12);
		} else if (hour == 0){
			hourText = "1";
		} else {
			hourText = hour;
		}
		time = hourText + ":" + minuteText + " " + ampm;
		return time;
	}

	
}
