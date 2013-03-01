package edu.ggc.it.schedule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
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
	
	private int mHour;
	private int mMinute;

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
				showTimePicker();
			} else if (view.getId() == R.id.btn_schedule_update_end_time) {
				showTimePicker();
			}
		}
	}
	
	private void showTimePicker() {
		/** Creating a bundle object to pass currently set time to the fragment */
		Bundle b = new Bundle();
		
		/** Adding currently set hour to bundle object */
		b.putInt("set_hour", mHour);
		
		/** Adding currently set minute to bundle object */
		b.putInt("set_minute", mMinute);
		
		/** Instantiating TimePickerDialogFragment */
		Fragment timePicker = new Fragment();
		
		/** Setting the bundle object on timepicker fragment */
		timePicker.setArguments(b);				
		
		/** Getting fragment manger for this activity */
		FragmentManager fm = getFragmentManager();				
		
		/** Starting a fragment transaction */
		FragmentTransaction ft = fm.beginTransaction();
		
		/** Adding the fragment object to the fragment transaction */
		ft.add(timePicker, "time_picker");
		
		/** Opening the TimePicker fragment */
		ft.commit();
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
	
	public class ScheduleHandler extends Handler {
		@Override
        public void handleMessage(Message m){   
        	/** Creating a bundle object to pass currently set Time to the fragment */
        	Bundle b = m.getData();
        	
        	/** Getting the Hour of day from bundle */
    		mHour = b.getInt("set_hour");
    		
    		/** Getting the Minute of the hour from bundle */
    		mMinute = b.getInt("set_minute");
    		
    		/** Displaying a short time message containing time set by Time picker dialog fragment */
    		Toast.makeText(getBaseContext(), b.getString("set_time"), Toast.LENGTH_SHORT).show();
        }
	}

}
