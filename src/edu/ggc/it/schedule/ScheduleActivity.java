package edu.ggc.it.schedule;

import edu.ggc.it.Credits;
import edu.ggc.it.Main;
import edu.ggc.it.News;
import edu.ggc.it.R;
import edu.ggc.it.todo.ToDoListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;

public class ScheduleActivity extends Activity {

	private Context scheduleContext;
	private ScheduleDatabase database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		scheduleContext = this;

		// open new database
		database = new ScheduleDatabase(scheduleContext);
		database.open();

		if (!classesExist()) {
			showAddScheduleItemDialog();
		}

		populateList();
	}

	private void populateList() {
		// TODO Populate the list from the database

	}

	private boolean classesExist() {
		Cursor c = database.queryAll();
		if (c.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void showAddScheduleItemDialog() {
		new AlertDialog.Builder(this)
				.setTitle("No Classes Found")
				.setMessage(R.string.schedule_no_classes_popup)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(new Intent(scheduleContext,
										ScheduleUpdateActivity.class));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).show();
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
		case R.id.add_class:
			startActivity(new Intent(scheduleContext,
					ScheduleUpdateActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

}
