package edu.ggc.it.reminders;

import java.util.Arrays;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.text.format.Time;

public class RemindersActivity extends Activity
{
	
	public static final String REMINDER_TEXT = "edu.ggc.it.reminders.reminderText";
	public static final String REMINDER_TIME = "edu.ggc.it.reminders.reminderTime";
	
	public static final String[] months_map = {"January", "February", "March", "April", "May",
	                                       "June", "July", "August", "September", "October",
	                                       "November", "December"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminders);
		
		// Set up the spinners on the view.
		
		// Months
		Spinner monthSpinner = (Spinner) findViewById(R.id.month_spinner);
		ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
		        R.array.months_array, android.R.layout.simple_spinner_item);
		monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		monthSpinner.setAdapter(monthAdapter);
		
		// Days
		Spinner daySpinner = (Spinner) findViewById(R.id.day_spinner);
		ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
		        R.array.days_array, android.R.layout.simple_spinner_item);
		dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daySpinner.setAdapter(dayAdapter);
		
		// Years
		Spinner yearSpinner = (Spinner) findViewById(R.id.year_spinner);
		ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
		        R.array.years_array, android.R.layout.simple_spinner_item);
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearSpinner.setAdapter(yearAdapter);
		
		// Hours
		Spinner timeSpinner = (Spinner) findViewById(R.id.times_spinner);
		ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
		        R.array.times_array, android.R.layout.simple_spinner_item);
		timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(timeAdapter);
		
		// AM/PM
		Spinner ampmSpinner = (Spinner) findViewById(R.id.ampm_spinner);
		ArrayAdapter<CharSequence> ampmAdapter = ArrayAdapter.createFromResource(this,
		        R.array.ampm_array, android.R.layout.simple_spinner_item);
		ampmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ampmSpinner.setAdapter(ampmAdapter);
		
		setupActionBar();
		
	}

	
	public void createReminder(View view)
	{
		
		Intent intent = new Intent(this, CreateReminderActivity.class);
		
		
		// Store the reminder text in the Intent.
		EditText reminderTextEdit = (EditText)findViewById(R.id.reminder_description);
		String reminderText = reminderTextEdit.getText().toString();
		intent.putExtra(REMINDER_TEXT, reminderText);
		
		
		// Retrieve the selected time options...
		Spinner monthSpinner = (Spinner)findViewById(R.id.month_spinner);
		String month_str = monthSpinner.getSelectedItem().toString();
		int month = Arrays.asList(months_map).indexOf(month_str);
		
		Spinner daySpinner = (Spinner)findViewById(R.id.day_spinner);
		int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
		
		Spinner yearSpinner = (Spinner)findViewById(R.id.year_spinner);
		int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
		
		Spinner hourSpinner = (Spinner)findViewById(R.id.times_spinner);
		int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString().substring(0, 2));
		
		Spinner ampmSpinner = (Spinner)findViewById(R.id.ampm_spinner);
		String ampm = ampmSpinner.getSelectedItem().toString();
		
		if(ampm.equals("PM"))
		{
			hour += 12;
		}
		
		// .. and store them in the intent.
		Time reminderTime = new Time();
		reminderTime.set(0, 0, hour, day, month, year);
		intent.putExtra(REMINDER_TIME, reminderTime.toMillis(false));
		
		startActivity(intent);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reminders, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
