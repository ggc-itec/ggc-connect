package edu.ggc.it.about;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Class: GGCGeographyActivity
 * 
 * Provides a link to GGC campus maps for the user.
 *
 */
public class GGCGeographyActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// sets the content view to the ggc geography layout
		setContentView(R.layout.activity_ggc_geography);
		
		//adds a link to the geographyLink TextView in the ggc geography layout
		TextView geogLink = (TextView) findViewById(R.id.geographyLink);
		geogLink.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "•	<a href='http://www.ggc.edu/admissions/visit-ggc/maps-and-directions/index.html'> Maps and Directions </a>";
		geogLink.setText(Html.fromHtml(text));
		geogLink.setClickable(true);
	}
	
}
