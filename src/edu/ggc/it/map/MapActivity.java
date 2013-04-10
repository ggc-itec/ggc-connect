package edu.ggc.it.map;


import edu.ggc.it.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
/**
 * @author Andrew F. Lynch
 *
 * This class is intended to show the location of the device on GGC Campus. 
 * This class works by getting the GPS data then passing it to a {@link MapActivity}
 * that has a background image and current location redDot icon the show where the 
 * phone is on campus. 
 */

public class MapActivity extends Activity {
	
	private Context context = this;
	private LocationManager locationManager;
	private GGCLocationListener ggcLocactionListener;
	private MapView mapView;
	
	/**
	 *  MapActivity has an image of GGC that helps users know where they are. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView = new MapView(context);
		setContentView( mapView);
		setUpGPS();
	}

	/**
	 * This method is a helper method hand some of the math from MotionEvents. 
	 * @param event
	 * @return float 
	 */
	private float spaceBetweenTwoFingers(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}
	/**
	 * This method is a helper method hand some of the math from MotionEvents. 
	 * @param event
	 * @return float 
	 */
	private void midPointBetweenTwoFingers(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * This method sets up the PGS objects and handles the case if the user has not yet turned on the GPS on the device.
	 * @param void
	 * @return void 
	 */
	protected void setUpGPS(){	
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    if (!gpsEnabled) {
	    	enableLocationSettings();//
	    }
		LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
		ggcLocactionListener = new GGCLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10, ggcLocactionListener);	
	}
	/**
	 * This method is called when device dose not have the GPS enabled. It prompts the user to turn on the GPS
	 * and opens up an activity that can turn on the GPS.
	 * @param void
	 * @return void 
	 */
	private void enableLocationSettings() {
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	/**
	 * This method cancels the GPS update request.
	 */
	protected void onStop() {
	    super.onStop();
	    locationManager.removeUpdates(ggcLocactionListener);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setContentView(R.layout.activity_empty);
		mapView = null;
	}
	
	/**
	 * This class implements {@link LocationListener} to be able to receive GPS data
	 * it gathers the data then converts the data into meters off set from the center of GGC
	 * it then passed this data to {@link MapView} so it can update the redDot location icon. 
	 * @author andrew
	 *
	 */
	private class GGCLocationListener implements LocationListener{
		
		@Override
		public void onLocationChanged(Location location) {
			
			float lonLong = (float)location.getLongitude();;
			float lat = (float) (float)location.getLatitude();
			Log.d("GPS","lat "+lat + " long "+lonLong);
			int lon = (int)(lonLong*1000000);
			int lati = (int)(lat*1000000);
			float longitude =(float)(lon/1000000.0);
			float latitude =(float)(lati/1000000.0);
			float longOffSet = (float) (84.002993 + longitude);
			float latiOffSet = (float) (latitude - 33.979813)*-1; //33.981
			float metersLonOffSet = (float) (longOffSet*98000);// 1.409// 30.920 // 92406.653
			float metersLatiOffSet = (float) (latiOffSet*87000);//4.70 // 30.860 // 110921.999
			mapView.setRedDotXY(metersLonOffSet,metersLatiOffSet);

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
