package edu.ggc.it.schedule;

import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import edu.ggc.it.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.widget.Spinner;

public class SchedulePreferenceActivity extends Activity {
	
	private Spinner spnReminderTime;
	
	public static final String SHARED_PREFERENCES_FILE = "schedule_reminder";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_preference);
		
		spnReminderTime = (Spinner) findViewById(R.id.schedule_pref_spn_reminder);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
		spnReminderTime.setSelection(settings.getInt("schedule_reminder_pos", 0));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(SHARED_PREFERENCES_FILE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("schedule_reminder_string", spnReminderTime.getSelectedItem().toString());
		editor.putInt("schedule_reminder_pos", spnReminderTime.getSelectedItemPosition());
		editor.commit();
	}
}
