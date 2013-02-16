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
	 *  MapActivity has an image of GGC for the user to use for navigation.
	 *  
	 *  As of this commit this application is getting PGS data
	 *  Although it does take about 60 to 90 seconds be able to get 
	 *  the GPS date. This is an issue that will be worked out in 
	 *  future commits. 
	 *  
	 *  TODO
	 *  - find a way to display a PDF 
	 *  - find a way to sink up GPS data with the PDF
	 *  - Enable zooming with the PDF
	 *  - fine a way to move and image of the PDF to show user location
	 */
	
	private TextView textViewGPSOutput;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.map_activity);
		
		LocationManager locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
		
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!gpsEnabled) {
	    	Toast.makeText(context, "GPS service is not Enabled.", Toast.LENGTH_LONG).show();
	    	//DOTO I need to look up the process the user has to do to be able too fix this problem. 
	    	enableLocationSettings();
	    	
	    }
	    
	    GGCLocationListener ggcLocationListener = new GGCLocationListener();
	    
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	            1000,          // 10-second interval.
	            10,             // 10 meters.
	            new GGCLocationListener());
	    
	    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				
		LocationProvider locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		
		Toast.makeText(context, locationProvider.toString(), Toast.LENGTH_LONG).show();
	    
	    textViewGPSOutput = (TextView) findViewById(R.id.textViewGPSOutPut);
	  	
	  	textViewGPSOutput.setText("0987654321\n1234567890");
	  	
    	//Toast.makeText(context, "TEST", Toast.LENGTH_LONG).show();

	}
	
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	/**
	 * @author Andrew
	 * GGCLocatoinListener is working but it takes about a 60 to 90 seconds to get the first 
	 * GPS updates. This issue will be dealt with in future commits.
	 */
	public class GGCLocationListener implements LocationListener {

	    @Override
	    public void onLocationChanged(Location location) {
	    	
	    	textViewGPSOutput.setText(location.getLatitude() + ", " + location.getLongitude());
	    	
	    	//Toast.makeText(context, "GPS IS WORKIN!!!", Toast.LENGTH_LONG).show();
	    	   	
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
