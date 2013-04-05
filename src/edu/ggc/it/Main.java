package edu.ggc.it;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import edu.ggc.it.catalog.ClassSearchActivity;
import edu.ggc.it.direction.DirectionActivity;
import edu.ggc.it.directory.DirectoryActivity;
import edu.ggc.it.gym.GymMainActivity;
import edu.ggc.it.love.SetupActivity;
import edu.ggc.it.map.MapActivity;
import edu.ggc.it.rss.EventsRSSActivity;
import edu.ggc.it.rss.NewsRSSActivity;
import edu.ggc.it.schedule.ScheduleActivity;
import edu.ggc.it.todo.ToDoListActivity;

/*  
 * ggc-connect is an app designed for the GGC community 
 * @author ggc-itec
 * 
 */
public class Main extends Activity {

	private Button directoryButton;
	private Button directionButton;
	private Button mapButton;
	private Button gymButton;
	private Button scheduleButton;
	private Button loveButton;
	private ImageButton facebookButton;
	private Button classSearchButton;
	private Context myContext;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myContext = this;
		MyListener myListener = new MyListener();
		directoryButton = (Button) findViewById(R.id.directory_button);
		directoryButton.setOnClickListener(myListener);

		directionButton = (Button) findViewById(R.id.direction_button);
		directionButton.setOnClickListener(myListener);

		mapButton = (Button) findViewById(R.id.map_button);
		mapButton.setOnClickListener(myListener);

		gymButton = (Button) findViewById(R.id.gym_button);
		gymButton.setOnClickListener(myListener);

		scheduleButton = (Button) findViewById(R.id.schedule_button);
		scheduleButton.setOnClickListener(myListener);

		loveButton = (Button) findViewById(R.id.love_button);
		loveButton.setOnClickListener(myListener);
		
		classSearchButton = (Button)findViewById(R.id.search_button);
		classSearchButton.setOnClickListener(myListener);
		
		facebookButton = (ImageButton)findViewById(R.id.facebook_page);
		facebookButton.setOnClickListener(myListener);

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
			new AlertDialog.Builder(this)
					.setTitle("Welcome")
					.setMessage(
							"ggc-connect is an app for the Georgia Gwinnett College community")
					.setNeutralButton("Close",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();
			return true;
		case R.id.news_rss:
			Intent newsRssIntent = new Intent(Main.this, NewsRSSActivity.class);
			Main.this.startActivity(newsRssIntent);
			return true;
		case R.id.events_rss:
			Intent eventsRssIntent = new Intent(Main.this, EventsRSSActivity.class);
			Main.this.startActivity(eventsRssIntent);
			return true;
		case R.id.credits:
			Intent myIntent = new Intent(Main.this, Credits.class);
			Main.this.startActivity(myIntent);
			return true;
		case R.id.links:
			Intent myIntent2 = new Intent(Main.this, News.class);
			Main.this.startActivity(myIntent2);
			return true;
		case R.id.todo:
			Intent myIntent3 = new Intent(Main.this, ToDoListActivity.class);
			Main.this.startActivity(myIntent3);
			return true;
		case R.id.feedback:
			String feedbackURL = "https://docs.google.com/forms/d/1_6-2W088X8q2RNziskqiGIRYGelE-d0YvLYpd7hcNI0/viewform";
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedbackURL));
			Main.this.startActivity(browserIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class MyListener implements OnClickListener {
		public void onClick(View view) {
			if (view.getId() == R.id.directory_button) {
				Intent myIntent = new Intent(myContext, DirectoryActivity.class);
				startActivity(myIntent);
			} else if (view.getId() == R.id.map_button) {
				startActivity(new Intent(myContext, MapActivity.class));
			} else if (view.getId() == R.id.direction_button) {
				startActivity(new Intent(myContext, DirectionActivity.class));
			} else if (view.getId() == R.id.gym_button) {
				startActivity(new Intent(myContext, GymMainActivity.class));
			} else if (view.getId() == R.id.schedule_button) {
				startActivity(new Intent(myContext, ScheduleActivity.class));
				
			} else if (view.getId() == R.id.love_button) {
				startActivity(new Intent(myContext, SetupActivity.class));
			} else if (view.getId() == R.id.search_button){
				startActivity(new Intent(myContext, ClassSearchActivity.class));
			} else if (view.getId() == R.id.facebook_page) {
				try {
					myContext.getPackageManager()
					.getPackageInfo("com.facebook.katana", 0);
					startActivity(new Intent(Intent.ACTION_VIEW,
			                Uri.parse("fb://profile/78573401446")));
				} catch (Exception e) {
					startActivity(new Intent(Intent.ACTION_VIEW,
			                Uri.parse("http://www.facebook.com/georgiagwinnett")));
				}
				
			}

		}
	}

}