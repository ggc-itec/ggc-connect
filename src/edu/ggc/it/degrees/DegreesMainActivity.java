//*******************************************************************
// Class: DegreesMainActivity
//
// This class is the first screen that the user sees when they open
// our application. From this screen, the user can follow the
// instructions to find the program plan for their major.
//
// Created By: Danny Mansour, Marcus Rogers, and Christina Davis
//*******************************************************************

package edu.ggc.it.degrees;

import edu.ggc.it.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DegreesMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_degrees_main);
		
		// Button that starts the application and lets the user choose their school
		
		Button start = (Button) findViewById(R.id.start_button);
		start.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(DegreesMainActivity.this, Schools.class);
				DegreesMainActivity.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Choose your desired school...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
	}
}