package edu.ggc.it.directory;

import edu.ggc.it.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to either update/delete items from the Saved Search Database
 * 
 * @author crystalist
 * @modified Jesse Perkins
 * 
 */
public class SavedSearchUpdateActivity extends Activity
{
	public static final String SAVE_ROW = "saverow";
	private SavedSearchDatabase database;
	private Long rowID;
	private EditText editTextFirst;
	private EditText editTextLast;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_updatedb);
		editTextFirst = (EditText) findViewById(R.id.firstNameText);
		editTextLast = (EditText) findViewById(R.id.lastNameText);
		rowID = null;

		if (bundle == null) {
			Bundle extras = getIntent().getExtras();

			if (extras != null && extras.containsKey("rowID")) {
				rowID = extras.getLong("rowID");
			}

		} else {
			rowID = bundle.getLong(SAVE_ROW);
		}

		database = new SavedSearchDatabase(this);
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
			editTextFirst.setText(cursor.getString(SavedSearchDatabase.INDEX_LASTNAMEFIRSTNAME));
			cursor.close();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void save() {
		String first = editTextFirst.getText().toString();
		String last = editTextLast.getText().toString();
		String last_first = last + ", " + first;
		String url = "http://www.ggc.edu/about-ggc/directory?firstname="
				+ first + "&firstname_modifier=like&lastname=" + last
				+ "&lastname_modifier=like&search=Search";
		
		if (rowID != null && !first.equals("")) {
			database.updateRow(rowID, database.createContentValues(last_first, url));
		} else if (!first.equals("")) {
			rowID = database.createRow(database.createContentValues(last_first, url));
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
