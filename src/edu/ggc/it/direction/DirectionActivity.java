package edu.ggc.it.direction;

import edu.ggc.it.R;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DirectionActivity extends Activity {
	private Button findButton;
	private ImageButton bldAButton;
	private ImageButton bldBButton;
	private ImageButton bldCButton;
	private ImageButton bldDButton;
	private ImageButton bldEButton;
	private ImageButton bldFButton;
	private ImageButton bldHButton;
	private ImageButton bldLButton;
	private ImageButton bldIButton;
	private ImageButton parkingButton;
	private ImageView img;
	
	private Context myContext;
	
	private EditText findText;
	private TextView instructionText;
	
	private LocationManager lm;
	private double latitude;
	private double longitude;
	private Location loc;

	private LocationList myLocationList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direction);
		myContext = this;
		myLocationList = new LocationList();
		MyListener myListener = new MyListener();
		findButton = (Button) findViewById(R.id.ButtonFindaPlace);
		findButton.setOnClickListener(myListener);

		bldAButton = (ImageButton) findViewById(R.id.bldAButton);
		bldAButton.setOnClickListener(myListener);
		
		bldBButton = (ImageButton) findViewById(R.id.bldBButton);
		bldBButton.setOnClickListener(myListener);
		
		bldCButton = (ImageButton) findViewById(R.id.bldCButton);
		bldCButton.setOnClickListener(myListener);
		
		bldDButton = (ImageButton) findViewById(R.id.bldDButton);
		bldDButton.setOnClickListener(myListener);
		
		bldEButton = (ImageButton) findViewById(R.id.bldEButton);
		bldEButton.setOnClickListener(myListener);
		
		bldFButton = (ImageButton) findViewById(R.id.bldFButton);
		bldFButton.setOnClickListener(myListener);
		
		bldHButton = (ImageButton) findViewById(R.id.bldHButton);
		bldHButton.setOnClickListener(myListener);
		
		bldLButton = (ImageButton) findViewById(R.id.bldLButton);
		bldLButton.setOnClickListener(myListener);
		
		bldIButton = (ImageButton) findViewById(R.id.bldIButton);
		bldIButton.setOnClickListener(myListener);
		
		parkingButton = (ImageButton) findViewById(R.id.parkingButton);
		parkingButton.setOnClickListener(myListener);
		
		instructionText = (TextView) findViewById(R.id.instruction_text);
		
		img = (ImageView) findViewById(R.id.imageMap);
		//img.setImageResource(R.drawable.my_image);
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_direction, menu);
		return true;
	}
	public class MyListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.ButtonFindaPlace) {
				if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
					instructionText.setText("Ooops... Your GPS seems to be disabled, please turn it on before using this function!!!");
			    }
				else{
					loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					latitude = loc.getLatitude();
					longitude = loc.getLongitude();
					//Get text from Find text box
					findText = (EditText) findViewById(R.id.editFindaPlace);
					String txt = findText.getText().toString();
					String rst = myLocationList.Find(txt);
					//TextView instr = (TextView) findViewById(R.id.instruction_text);
					//instructionText = (TextView) findViewById(R.id.instruction_text);
					instructionText.setText("Your location is: \n Latitude: "+latitude+"\n Longtitude: "+longitude + "\n Hey, You can find the "+txt+" at: "+rst+"Top"+img.getTop()+"Left"+img.getLeft()+"Bottom"+img.getBottom()+"Right"+img.getRight());
					//Toast.makeText(myContext, "We are building this function, will be available soon..."+latitude+" : "+longitude, 100).show();
				}
			} else if (view.getId() == R.id.bldAButton) {
				//startActivity(new Intent(myContext, News.class));
				//img.setImageResource(R.drawable.academic);
				//Toast.makeText(myContext, "You click on Building A button", 100).show();
				//Set text for Direction Text Box
				instructionText.setText("What this building has: " + 
						"\n Food Court, ATM. " +
						"\n Campus Police, Parking Decal Service." +
						"\n Testing and Help Desk Services.");
				img.setImageResource(R.drawable.building_a);
			} else if (view.getId() == R.id.bldBButton) {
				img.setImageResource(R.drawable.building_b);
				instructionText.setText("What this building has: " + 
						"\n Food Court, Stage, and Computer Lab on the base floor " +
						"\n AEC: Writing Lab and Math Lab on the second floor" +
						"\n Study Area on third floor");
				//Toast.makeText(myContext, "You click on Building B button", 100).show();
			} else if( view.getId() == R.id.bldCButton) {
				img.setImageResource(R.drawable.building_c);
				instructionText.setText("What this building has: " + 
						"\n Auditorium @ 1260 " );
				//Toast.makeText(myContext, "You click on Building C button", 100).show();
			} else if( view.getId() == R.id.bldDButton) {
				img.setImageResource(R.drawable.building_d);
				instructionText.setText("What this building has: " + 
						"\n Admission." +
						"\n Financial Aid");
				//Toast.makeText(myContext, "You click on Building D button", 100).show();
			} else if( view.getId() == R.id.bldEButton) {
				img.setImageResource(R.drawable.building_e1);
				instructionText.setText("What this building has: " + 
						"\n 1st Floor: Dining Room, Game Room, Muilty Purpose Room" +
						"\n 2nd Floor: Reception, Book Store, L.V.I.S" +
						"\n 3rd Floor: Student Involvement Center");
				//Toast.makeText(myContext, "You click on Building E button", 100).show();
			} else if( view.getId() == R.id.bldFButton) {
				img.setImageResource(R.drawable.building_f);
				instructionText.setText("What this building has: " + 
						"\n Olympic Pool, Basket Ball, Racquet Ball Courts on the 1st floor " +
						"\n Weight Area, Cadio Area, and Spinning on the 2nd floor" +
						"\n Aerobics, Elavated Track on the 2nd floor");
				//Toast.makeText(myContext, "You click on Building Fbutton", 100).show();
			} else if( view.getId() == R.id.bldHButton) {
				Toast.makeText(myContext, "You click on Building H button", 100).show();
			} else if( view.getId() == R.id.bldIButton) {
				Toast.makeText(myContext, "You click on Building I button", 100).show();
			} else if( view.getId() == R.id.bldLButton) {
				img.setImageResource(R.drawable.building_l);
				instructionText.setText("What this building has: " + 
						"\n 1st floor: Computer Lab, Research Commons, Cafe', General Collection, Study rooms" +
						"\n 2nd floor: AEC, Research Services, Center for Teaching Excellence, Study rooms" +
						"\n 3rd floor: Grand Reading Room, Study rooms");
				//Toast.makeText(myContext, "You click on Building L button", 100).show();
			} else if( view.getId() == R.id.parkingButton) {
				Toast.makeText(myContext, "You click on Building Parking button", 100).show();
			}

		}
	}
}
