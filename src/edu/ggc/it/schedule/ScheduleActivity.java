package edu.ggc.it.schedule;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	
	private Context scheduleContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		scheduleContext = this;
		
		showAddScheduleItemDialog();		
	}

	private void showAddScheduleItemDialog() {
		new AlertDialog.Builder(this)
	    .setTitle("No Classes Found")
	    .setMessage("No classes were found on your schedule. Would you like to add one now?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Toast.makeText(scheduleContext, "Show add screen", Toast.LENGTH_LONG)
				.show();
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	Toast.makeText(scheduleContext, "Go back to main menu", Toast.LENGTH_LONG)
				.show();
	        }
	     })
	     .show();
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
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class ScheduleListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			/*if (view.getId() == R.id.add_schedule_item_button) {
				//TODO: Display the add schedule item screen
				Toast.makeText(scheduleContext, 
						"This would show add schedule item screen...", 
						Toast.LENGTH_LONG).show();
			}*/
		}
		
	}

}
