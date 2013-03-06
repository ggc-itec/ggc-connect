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
public class ScheduleUpdateActivity extends Activity {

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
		String startTime = btnStartTime.getText().toString();
		String endTime = btnEndTime.getText().toString();
		String buildingLocation = spnBuildingLocation.getSelectedItem()
				.toString();
		String roomLocation = txtRoomLocation.getText().toString();
		String days = "";

		if (chkMonday.isChecked()) {
			days += "M";
		} else if (chkTuesday.isChecked()) {
			days += "T";
		} else if (chkWednesday.isChecked()) {
			days += "W";
		} else if (chkThursday.isChecked()) {
			days += "R";
		} else if (chkFriday.isChecked()) {
			days += "F";
		} else if (chkSaturday.isChecked()) {
			days += "S";
		}

		database = new ScheduleDatabase(scheduleContext);
		database.open();
		database.createRow(database.createContentValues(className, startTime,
				endTime, days, buildingLocation, roomLocation));

		finish();
	}
	
	private void showTimePickerDialog(View view) {
	    DialogFragment t = new TimePickerFragment();
	    Bundle args = new Bundle();
	    args.putInt("buttonSource", view.getId());
	    t.setArguments(args);
	    t.show(getFragmentManager(), "timePicker");
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {
		
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hour, int minute) {
			Bundle args = this.getArguments();
			int buttonSource = args.getInt("buttonSource");
			String time = "";
			if (hour > 12) {
				hour = hour-12;
				time = hour + ":" + minute + " PM";
			} else {
				time = hour + ":" + minute + " AM";
			}
			((Button)getActivity().findViewById(buttonSource)).setText(time);
		}
	}
}
