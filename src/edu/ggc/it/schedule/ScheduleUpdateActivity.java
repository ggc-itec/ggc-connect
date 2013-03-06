package edu.ggc.it.schedule;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
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
	
	private int startTimeHour;
	private int startTimeMinute;
	private int endTimeHour;
	private int endTimeMinute;
	
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
				showTimePickerDialog(view);
			} else if (view.getId() == R.id.btn_schedule_update_end_time) {
				showTimePickerDialog(view);
			}
		}
	}

	/**
	 * This method adds a class to the schedule
	 */
	private void addClass() {
		String className = (String) txtClass.getText().toString();
		String startTime = startTimeHour + ":" + startTimeMinute;
		String endTime = endTimeHour + ":" + endTimeMinute;
		String buildingLocation = spnBuildingLocation.getSelectedItem()
				.toString();
		String roomLocation = txtRoomLocation.getText().toString();
		int monday = (chkMonday.isChecked()) ? 1 : 0;
		int tuesday = (chkTuesday.isChecked()) ? 1 : 0;
		int wednesday = (chkWednesday.isChecked()) ? 1 : 0;
		int thursday = (chkThursday.isChecked()) ? 1 : 0;
		int friday = (chkFriday.isChecked()) ? 1 : 0;
		int saturday = (chkSaturday.isChecked()) ? 1 : 0;
		
		if (!validateClassName(className)) {
			validData = false;
		} else if (!validateStartTime(startTime)) {
			validData = false;
		} else if (!validateEndTime(endTime)) {
			validData = false;
		} else if (!validateRoomLocation(roomLocation)) {
			validData = false;
		} else if (!validateDays(monday, tuesday, wednesday, thursday, friday, saturday)) {
			validData = false;
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

	private boolean validateRoomLocation(String roomLocation) {
		return !roomLocation.isEmpty();
	}

	private boolean validateEndTime(String endTime) {
		return !endTime.equals("Set End Time");
	}

	private boolean validateStartTime(String startTime) {
		return !startTime.equals("Set Start Time");
	}

	private boolean validateClassName(String className) {
		return !className.isEmpty();
	}

	private void showTimePickerDialog(View view) {
	    DialogFragment t = new TimePickerFragment();
	    Bundle args = new Bundle();
	    args.putInt("buttonSource", view.getId());
	    t.setArguments(args);
	    t.show(getFragmentManager(), "timePicker");
	}

	/**
	 * This is called when the user picks a time through the time picker pop up.
	 */
	@Override
	public void onTimeSet(int buttonSource, int hour, int minute) {
		String time = "";
		String strMinute = (String) ((minute < 10) ? "0" + minute : minute);
		if (hour > 12) {
			time = (hour-12) + ":" + strMinute + " PM";
		} else {
			time = hour + ":" + strMinute + " AM";
		}
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

	
}
