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
 */
public class DirectoryActivity extends Activity
{
    public final static String EXTRA_MESSAGE = "edu.ggc.it.directory.MESSAGE";
	private Intent directorySearchIntent;
	private ListView recentSearches;
	private EditText firstNameField;
	private EditText lastNameField;
	private SavedSearchDatabase searchDatabase;
	private ArrayList<Long> listRowID = new ArrayList<Long>();
	private Context directoryContext;

	@Override
	public void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		directoryContext = this;
		setContentView(R.layout.activity_ggcdirectory);
        directorySearchIntent = new Intent(this, DirectorySearchWebView.class);

		ListView list = (ListView) findViewById(R.id.directory_listView);
		list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.parent_directories)));
		list.setOnItemClickListener(new DepartmentOnClickListener());

        recentSearches = (ListView) findViewById(R.id.listView1);
		SavedSearchDatabaseHelper.init(this);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

        TabSpec departmentsTab = tabHost.newTabSpec("First Tab");
		departmentsTab.setIndicator("Departments");
		departmentsTab.setContent(R.id.tab1);

        TabSpec newSearchTab = tabHost.newTabSpec("Second Tab");
		newSearchTab.setIndicator("Name Search");
		newSearchTab.setContent(R.id.tab2);

        tabHost.addTab(newSearchTab);
		tabHost.addTab(departmentsTab);

        Button clearSearch = (Button) findViewById(R.id.clearSearchButton);
        clearSearch.setOnClickListener(new DirectoryButtonListener());

        Button saveSearch = (Button) findViewById(R.id.saveSearchButton);
        saveSearch.setOnClickListener(new DirectoryButtonListener());

        Button search = (Button) findViewById(R.id.SearchDirectoryButton);
        search.setOnClickListener(new DirectoryButtonListener());

        firstNameField = (EditText) findViewById(R.id.firstNameText);
		lastNameField = (EditText) findViewById(R.id.lastNameText);
		firstNameField.clearFocus();
		lastNameField.clearFocus();

        searchDatabase = new SavedSearchDatabase(this);
		searchDatabase.open();

        populateList();

        recentSearches.setOnItemClickListener(new SavedSearchListener());
		recentSearches.setOnItemLongClickListener(new LongPressDeleteListener());
	}

	@Override
	protected void onDestroy()
    {
		super.onDestroy();
		searchDatabase.close();
	}

    protected void onResume()
    {
        super.onResume();
        refreshList();
    }

    public void searchName() {
        Intent intent = new Intent(this, DirectorySearchWebView.class);
        EditText firstNameText = (EditText) findViewById(R.id.firstNameText);
        EditText lastNameText = (EditText) findViewById(R.id.lastNameText);
        String firstname = firstNameText.getText().toString();
        String lastname = lastNameText.getText().toString();
        String wholeUrl = "http://www.ggc.edu/about-ggc/directory?firstname="
                + firstname + "&firstname_modifier=like&lastname=" + lastname
                + "&lastname_modifier=like&search=Search";
        intent.putExtra(EXTRA_MESSAGE, wholeUrl);
        startActivity(intent);
    }


    /**
     * To populate listview with searches saved in the database
     */
    private void populateList()
    {
        Cursor cursor = searchDatabase.queryAllByAscending();
        startManagingCursor(cursor);
        String[] from = new String[] { searchDatabase.KEY_LASTNAMEFIRSTNAME };
        int[] to = new int[] { R.id.taskEntry };
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_row, cursor, from, to);
        recentSearches.setAdapter(cursorAdapter);
    }

    /**
     * To refresh list upon updating database and clears focus and hides keyboard
     */
    private void refreshList()
    {
        listRowID.clear();
        populateList();
        firstNameField.clearFocus();
        lastNameField.clearFocus();
        findViewById(R.id.linearLayout_focus).requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(firstNameField.getWindowToken(), 0);
    }

    /**
	 * For saving searches
	 * @param lastFirst
	 *            string created from text entered into the search text fields
	 * @param url
	 *            string created by injecting first and last name entries into
	 *            the search url from ggc website directory
	 */
	private void updateDatabase(String lastFirst, String url)
    {
		ContentValues values = searchDatabase.createContentValues(lastFirst,url);
		searchDatabase.createRow(values);
		Toast.makeText(this,"To delete a search touch and hold a Saved Search",Toast.LENGTH_LONG).show();
		firstNameField.setText("");
		lastNameField.setText("");
		refreshList();
	}

	/**
	 * To filter empty search fields
	 */
	private void filterEmptySearchFields()
    {
		String first = firstNameField.getText().toString().trim();
		String last = lastNameField.getText().toString().trim();
		String lastFirst;
		String url = "http://www.ggc.edu/about-ggc/directory?firstname="+ first
                + "&firstname_modifier=like&lastname=" + last
				+ "&lastname_modifier=like&search=Search";

		if ((first != null && !first.isEmpty()) && (last != null && !last.isEmpty())) {
			lastFirst = last + ", " + first;
			updateDatabase(lastFirst, url);
		}

		if ((first == null || first.isEmpty()) && (last != null && !last.isEmpty())) {
			lastFirst = last;
			updateDatabase(lastFirst, url);
		}

		if ((last == null || last.isEmpty()) && (first != null && !first.isEmpty())) {
			lastFirst = first;
			updateDatabase(lastFirst, url);
		}

		if ((last == null || last.isEmpty()) && (first == null || first.isEmpty())) {
			emptySavedSearchAlertDialog();
		}
	}

    public class DirectoryButtonListener implements android.view.View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.clearSearchButton){
                firstNameField.setText("");
                lastNameField.setText("");
            } else if (v.getId() == R.id.saveSearchButton) {
                filterEmptySearchFields();
            } else if (v.getId() == R.id.SearchDirectoryButton) {
                searchName();
            }
        }
    }

    public class DepartmentOnClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id)
        {
            String wholeUrl;
            if (position == 0) {
                wholeUrl = "http://www.ggc.edu/admissions/meet-our-staff/";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
            if (position == 1) {
                wholeUrl = "http://www.ggc.edu/academics/schools/school-of-business/meet-our-faculty-and-staff/index.html";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
            if (position == 2) {
                wholeUrl = "http://www.ggc.edu/academics/schools/school-of-education/meet-the-faculty-and-staff/index.html";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
            if (position == 3) {
                wholeUrl = "http://www.ggc.edu/academics/schools/school-of-health-sciences/meet-the-faculty-staff/index.html";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
            if (position == 4) {
                wholeUrl = "http://www.ggc.edu/academics/schools/school-of-liberal-arts/meet%20the%20facultyand%20staff/index.html";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
            if (position == 5) {
                wholeUrl = "http://www.ggc.edu/academics/schools/school-of-science-and-technology/meet-faculty-and-staff.html";
                directorySearchIntent.putExtra(EXTRA_MESSAGE, wholeUrl);
                startActivity(directorySearchIntent);
            }
        }
    }

	public class SavedSearchListener implements AdapterView.OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long rowId)
        {
			String url = searchDatabase.getName(rowId);
			directorySearchIntent.putExtra(EXTRA_MESSAGE, url);
			startActivity(directorySearchIntent);
			findViewById(R.id.linearLayout_focus).requestFocus();
		}
	}

	public class LongPressDeleteListener implements OnItemLongClickListener
    {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long rowId)
        {
			showConfirmDeleteSavedSearch(rowId);
			return false;
		}

	}

	private void showConfirmDeleteSavedSearch(final long rowId)
    {
		String message = "WARNING: Are you sure you want to delete this search?";
        new AlertDialog.Builder(directoryContext).setTitle("Confirm Clear Saved Search")
                .setMessage(message).setIcon(android.R.drawable.stat_sys_warning).
                setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								searchDatabase.deleteRow(rowId);
								refreshList();
							}
						}).setNegativeButton("No", null).show();
	}

	private void emptySavedSearchAlertDialog()
    {
		String message = "WARNING: You must enter at minimum part of the first name or last name " +
                "to be able to create a saved search";
        new AlertDialog.Builder(directoryContext).setTitle("Saved Search Fields Empty")
				.setMessage(message).setIcon(android.R.drawable.stat_sys_warning)
				.setPositiveButton("OK", null).show();
	}
}