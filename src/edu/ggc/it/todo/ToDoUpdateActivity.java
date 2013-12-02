package edu.ggc.it.todo;

import edu.ggc.it.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to either update/delete items from the ToDo Database
 * 
 * @author crystalist
 * 
 */
public class ToDoUpdateActivity extends Activity {

	/**
	 * Hold value of the row that was being edited
	 */
	public static final String SAVE_ROW = "saverow";

	private ToDoDatabase database;
	private Long rowID;
	private EditText editTextTask;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_updatedb);
		editTextTask = (EditText) findViewById(R.id.editTextTask);
		rowID = null;

		if (bundle == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey("rowID")) {
				rowID = extras.getLong("rowID");
			}
		} else {
			rowID = bundle.getLong(SAVE_ROW);
		}

		database = new ToDoDatabase(this);
		database.open();
		databaseToUI();

		Button saveButton = (Button) findViewById(R.id.buttonSaveToDoDB);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				save();
			}
		});

		Button deleteButton = (Button) findViewById(R.id.buttonDeleteToDoDB);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				delete();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		database.close();
	}

	private void databaseToUI() {
		if (rowID != null) {
			Cursor cursor = database.query(rowID);
			editTextTask.setText(cursor.getString(ToDoDatabase.INDEX_TASK));
			cursor.close();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

    /**
     * update only if the string is not empty
     */
	private void save() {
		String task = editTextTask.getText().toString();

		if (rowID != null && !task.equals("")) {
			database.updateRow(rowID, database.createContentValues(task));
		} else if (!task.equals("")) {
			rowID = database.createRow(database.createContentValues(task));
		}
		finish();
	}

	private void delete() {
		if (rowID != null) {
			database.deleteRow(rowID);
		}
		finish();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SAVE_ROW, rowID);
	}
}
