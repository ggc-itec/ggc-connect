package edu.ggc.it.love;

import java.util.List;

import edu.ggc.it.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableRow;

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
	private String[] subjectIds;
	private Banner banner;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.love_setup);
		
		banner = new Banner(this);
		
		// find views
		subjectRow = (TableRow)findViewById(R.id.ds_subject_row);
		subjectInput = (Spinner)findViewById(R.id.ds_subject_input);
		courseRow = (TableRow)findViewById(R.id.ds_course_row);
		courseInput = (Spinner)findViewById(R.id.ds_course_input);
		subjectIds = getResources().getStringArray(R.array.ds_subject_codes);
		
		// listen for buttons to be clicked
		SetupListener listener = new SetupListener();
		
		classButton = (Button)findViewById(R.id.ds_class_button);
		classButton.setOnClickListener(listener);
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
					case SUBJECT:
						showCourses();
						step = AddStep.COURSE;
					case COURSE:
						showSections();
						step = AddStep.SECTION;
					case SECTION:
						addCourse();
						step = AddStep.ADD;
				}
			}
		}
		
		private void showSubjects(){
			subjectRow.setVisibility(View.VISIBLE);
			classButton.setText(R.string.ds_next_label);
		}
		
		private void showCourses(){
			int selected = subjectInput.getSelectedItemPosition();
			String code = subjectIds[selected];
			List<String> courses = banner.getCourseNumbers(code);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetupActivity.this,
					android.R.layout.simple_spinner_item, courses);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			courseInput.setAdapter(adapter);
			courseRow.setVisibility(View.VISIBLE);
		}
		
		private void showSections(){
			
		}
		
		private void addCourse(){
			
		}
		
	}
}
