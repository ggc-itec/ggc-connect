package edu.ggc.it.gpacalc;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import edu.ggc.it.R;

public class GPACalcSemester extends Activity
{
	private Spinner creditHoursOneSpinner;
	private Spinner creditHoursTwoSpinner;
	private Spinner creditHoursThreeSpinner;
	private Spinner creditHoursFourSpinner;
	private Spinner gradeOneSpinner;
	private Spinner gradeTwoSpinner;
	private Spinner gradeThreeSpinner;
	private Spinner gradeFourSpinner;
	
	private EditText classOneGradeEditText;
	
	private EditText classTwoGradeEditText;

	private EditText classThreeGradeEditText;

	private EditText classFourGradeEditText;
	private TextView newGPATextView;
	private Button submitButton;

	private int hours;
	private float gpa;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gpacalc_semester);
		processIntent();
		
		createCreditHoursSpinners();
        createGradesSpinners();
        
        
		
		newGPATextView = (TextView) findViewById(R.id.text_new_gpa);
		submitButton = (Button) findViewById(R.id.button_submit);
		submitButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				newGPATextView.setText(Float.toString(calculateGPA()));
			}
		});
	}

	private void createGradesSpinners()
	{
		gradeOneSpinner = (Spinner) findViewById(R.id.input_class_one_grade);
		gradeTwoSpinner = (Spinner) findViewById(R.id.input_class_two_grade);
		gradeThreeSpinner = (Spinner) findViewById(R.id.input_class_three_grade);
		gradeFourSpinner = (Spinner) findViewById(R.id.input_class_four_grade);
		

        List<String> gradesList = new ArrayList<String>();
        gradesList.add("A");
        gradesList.add("B");
        gradesList.add("C");
        gradesList.add("D");
        gradesList.add("F");
        
        
        ArrayAdapter<String> gradesSpinnerAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, gradesList);
        gradesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gradeOneSpinner.setAdapter(gradesSpinnerAdapter);
        gradeTwoSpinner.setAdapter(gradesSpinnerAdapter);
        gradeThreeSpinner.setAdapter(gradesSpinnerAdapter);
        gradeFourSpinner.setAdapter(gradesSpinnerAdapter);
		
	}

	private void createCreditHoursSpinners()
	{
		creditHoursOneSpinner = (Spinner) findViewById(R.id.input_class_one_hours);
		creditHoursTwoSpinner = (Spinner) findViewById(R.id.input_class_two_hours);
		creditHoursThreeSpinner = (Spinner) findViewById(R.id.input_class_three_hours);
		creditHoursFourSpinner = (Spinner) findViewById(R.id.input_class_four_hours);
		

        List<String> creditHoursList = new ArrayList<String>();
        for(int i = 1; i < 5; i++)
        {
        	creditHoursList.add(i+"");
        }
        
        ArrayAdapter<String> creditHoursSpinnerAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, creditHoursList);
        creditHoursSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditHoursOneSpinner.setAdapter(creditHoursSpinnerAdapter);
        creditHoursTwoSpinner.setAdapter(creditHoursSpinnerAdapter);
        creditHoursThreeSpinner.setAdapter(creditHoursSpinnerAdapter);
        creditHoursFourSpinner.setAdapter(creditHoursSpinnerAdapter);
        
		
	}

	private void processIntent() {
		Intent receivedIntent = getIntent();
		gpa = receivedIntent.getFloatExtra("gpa", 0);
		hours = receivedIntent.getIntExtra("hours", 0);
	}
	
	private float convertGradeLetter(String letter)
	{
		if (letter.equals("A"))
			return 4.0f;
		else if (letter.equals("B"))
			return 3.0f;
		else if (letter.equals("C"))
			return 2.0f;
		else if (letter.equals("D"))
			return 1.0f;
		else if (letter.equals("F"))
			return 0.0f;
		else return -1;
	}

	private float calculateGPA()
	{
		float hours1 = Float.parseFloat(creditHoursOneSpinner.getSelectedItem().toString());
		float hours2 = Float.parseFloat(creditHoursTwoSpinner.getSelectedItem().toString());
		float hours3 = Float.parseFloat(creditHoursThreeSpinner.getSelectedItem().toString());
		float hours4 = Float.parseFloat(creditHoursFourSpinner.getSelectedItem().toString());

		float oldPoints = gpa * hours;
		float class1 = hours1 * convertGradeLetter(gradeOneSpinner.getSelectedItem().toString());
		float class2 = hours2 * convertGradeLetter(gradeTwoSpinner.getSelectedItem().toString());
		float class3 = hours3 * convertGradeLetter(gradeThreeSpinner.getSelectedItem().toString());
		float class4 = hours4 * convertGradeLetter(gradeFourSpinner.getSelectedItem().toString());

		float totalPoints = oldPoints + class1 + class2 + class3 + class4;
		float totalHours = hours + hours1 + hours2 + hours3 + hours4;

		return totalPoints / totalHours;

	}
}