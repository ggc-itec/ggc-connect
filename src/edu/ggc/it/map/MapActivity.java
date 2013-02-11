package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import edu.ggc.it.R;

public class MapActivity extends Activity {
	
	/**
	 *  MapActivity has an image of GGC for the user to see.
	 *  TODO add GPS Object to this. That will handle all of
	 *  GPS data and return the latitude and longitude of the phone,
	 *  and then show a representation of the location of the user on the
	 *  map image of GGC.
	 */
	
	private TextView textViewGPSOutput;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.map_activity);
		
		LocationManager locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
		
		//locationManager.addGpsStatusListener((Listener) new GGCLocationListener());//This is being  cast to "Listener" object. If there are problems with the listener I would start looking here. 
		
		LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {
	    	
	    	Toast.makeText(context, "Your need to enable the GPS service, good luck.", Toast.LENGTH_LONG);
	    	//DOTO I need to look up the process the user has to do to be able too fix this problem. 
	
	        // Build an alert dialog here that requests that the user enable
	        // the location services, then when the user clicks the "OK" button,
	        // call enableLocationSettings()
	    }
	    	    		
	    textViewGPSOutput = (TextView) findViewById(R.id.textViewGPSOutPut);
	  	
	  	textViewGPSOutput.setText("0987654321\n1234567890");	
		
	}
	
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	public class GGCLocationListener implements LocationListener {

	    @Override
	    public void onLocationChanged(Location location) {
	        // A new location update is received.  Do something useful with it.  In this case,
	        // we're sending the update to a handler which then updates the UI with the new
	        // location.
	    	
	    	textViewGPSOutput.setText(location.getLatitude() + ", " + location.getLongitude());
	    	
	    	
	        //Message.obtain(mHandler, UPDATE_LATLNG,location.getLatitude() + ", " + location.getLongitude()).sendToTarget();

	    }
	

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}


}
