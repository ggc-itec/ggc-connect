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

public class GPACalcActivity extends Activity
{
	private EditText gpaEditText;
	private EditText creditHoursEditText;
	private Button nextButton;
	private Context context;
	private CharSequence text;
	private int duration = Toast.LENGTH_SHORT;
	private Toast toast;
	

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
				String stringGPA = gpaEditText.getText().toString();
				String stringHours = creditHoursEditText.getText().toString();
				float gpa = 0;
				int hours = 0;
				if(!stringGPA.isEmpty())
				{
					gpa = Float.parseFloat(stringGPA);
				}
				else
				{
					text = "Please enter a value for GPA!";
					context = getApplicationContext();
					toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				if (!stringHours.isEmpty())
				{
					hours = Integer.parseInt(stringHours);
				}
				else 
				{
					text = "Please enter a value for Credit Hours!";
					context = getApplicationContext();
					toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				if (!stringGPA.isEmpty() && !stringHours.isEmpty())
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
