package edu.ggc.it.gym;

import edu.ggc.it.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import edu.ggc.it.map.ImageTouchFBuildingActivity;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class GymScheduleActivity extends Activity {
	
	

	

	private TextView Phone;
	private TextView email;

	@Override
	/**
	 * This method creates all of the Text Views in the activity
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_schedule);


		Phone = (TextView) findViewById(R.id.gym_phone);
		email = (TextView) findViewById(R.id.gym_email);

		TextView Phone = (TextView) findViewById(R.id.gym_phone);
		Phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent ii = new Intent(Intent.ACTION_DIAL);
				//Sets the phone number that is going to appear in the dialog screen
				ii.setData(Uri.parse("tel:6784075970"));
				startActivity(ii);

			}
		});



		TextView email = (TextView) findViewById(R.id.gym_email);
		email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				//adds email address to intent
				intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "wellnessrec@ggc.edu" });
				//intent.setData(Uri.parse("mailto:default@recipient.com")); 
				//adds tag at end of email
				intent.putExtra(Intent.EXTRA_TEXT, "\n" + "\n" + "\n" + "Sent from GGC Connect");
				//lets user choose their email client
				//startActivity(Intent.createChooser(intent, "Send Email Via"));

				startActivity(intent);
			}
		});
		
		TextView buildingF = (TextView) findViewById(R.id.gymfbuilding);
		buildingF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent buildingFintent = new Intent(GymScheduleActivity.this, ImageTouchFBuildingActivity.class);
				startActivity(buildingFintent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gym_schedule, menu);
		return true;
	}

	

}
