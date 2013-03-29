package edu.ggc.it.love;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ggc.it.R;
import edu.ggc.it.banner.Banner;
import edu.ggc.it.banner.Section;
import edu.ggc.it.banner.Schedule;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.ToggleButton;

/**
 * This activity prompts the user for their profile information
 * on their first run of the activity, or forward them to the
 * search page if the information has already been entered.
 * @author Jacob
 *
 */
public class SetupActivity extends Activity{
	private Button classButton;
	private Schedule schedule;
	private TableRow subjectRow;
	private Spinner subjectInput;
	private TableRow courseRow;
	private Spinner courseInput;
	private TableRow searchByRow;
	private ToggleButton searchByToggle;
	private TableRow sectionRow;
	private Spinner sectionInput;
	private TableRow listRow;
	private ListView classList;
	private String[] subjectIds;
	private Map<String, Map<String, String>> courseMap;
	private Map<String, Map<String, String>> sectionMap;
	private boolean showCourseNumber;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.love_setup);
		
		courseMap = new HashMap<String, Map<String, String>>();
		sectionMap = new HashMap<String, Map<String, String>>();
		schedule = new Schedule();
		showCourseNumber = false;
		
		// find views
		subjectRow = (TableRow)findViewById(R.id.ds_subject_row);
		subjectInput = (Spinner)findViewById(R.id.ds_subject_input);
		courseRow = (TableRow)findViewById(R.id.ds_course_row);
		courseInput = (Spinner)findViewById(R.id.ds_course_input);
		searchByRow = (TableRow)findViewById(R.id.ds_search_by_row);
		searchByToggle = (ToggleButton)findViewById(R.id.ds_search_by_toggle);
		sectionRow = (TableRow)findViewById(R.id.ds_section_row);
		sectionInput = (Spinner)findViewById(R.id.ds_section_input);
		listRow = (TableRow)findViewById(R.id.ds_class_list_row);
		classList = (ListView)findViewById(R.id.ds_class_list);
		subjectIds = getResources().getStringArray(R.array.ds_subject_codes);
		
		// listen for buttons to be clicked
		SetupListener listener = new SetupListener();
		
		classButton = (Button)findViewById(R.id.ds_class_button);
		classButton.setOnClickListener(listener);
		searchByToggle.setOnClickListener(listener);
	}
	
	private enum AddStep {
		ADD, SUBJECT, COURSE, SECTION
	}
	
	private class SetupListener implements OnClickListener{
		private AddStep step = AddStep.ADD;
		
		@Override
		public void onClick(View button) {
			if (button.getId() == R.id.ds_class_button){
				switch (step){
					case ADD:
						showSubjects();
						step = AddStep.SUBJECT;
						break;
					case SUBJECT:
						showCourses();
						step = AddStep.COURSE;
						break;
					case COURSE:
						showSections();
						step = AddStep.SECTION;
						break;
					case SECTION:
						addCourse();
						step = AddStep.ADD;
						break;
				}
			} else if (button.getId() == R.id.ds_search_by_toggle){
				showCourseNumber = !showCourseNumber;
				showCourses();
			}
		}
		
		private void showSubjects(){
			subjectRow.setVisibility(View.VISIBLE);
			classButton.setText(R.string.ds_next_label);
		}
		
		private void showCourses(){
			AsyncTask<String, Void, Map<String, String>> task =
					new AsyncTask<String, Void, Map<String, String>>(){
				protected Map<String, String> doInBackground(String... codes){
					String code = codes[0]; // only one
					Map<String, String> courses = null;
					if (courseMap.containsKey(code)){
						courses = courseMap.get(code);
					} else{
						courses = Banner.getCourseNumbers(code);
						courseMap.put(code, courses);
					}
					
					return courses;
				}
				
				protected void onPostExecute(Map<String, String> courses){
					List<String> displayValues = null;
					if (showCourseNumber)
						displayValues = new ArrayList<String>(courses.keySet());
					else
						displayValues = new ArrayList<String>(courses.values());
					
					Collections.sort(displayValues);
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetupActivity.this,
							android.R.layout.simple_spinner_item, displayValues);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					courseInput.setAdapter(adapter);
					courseRow.setVisibility(View.VISIBLE);
					searchByRow.setVisibility(View.VISIBLE);
				}
			};
			
			
			int selected = subjectInput.getSelectedItemPosition();
			String code = subjectIds[selected];
			task.execute(code);
		}
		
		private void showSections(){
			AsyncTask<String, Void, Map<String, String>> task =
					new AsyncTask<String, Void, Map<String, String>>(){
				protected Map<String, String> doInBackground(String... args){
					String subject = args[0];
					String course = args[1];
					Map<String, String> sections = null;
					String courseCode = subject + course;
					if (sectionMap.containsKey(courseCode)){
						sections = sectionMap.get(courseCode);
					} else{
						sections = Banner.getSections(subject, course);
						sectionMap.put(courseCode, sections);
					}
					
					return sections;
				}
				
				protected void onPostExecute(Map<String, String> sections){
					List<String> sectionNumbers = new ArrayList<String>(sections.keySet());
					Collections.sort(sectionNumbers);
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetupActivity.this,
							android.R.layout.simple_spinner_item, sectionNumbers);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sectionInput.setAdapter(adapter);
					sectionRow.setVisibility(View.VISIBLE);
					classButton.setText(R.string.ds_done_label);
				}
			};
			
			int selected = subjectInput.getSelectedItemPosition();
			String subject = subjectIds[selected];
			String course = (String)courseInput.getSelectedItem();
			if (!showCourseNumber){
				// we need the number, but this is the name
				// TODO: this does not handle the user changing the subject after the course
				// has been selected
				for (Map.Entry<String, String> entry: courseMap.get(subject).entrySet()){
					if (entry.getValue().equals(course)){
						course = entry.getKey();
						break;
					}
				}
			}
			
			task.execute(subject, course);
		}
		
		private void addCourse(){
			AsyncTask<String, Void, Section> task = new AsyncTask<String, Void, Section>(){
				protected Section doInBackground(String... args){
					String subject = args[0];
					String course = args[1];
					String crn = args[2];
					
					return Banner.getSection(subject, course, crn);
				}
				
				protected void onPostExecute(Section course){
					schedule.addCourse(course);
					
					List<Section> courses = schedule.getCourses();
					List<String> courseStrings = new ArrayList<String>(courses.size());
					
					for (Section c: courses)
						courseStrings.add(c.toString());
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetupActivity.this,
							android.R.layout.simple_list_item_1, courseStrings);
					classList.setAdapter(adapter);
					listRow.setVisibility(View.VISIBLE);
					
					subjectRow.setVisibility(View.GONE);
					courseRow.setVisibility(View.GONE);
					searchByRow.setVisibility(View.GONE);
					sectionRow.setVisibility(View.GONE);
					classButton.setText(R.string.ds_add_class_label);
				}
			};
			
			int selected = subjectInput.getSelectedItemPosition();
			String subject = subjectIds[selected];
			String course = (String)courseInput.getSelectedItem();
			if (!showCourseNumber){
				// we need the number, but this is the name
				// TODO: this does not handle the user changing the subject after the course
				// has been selected
				for (Map.Entry<String, String> entry: courseMap.get(subject).entrySet()){
					if (entry.getValue().equals(course)){
						course = entry.getKey();
						break;
					}
				}
			}
			Map<String, String> sections = sectionMap.get(subject + course);
			String crn = sections.get((String)sectionInput.getSelectedItem());
			
			task.execute(subject, course, crn);
		}
		
	}
}
