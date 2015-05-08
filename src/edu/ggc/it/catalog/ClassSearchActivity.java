package edu.ggc.it.catalog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import edu.ggc.it.R;
import edu.ggc.it.banner.Banner;
import edu.ggc.it.banner.Course;
import edu.ggc.it.banner.CourseDataSource;
import edu.ggc.it.banner.CourseSearchBuilder;
import edu.ggc.it.banner.Instructor;
import edu.ggc.it.banner.Section;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity allows the user to specify the criteria for classes to search for.
 * @author Jacob
 *
 */
public class ClassSearchActivity extends Activity
{
	private static final String TERM_SPRING = "02";
	private static final String TERM_SUMMER = "05";
	private static final String TERM_FALL = "08";
	private static final String TAG = "ClassSearchActivity";
	private static final SimpleDateFormat ANDROID_TIME = new SimpleDateFormat("hh:mma", Locale.US);
	private int cs_selected;
	private int cs_unselected;
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
	private CheckedTextView mondayInput;
	private CheckedTextView tuesdayInput;
	private CheckedTextView wednesdayInput;
	private CheckedTextView thursdayInput;
	private CheckedTextView fridayInput;
	private CheckedTextView saturdayInput;
	private TableRow instructorRow;
	private AutoCompleteTextView instructorInput;
	private TableRow searchRow;
    private Map<String, String> terms;
	private CourseDataSource courseDS;
    private Set<String> courseNumbers;
	private Set<String> courseNames;
	private Set<String> instructorNames;
	private ArrayAdapter<String> courseAdapter;
	private ArrayAdapter<String> instructorAdapter;
	private Integer asyncCounter;
	
