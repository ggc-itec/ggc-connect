package edu.ggc.it.reminders;

import edu.ggc.it.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

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
		Notification noti = new NotificationCompat.Builder(context)
		.setContentTitle("GGCReminders")
		.setContentText(intent.getStringExtra(REMINDER_TEXT))
		.setSmallIcon(R.drawable.ggc_connect_icon)
		.build();

		//Send the notification to the device NotificationManager
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, noti);

		//Release the wake lock - we're done.
		wl.release();
		
	}

}