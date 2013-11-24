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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import edu.ggc.it.R;

public class GPACalcSemester extends Activity
{
	// List of all the info on as many classes as the user inputs
	// First is the creditHours info, then the Grade info: they are always
	// grouped together by the class they refer to
	private ArrayList<Spinner> classInfoList = new ArrayList<Spinner>();

	// How many classes are currently on-screen
	private int totalClasses = 0;

	// The text that will show the new GPA after it is calculated
	private TextView newGPATextView;

	// All the buttons
	private Button submitButton;
	private Button addAClassButton;
	private Button removeAClassButton;

	// The hours and GPA recieved from the previous activity: GPACalcActivity
	// They represent the previous hours and GPA of the user before this
	// semester
	private int previousHours;
	private float previousGPA;
	
	private LinearLayout ll;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Create the screen layout
		//setContentView(R.layout.activity_gpacalc_semester);

		// Receive data from previous activity GPACalcActivity
		processIntent();

		// Add two classes (class as in schooling, not as in programming) and
		// their spinners to the screen
		//addAClass();
		//addAClass();

		ScrollView sv = new ScrollView(this);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		addAClass();
		this.setContentView(sv);

		// Add a button that can be pressed to add another class (class as in
		// schooling, not as in programming)
		/*addAClassButton = (Button) findViewById(R.id.button_add_class);
		addAClassButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				addAClass();
			}
		});*/

		// Add a button that can be pressed to remove the most recent
		// (bottom-most when looking at the screen) class (class as in
		// schooling, not as in programming)
		// The button is invisible when there is only one class remaining
		/*removeAClassButton = (Button) findViewById(R.id.button_remove_class);
		removeAClassButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				removeAClass();
			}
		});*/

		// Add the button that when pressed calculates the GPA and shows it to
		// the user
		/*newGPATextView = (TextView) findViewById(R.id.text_new_gpa);
		submitButton = (Button) findViewById(R.id.button_submit);
		submitButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				newGPATextView.setText(Float.toString(calculateGPA()));
			}
		});*/
	}

	/**
	 * Method: addAClass * adds a new class - school class, not programming
	 * class - to the screen. The class has two spinners: credit hours and
	 * grade. The user is to choose from the drop-down menu the correct option.
	 */
	private void addAClass()
	{

		// Add the spinners to the class info list
		classInfoList.add(createCreditHoursSpinner());
		classInfoList.add(createGradeSpinner());

		// Increment total classes number by one
		totalClasses++;
	}

	/**
	 * Method: removeAClass * Removes the bottom-most - the most recently
	 * created - class'es spinners from both the screen and the class info list.
	 * Can only be called when there are at least two classes.
	 */
	private void removeAClass()
	{
		// Get the location of the most recently created class'es second spinner
		int spinnerLocation = classInfoList.size() - 1;

		// Remove the spinners from the class info list
		Spinner creditHourSpinnerToRemove = classInfoList
				.remove(spinnerLocation);
		Spinner gradeSpinnerToRemove = classInfoList
				.remove(spinnerLocation - 1); // The grade spinner is always
		// right before the creditHour
		// spinner

		// Decrement total classes number by one
		totalClasses--;
	}

	/**
	 * Method: createGradeSpinner * creates a grade spinner with the options: A,
	 * B, C, D, and F. The options are Strings, not Char(acter)s.
	 * 
	 * @return gradeSpinner the created grade spinner
	 */
	private Spinner createGradeSpinner()
	{
		// Create the grade spinner
		//Spinner gradeSpinner = (Spinner) findViewById(R.id.input_class_one_grade);

		Spinner gradeSpinner = new Spinner(this);
		ll.addView(gradeSpinner);

		// Create all the options for the grade spinner
		List<String> gradesList = new ArrayList<String>();
		gradesList.add("A");
		gradesList.add("B");
		gradesList.add("C");
		gradesList.add("D");
		gradesList.add("F");

		// Attach the grade list to the spinner with an adapter
		ArrayAdapter<String> gradesSpinnerAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, gradesList);

		gradesSpinnerAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		gradeSpinner.setAdapter(gradesSpinnerAdapter);

		// Return the created spinner
		return gradeSpinner;
	}

	/**
	 * Method: createCreditHoursSpinner * creates a credit hours spinner with
	 * the options: 1, 2, 3, and 4. The options are Strings, not int(eger)s.
	 * 
	 * @return creditHoursSpinner the created credit hours spinner
	 */
	private Spinner createCreditHoursSpinner()
	{
		// Create the credit hours spinner
		//Spinner creditHoursSpinner = (Spinner) findViewById(R.id.input_class_one_hours);
		Spinner creditHoursSpinner = new Spinner(this);
		ll.addView(creditHoursSpinner);

		// Create all the options for the credit hours spinner
		List<String> creditHoursList = new ArrayList<String>();
		creditHoursList.add("1");
		creditHoursList.add("2");
		creditHoursList.add("3");
		creditHoursList.add("4");

		// Attach the credit hours list to the spinner with an adapter
		ArrayAdapter<String> creditHoursSpinnerAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, creditHoursList);

		creditHoursSpinnerAdapter
		.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		creditHoursSpinner.setAdapter(creditHoursSpinnerAdapter);

		// Return the created credit hours spinner
		return creditHoursSpinner;
	}

	private void processIntent()
	{
		Intent receivedIntent = getIntent();
		previousGPA = receivedIntent.getFloatExtra("gpa", 0);
		previousHours = receivedIntent.getIntExtra("hours", 0);
	}

	/**
	 * Method: convertGradeLetter * converts a grade letter into its
	 * corresponding GPA number. A becomes 4.0, C becomes 2.0, F becomes 0.0,
	 * ect.
	 * 
	 * @param letter
	 *            the grade letter which is being converted
	 * @return the grade as a float number
	 */
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
		else
			return -1; // THIS IS AN ERROR. SOMBODY NEEDS TO MAKE THIS THROW AN
		// EXCEPTION/ASSERT.
	}

	/**
	 * Method: calculateGPA * takes all the inputed values from the spinners and
	 * the previous GPA and hours and uses them to calculate the new GPA. Note:
	 * this is basically the end of the program. Called when the submitButton is
	 * pressed.
	 * 
	 * @return the new GPA
	 */
	private float calculateGPA()
	{
		// Initialize the totals and input the previous GPA and hours to them
		float totalPoints = previousGPA * previousHours;
		float totalHours = previousHours;

		// Go through the classInfo list and add each class'es values to the
		// totals
		while (classInfoList.size() > 1)
		{
			// Get the class'es hours and convert it to float
			float hours = Float.parseFloat(classInfoList.remove(0)
					.getSelectedItem().toString());

			// Get the class'es grade
			float grade = convertGradeLetter(classInfoList.remove(1)
					.getSelectedItem().toString());

			// Get the class'es points using the hours and grade
			float points = hours * grade;

			// Add the hours and points to the totals
			totalHours += hours;
			totalPoints += points;
		}

		// Return the GPA using totalPoints and totalHours
		return totalPoints / totalHours;
	}
}