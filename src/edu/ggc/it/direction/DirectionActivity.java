package edu.ggc.it.direction;

import edu.ggc.it.R;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
/**
 * This class is an activity class
 * @author Thai Pham
 * @version 0.1
 *
 */
public class DirectionActivity extends Activity {
	//This aims to get the width of the image view 
	private int imgWidth;
	//This aims to get the height of the image
	private int imgHeight;
	//This aims to get the height of the image view 
	private int imgViewHeight;
	//This aims to get the left of the image view 
	private int imgLeft;
	//This aims to get the top of the image view 
	private int imgViewTop;
	//This aims to get the space between the top of View and top of image
	private int imgPadding;
	//Condition value allows program get position of image one time
	private boolean save = false;
	//Create a imageview for the GGC map
	private ImageView img;
	//Create a imageview for the current position on map
	private ImageView img1;
	//Create new context for activity
	private Context myContext;
	//Create a textview to display instructions to users
	private TextView instructionText;
	//Create location manager 
	private LocationManager lm;
	//This will get the user's latitude
	private double latitude;
	//This will get the user's longitude
	private double longitude;
	//Create new location
	private Location loc;
	//Create a location list that holds list of places in GGC
	private LocationList myLocationList;
	//Create a new spinner to display list of places in GGC 
	private Spinner spin;
	//This will get the item that users choose from place's list of spinner
	private String spin_val;
	/**
	 * @Override
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direction);
		myContext = this;
		//Init locationlist
		myLocationList = new LocationList();
		//Create new listenner for spinner
		myItemSelectedListenner mySelectedListenner = new myItemSelectedListenner();
		spin = (Spinner) findViewById(R.id.spinnerText);
		spin.setOnItemSelectedListener(mySelectedListenner);
		
		instructionText = (TextView) findViewById(R.id.instruction_text);
		img = (ImageView) findViewById(R.id.imageMap);
		img1=(ImageView) findViewById(R.id.imageYou);
		
		//Require real device has GPS
		//lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//latitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLatitude();
		//longitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLongitude();
		
		//Test without real device has GPS
		latitude = 33.98474;
		longitude =  -84.00265;
		
		//Create ArrayAdapter for spinner
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
	
	/**
	 * @Override
	 *
	 */
	public class myItemSelectedListenner implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
			spin_val = myLocationList.getInstruction(position);
			String bld = myLocationList.getBuilding(position);
			if (!save){//allow device get position one time
				imgLeft = img.getLeft();
				imgViewTop = img.getTop();
				imgWidth = img.getWidth();
				//Find the relative height of image on its width
				imgHeight = imgWidth*913/1188;
				imgViewHeight = img.getHeight();
				//Find the space between the top of View and Image
				imgPadding = (imgViewHeight - imgHeight)/2;
				//Update the top for the image
				imgViewTop = imgViewTop + imgPadding;
				//Do not to re-get above data
				save = true;
			}
			//Set the x for the current user's position on Map(-84.01209 to -83.99772)
			img1.setX((float) (imgLeft+((longitude + 84.01209)*100000*imgWidth/1437)));
			//Set the y for the current user's position on Map(33.98565 to 33.97652)
			img1.setY((float) (imgViewTop-30+((33.98565 - latitude)*100000*imgHeight/913)));
			
			//Check and run these lines when user click nothing or the first row in the place's list
			if(bld.length()==0){
				img.setImageResource(R.drawable.thai_ggc_map);	
				img1.setImageResource(R.drawable.you);
				//Set the current position of user on Map
				img1.setVisibility(View.VISIBLE);
				//Display instruction to the textview 
				instructionText.setText(spin_val);
			}
			else{//Run these lines when users click on any item on the list of spinner
				img1.setVisibility(View.INVISIBLE);
				instructionText.setText("It is located on building "+ bld
						+ "\nInstruction: \n"+"    "+spin_val
						+ "\nHere is building "+bld+" map");
			}
			//Depend on building where the instruction points to, these lines will show the appropriate map
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
}
