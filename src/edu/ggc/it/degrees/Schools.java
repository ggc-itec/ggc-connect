//*******************************************************************
// Class: Schools
//
// This class displays all the schools at Georgia Gwinnett College.
// The users can navigate through the schools to find their degree 
// program plan.
//
// Created By: Danny Mansour, Marcus Rogers, and Christina Davis
//*******************************************************************

package edu.ggc.it.degrees;

import edu.ggc.it.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Schools extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schools);
		
		// Button that will take the user to Business degree page
		
		Button businessButton = (Button) findViewById(R.id.school_business);
		businessButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, Business.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading School of Business Majors...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user to Education degree page
		
		Button educationButton = (Button) findViewById(R.id.school_education);
		educationButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, Education.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading School of Education Majors...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user to Health Science degree page
		
		Button healthScienceButton = (Button) findViewById(R.id.school_health);
		healthScienceButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, HealthScience.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading School of Health Sciences Majors...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user to Liberal Arts degree page
		
		Button liberalArtsButton = (Button) findViewById(R.id.school_liberalarts);
		liberalArtsButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, LiberalArts.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading School of Liberal Arts Majors...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user to Science and Technology degree page
		
		Button scienceTechnologyButton = (Button) findViewById(R.id.school_sciencetech);
		scienceTechnologyButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, ScienceTechnology.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Loading School of Science and Technology Majors...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
		
		// Button that will take the user back one page
		
		Button goBack = (Button) findViewById(R.id.schools_back);
		goBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Schools.this, DegreesMainActivity.class);
				Schools.this.startActivity(intent);
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Taking you back...", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL, 10, 20);
				toast.show();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schools, menu);
		return true;
	}

}
