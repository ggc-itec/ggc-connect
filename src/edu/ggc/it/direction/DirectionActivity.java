package edu.ggc.it.direction;

import edu.ggc.it.R;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
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
	//Create a imageview for the place position on map
	private ImageView img2;
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
	//This will get the destination's latitude
	private double latitudeDes;
	//This will get the destination's longitude
	private double longitudeDes;
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
		//Check if user's device GPS is enable or not. If not, let user to enable it
		checkGPS();
		//Init locationlist
		myLocationList = new LocationList();
		//Create new listenner for spinner
		myItemSelectedListenner mySelectedListenner = new myItemSelectedListenner();
		spin = (Spinner) findViewById(R.id.spinnerText);
		spin.setOnItemSelectedListener(mySelectedListenner);
		
		instructionText = (TextView) findViewById(R.id.instruction_text);
		img = (ImageView) findViewById(R.id.imageMap);
		img1=(ImageView) findViewById(R.id.imageYou);
		img2=(ImageView) findViewById(R.id.imageHere);
		
		//Create ArrayAdapter for spinner
		ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_dropdown_item, myLocationList.getNameList());
	      // setting adapter to spinner
	    spin.setAdapter(spin_adapter);       
	}
	
	/**
	 * This method is to check if the GPS on users' device is on or off. If it is off, allow users turn on
	 */
	public void checkGPS(){
		//Create a new location manager
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//Run these codes when testing on a real device has GPS
		if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			buildAlertMessageNoGps();
		}else{
			lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			latitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLatitude();
			longitude = lm.getLastKnownLocation(lm.GPS_PROVIDER).getLongitude();
		}
		//Test with a specific location without real device has GPS
		latitude = 33.98095;
		longitude = -84.00526;
	}
	
	private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
        alert.show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_direction, menu);
		return true;
	}
	
	/**
	 * This method is a listenner for the spinner in order to get the position of item which is selected
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
				//save = true;
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
				img2.setVisibility(View.INVISIBLE);
				//Display instruction to the textview 
				instructionText.setText(spin_val+latitude+longitude);
			}
			else{//Run these lines when users click on any item on the list of spinner
				img2.setVisibility(View.VISIBLE);
				instructionText.setText("It is located on building "+ bld
						+ "\nInstruction: \n"+"    "+spin_val);
			}
			//Depend on building where the instruction points to, these lines will show the appropriate map
			if (bld.equals("A")){
				latitudeDes = 33.97999;
				longitudeDes = -84.00096;
			}else if(bld.equals("B")){
				latitudeDes = 33.98095;
				longitudeDes =  -84.00526;
			}else if(bld.equals("C")){
				latitudeDes = 33.98040;
				longitudeDes =  -84.00622;
			}else if(bld.equals("D")){
				latitudeDes = 33.97970; 
				longitudeDes = -83.99837;
			}else if(bld.equals("E")){
				latitudeDes = 33.97939; 
				longitudeDes = -84.00553;
			}else if(bld.equals("R")){
				latitudeDes = 33.98032; 
				longitudeDes = -84.00914;
			}else if(bld.equals("S")){
				latitudeDes = 33.97884; 
				longitudeDes = -84.00848;
			}else if(bld.equals("F")){
				latitudeDes = 33.97756; 
				longitudeDes = -83.99980;
			}else if(bld.equals("L")){
				latitudeDes = 33.97959;
				longitudeDes =  -84.00454;
			}else if(bld.equals("BB")){
				latitudeDes = 33.98402;
				longitudeDes =  -84.00212;
			}else if(bld.equals("PD")){
				latitudeDes = 33.98162;
				longitudeDes = -83.99993;
			}else if(bld.equals("P")){
				latitudeDes = 33.97860;
				longitudeDes = -84.01110;
			}
			//Set the x for the current destination's position on Map(-84.01209 to -83.99772)
			img2.setX((float) (imgLeft+((longitudeDes + 84.01209)*100000*imgWidth/1437)));
			//Set the y for the current destination's position on Map(33.98565 to 33.97652)
			img2.setY((float) (imgViewTop-30+((33.98565 - latitudeDes)*100000*imgHeight/913)));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub	
		}
	}
}
