package edu.ggc.it.gym;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.ggc.it.R;

public class GroupsActivity extends Activity {
	private Button muscle1;
	private Button bootCamp;
	private Button Strength;
	private Button yogaletes;
	private Button interval1;
	private Button interval2;
	private Button muscle2;
	private Button bootcamp2;
	private Button kickboxing;
	private Button yoga;
	private Button interval3;
	private Context myContext;

	@Override
	/**
	 * Create and initialize the Group activity and attach the
	 * listeners for each button.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		myContext = this;

		muscle1 = (Button) findViewById(R.id.Muscle1);
		muscle1.setOnClickListener(new ButtonListener());

		bootCamp = (Button) findViewById(R.id.camp1);
		bootCamp.setOnClickListener(new ButtonListener());

		Strength = (Button) findViewById(R.id.strength);
		Strength.setOnClickListener(new ButtonListener());

		yogaletes = (Button) findViewById(R.id.letes);

		yogaletes.setOnClickListener(new ButtonListener());

		interval1 = (Button) findViewById(R.id.interval1);
		interval1.setOnClickListener(new ButtonListener());

		interval2 = (Button) findViewById(R.id.interval2);
		interval2.setOnClickListener(new ButtonListener());

		interval3 = (Button) findViewById(R.id.interval3);
		interval3.setOnClickListener(new ButtonListener());

		muscle2 = (Button) findViewById(R.id.Muscle2);
		muscle2.setOnClickListener(new ButtonListener());

		bootcamp2 = (Button) findViewById(R.id.camp2);
		bootcamp2.setOnClickListener(new ButtonListener());

		kickboxing = (Button) findViewById(R.id.kickboxing);
		kickboxing.setOnClickListener(new ButtonListener());

		muscle2 = (Button) findViewById(R.id.Muscle2);
		muscle2.setOnClickListener(new ButtonListener());

		bootcamp2 = (Button) findViewById(R.id.camp2);
		bootcamp2.setOnClickListener(new ButtonListener());

		kickboxing = (Button) findViewById(R.id.kickboxing);
		kickboxing.setOnClickListener(new ButtonListener());

		yoga = (Button) findViewById(R.id.yoga);
		yoga.setOnClickListener(new ButtonListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_groups, menu);
		return true;
	}

	/**
	 * Associates each button in the activity with a specific view
	 * representing one of the group classes offered at the rec center.
	 */
	public class ButtonListener implements OnClickListener {
		public void onClick(View view) {

			if (view.getId() == R.id.Muscle1 || view.getId() == R.id.Muscle2) {
				showGymClassDescription("Tone and build muscle mass utilizing different " +
						"resistance tools.");
			} 
			else if (view.getId() == R.id.camp1 || view.getId() == R.id.camp2) {
				showGymClassDescription("Boot camp is a group exercise class that mixes traditional " +
						"calisthenics and body weight exercises with interval " +
						"training and strength training.");
			} 
			else if (view.getId() == R.id.Muscle1
					|| view.getId() == R.id.Muscle2) {
				showGymClassDescription("Tone and build muscle mass utilizing different " +
						"resistance tools.");
			} 
			else if (view.getId() == R.id.camp1
					|| view.getId() == R.id.camp2) {
				showGymClassDescription("Boot camp is a group exercise class that mixes " +
						"traditional calisthenics and body weight exercises" +
						" with interval training and strength training.");
			} 
			else if (view.getId() == R.id.letes) {
				showGymClassDescription("Combines Yoga and Pilates");
			} 
			else if (view.getId() == R.id.yoga) {
				showGymClassDescription("Appropriate for the beginner who wants to build a strong " +
						"foundation of basic Yoga postures and breathing techniques," +
						"as well as for the practitioner who wants to refine and master " +
						"the fundamentals. It is an invitation to stretch, relax, unwind " +
						"and de-stress.");
			} 
			else if (view.getId() == R.id.interval1
					|| view.getId() == R.id.interval2
					|| view.getId() == R.id.interval3) {
				showGymClassDescription("A great cardio & strength training workout.");
			} 
			else if (view.getId() == R.id.kickboxing) {
				showGymClassDescription("Martial arts-inspired interval workout that combines kickboxing " +
						"with athletic drills.");
			}
			else if (view.getId() == R.id.strength) {
				showGymClassDescription("This interval training routine blends strength and " +
						"cardio alternating circuits using body weight, a variety " +
						"of equipment and partner work. The circuit training " +
						"workout is loaded with functional exercises designed " +
						"to give you the ultimate challenge.");
			}

		}

		
		/**
		 * Initializes and displays an alert dialog describing the class the user clicked on.
		 * @param descriptionText a String containing the content for the popup
		 */
		public void showGymClassDescription(String descriptionText){

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(myContext);
			alertDialogBuilder.setMessage(descriptionText);
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}

	}
}
