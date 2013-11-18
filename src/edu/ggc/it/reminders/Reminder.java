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

	public Reminder(String reminderText, long reminderTime){
		this.reminderText = reminderText;
		this.reminderTime = reminderTime;
	}

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) 
	{   
		Log.d("timer", "Received");
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		wl.acquire();

		Notification noti = new Notification.Builder(context)
		.setContentTitle("GGCReminders")
		.setContentText(intent.getStringExtra(REMINDER_TEXT))
		.setSmallIcon(R.drawable.green)
		.build();

		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, noti);

		Log.d("timer", "Alarm going off now.");
		wl.release();
	}

	public void SetAlarm(Context context)
	{

		Date date = new Date(this.reminderTime);
		DateFormat formatter = new SimpleDateFormat("M-d-y HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date);

		Log.d("timer", "The time for the alarm is " + dateFormatted);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Reminder.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.set(AlarmManager.RTC_WAKEUP, this.reminderTime, pi);
	}

	public void CancelAlarm(Context context)
	{
		Intent intent = new Intent(context, Reminder.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}