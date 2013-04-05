package edu.ggc.it.directory;

import java.util.ArrayList;
import edu.ggc.it.R;
import edu.ggc.it.directory.SavedSearchDatabase.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

/**
 * Activity to Search for current staff
 * 
 * @author Jesse Perkins
 * 
 */

public class DirectoryActivity extends Activity {

	private TabHost tabHost;
	private Intent intent2;
	private ListView list;
	private ListView recentSearches;
	public final static String EXTRA_MESSAGE = "edu.ggc.it.directory.MESSAGE";
	public final static String lastName = "edu.ggc.it.directory.MESSAGE";
	private Button clearSearch;
	private EditText firstNameField;
	private EditText lastNameField;
	private Button saveSearch;
	private SimpleCursorAdapter cursorAdapter;
	private SavedSearchDatabase searchDatabase;
	private ArrayList<Long> listRowID = new ArrayList<Long>();
	private Context directoryContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		directoryContext = this;
		setContentView(R.layout.activity_ggcdirectory);
		intent2 = new Intent(this, DirectorySearchWebView.class);
		list = (ListView) findViewById(R.id.directory_listView);
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.parent_directories)));
		list.setOnItemClickListener(new DepartmentOnClickListener());
		recentSearches = (ListView) findViewById(R.id.listView1);
		SavedSearchDatabaseHelper.init(this);
		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		TabSpec spec1 = tabHost.newTabSpec("First Tab");
		spec1.setIndicator("Departments");
		spec1.setContent(R.id.tab1);

		TabSpec spec2 = tabHost.newTabSpec("Second Tab");
		spec2.setIndicator("Name Search");
		spec2.setContent(R.id.tab2);

		tabHost.addTab(spec2);
		tabHost.addTab(spec1);

		clearSearch = (Button) findViewById(R.id.clearSearchButton);
		firstNameField = (EditText) findViewById(R.id.firstNameText);
		lastNameField = (EditText) findViewById(R.id.lastNameText);
		firstNameField.clearFocus();
		lastNameField.clearFocus();
		clearSearch.setOnClickListener(new ClearSearchListener());
		saveSearch = (Button) findViewById(R.id.saveSearchButton);
		saveSearch.setOnClickListener(new SaveSearchListener());
		searchDatabase = new SavedSearchDatabase(this);
		searchDatabase.open();

		populateList();
		recentSearches.setOnItemClickListener(new SavedSearchListener());
		recentSearches
				.setOnItemLongClickListener(new LongPressDeleteListener());

	}

	/**
	 * @method for taking input and searching GGC directory
	 */

	public void searchName(View view) {
		Intent intent = new Intent(this, DirectorySearchWebView.class);
		EditText editText = (EditText) findViewById(R.id.firstNameText);
		EditText editText2 = (EditText) findViewById(R.id.lastNameText);
		String message = editText.getText().toString();
		String message2 = editText2.getText().toString();
		String wholeUrl = "http://www.ggc.edu/about-ggc/directory?firstname="
				+ message + "&firstname_modifier=like&lastname=" + message2
				+ "&lastname_modifier=like&search=Search";
		intent.putExtra(EXTRA_MESSAGE, wholeUrl);
		startActivity(intent);
	}

	public class ClearSearchListener implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.clearSearchButton)
				;
			firstNameField.setText("");
			lastNameField.setText("");
		}

	}

	public class DepartmentOnClickListener implements
			android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			String wholeUrl;
			if (position == 0) {
				wholeUrl = "http://www.ggc.edu/admissions/meet-our-staff/";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 1) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-business/meet-our-faculty-and-staff/index.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 2) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-education/meet-the-faculty-and-staff/index.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 3) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-health-sciences/meet-the-faculty-staff/index.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 4) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-liberal-arts/meet%20the%20facultyand%20staff/index.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 5) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-science-and-technology/meet-faculty-and-staff.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			
		}

	}

	public class SaveSearchListener implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			filterEmptySearchFields();
		}
	}

	/**
	 * @Method for saving searches
	 * @param last_first
	 *            string created from text entered into the search text fields
	 * @param url
	 *            string created by injecting first and last name entries into
	 *            the search url from ggc website directory
	 */

	public void updateDatabase(String last_first, String url) {
		// TODO: check for searches already saved

		ContentValues values = searchDatabase.createContentValues(last_first,
				url);
		searchDatabase.createRow(values);
		Toast.makeText(this,
				"To delete a search touch and hold a Saved Search",
				Toast.LENGTH_LONG).show();
		firstNameField.setText("");
		lastNameField.setText("");
		refreshList();
	}

	/**
	 * @method to filter empty search fields
	 */

	private void filterEmptySearchFields() {
		String first = firstNameField.getText().toString().trim();
		String last = lastNameField.getText().toString().trim();
		String last_first;
		String url = "http://www.ggc.edu/about-ggc/directory?firstname="
				+ first + "&firstname_modifier=like&lastname=" + last
				+ "&lastname_modifier=like&search=Search";
		if ((first != null && !first.equalsIgnoreCase(""))
				&& (last != null && !last.equalsIgnoreCase(""))) {
			last_first = last + ", " + first;
			updateDatabase(last_first, url);

		}
		if ((first == null || first.equalsIgnoreCase(""))
				&& (last != null && !last.equalsIgnoreCase(""))) {
			last_first = last;
			updateDatabase(last_first, url);
		}
		if ((last == null || last.equalsIgnoreCase(""))
				&& (first != null && !first.equalsIgnoreCase(""))) {
			last_first = first;
			updateDatabase(last_first, url);

		}
		if ((last == null || last.equalsIgnoreCase(""))
				&& (first == null || first.equalsIgnoreCase(""))) {
			emptySavedSearchAlertDialog();

		}

	}
	
	/**
	 * @method to refresh list upon updating database and clears focus and hides keyboard
	 */
	
	private void refreshList() {
		listRowID.clear();
		populateList();
		firstNameField.clearFocus();
		lastNameField.clearFocus();
		findViewById(R.id.linearLayout_focus).requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(firstNameField.getWindowToken(), 0);

	}

	protected void onResume() {
		super.onResume();
		refreshList();
	}

	public class SavedSearchListener implements
			android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long rowId) {
			String url = searchDatabase.getName(rowId);
			intent2.putExtra(EXTRA_MESSAGE, url);
			startActivity(intent2);
			findViewById(R.id.linearLayout_focus).requestFocus();

		}

	}

	public class LongPressDeleteListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v,
				int position, long rowId) {
			showConfirmDeleteSavedSearch(rowId);
			return false;
		}

	}

	/**
	 * @method show AlertDialog when to confirm delete saved search
	 * @param rowId
	 */
	private void showConfirmDeleteSavedSearch(final long rowId) {
		new AlertDialog.Builder(directoryContext)
				.setTitle("Confirm Clear Saved Search")
				.setMessage(
						"WARNING: Are you sure you want to delete this search?")
				.setIcon(android.R.drawable.stat_sys_warning)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								searchDatabase.deleteRow(rowId);
								refreshList();
							}
						}).setNegativeButton("No", null).show();
	}

	/**
	 * @method show AlertDialog when trying to saved an empty search
	 * 
	 */
	private void emptySavedSearchAlertDialog() {
		new AlertDialog.Builder(directoryContext)
				.setTitle("Saved Search Fields Empty")
				.setMessage(
						"WARNING: You must enter at minimum part of the first name or last name"
								+ " to be able to create a saved search")
				.setIcon(android.R.drawable.stat_sys_warning)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	/**
	 * @method to populate listview with searches saved in the database
	 */
	private void populateList() {
		Cursor cursor = searchDatabase.queryAllByAscending();
		startManagingCursor(cursor);
		String[] from = new String[] { searchDatabase.KEY_LASTNAMEFIRSTNAME };
		int[] to = new int[] { R.id.taskEntry };
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row,
				cursor, from, to);
		recentSearches.setAdapter(cursorAdapter);
	}

}
