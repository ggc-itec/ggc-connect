package edu.ggc.it.about;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Class: GGCFunFactsActivity
 * 
 * Provides some interesting information about GGC.
 *
 */
public class GGCFunFactsActivity extends Activity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// sets the content view of this activity to the fun facts layout
		setContentView(R.layout.activity_ggc_fun_facts);
		
		//adds a clickable link to the flickrLink TextView in the activity_ggc_fun_facts.xml layout
		TextView flickrLink = (TextView) findViewById(R.id.flickrLink);
		flickrLink.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "•	<a href='http://www.flickr.com/photos/georgiagwinnett/7290433094/'> GGC Flickr Photos </a>";
		flickrLink.setText(Html.fromHtml(text));
		flickrLink.setClickable(true);
	}
}
