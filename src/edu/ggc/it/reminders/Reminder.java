package edu.ggc.it.reminders;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.ggc.it.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public class Reminder extends BroadcastReceiver 
{    

	public String reminderText;
	public long reminderTime;
	public static final String REMINDER_TEXT = "edu.ggc.it.reminders.reminderText";

	public Reminder(){}

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) 
	{   

		//Request a wake lock from the device so that the notification will be shown
		//if the device is locked/sleeping.
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		//Build the notification for our reminder.
		Notification noti = new Notification.Builder(context)
		.setContentTitle("GGCReminders")
		.setContentText(intent.getStringExtra(REMINDER_TEXT))
		.setSmallIcon(R.drawable.ggc_connect_icon)
		.build();

		//Send the notification to the device NotificationManager
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, noti);

		Log.d("timer", "Alarm going off now.");
		
		//Release the wake lock - we're done.
		wl.release();
		
	}

}