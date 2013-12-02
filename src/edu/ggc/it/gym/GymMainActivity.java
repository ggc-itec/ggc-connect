package edu.ggc.it.gym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.ggc.it.R;

public class GymMainActivity extends Activity
{
    public final static String EXTRA_MESSAGE = "edu.ggc.it.directory.MESSAGE";
    private Context context;

    /**
     * This method creates all of the buttons according to their names and
     * locations of the button
     */
    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_main);
		
		context = this;

		Button schedule = (Button) findViewById(R.id.gymSchedule);
		schedule.setOnClickListener(new ButtonListener());
		Button groups = (Button) findViewById(R.id.Group);
		groups.setOnClickListener(new ButtonListener());
		Button magazine = (Button) findViewById(R.id.healthMagazine);
		magazine.setOnClickListener(new ButtonListener());
		TextView quote = (TextView) findViewById(R.id.quoteTextView);
		
		try {
			AssetManager am = context.getAssets();
			InputStream in = am.open("quotes.txt");			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String str;
			ArrayList <String> quotes = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				quotes.add(str);
				Collections.shuffle(quotes);
			}
			in.close();
			quote.setText(quotes.get(0).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.activity_gym_main, menu);
		return true;
	}

	/**
	 * Creates the listeners for all of the buttons individually
	 */
	public class ButtonListener implements OnClickListener
    {
		public void onClick(View view) {
            if (view.getId() == R.id.gymSchedule){
				startActivity(new Intent (context, GymScheduleActivity.class));
			}
			else if (view.getId() == R.id.Group){
				startActivity( new Intent ( context, GroupsActivity.class));
            }
            else if (view.getId() == R.id.healthMagazine){
				String url = "http://readsh101.com/ggc.html";
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
			}
		}
	}
}