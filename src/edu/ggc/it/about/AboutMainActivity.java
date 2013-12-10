package edu.ggc.it.about;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * /////////////////////////////////////////////////////////////////////////////////////////
 * ///                             COPYRIGHT DISCLAIMER                                  ///
 * ///                                                                                   ///
 * ///  Owner(s) of (or contributor(s) to) the GGC Connect App claim no ownership of     ///
 * ///  the GGC Alma Mater music or lyrics. The music and lyrics belong to their         ///
 * ///  respective owner(s).                                                             ///
 * ///                                                                                   ///
 * ///                      GEORGIA GWINNETT COLLEGE ALMA MATER                          ///
 * ///        Lyrics by Dr. Alvina Atkinson and Brittany Dertz, Class of 2011            ///
 * ///        Music by Dr. Thomas Young                                                  ///
 * ///                                                                                   ///
 * ///  For more information:                                                            ///
 * ///                                                                                   ///
 * ///  http://www.ggc.edu/student-life/commencement-landing/spring-commencement-2013/   ///
 * ///  ceremony-and-reception-info/alma-mater/index.html                                ///
 * ///                                                                                   ///
 * ///  http://www.ggc.edu/about-ggc/news/6-2-11-GGC%20Alma%20Mater%20premiers%20at%20   ///
 * ///  June%202%20commencement.html                                                     ///
 * /////////////////////////////////////////////////////////////////////////////////////////
 * 
 * Class: MainActivity
 * 
 * Main page for AboutGGC. Provides buttons for GGC history, geography, song, and fun facts.
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
