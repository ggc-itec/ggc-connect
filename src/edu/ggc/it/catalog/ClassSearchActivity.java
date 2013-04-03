package edu.ggc.it.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.ggc.it.R;
import edu.ggc.it.banner.Banner;
import edu.ggc.it.banner.Course;
import edu.ggc.it.banner.CourseDataSource;
import edu.ggc.it.banner.Instructor;
import edu.ggc.it.banner.Section;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * This activity allows the user to specify the criteria for classes to search for.
 * @author Jacob
 *
 */
public class ClassSearchActivity extends Activity {
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
	
	private Set<String> courseNumbers;
	private Set<String> courseNames;
	private Set<String> instructorNames;
	
	private ArrayAdapter<String> courseAdapter;
	private ArrayAdapter<String> instructorAdapter;
	
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
		
		// create auto-complete objects
		courseNumbers = new HashSet<String>();
		courseNames = new HashSet<String>();
		instructorNames = new HashSet<String>();
		
		courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
		instructorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
		
		courseNumberInput.setAdapter(courseAdapter);
		instructorInput.setAdapter(instructorAdapter);
		
		// populate spinner
		getTerms();
		
		// connect to database
		courseDS = new CourseDataSource(this);
		courseDS.open();
		
		// add event listeners
		TermChangeListener termListener = new TermChangeListener();
		termInput.setOnItemSelectedListener(termListener);
		
		SubjectClickListener clickListener = new SubjectClickListener();
		subjectList.setOnItemClickListener(clickListener);
		
		SubmitButtonListener submitListener = new SubmitButtonListener();
		searchButton.setOnClickListener(submitListener);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		courseDS.close();
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
	
	private class TermChangeListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			clearSelections();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			clearSelections();
		}
		
		private void clearSelections(){
			synchronized (ClassSearchActivity.this){
				hideOptions();
				courseNames.clear();
				courseNumbers.clear();
				instructorNames.clear();
				
				SparseBooleanArray checkedItems = subjectList.getCheckedItemPositions();
				for (int i = 0; i < checkedItems.size(); i++){
					if (checkedItems.get(i))
						subjectList.setItemChecked(i, false);
				}
			}
		}
	}
	
	private class SubjectClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String subjId = getResources().getStringArray(R.array.ds_subject_codes)[position];
			String term = terms.get(termInput.getSelectedItem());
			final boolean selected = subjectList.getCheckedItemPositions().get(position);
			
			AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
				@Override
				protected Boolean doInBackground(String... args) {
					String subjId = args[0];
					String term = args[1];
					
					// have we already imported these courses for this term?
					if (!courseDS.hasCourses(term, subjId)){
						// pull it from Banner
						List<Course> courses = Banner.getCourses(term, subjId);
						courseDS.addCourses(courses);
						List<Section> sections = Banner.getSubjectSections(term, subjId);
						courseDS.addSections(sections);
					}
					
					if (selected){
						// pull the courses and instructors and add them to our sets
						List<Course> courses = courseDS.getSubjectCourses(term, subjId);
						List<Instructor> instructors = courseDS.getSubjectInstructors(term, subjId);
						
						synchronized (ClassSearchActivity.this){
							for (Course course: courses){
								courseNumbers.add(course.getId());
								courseNames.add(course.getName());
							}
							
							for (Instructor instructor: instructors)
								instructorNames.add(instructor.getName());
						}
					} else{
						// rebuild the list
						SparseBooleanArray selectedItems = subjectList.getCheckedItemPositions();
						
						synchronized (ClassSearchActivity.this){
							courseNumbers.clear();
							courseNames.clear();
							instructorNames.clear();
							
							for (int i = 0; i < selectedItems.size(); i++){
								if (selectedItems.get(i)){
									String subj = getResources().getStringArray(R.array.ds_subject_codes)[i];
									List<Course> courses = courseDS.getSubjectCourses(term, subj);
									List<Instructor> instructors = courseDS.getSubjectInstructors(term, subj);
									
									for (Course course: courses){
										courseNumbers.add(course.getId());
										courseNames.add(course.getName());
									}
									
									for (Instructor instructor: instructors)
										instructorNames.add(instructor.getName());
								}
							}
						}
					}
					
					return subjectList.getCheckedItemCount() > 0;
				}
				
				@Override
				protected void onPostExecute(Boolean hasSelections){
					synchronized (ClassSearchActivity.this){
						if (hasSelections){
							// set adapters for the auto-complete views
							courseAdapter.clear();
							courseAdapter.addAll(courseNumbers);
							courseAdapter.notifyDataSetChanged();
							instructorAdapter.clear();
							instructorAdapter.addAll(instructorNames);
							instructorAdapter.notifyDataSetChanged();
							
							showOptions();
						} else{
							// hide options until a selection is made
							hideOptions();
						}
					}
				}
			};
			
			task.execute(subjId, term);
		}
		
	}
	
	private class SubmitButtonListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			Toast toast = Toast.makeText(ClassSearchActivity.this, "search!", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}
}
