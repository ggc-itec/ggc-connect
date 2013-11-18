package edu.ggc.it.reminders;

import java.util.Arrays;

import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import edu.ggc.it.R.menu;
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

public class RemindersActivity extends Activity {
	
	public static final String REMINDER_TEXT = "edu.ggc.it.reminders.reminderText";
	public static final String REMINDER_TIME = "edu.ggc.it.reminders.reminderTime";
	
	public static final String[] months_map = {"January", "February", "March", "April", "May",
	                                       "June", "July", "August", "September", "October",
	                                       "November", "December"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminders);
		// Show the Up button in the action bar.
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.months_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		        R.array.days_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter2);
		
		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
		        R.array.years_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner3.setAdapter(adapter3);
		
		Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
		        R.array.times_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner4.setAdapter(adapter4);
		
		Spinner spinner6 = (Spinner) findViewById(R.id.spinner6);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
		        R.array.ampm_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner6.setAdapter(adapter5);
		
		setupActionBar();
	}
	
	
	public void createReminder(View view){
		
		Intent intent = new Intent(this, CreateReminderActivity.class);
		
		EditText reminderTextEdit = (EditText)findViewById(R.id.reminderDescription);
		String reminderText = reminderTextEdit.getText().toString();
		intent.putExtra(REMINDER_TEXT, reminderText);
		
		Spinner monthSpinner = (Spinner)findViewById(R.id.spinner1);
		String month_str = monthSpinner.getSelectedItem().toString();
		int month = Arrays.asList(months_map).indexOf(month_str);
		
		Spinner daySpinner = (Spinner)findViewById(R.id.spinner2);
		int day = Integer.parseInt(daySpinner.getSelectedItem().toString());
		
		Spinner yearSpinner = (Spinner)findViewById(R.id.spinner3);
		int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());
		
		Spinner hourSpinner = (Spinner)findViewById(R.id.spinner4);
		int hour = Integer.parseInt(hourSpinner.getSelectedItem().toString().substring(0, 2));
		
		Spinner ampmSpinner = (Spinner)findViewById(R.id.spinner6);
		String ampm = ampmSpinner.getSelectedItem().toString();
		
		if(ampm.equals("PM")){
			hour += 12;
		}
		
		Time reminderTime = new Time();
		reminderTime.set(0, 58, hour, day, month, year);
		intent.putExtra(REMINDER_TIME, reminderTime.toMillis(false));
		
		startActivity(intent);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reminders, menu);
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
