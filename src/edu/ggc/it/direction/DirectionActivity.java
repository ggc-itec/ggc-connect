package edu.ggc.it.direction;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import edu.ggc.it.R;
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
	//This aims to get the width of the image view 
	private int imgViewWidth;
	//This aims to get the left of the image view 
	private int imgLeft;
	//This aims to get the top of the image view 
	private int imgViewTop;
	//This aims to get the space between the top of View and top of image
	private int imgPadding;
	//Create a imageview for the GGC map
	private TouchImageView img;
	//Create a imageview for the current position on map
	private static ImageView img1;
	//Create a imageview for the place position on map
	private static ImageView img2;
	//Create new context for activity
	private Context myContext;
	//Create a textview to display instructions to users
	private TextView instructionText;
	//Create location manager 
	private LocationManager lm;
	//Create a location
	UserLocationListener myLocationListener;
	//This will get the user's latitude
	private double latitude;
	//This will get the user's longitude
	private double longitude;
	//This will get the destination's latitude
	private double latitudeDes;
	//This will get the destination's longitude
	private double longitudeDes;
	//Create a location list that holds list of places in GGC
	private LocationArray myLocationList;
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
		myLocationList = new LocationArray();
		//Create new listenner for spinner
		MyItemSelectedListenner mySelectedListenner = new MyItemSelectedListenner();
		spin = (Spinner) findViewById(R.id.spinnerText);
		spin.setOnItemSelectedListener(mySelectedListenner);
		
		instructionText = (TextView) findViewById(R.id.instruction_text);
		img = (TouchImageView) findViewById(R.id.imageMap);
        img.setMaxZoom(4f);
		img1=(ImageView) findViewById(R.id.imageYou);
		img2=(ImageView) findViewById(R.id.imageHere);
		//Create ArrayAdapter for spinner
		ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(myContext, android.R.layout.simple_spinner_dropdown_item, myLocationList.getNameList());
		// setting adapter to spinner
	    spin.setAdapter(spin_adapter);    
	}
	
	/**
	 * This function aims to release all objects to NULL in order to save memory
	 */
	public void onBackPressed (){
		img = null;
		img1 = null;
		img2 = null;
		//Set Location manager to null to turn GPS off
		lm.removeUpdates(myLocationListener);
		lm = null;
		super.onBackPressed();
		return;
	}
	
	/**
	 * This function aims to update user's location when it changes, to correct location of users on the map
	 */
	public void updateLocation(){
		//Set the x for the current user's position on Map(-84.01209 to -83.99772)
		img1.setX((float) (imgLeft+((longitude + 84.01209)*100000*imgWidth/1437)));
		//Set the y for the current user's position on Map(33.98565 to 33.97652)
		img1.setY((float) (imgViewTop-30+((33.98565 - latitude)*100000*imgHeight/913)));
		img1.setImageResource(R.drawable.you);
		img1.invalidate();
	}
	
	public static void refreshMarkers(){
		img1.setVisibility(View.VISIBLE);
		img2.setVisibility(View.VISIBLE);
	}
	
	/**
	 * This function aims to hide cursors of destination and user when the map is zooming.
	 */
	public static void hideLocation(){
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
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
			lm.getProvider(LocationManager.GPS_PROVIDER);
			myLocationListener = new UserLocationListener();
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, myLocationListener);
		}
	}
	
	/**
	 * This class to override LocationListener to get current location of users.
	 *
	 */
	private class UserLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			longitude= location.getLongitude();
		    latitude= location.getLatitude();
		    updateLocation();
		}
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub	
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub	
		}	
	}
	
	/**
	 * This method is used to warn users when they click on Direction button if the GPS on their device is not enable
	 * and allow them to enable it
	 */
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
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

		List<Address> addresses = null;

		try {
		    addresses = geocoder.getFromLocation(latitude, longitude, 3);
		} catch (IOException ex) {
		    //Handle IOException
		}
		String zipCode="";
		for (int i = 0; i < addresses.size(); i++) {
		    Address address = addresses.get(i);
		    if (address.getPostalCode() != null)
		        zipCode = address.getPostalCode();
		}
		String url="http://www.weather.com/weather/today/"+zipCode;
		if (item.getItemId()==R.id.weather) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			} catch (Exception e) {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(url)));
			}
		}
		return true;
	}

	
	/**
	 * This method is a listenner for the spinner in order to get the position of item which is selected
	 *
	 */
	public class MyItemSelectedListenner implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
			Configuration config = getResources().getConfiguration();
			spin_val = myLocationList.getInstruction(position);
			String bld = myLocationList.getBuilding(position);
			imgLeft = img.getLeft();
			imgViewTop = img.getTop();
			if(config.orientation == 1){//Portrait
				imgWidth = img.getWidth();
				//Find the relative height of image on its width
				imgHeight = imgWidth*913/1188;
				imgViewHeight = img.getHeight();
				//Find the space between the top of View and Image
				imgPadding = (imgViewHeight - imgHeight)/2;
				//Update the top for the image
				imgViewTop = imgViewTop + imgPadding;
			}else
				if(config.orientation == 2){
					imgHeight = img.getHeight();
					//Find the relative width of image on its height
					imgWidth = imgHeight*1188/913;
					imgViewWidth = img.getWidth();
					//Find the space between the left of View and Image
					imgPadding = (imgViewWidth - imgWidth)/2;
					//Update the left for the image
					imgLeft = imgLeft + imgPadding;
					//instructionText.setVisibility(View.GONE);
				}
			//Set the x for the current user's position on Map(-84.01209 to -83.99772)
			img1.setX((float) (imgLeft+((longitude + 84.01209)*100000*imgWidth/1437)));
			//Set the y for the current user's position on Map(33.98565 to 33.97652)
			img1.setY((float) (imgViewTop-30+((33.98565 - latitude)*100000*imgHeight/913)));
			
			//Check and run these lines when user click nothing or the first row in the place's list
			if(bld.length()==0){
				instructionText.setText(spin_val);
				img.setImageResource(R.drawable.thai_ggc_map);	
				img2.setVisibility(View.INVISIBLE);
				img1.setImageResource(R.drawable.you);
			}
			else{//Run these lines when users click on any item on the list of spinner
				img2.setVisibility(View.VISIBLE);
				if(bld.length()==1){
				instructionText.setText("It is located on building "+ bld
						+ "\nInstruction: Your location is Yellow dot, Destination is Red dot\n"+"    "+spin_val);
				}
				else{
					instructionText.setText("\nInstruction: Your location is Yellow dot, Destination is Red dot\n"+"    "+spin_val);
				}
			}
			//Depend on building where the instruction points to, these lines will show the appropriate map
			latitudeDes = myLocationList.getLatitude(position);
			longitudeDes = myLocationList.getLongitude(position);
			//Set the x for the current destination's position on Map(-84.01209 to -83.99772)
			img2.setX((float) (imgLeft+((longitudeDes + 84.01209)*100000*imgWidth/1437)));
			//Set the y for the current destination's position on Map(33.98565 to 33.97652)
			img2.setY((float) (imgViewTop-30+((33.98565 - latitudeDes)*100000*imgHeight/913)));
			img.setOriginalSize();
			//Log.d("New values: X" + img.getLeft(), "Y"+img.getTop());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub	
		}
	}
}
