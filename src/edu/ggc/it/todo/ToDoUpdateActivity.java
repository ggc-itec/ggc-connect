package edu.ggc.it.todo;

import edu.ggc.it.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ToDoUpdateActivity extends Activity {

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

		Button button = (Button) findViewById(R.id.buttonSaveDB);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
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
			// cursor.getColumnIndex(columnName);
			editTextTask.setText(cursor.getString(ToDoDatabase.INDEX_TASK));
			cursor.close();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		save();
	}

	private void save() {
		String task = editTextTask.getText().toString();

		if (rowID != null) {
			database.updateRow(rowID, database.createContentValues(task));
		} else {
			rowID = database.createRow(database.createContentValues(task));
		}
	}

	public static final String SAVE_ROW = "saverow";

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SAVE_ROW, rowID);
	}
}
