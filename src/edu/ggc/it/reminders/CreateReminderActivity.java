package edu.ggc.it.reminders;

import java.text.SimpleDateFormat;
import java.sql.Date;

import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import edu.ggc.it.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class CreateReminderActivity extends Activity {
	
	public static final String REMINDER_TEXT = "edu.ggc.it.reminders.reminderText";
	public static final String REMINDER_TIME = "edu.ggc.it.reminders.reminderTime";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_reminder);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		String reminderText = intent.getStringExtra(RemindersActivity.REMINDER_TEXT);
		long reminderTime = intent.getLongExtra(RemindersActivity.REMINDER_TIME, 0);
		
		Date date = new Date(reminderTime);
		//DateFormat formatter = new SimpleDateFormat("M-d-y HH:mm:ss:SSS");
		//String dateFormatted = formatter.format(date);
		
		/*
		 * Note: Starting with API level 19, the OS will take liberties as to
		 * exactly when the alarm goes off in order to minimize battery usage.
		 * 
		 * Typically, the alarm will be delivered within a few seconds of the 
		 * designated time.  This should be sufficient for the intended use case
		 * of the reminders.
		 */
		
	    Intent intent2 = new Intent(this, edu.ggc.it.reminders.Reminder.class);
	    intent2.putExtra(REMINDER_TEXT, reminderText);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent2, 0);
	    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);

		TextView reminder_result = (TextView)findViewById(R.id.reminder_text_view);
		
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter2 = new SimpleDateFormat("M-d-y HH:mm:ss:SSS");
		String newDate = formatter2.format(reminderTime);
		reminder_result.setText("Your reminder has been set for " + newDate);

		
		

	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_reminder, menu);
		return true;
	}

}
