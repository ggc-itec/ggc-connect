package edu.ggc.it.social;

import java.util.Calendar;

import android.app.Activity;

import com.parse.Parse;
import com.parse.ParseObject;

import edu.ggc.it.R;

public class SocialUpdateActivity extends Activity {

	
	
	private void uploadParse() {
		Parse.initialize(this, getString(R.string.parse_app_id),
				getString(R.string.parse_client_key));
		ParseObject testObject = new ParseObject("ggcconnect");
		testObject.put("name", "tom");
		String mydate = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		testObject.put("date", mydate);
		String subject = "Testing";
		testObject.put("subject", subject);
		String body = "Testing one two three testing one two three";
		testObject.put("body", body);
		testObject.saveInBackground();
	}

}
