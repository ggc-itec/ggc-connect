package edu.ggc.it.about;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Class: MainActivity
 * 
 * Main page for AboutGGC. Provides buttons for GGC history, geography, song, fun facts.
 *
 */
public class AboutMainActivity extends Activity
{
	private Button histButton;
	private Button geographyButton;
	private Button ggcSongButton;
	private Button funFactsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_main);
		
		//initialize buttons
		histButton = (Button) findViewById(R.id.button_History);
		geographyButton = (Button) findViewById(R.id.button_Geography);
		ggcSongButton = (Button) findViewById(R.id.button_GGCSong);
		funFactsButton = (Button) findViewById(R.id.button_FunFacts);
		
		setButtonListeners();
	}
	
	/**
	 * Method: setButtonListeners
	 * Sets a listener to register button clicks and set action to be performed when each button is clicked.
	 */
	public void setButtonListeners()
	{
		histButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(),HistoryButtonActivity.class);
				startActivity(intent);
			}
		});
		
		geographyButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(),GGCGeographyActivity.class);
				startActivity(intent);
			}
		});
		
		ggcSongButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(),GGCSongActivity.class);
				startActivity(intent);
			}
		});
		
		funFactsButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(getApplicationContext(),GGCFunFactsActivity.class);
				startActivity(intent);
			}
		});
	}
}
