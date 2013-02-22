package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import edu.ggc.it.R;


public class MapActivity extends Activity {
	
	Context context = this;
	LocationManager locationManager;
	GGCLocationListener ggcLocactionListener;
	ImageButton imageButtonABuilding;
	ImageButton imageButtonBBuilding;
	ImageButton imageButtonCBuilding;
	ImageButton imageButtonDBuilding;
	ImageButton imageButtonFBuilding;
	ImageButton imageButtonLBuilding;
	ImageButton imageButtonStudentCenter;
	
	ImageView imageTestIcon;
	
	/**
	 *  MapActivity has an image of GGC that helps users know where they are. 
	 *  
	 *  TODO 
	 * 	- need to find a way to show PDFs
	 *  - need to enable zooming
	 *  - need to figure out the math for showing current location on the map
	 *  - need to find a way to draw over an PDF (to show were current location is)
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.map_activity);
		imageTestIcon = (ImageView) findViewById(R.id.imageViewTestIcon);
		setUpGPS();
		setUpImageButtons();
		
	
	}
	
	
	/**
	 *  setUpImageButtons sets up all of the ImageButtons, sets their background color to TRANSPARENT, and adds and {@link GGCOnClickListener} to each
	 *  of the ImageButtons. 
	 */
	private void setUpImageButtons() {
		GGCOnClickListener ggcOnClickListener = new GGCOnClickListener();
		//A
		imageButtonABuilding = (ImageButton) findViewById(R.id.ImageButtonABuilding);
		//imageButtonABuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonABuilding.setOnClickListener(ggcOnClickListener);
		//B
		imageButtonBBuilding = (ImageButton) findViewById(R.id.ImageButtonBBuilding);
		//imageButtonBBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonBBuilding.setOnClickListener(ggcOnClickListener);
		//C
		imageButtonCBuilding = (ImageButton) findViewById(R.id.ImageButtonCBuilding);
		//imageButtonCBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonCBuilding.setOnClickListener(ggcOnClickListener);
		//D
		imageButtonDBuilding = (ImageButton) findViewById(R.id.ImageButtonDBuilding);
		//imageButtonDBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonDBuilding.setOnClickListener(ggcOnClickListener);
		//F
		imageButtonFBuilding = (ImageButton) findViewById(R.id.ImageButtonFBuilding);
		//imageButtonFBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonFBuilding.setOnClickListener(ggcOnClickListener);
		//L
		imageButtonLBuilding = (ImageButton) findViewById(R.id.ImageButtonLBuilding);
		//imageButtonLBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonLBuilding.setOnClickListener(ggcOnClickListener);
		//Student Center
		imageButtonStudentCenter = (ImageButton) findViewById(R.id.ImageButtonStudentCenterBuilding);
		//imageButtonStudentCenter.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonStudentCenter.setOnClickListener(ggcOnClickListener);

		
		//imageButtonStudentCenter.setTranslationX();
	}


	protected void setUpGPS(){
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {

	    }
	
		LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		
		ggcLocactionListener = new GGCLocationListener();
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10, ggcLocactionListener);
		
		
	}
	
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	protected void onStop() {
	    super.onStop();
	    locationManager.removeUpdates(ggcLocactionListener);
	}
	
	private class GGCOnClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			
			if(view.getId() == R.id.ImageButtonABuilding ){ 
				startActivity(new Intent(context, ImageTouchABuildingActivity.class));
			}else if(view.getId() == R.id.ImageButtonBBuilding ){ 
				startActivity(new Intent(context, ImageTouchBBuildingActivity.class));
			}else if(view.getId() == R.id.ImageButtonCBuilding ){ 
				startActivity(new Intent(context, ImageTouchCBuildingActivity.class));
			}else  if(view.getId() == R.id.ImageButtonDBuilding ){ 
				startActivity(new Intent(context, ImageTouchDBuildingActivity.class));
			}else  if(view.getId() == R.id.ImageButtonFBuilding ){ 
				startActivity(new Intent(context, ImageTouchFBuildingActivity.class));
			}else  if(view.getId() == R.id.ImageButtonLBuilding ){ 
				startActivity(new Intent(context, ImageTouchLBuildingActivity.class));
			}else  if(view.getId() == R.id.ImageButtonStudentCenterBuilding){ 
				startActivity(new Intent(context, ImageTouchStudentCenterActivity.class));
			}
			//ImageTouchABuildingActivity touchActivity = new ImageTouchABuildingActivity();
			/*
			Toast.makeText(context, "WORKING!!", Toast.LENGTH_LONG).show();		
			setContentView(R.layout.a_building);
			imageTestIcon.setTranslationX(110);// this is the amount that the image is got be translated so it is not a relative index 
			imageTestIcon.setTranslationY(100);// I will need a method to take care of the math 
			*/
			// I changed the target IPA to 11 because 8 did not have 
			// setTranstion()
		}		
		
	}

	private class GGCLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			String lat = latitude+"";
			lat = lat.substring(0,5);
			String lon = longitude+"";
			lon = lon.substring(0,6);
			if (Double.parseDouble(lat) == 33.98 && Double.parseDouble(lon) == -84.00  ) {
				imageTestIcon.setTranslationX(100);
				imageTestIcon.setTranslationY(-100);		
			}
			
			Toast.makeText(context, "GPS lati " +latitude+" long "+ longitude , Toast.LENGTH_LONG).show();
			Log.d("GPS Data", "GPS lati " +latitude+" long "+longitude );
		}
		
		//So 1 second of latitude = 30.86 meters, or in feet = 101.2 ft.
		// 1 second of longitude = 30.922 meters.
		// C Building lati 33.98067802886346 long -84.0065738567545

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
}
