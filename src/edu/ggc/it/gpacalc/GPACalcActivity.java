package edu.ggc.it.gpacalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.ggc.it.R;

/**
 * @author Jordan Smith
 * @author Kyle Dornblaser
 * @author Timothy McCravy
 *
 * This activity gets the user's current GPA and total credit hours before the current semester.
 */
public class GPACalcActivity extends Activity
{
    private EditText gpaEditText;
    private EditText creditHoursEditText;
    private Button nextButton;
    private Context context;
    private CharSequence toastText;
    private int toastDuration = Toast.LENGTH_SHORT;
    private Toast toast;


    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpacalc);

        gpaEditText = (EditText) findViewById(R.id.input_gpa);
        creditHoursEditText = (EditText) findViewById(R.id.input_credit_hours);

        nextButton = (Button) findViewById(R.id.button_next);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Get GPA and Hours from EditText fields
                String stringGPA = gpaEditText.getText().toString();
                String stringHours = creditHoursEditText.getText().toString();
                float gpa = 0;
                int hours = 0;

                //Error checking for empty Hours, a single period, and high values in the GPA text field
                if(!stringGPA.isEmpty() && !stringGPA.equals(".") && Float.parseFloat(stringGPA) <= 4.0)
                {
                    gpa = Float.parseFloat(stringGPA);
                }
                else
                {
                    toastText = "Please enter a proper value for GPA!";
                    context = getApplicationContext();
                    toast = Toast.makeText(context, toastText, toastDuration);
                    toast.show();
                }

                //Error checking for empty Hours, a single period, and high values in the Credit Hours text field
                if (!stringHours.isEmpty() && !stringHours.equals(".") && Float.parseFloat(stringHours) <= 300)
                {
                    
                    hours = (int) Float.parseFloat(stringHours);
                }
                else 
                {
                    toastText = "Please enter a proper value for Credit Hours!";
                    context = getApplicationContext();
                    toast = Toast.makeText(context, toastText, toastDuration);
                    toast.show();
                }

                //If both fields are not empty, create a new intent and start next activity
                if (!stringGPA.isEmpty() && !stringHours.isEmpty() && Float.parseFloat(stringGPA) <= 4.0 && Float.parseFloat(stringHours) <= 300)
                {
                    Intent intent = new Intent(getBaseContext(),GPACalcSemester.class);
                    intent.putExtra("gpa", gpa);
                    intent.putExtra("hours",hours);
                    startActivity(intent);
                }
            }
        });
    }
}