package edu.ggc.it;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

	/** Called when user presses Menu key */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.app_menu, menu);
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.welcome:
	        //newGame();
	        return true;
	    case R.id.credits:
	        Intent myIntent = new Intent(Main.this,Credits.class);
	        Main.this.startActivity(myIntent);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public class MyListener implements OnClickListener {
		public void onClick(View view) {
			timeButton.setText("Hello Android");

		}
	}

}