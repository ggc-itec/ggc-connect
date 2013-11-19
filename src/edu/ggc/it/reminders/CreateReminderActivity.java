package edu.ggc.it.reminders;

import java.text.SimpleDateFormat;
import java.util.Random;
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

public class CreateReminderActivity extends Activity
{
	
	public static final String REMINDER_TEXT = "edu.ggc.it.reminders.reminderText";
	public static final String REMINDER_TIME = "edu.ggc.it.reminders.reminderTime";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_reminder);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		// Pull the submitted date and time form the intent
		Intent intent = getIntent();
		String reminderText = intent.getStringExtra(RemindersActivity.REMINDER_TEXT);
		long reminderTime = intent.getLongExtra(RemindersActivity.REMINDER_TIME, 0);
		
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
	    
	    /*
	     * To set up an alarm, we create a PendingIntent and send it to the device's
	     * alarm service manager.
	     */
	    final Random rand = new Random();
	    int alarmIdentifier = rand.nextInt(99999) + 1;
	    
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), alarmIdentifier, intent2, 0);
	    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);

	    //Alarm's been set, so display a confirmation to the user.
		TextView reminderResult = (TextView)findViewById(R.id.reminder_text_view);
		SimpleDateFormat resultFormatter = new SimpleDateFormat("M-d-y HH:mm");
		String newDate = resultFormatter.format(reminderTime);
		reminderResult.setText(newDate);

	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_reminder, menu);
		return true;
	}

}
