package edu.ggc.it;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/*
 * ggc-connect is a Pedagogical app designed to teach Android programming to students
 *     at Georgia Gwinnett College.
 * 
 */
public class Main extends Activity  {

	private Button timeButton;
	private TextView myText;
	private Context myContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		
		
		
		setContentView(R.layout.main);

		myContext = this;
		
		timeButton = (Button) findViewById(R.id.button1);
		timeButton.setOnClickListener(new MyListener());
		
		
		
		
		
		//myText = (TextView) findViewById(R.id.myTextView);
		
		//Date d = new Date();
		
		//myText.setText("Today's date is " + d.toString() + "   ") ;

		Toast.makeText(this, "Hee hee", 3);
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
			
			Intent myIntent = new Intent(myContext,News.class);
		    startActivity(myIntent);

		}
	}

}