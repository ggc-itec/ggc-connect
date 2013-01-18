package edu.ggc.it;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Credits extends Activity {

	private TextView myText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// will throw exception if you do not call this
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credits);

		myText = (TextView) findViewById(R.id.creditsTextView);
	}

}
