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


public class GymScheduleActivity extends Activity
{
    /**
     * This method creates all of the Text Views in the activity
     */
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_schedule);

        TextView Phone = (TextView) findViewById(R.id.gym_phone);
		Phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent ii = new Intent(Intent.ACTION_DIAL);
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
				intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "wellnessrec@ggc.edu" });
				intent.putExtra(Intent.EXTRA_TEXT, "\n\n\nSent from GGC Connect");
				startActivity(intent);
			}
		});
		
		TextView buildingF = (TextView) findViewById(R.id.gymfbuilding);
		buildingF.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent buildingFIntent = new Intent(GymScheduleActivity.this, ImageTouchFBuildingActivity.class);
				startActivity(buildingFIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.activity_gym_schedule, menu);
		return true;
	}
}