	@Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.class_search_menu, menu);
                
                return true;
        }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    if( item.getItemId() == R.id.action_catalog_refresh)
	    {
	        new AlertDialog.Builder(this)
	        .setTitle("Refresh Data")
	        .setMessage("Are you sure you want to delete downloaded data?")
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                courseDS.deleteTables();
	            }
	         })
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                //don't do anything
	            }
	         })
	         .show();
	    }
	            
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_search);
		
		terms = new LinkedHashMap<String, String>();
		
		asyncCounter = 0;
		
		Resources resources = getResources();
		cs_selected = resources.getColor(R.color.cs_selected);
		cs_unselected = resources.getColor(R.color.cs_unselected);

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
		mondayInput = (CheckedTextView)findViewById(R.id.cs_monday_check);
		tuesdayInput = (CheckedTextView)findViewById(R.id.cs_tuesday_check);
		wednesdayInput = (CheckedTextView)findViewById(R.id.cs_wednesday_check);
		thursdayInput = (CheckedTextView)findViewById(R.id.cs_thursday_check);
		fridayInput = (CheckedTextView)findViewById(R.id.cs_friday_check);
		saturdayInput = (CheckedTextView)findViewById(R.id.cs_saturday_check);
		instructorRow = (TableRow)findViewById(R.id.cs_instructor_row);
		instructorInput = (AutoCompleteTextView)findViewById(R.id.cs_instructor_input);
		searchRow = (TableRow)findViewById(R.id.cs_search_row);
        Button searchButton = (Button) findViewById(R.id.cs_search_button);

		courseNumbers = new HashSet<String>();
		courseNames = new HashSet<String>();
		instructorNames = new HashSet<String>();
		
		courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
		instructorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
		
		courseNumberInput.setAdapter(courseAdapter);
		instructorInput.setAdapter(instructorAdapter);
		
		courseNumberInput.setThreshold(1);
		instructorInput.setThreshold(1);

		getTerms();

		getSubjects();

		courseDS = new CourseDataSource(this);
		courseDS.open();

		TermChangeListener termListener = new TermChangeListener();
		termInput.setOnItemSelectedListener(termListener);
		
		SubjectClickListener clickListener = new SubjectClickListener();
		subjectList.setOnItemClickListener(clickListener);
		
		SubmitButtonListener submitListener = new SubmitButtonListener();
		searchButton.setOnClickListener(submitListener);
		
		DayClickListener dayListener = new DayClickListener();
		mondayInput.setOnClickListener(dayListener);
		tuesdayInput.setOnClickListener(dayListener);
		wednesdayInput.setOnClickListener(dayListener);
		thursdayInput.setOnClickListener(dayListener);
		fridayInput.setOnClickListener(dayListener);
		saturdayInput.setOnClickListener(dayListener);

        TextView focusHolder = (TextView) findViewById(R.id.focusholder);
		focusHolder.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(termInput.getWindowToken(), 0);
		imm.hideSoftInputFromInputMethod(subjectList.getWindowToken(), 0);
		imm.hideSoftInputFromInputMethod(focusHolder.getWindowToken(), 0);
	}
	
	@Override
	protected void onDestroy()
    {
		super.onDestroy();
		courseDS.close();
	}
	
	private void getTerms()
    {
	    
	    terms.put("Fall 2015", "2015" + TERM_FALL);
	    terms.put("Summer 2015", "2015" + TERM_SUMMER);
	    terms.put("Spring 2015", "2015" + TERM_SPRING);
            terms.put("Summer 2015", "2015" + TERM_SUMMER);
	    terms.put("Fall 2014", "2014" + TERM_FALL);
            terms.put("Summer 2014", "2014" + TERM_SUMMER);
            terms.put("Spring 2014",  "2014" + TERM_SPRING);
            terms.put("Fall 2013", "2013" + TERM_FALL);
            terms.put("Summer 2013", "2013" + TERM_SUMMER);		
            terms.put("Spring 2013",  "2013" + TERM_SUMMER);
	    
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				new ArrayList<String>(terms.keySet()));
		termInput.setAdapter(adapter);
	}

	/**
	 * Populates the subject list
	 */
	private void getSubjects()
    {
		String[] subjects = getResources().getStringArray(R.array.ds_subject_names);
		HighlightAdapter adapter = new HighlightAdapter(this, android.R.layout.simple_list_item_1, subjects);
		subjectList.setAdapter(adapter);
	}
	
	private void showOptions()
    {
		courseNumberRow.setVisibility(View.VISIBLE);
		creditsRow.setVisibility(View.VISIBLE);
		startTimeRow.setVisibility(View.VISIBLE);
		endTimeRow.setVisibility(View.VISIBLE);
		daysRow.setVisibility(View.VISIBLE);
		instructorRow.setVisibility(View.VISIBLE);
		searchRow.setVisibility(View.VISIBLE);
	}
	
	private void hideOptions()
    {
		courseNumberRow.setVisibility(View.INVISIBLE);
		creditsRow.setVisibility(View.INVISIBLE);
		startTimeRow.setVisibility(View.INVISIBLE);
		endTimeRow.setVisibility(View.INVISIBLE);
		daysRow.setVisibility(View.INVISIBLE);
		instructorRow.setVisibility(View.INVISIBLE);
		searchRow.setVisibility(View.INVISIBLE);
	}
	
	private class HighlightAdapter extends ArrayAdapter<String>
    {
		public HighlightAdapter(Context context, int textViewResourceId, String[] objects)
        {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
        {
			View result = super.getView(position, convertView, parent);
			
			if (subjectList.isItemChecked(position)) {
				result.setBackgroundColor(cs_selected);
            } else {
				result.setBackgroundColor(cs_unselected);
            }
			
			return result;
		}
	}
	
	private class TermChangeListener implements OnItemSelectedListener
    {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
			clearSelections();
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
        {
			clearSelections();
		}

		private void clearSelections()
        {
			synchronized (ClassSearchActivity.this)
            {
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
	
	private class SubjectClickListener implements OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
			String subjId = getResources().getStringArray(R.array.ds_subject_codes)[position];
			String term = terms.get(termInput.getSelectedItem());
			final boolean selected = subjectList.getCheckedItemPositions().get(position);
			
			if (selected) {
				view.setBackgroundColor(cs_selected);
            } else {
				view.setBackgroundColor(cs_unselected);
            }
			
			AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>()
            {
				@Override
				protected Boolean doInBackground(String... args) {
					String subjId = args[0];
					String term = args[1];
					
					synchronized (asyncCounter){
						asyncCounter++;
					}
					
					try{
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
							SparseBooleanArray selectedItems = subjectList.getCheckedItemPositions();
							
							synchronized (ClassSearchActivity.this){
								courseNumbers.clear();
								courseNames.clear();
								instructorNames.clear();
								
								for (int i = 0; i < selectedItems.size(); i++){
									int index = selectedItems.keyAt(i);
									if (selectedItems.get(index)){
										String subj = getResources().getStringArray(R.array.ds_subject_codes)[index];
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
					} finally{
						synchronized (asyncCounter){
							asyncCounter--;
						}
					}
						
					return subjectList.getCheckedItemCount() > 0;
				}
				
				@Override
				protected void onPostExecute(Boolean hasSelections)
                {
					synchronized (ClassSearchActivity.this){
						if (hasSelections){
							courseAdapter.clear();
							courseAdapter.addAll(courseNumbers);
							courseAdapter.notifyDataSetChanged();
							instructorAdapter.clear();
							instructorAdapter.addAll(instructorNames);
							instructorAdapter.notifyDataSetChanged();
							
							showOptions();
						}
					}
				}
			};
			
			task.execute(subjId, term);
		}
	}
	
	private class SubmitButtonListener implements OnClickListener
    {
		@Override
		public void onClick(View view)
        {
			if (subjectList.getCheckedItemCount() == 0){
				Toast needSubject = Toast.makeText(ClassSearchActivity.this, "Select at least one subject", Toast.LENGTH_SHORT);
				needSubject.show();
				return;
			}
			
			synchronized (asyncCounter){
				if (asyncCounter > 0){
					Toast stillLoading = Toast.makeText(ClassSearchActivity.this,
							"Still loading class information; please try again in a moment", Toast.LENGTH_LONG);
					stillLoading.show();
					return;
				}
			}

			CourseSearchBuilder searchBuilder = new CourseSearchBuilder();
			searchBuilder.setTerm(terms.get(termInput.getSelectedItem()));
			
			SparseBooleanArray selectedItems = subjectList.getCheckedItemPositions();
			for (int i = 0; i < selectedItems.size(); i++){
				int index = selectedItems.keyAt(i);
				if (selectedItems.get(index)) {
					searchBuilder.addSubject(getResources().getStringArray(R.array.ds_subject_codes)[index]);
                }
			}
			
			String courseNumber = courseNumberInput.getText().toString().trim();
			if (!courseNumber.equals("")) {
				searchBuilder.setCourseNumber(courseNumber);
            }
			
			String instructor = instructorInput.getText().toString().trim();
			if (!instructor.equals("")) {
				searchBuilder.setInstructor(instructor);
            }
			
			String credits = creditsInput.getText().toString().trim();
			if (!credits.equals("")){
				try{
					searchBuilder.setMinCredits(Double.parseDouble(credits));
				} catch (NumberFormatException npfe){
					Log.d(TAG, "User entered invalid number " + credits);
					Toast numError = Toast.makeText(ClassSearchActivity.this, credits + " is not a valid number", Toast.LENGTH_SHORT);
					numError.show();
					return;
				}
			}
			
			String startTime = startTimeInput.getText().toString().trim();
			if (!startTime.equals("")){
				try{
					searchBuilder.setBeginTime(ANDROID_TIME.parse(startTime));
				} catch (ParseException pe){
					Log.d(TAG, "User entered invalid time " + startTime);
					Toast timeError = Toast.makeText(ClassSearchActivity.this, startTime + " is not a valid time", Toast.LENGTH_SHORT);
					timeError.show();
					return;
				}
			}
			
			String endTime = endTimeInput.getText().toString().trim();
			if (!startTime.equals("")){
				try{
					searchBuilder.setBeginTime(ANDROID_TIME.parse(endTime));
				} catch (ParseException pe){
					Log.d(TAG, "User entered invalid time " + endTime);
					Toast timeError = Toast.makeText(ClassSearchActivity.this, endTime + " is not a valid time", Toast.LENGTH_SHORT);
					timeError.show();
					return;
				}
			}

			if (mondayInput.isChecked()) {
				searchBuilder.addDay(mondayInput.getText().toString());
            }
			if (tuesdayInput.isChecked()) {
				searchBuilder.addDay(tuesdayInput.getText().toString());
            }
			if (wednesdayInput.isChecked()) {
				searchBuilder.addDay(wednesdayInput.getText().toString());
            }
			if (thursdayInput.isChecked()) {
				searchBuilder.addDay(thursdayInput.getText().toString());
            }
			if (fridayInput.isChecked()) {
				searchBuilder.addDay(fridayInput.getText().toString());
            }
			if (saturdayInput.isChecked()) {
				searchBuilder.addDay(saturdayInput.getText().toString());
            }
			
			Intent searchIntent = new Intent(ClassSearchActivity.this, SearchResultsActivity.class);
			searchIntent.putExtra(CourseSearchBuilder.KEY, searchBuilder);
			startActivity(searchIntent);
		}
		
	}
	
	private class DayClickListener implements OnClickListener
    {
		@Override
		public void onClick(View view) {
			CheckedTextView ctv = (CheckedTextView)view;
			
			if (!ctv.isChecked()){
				ctv.setBackgroundColor(cs_selected);
				ctv.setChecked(true);
			} else{
				ctv.setBackgroundColor(cs_unselected);
				ctv.setChecked(false);
			}
		}
	}
}
