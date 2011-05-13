package edu.ggc.it;


import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity  {

	private Button timeButton;
	private TextView myText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		timeButton = (Button) findViewById(R.id.button1);
		timeButton.setOnClickListener(new MyListener());

		myText = (TextView) findViewById(R.id.myTextView);
		
		Date d = new Date();
		myText.setText(d.toString());

	}


	public class MyListener implements OnClickListener {
		public void onClick(View view) {
			timeButton.setText("Hello Android");

		}
	}

}