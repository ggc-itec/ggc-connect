package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import edu.ggc.it.R;

public class MapActivity extends Activity {
	
	Context context = this;
	LocationManager locationManager;
	GGCLocationListener ggcLocactionListener;
	
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
		setContentView(new MapView(this));
		setUpGPS();
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

	private class GGCLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(context, "GPS lati " +location.getLatitude()+" long "+location.getLongitude() , Toast.LENGTH_LONG).show();
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
}
