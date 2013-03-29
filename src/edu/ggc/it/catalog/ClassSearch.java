package edu.ggc.it.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ggc.it.R;
import edu.ggc.it.banner.CourseDataSource;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableRow;

/**
 * This activity allows the user to specify the criteria for classes to search for.
 * @author Jacob
 *
 */
public class ClassSearch extends Activity {
	private Spinner termInput;
	private ListView subjectList;
	private TableRow courseNumberRow;
	private AutoCompleteTextView courseNumberInput;
	private TableRow creditsRow;
	private EditText creditsInput;
	private TableRow startTimeRow;
	private EditText startTimeInput;
	private TableRow endTimeRow;
	private EditText endTimeInput;
	private TableRow daysRow;
	private ListView daysList;
	private TableRow instructorRow;
	private MultiAutoCompleteTextView instructorInput;
	private TableRow searchRow;
	private Button searchButton;
	private Map<String, String> terms;
	private CourseDataSource courseDS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_search);
		
		terms = new HashMap<String, String>();
		
		// find views
		termInput = (Spinner)findViewById(R.id.cs_term_input);
		subjectList = (ListView)findViewById(R.id.cs_subject_list);
		courseNumberRow = (TableRow)findViewById(R.id.cs_course_number_row);
		courseNumberInput = (AutoCompleteTextView)findViewById(R.id.cs_course_number_input);
		creditsRow = (TableRow)findViewById(R.id.cs_credits_row);
		creditsInput = (EditText)findViewById(R.id.cs_credits_input);
		startTimeRow = (TableRow)findViewById(R.id.cs_start_time_row);
		startTimeInput = (EditText)findViewById(R.id.cs_start_time_input);
		endTimeRow = (TableRow)findViewById(R.id.cs_end_time_row);
		endTimeInput = (EditText)findViewById(R.id.cs_end_time_input);
		daysRow = (TableRow)findViewById(R.id.cs_days_row);
		daysList = (ListView)findViewById(R.id.cs_days_list);
		instructorRow = (TableRow)findViewById(R.id.cs_instructor_row);
		instructorInput = (MultiAutoCompleteTextView)findViewById(R.id.cs_instructor_input);
		searchRow = (TableRow)findViewById(R.id.cs_search_row);
		searchButton = (Button)findViewById(R.id.cs_search_button);
		
		// populate spinner
		getTerms();
		
		// connect to database
		courseDS = new CourseDataSource(this);
		courseDS.open();
		
		// add event listeners
		SubjectSelectListener selectListener = new SubjectSelectListener();
		
		subjectList.setOnItemSelectedListener(selectListener);
	}
	
	private void getTerms(){
		// still trying to determine how to generate this list automatically; in the meantime, it's hardcoded
		terms.put("Spring 2013",  "201302");
		terms.put("Summer 2013", "201305");
		terms.put("Fall 2013", "201308");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				new ArrayList<String>(terms.keySet()));
		termInput.setAdapter(adapter);
	}
	
	private void showOptions(){
		courseNumberRow.setVisibility(View.VISIBLE);
		creditsRow.setVisibility(View.VISIBLE);
		startTimeRow.setVisibility(View.VISIBLE);
		endTimeRow.setVisibility(View.VISIBLE);
		daysRow.setVisibility(View.VISIBLE);
		instructorRow.setVisibility(View.VISIBLE);
		searchRow.setVisibility(View.VISIBLE);
	}
	
	private void hideOptions(){
		courseNumberRow.setVisibility(View.INVISIBLE);
		creditsRow.setVisibility(View.INVISIBLE);
		startTimeRow.setVisibility(View.INVISIBLE);
		endTimeRow.setVisibility(View.INVISIBLE);
		daysRow.setVisibility(View.INVISIBLE);
		instructorRow.setVisibility(View.INVISIBLE);
		searchRow.setVisibility(View.INVISIBLE);
	}
	
	private class SubjectSelectListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			String subjId = getResources().getStringArray(R.array.ds_subject_codes)[position];
			String term = terms.get(termInput.getSelectedItem());
			
			// have we already imported these courses for this term?
			if (courseDS.hasCourses(term, subjId)){
				// pull it from the DB
				
			} else{
				// pull it from Banner
				
			}
			
			showOptions();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// disallow setting other search options until a subject has been selected
			hideOptions();
		}
		
	}
}
