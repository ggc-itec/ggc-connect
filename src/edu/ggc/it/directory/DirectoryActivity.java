package edu.ggc.it.directory;

import edu.ggc.it.R;
import edu.ggc.it.directory.SavedSearchDatabase.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ggcdirectory);
		intent2 = new Intent(this, DirectorySearchWebView.class);
		list = (ListView) findViewById(R.id.directory_listView);
		list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.parent_directories)));
		list.setOnItemClickListener(new departmentOnClickListener());
		recentSearches = (ListView) findViewById(R.id.listView1);
		SavedSearchDatabaseHelper.init(this);
		// recentSearches.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, getResources()
		// .getStringArray(R.array.parent_directories)));
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
		clearSearch.setOnClickListener(new clearSearchListener());
		saveSearch = (Button) findViewById(R.id.saveSearchButton);
		saveSearch.setOnClickListener(new saveSearchListener());
		searchDatabase = new SavedSearchDatabase(this);
		searchDatabase.open();

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

	public class clearSearchListener implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.clearSearchButton)
				;
			firstNameField.setText("");
			lastNameField.setText("");
		}

	}

	public class departmentOnClickListener implements
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
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-liberal-arts/meet%20the%20facultyand%20staff/index.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}
			if (position == 4) {
				wholeUrl = "http://www.ggc.edu/academics/schools/school-of-science-and-technology/meet-faculty-and-staff.html";
				intent2.putExtra(EXTRA_MESSAGE, wholeUrl);
				startActivity(intent2);
			}

		}

	}

	public class saveSearchListener implements
			android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			updateDatabase();
		}
	}

	/**
	 * @Method for saving searches
	 */
	
	public void updateDatabase() {
		//TODO: check for searches already saved
		String first = firstNameField.getText().toString().trim();
		String last = lastNameField.getText().toString().trim();
		String url = "http://www.ggc.edu/about-ggc/directory?firstname="
				+ first + "&firstname_modifier=like&lastname=" + last
				+ "&lastname_modifier=like&search=Search";
		ContentValues values = searchDatabase.createContentValues(first, last,
				url);
		searchDatabase.createRow(values);
		firstNameField.setText("");
		lastNameField.setText("");
	}

}
