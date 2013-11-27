package edu.ggc.it.gpacalc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import edu.ggc.it.R;

/**
 * @author Kyle Dornblaser
 * @author Jordan Smith
 * @author Timothy McCravy
 * 
 * This activity gets the user's current course credit hours and the estimated grade for the
 * current semester and displays the GPA in a dialog.
 */

public class GPACalcSemester extends Activity
{
    //Holds the creditHours spinners
    private ArrayList<Spinner> creditHoursList = new ArrayList<Spinner>();
    //Holds the grades spinners
    private ArrayList<Spinner> gradesList = new ArrayList<Spinner>();
    //Holds the dynamically generated LinearLayouts
    private ArrayList<LinearLayout> childLayoutList = new ArrayList<LinearLayout>();

    private Button submitButton;
    private Button addCourseButton;
    private Button removeCourseButton;

    //The hours and GPA received from the previous activity: GPACalcActivity
    //They represent the previous hours and GPA of the user before this semester
    private int previousHours;
    private float previousGPA;

    //Layout that can be modified
    private LinearLayout semester_layout;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Receive data from previous activity GPACalcActivity
        processIntent();

        //Create the screen layout
        setContentView(R.layout.activity_gpacalc_semester);
        semester_layout = (LinearLayout) findViewById(R.id.semester_layout);

        //Initial Class
        addCourse();

        //Clicking the button adds a course
        addCourseButton = (Button) findViewById(R.id.button_add_course);
        addCourseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addCourse();
            }
        });

        //Removes the most recent course added when clicked
        removeCourseButton = (Button) findViewById(R.id.button_remove_course);
        removeCourseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Cannot remove the last course
                if (childLayoutList.size() > 1)
                {
                    removeCourse();
                }
            }
        });

        //calculates the GPA and shows it when clicked
        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayGPA();
            }
        });
    }

    /**
     * Adds a course to the screen
     */
    private void addCourse()
    {
        //Adds a new LinearLayout to the ArrayList to be used by each course. This puts each course on its own line.
        childLayoutList.add(new LinearLayout(this));
        childLayoutList.get(childLayoutList.size()-1).setOrientation(LinearLayout.HORIZONTAL);

        //Add the spinners to the related ArrayLists
        creditHoursList.add(createCreditHoursSpinner());
        gradesList.add(createGradeSpinner());

        //Add TextViews and Spinners to the LinearLayout ArrayList
        childLayoutList.get(childLayoutList.size()-1).addView(createTextView("Credit Hours"));
        childLayoutList.get(childLayoutList.size()-1).addView(creditHoursList.get(creditHoursList.size()-1));
        childLayoutList.get(childLayoutList.size()-1).addView(createTextView("Expected Grade"));
        childLayoutList.get(childLayoutList.size()-1).addView(gradesList.get(gradesList.size()-1));

        //Add the LinearLayout to the screen
        semester_layout.addView(childLayoutList.get(childLayoutList.size()-1));
    }

    /**
     * Removes the most recent course added to the screen
     */
    private void removeCourse()
    {
        //Remove the spinners from the related ArrayLists
        creditHoursList.remove(creditHoursList.size()-1);
        gradesList.remove(gradesList.size()-1);

        //Removes the course from the screen
        semester_layout.removeView(childLayoutList.get(childLayoutList.size()-1));
        childLayoutList.remove(childLayoutList.size()-1);
    }

    /**
     * Returns a TextView that contains the passed through string.
     * 
     * @param  text  will display in the TextView
     * @return 		 a TextView with the passed through string
     */
    private TextView createTextView(String text)
    {
        TextView creditHoursTextView = new TextView(this);
        creditHoursTextView.setText(text + ": ");
        
        return creditHoursTextView;
    }

    /**
     * Returns a Spinner that contains the choices A, B, C, D, and F
     * 
     * @return  the created grade spinner
     */
    private Spinner createGradeSpinner()
    {
        //Create the grade spinner
        Spinner gradeSpinner = new Spinner(this);

        //Create all the options for the grade spinner
        List<String> gradesList = new ArrayList<String>();
        gradesList.add("A");
        gradesList.add("B");
        gradesList.add("C");
        gradesList.add("D");
        gradesList.add("F");

        //Attach the grade list to the spinner with an adapter
        ArrayAdapter<String> gradesSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, gradesList);
        gradesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gradeSpinner.setAdapter(gradesSpinnerAdapter);

        //Return the created spinner
        return gradeSpinner;
    }

    /**
     * Returns a Spinner that contains the choices 1, 2, 3, 4
     * 
     * @return  the created credit hours spinner
     */
    private Spinner createCreditHoursSpinner()
    {
        //Create the credit hours spinner
        Spinner creditHoursSpinner = new Spinner(this);

        //Create all the options for the credit hours spinner
        List<String> creditHoursList = new ArrayList<String>();
        creditHoursList.add("1");
        creditHoursList.add("2");
        creditHoursList.add("3");
        creditHoursList.add("4");

        //Attach the credit hours list to the spinner with an adapter
        ArrayAdapter<String> creditHoursSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, creditHoursList);
        creditHoursSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        creditHoursSpinner.setAdapter(creditHoursSpinnerAdapter);

        //Return the created credit hours spinner
        return creditHoursSpinner;
    }

    /**
     * Retrieve the intent from the previous activity
     */
    private void processIntent()
    {
        Intent receivedIntent = getIntent();
        previousGPA = receivedIntent.getFloatExtra("gpa", 0);
        previousHours = receivedIntent.getIntExtra("hours", 0);
    }

    /**
     * Converts the passed through String letter grade into a float number on the 4.0 GPA scale 
     * 
     * @param  letter  the grade letter which is being converted
     * @return         the grade as a float on 4.0 GPA scale
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
        else
            return 0.0f;
    }

    /**
     * Build an AlertDialog box and display the calculated GPA in it
     */
    private void displayGPA()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Calculated GPA");
        DecimalFormat df = new DecimalFormat("#.##");
        alert.setMessage(df.format(calculateGPA()));

        alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    /**
     * Takes all the inputed values from the spinners and
     * the previous GPA and hours and uses them to calculate the new GPA. Note:
     * this is basically the end of the program. Called when the submitButton is
     * pressed.
     * 
     * @return the new GPA
     */
    private float calculateGPA()
    {
        //Initialize the totals and input the previous GPA and hours to them
        float totalPoints = previousGPA * previousHours;
        float totalHours = previousHours;

        //Go through the classInfo list and add each course's values to the totals
        for (int i = 0; i < creditHoursList.size() && i < gradesList.size(); i++)
        {
            //Get the course hours and convert it to float
            float hours = Float.parseFloat(creditHoursList.get(i).getSelectedItem().toString());

            //Get the course grade
            float grade = convertGradeLetter(gradesList.get(i).getSelectedItem().toString());

            //Get the course points using the hours and grade
            float points = hours * grade;

            //Add the hours and points to the totals
            totalHours += hours;
            totalPoints += points;
        }

        //Return the GPA using totalPoints and totalHours
        return totalPoints / totalHours;
    }
}