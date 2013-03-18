package edu.ggc.it.map;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.Matrix2f;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.ggc.it.R;

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

	private float spaceBetweenTwoFingers(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	private void midPointBetweenTwoFingers(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

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
			float lat = (float)location.getLatitude();
			float lonLong = (float)location.getLongitude();
			Log.d("GPS","lat "+lat + " long "+lonLong);
			Toast.makeText(context, "GPS lati " +lat+" long "+ lonLong , Toast.LENGTH_LONG).show();
			int lati = (int)(lat*1000000);
			int lon = (int)(lonLong*1000000);
			float latitude =(float)(lati/1000000.0);
			float longitude =(float)(lon/1000000.0);
			float latiOffSet = (float) (latitude - 33.981)*-1; //33.981
			float longOffSet = (float) (84.006 + longitude);
			float metersLatiOffSet = (float) (latiOffSet*350600);//4.70 // 30.860
			float metersLonOffSet = (float) (longOffSet*17000);// 1.409// 30.920
			/*
			//30.92 // I mover all the decimal places over 3 spaces to the right
			Log.d("GPS DATA", "intLati"+lati+"intLong"+lon+"latitude "+latitude +" longitude "+longitude +" latiOffSet"+latiOffSet+" longOffSet "+longOffSet+" mLatiOffSet "+ metersLatiOffSet+" mLonOffSet "+ metersLonOffSet);
			mapView.setRedDotXY(metersLatiOffSet, metersLonOffSet);
			*/
		}
		// So 1 second of latitude = 30.86 meters, or in feet = 101.2 ft.
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
