package edu.ggc.it.love;

import android.app.Activity;

/**
 * This activity prompts the user for their profile information
 * on their first run of the activity, or forward them to the
 * search page if the information has already been entered.
 * @author Jacob
 *
 */
public class SetupActivity extends Activity{
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.love_setup);
	}
}
