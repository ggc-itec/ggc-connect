package edu.ggc.it.gym;

import edu.ggc.it.Main;
import edu.ggc.it.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GymMainActivity extends Activity {

	
		private Button contract;
		private Button schedule;
		private Button groups;
		private Button buddiesButton;
		private Button magazine;
		private Button HomeButton;
		private Context myContext;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_gym_main);
			
			myContext = this;
			contract = (Button) findViewById(R.id.firstTime);
			contract.setOnClickListener(new ButtonListener());
			
			schedule = (Button) findViewById(R.id.gymSchedule);
			schedule.setOnClickListener(new ButtonListener());
			
			groups = (Button) findViewById(R.id.Group);
			groups.setOnClickListener(new ButtonListener());
			
			buddiesButton = (Button) findViewById(R.id.buddies);
			buddiesButton.setOnClickListener(new ButtonListener());
			
			magazine = (Button) findViewById(R.id.healthMagazine);
			magazine.setOnClickListener(new ButtonListener());
			
			HomeButton = (Button) findViewById(R.id.homeButton);
			HomeButton.setOnClickListener(new ButtonListener());
			
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_gym_main, menu);
			return true;
		}
		
		public class ButtonListener implements OnClickListener {
			public void onClick(View view) {
				
				if (contract.isPressed()){
					
				}
				else if (view.getId() == R.id.gymSchedule){
					startActivity(new Intent (myContext, GymScheduleActivity.class));
					//Toast.makeText(myContext, "What a cute cat!", Toast.LENGTH_LONG)
					//.show();
				}
				else if (groups.isPressed()){
					startActivity( new Intent ( myContext, GroupsActivity.class));
				}
				else if (view.getId() == R.id.buddies){
					//startActivity(new Intent (myContext, WorkoutBuddies.class));
			Toast.makeText(myContext, "What a cute cat!", Toast.LENGTH_LONG)
					.show();
				}
				else if (magazine.isPressed()){
					
				}
				else if (view.getId() == R.id.homeButton) {
					startActivity(new Intent (myContext, Main.class));
				}
			}

		}

	}
