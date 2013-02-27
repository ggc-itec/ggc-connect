package edu.ggc.it.schedule;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import edu.ggc.it.R;

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
				//TODO: Show time picker dialog
				showDialog(R.id.schedule_start_time_picker);
			} else if (view.getId() == R.id.btn_schedule_update_end_time) {
				showDialog(R.id.schedule_end_time_picker);
			}
		}
	}
	
	/*@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	        case R.id.schedule_start_time_picker:
	            return new TimePickerDialog(this,StartTimeSetListener, hour, minute, false);
	        case R.id.schedule_end_time_picker:
	            return new TimePickerDialog(this,TimeSetListener,hour, minute, false);                
	    }
	    return null;
	}*/
	
	/**
	 * This method adds a class to the schedule
	 */
	private void addClass() {
		String className = (String) txtClass.getText().toString();
		String startTime = btnStartTime.getText().toString();
		String endTime = btnEndTime.getText().toString();
		String buildingLocation = spnBuildingLocation.getSelectedItem().toString();
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
		database.createRow(database.createContentValues(className, startTime, endTime, days, buildingLocation, roomLocation));
		
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

}
