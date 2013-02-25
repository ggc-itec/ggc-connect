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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DirectionActivity extends Activity {
	
	private Button findButton;
	private ImageView img;
	
	private Context myContext;
	
	private EditText findText;
	private TextView instructionText;
	
	private LocationManager lm;
	private double latitude;
	private double longitude;
	private Location loc;

	private LocationList myLocationList;
	
	private Spinner spin;
	private String spin_val;
	private PlaceList myPlaceList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direction);
		myContext = this;
		myLocationList = new LocationList();
		
		myItemSelectedListenner mySelectedListenner = new myItemSelectedListenner();
		spin = (Spinner) findViewById(R.id.spinnerText);
		spin.setOnItemSelectedListener(mySelectedListenner);

		instructionText = (TextView) findViewById(R.id.instruction_text);
		img = (ImageView) findViewById(R.id.imageMap);
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		latitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLatitude();
		longitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLongitude();
		
		ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myLocationList.getNameList());
	      // setting adapter to spinner
	    spin.setAdapter(spin_adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_direction, menu);
		return true;
	}
	public class myItemSelectedListenner implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
			// TODO Auto-generated method stub
			spin_val = myLocationList.getInstruction(position);
			String bld = myLocationList.getBuilding(position);
			if(bld.length()==0){
				instructionText.setText(spin_val);
				img.setImageResource(R.drawable.thai_ggc_map);
			}
			else{
			instructionText.setText("It is located on building "+ bld
					//Line below just used for test
					+"\n Latitude: "+latitude +"\n Longitude: "+longitude
					+ "\nInstruction: \n"+"    "+spin_val
					+ "\nHere is building "+bld+" map");
			}
			//bld = myLocationList.getBuilding(position);
			if (bld.equals("A")){
				img.setImageResource(R.drawable.building_a);
			}else if(bld.equals("B")){
				img.setImageResource(R.drawable.building_b);
			}else if(bld.equals("C")){
				img.setImageResource(R.drawable.building_c);
			}else if(bld.equals("D")){
				img.setImageResource(R.drawable.building_d);
			}else if(bld.equals("E1")){
				img.setImageResource(R.drawable.building_e1);
			}else if(bld.equals("E2")){
				img.setImageResource(R.drawable.building_e2);
			}else if(bld.equals("E3")){
				img.setImageResource(R.drawable.building_e3);
			}else if(bld.equals("F")){
				img.setImageResource(R.drawable.building_f);
			}else if(bld.equals("L")){
				img.setImageResource(R.drawable.building_l);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub	
		}
		
	}
	public class MyListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			
		}
	}
}
