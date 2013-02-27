package edu.ggc.it.schedule;

import android.app.Activity;
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
	private CheckBox chkMonday, chkTuesday, chkWednesday, chkThursday, chkFriday, chlSaturday;
	private Spinner buildingLocation;
	private EditText txtRoomLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_update_class);
		
		// Create listeners for each button
		btnCancel = (Button) findViewById(R.id.btn_schedule_update_cancel);
		btnCancel.setOnClickListener(new ScheduleUpdateListener());
		
		btnUpdateClass = (Button) findViewById(R.id.btn_schedule_update_submit);
		btnUpdateClass.setOnClickListener(new ScheduleUpdateListener());
		
		btnStartTime = (Button) findViewById(R.id.btn_schedule_update_start_time);
		btnStartTime.setOnClickListener(new ScheduleUpdateListener());
		
		btnEndTime = (Button) findViewById(R.id.btn_schedule_update_end_time);
		btnEndTime.setOnClickListener(new ScheduleUpdateListener());
	}
	
	/**
	 * Listener inner class for the add class activity.
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
			}
		}
	}
	
	/**
	 * This method adds a class to the schedule
	 */
	private void addClass() {
		
	}

}
