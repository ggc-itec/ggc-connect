package edu.ggc.it.gym;

import edu.ggc.it.Main;
import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import edu.ggc.it.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);

		myContext = this;


		muscle1 = (Button) findViewById(R.id.muscle1);
		muscle1.setOnClickListener(new ButtonListener());

		bootCamp = (Button) findViewById(R.id.boot1);
		bootCamp.setOnClickListener(new ButtonListener());

		Strength = (Button) findViewById(R.id.strength1);
		Strength.setOnClickListener(new ButtonListener());

		yogaletes = (Button) findViewById(R.id.yogaletes);
		yogaletes.setOnClickListener(new ButtonListener());

		interval1 = (Button) findViewById(R.id.interval1);
		interval1.setOnClickListener(new ButtonListener());

		interval2 = (Button) findViewById(R.id.interval2);
		interval2.setOnClickListener(new ButtonListener());

		interval3 = (Button) findViewById(R.id.interval3);
		interval3.setOnClickListener(new ButtonListener());

		muscle2 = (Button) findViewById(R.id.muscle2);
		muscle2.setOnClickListener(new ButtonListener());

		bootcamp2 = (Button) findViewById(R.id.boot2);
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
	public class ButtonListener implements OnClickListener {
		public void onClick(View view) {

			if (view.getId() == R.id.muscle1 || view.getId() == R.id.muscle2){
				//Toast.makeText(myContext, "Tone and build muscle mass utilizing different resistance tools.", Toast.LENGTH_LONG)
				//.show();
				muscleDesc();				
			}
			else if (view.getId() == R.id.boot1 || view.getId() == R.id.boot2){
				bootDesc();
				//Toast.makeText(myContext, "Boot camp is a group exercise class that mixes traditional calisthenics and body weight exercises with interval training and strength training.", Toast.LENGTH_LONG).show();
			}
			else if (view.getId() == R.id.yogaletes){
				//Toast.makeText(myContext, "Combines Yoga and Pilates.", Toast.LENGTH_LONG).show();
				yogaletesDesc();
			}
			else if (view.getId() == R.id.yoga){
				yogaDesc();
				//Toast.makeText(myContext, "Appropriate for the beginner who wants to build a strong foundation of basic Yoga postures and breathing techniques, as well as for the practitioner who wants to refine and master the fundamentals. It is an invitation to stretch, relax, unwind and de-stress.", Toast.LENGTH_LONG).show();
			}
			else if (view.getId() == R.id.interval1 || view.getId() == R.id.interval2 || view.getId() == R.id.interval3){
				intervalDesc();
				//Toast.makeText(myContext, "A great cardio & strength training workout.", Toast.LENGTH_LONG).show();
			}
			else if (view.getId() == R.id.kickboxing) {
				kickDesc();
				//Toast.makeText(myContext, "Martial arts-inspired interval workout that combines kickboxing with athletic drills.", Toast.LENGTH_LONG).show();
			}
			else if (view.getId() == R.id.strength1) {
				strengthDesc();
				//Toast.makeText(myContext, "This interval training routine blends strength and cardio alternating circuits using body weight, a variety of equipment and partner work. The circuit training workout is loaded with functional exercises designed to give you the ultimate challenge.", Toast.LENGTH_LONG).show();
			}

		}

	}

	private void showAddScheduleItemDialog() {
		new AlertDialog.Builder(this)
		// .setTitle("No Classes Found")
		.setMessage(R.string.schedule_no_classes_popup)



		.show();
	}
	public void muscleDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		//alertDialogBuilder.setTitle("Your Title");

		// set dialog message;
		alertDialogBuilder
		.setMessage("Tone and build muscle mass utilizing different resistance tools.");
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	public void bootDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("Boot camp is a group exercise class that mixes traditional calisthenics and body weight exercises with interval training and strength training.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	public void yogaletesDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("Combines Yoga and Pilates");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	public void yogaDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("Appropriate for the beginner who wants to build a strong foundation of basic Yoga postures and breathing techniques, as well as for the practitioner who wants to refine and master the fundamentals. It is an invitation to stretch, relax, unwind and de-stress.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
	public void intervalDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("A great cardio & strength training workout.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	public void kickDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("Martial arts-inspired interval workout that combines kickboxing with athletic drills.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	public void strengthDesc() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set dialog message;
		alertDialogBuilder
		.setMessage("This interval training routine blends strength and cardio alternating circuits using body weight, a variety of equipment and partner work. The circuit training workout is loaded with functional exercises designed to give you the ultimate challenge.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
}
