package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.ggc.it.R;
/*
//import android.support.v4.widget.ImageView;
Log.d("LOCAL", "view Hights: "+view.getMeasuredHeight()+" view Width :"+view.getMeasuredWidth()+" backGround scaleX: "+backGround.getScaleX()
		+ " backGround scaleY: "+backGround.getScaleY()+" BG ScrollX: "+ backGround.getScrollX()+" BG ScrollY: "+backGround.getScrollX()
		+"BG Width: "+ backGround.getWidth()+ " BG Hight: " +backGround.getHeight()+ "BG X: "+backGround.getY()+" BG Y: "+backGround.getY() );
*/

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
	ImageView redDot;
	ImageView backGround;
	RelativeLayout relativeLayout;
	private Matrix matrix = new Matrix();
	private Matrix oldMatrix = new Matrix();
	public enum State {
		NONE, DRAGGING, ZOOMING
	};
	private State mode = State.NONE;
	private PointF start = new PointF();
	private PointF middle = new PointF();
	float oldDistance = 1f;
	double oldXVal;
	double oldYVal;
	double oldXScale;
	double oldYScale;
	double redDotXOffSet;
	double redDotYOffSet;
	double imageButtonABuildingXOffSet;
	double imageButtonABuildingYOffSet;
	Boolean firstRun;

	
	/**
	 *  MapActivity has an image of GGC that helps users know where they are. 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.map_activity);
		redDot = (ImageView) findViewById(R.id.imageViewTestIcon);
		backGround = (ImageView) findViewById(R.id.imageView_ggc_full_map_small);
		backGround.setOnTouchListener(new TouchListener());
		setUpGPS();
		setUpImageButtons();
		oldXScale=1;
		oldXScale=1;
		firstRun =true;
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
				redDot.setX(50);
				redDot.setY(50);
			}
			Toast.makeText(context, "GPS lati " +latitude+" long "+ longitude , Toast.LENGTH_LONG).show();
			Log.d("GPS Data", "GPS lati " +latitude+" long "+longitude );
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
	
	public class TouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageView view = (ImageView) v;

			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
				oldMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = State.DRAGGING;
			}

			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
				oldDistance = spaceBetweenTwoFingers(event);
				oldMatrix.set(matrix);
				midPointBetweenTwoFingers(middle, event);
				mode = State.ZOOMING;
			}

			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
				mode = State.NONE;
			}

			if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE) {
				if (mode == State.DRAGGING) {
					matrix.set(oldMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY()
							- start.y);
				} else if (mode == State.ZOOMING) {
					float newDistance = spaceBetweenTwoFingers(event);
					matrix.set(oldMatrix);
					float scale = newDistance / oldDistance;
					matrix.postScale(scale, scale, middle.x, middle.y);
				}
			}
			view.setImageMatrix(matrix);
			String s = matrix.toShortString();
			String[] strMatrix = s.split("[\\[ \\] , ]"); 
			double xScale = Double.parseDouble(strMatrix[1]);
			double xVal = Double.parseDouble(strMatrix[5]);
			double yScale = Double.parseDouble(strMatrix[9]);
			double yVal = Double.parseDouble(strMatrix[11]);
			if(firstRun == true){
				redDotXOffSet = redDot.getX()/backGround.getWidth();
				redDotYOffSet = redDot.getY()/backGround.getHeight();
				imageButtonABuildingXOffSet = imageButtonABuilding.getX()/backGround.getWidth();
				imageButtonABuildingYOffSet = imageButtonABuilding.getY()/backGround.getHeight();
				oldXVal = xVal;
				oldYVal = yVal;
				firstRun = false;
			}
			double changeInX = xVal -oldXVal;
			double changeInY = yVal -oldYVal;
			double width = backGround.getWidth()*xScale;
			double height = backGround.getHeight()*yScale;
			if(oldXScale != xScale){
				redDot.setX((float) (xVal+(redDotXOffSet*width)) );
				redDot.setY((float) (yVal+(redDotYOffSet*height)) );
				redDot.setX((float) (redDot.getX()+(((redDot.getWidth()*xScale)-redDot.getWidth())/2)));
				redDot.setY((float) (redDot.getY()+(((redDot.getWidth()*yScale)-redDot.getHeight())/2)));
				redDot.setScaleX((float) xScale);
				redDot.setScaleY((float) yScale);
			
	
			}else{
				redDot.setX((float) (redDot.getX()+(changeInX)));
				redDot.setY((float) (redDot.getY()+(changeInY)));
			}
			imageButtonABuilding.setX((float) xVal);
			imageButtonABuilding.setY((float) yVal);
			Log.d("H/W view", view.getWidth()+"=W - H="+view.getHeight());
			Log.d("Height/Width", height +"=W - H="+width);
			Log.d("xScale/yScal", xScale+"=W - H="+yScale);
			
			
			
			
			oldXScale =xScale;
			oldYScale =yScale;
			oldXVal =xVal;
			oldYVal =yVal;
			return true;
		}
		public int[] firstRunDataLog(View view){
			int[] answer = new int[9];//0 viewXOffSet //1 viewYOffSet //
			//answer[0] = view.getX()/backGround.getWidth();
			redDotXOffSet = redDot.getX()/backGround.getWidth();
			redDotYOffSet = redDot.getY()/backGround.getHeight();
			imageButtonABuildingXOffSet = imageButtonABuilding.getX()/backGround.getWidth();
			
			return null;
			
		}
	
}
}
