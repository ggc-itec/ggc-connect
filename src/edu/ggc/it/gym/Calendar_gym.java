package edu.ggc.it.gym;

import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import edu.ggc.it.R.menu;
import edu.ggc.it.gym.GroupsActivity.ButtonListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Calendar_gym extends Activity
{
	private Context context;

    /*
     Creates all of the buttons
    */
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groups);
	    context = this;
		Button muscle1 = getListenedButton(R.id.Muscle1);
		Button bootCamp1 = getListenedButton(R.id.camp1);
		Button strength = getListenedButton(R.id.strength);
		Button yogaletes = getListenedButton(R.id.letes);
		Button interval1 = getListenedButton(R.id.interval1);
		Button interval2 = getListenedButton(R.id.interval2);
		Button interval3 = getListenedButton(R.id.interval3);
		Button muscle2 = getListenedButton(R.id.Muscle2);
		Button bootCamp2 = getListenedButton(R.id.camp2);
		Button kickbox = getListenedButton(R.id.kickboxing);
		Button yoga = getListenedButton(R.id.yoga);
	}

    private Button getListenedButton(final int id)
    {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(new ButtonListener());
        return button;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.activity_groups, menu);
		return true;
	}
	/*
	 Button listener shows the descriptions of each of the buttons when a button is clicked
	 */
	public class ButtonListener implements OnClickListener
    {
		public void onClick(View view) {

			if (view.getId() == R.id.Muscle1 || view.getId() == R.id.Muscle2){
				showClassDescription("Tone and build muscle mass utilizing " +
                        "different resistance tools.");
			}
			else if (view.getId() == R.id.camp1 || view.getId() == R.id.camp2){
				showClassDescription("Boot camp is a group exercise class that mixes traditional " +
                        "calisthenics and body weight exercises with interval training and " +
                        "strength training.");
			}
			else if (view.getId() == R.id.letes){
				showClassDescription("Combines Yoga and Pilates");
			}
			else if (view.getId() == R.id.yoga){
				showClassDescription("Appropriate for the beginner who wants to build a strong " +
                        "foundation of basic Yoga postures and breathing techniques, as well " +
                        "as for the practitioner who wants to refine and master the fundamentals. " +
                        "It is an invitation to stretch, relax, unwind and de-stress.");
			}
			else if (view.getId() == R.id.interval1 || view.getId() == R.id.interval2 || view.getId() == R.id.interval3){
				showClassDescription("A great cardio & strength training workout.");
			}
			else if (view.getId() == R.id.kickboxing) {
				showClassDescription("Martial arts-inspired interval workout that combines " +
                        "kickboxing with athletic drills.");
			}
			else if (view.getId() == R.id.strength) {
				showClassDescription("This interval training routine blends strength and cardio " +
                        "alternating circuits using body weight, a variety of equipment and " +
                        "partner work. The circuit training workout is loaded with functional " +
                        "exercises designed to give you the ultimate challenge.");
			}
		}

        /*
         Shows the description of the class
        */
        public void showClassDescription(final String description)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(description);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
	}
}
