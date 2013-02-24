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
import android.text.style.BackgroundColorSpan;
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
Log.d("LOCAL", "view Hights: "+view.getMeasuredHeight()+" view Width :"+view.getMeasuredWidth()+" imageViewBackGround scaleX: "+imageViewBackGround.getScaleX()
		+ " imageViewBackGround scaleY: "+imageViewBackGround.getScaleY()+" BG ScrollX: "+ imageViewBackGround.getScrollX()+" BG ScrollY: "+imageViewBackGround.getScrollX()
		+"BG Width: "+ imageViewBackGround.getWidth()+ " BG Hight: " +imageViewBackGround.getHeight()+ "BG X: "+imageViewBackGround.getY()+" BG Y: "+imageViewBackGround.getY() );
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
	ImageView imageViewBackGround;
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
	double[] backGroundImageScaleAndValArray;
	float[] redDotArray;
	float[] imageButtonABuildingArray;
	float[] imageButtonBBuildingArray;
	float[] imageButtonCBuildingArray;
	float[] imageButtonDBuildingArray;
	float[] imageButtonFBuildingArray;
	float[] imageButtonLBuildingArray;
	float[] imageButtonStudentCenterArray;


	double oldXScale;
	double oldYScale;

	Boolean firstRun;

	
	/**
	 *  MapActivity has an image of GGC that helps users know where they are. 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.map_activity);
		redDot = (ImageView) findViewById(R.id.imageViewTestIcon);
		imageViewBackGround = (ImageView) findViewById(R.id.imageView_ggc_full_map_small);
		imageViewBackGround.setOnTouchListener(new TouchListener());
		setUpGPS();
		setUpImageButtons();
		setImageButtonsLocation();
		oldXScale=1;
		oldXScale=1;
		firstRun =true;
		backGroundImageScaleAndValArray = new double[4];
	}
	
	private void setImageButtonsLocation() {
		imageButtonABuilding.setX(30);
		imageButtonABuilding.setY(410);
		imageButtonBBuilding.setX(140);
		imageButtonBBuilding.setY(30);

		

/*
		imageButtonCBuilding;
		imageButtonDBuilding;
		imageButtonFBuilding;
		imageButtonLBuilding;
		imageButtonStudentCenter;
		*/		
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
		imageButtonStudentCenter = (ImageButton) findViewById(R.id.ImageButtonStudentCenter);
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
			}else  if(view.getId() == R.id.ImageButtonStudentCenter){ 
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
			backGroundImageScaleAndVal(backGroundImageScaleAndValArray,strMatrix);
			if(firstRun == true){
				redDotArray = firstRunDataLog(redDot);
				imageButtonABuildingArray = firstRunDataLog(imageButtonABuilding);
				imageButtonBBuildingArray = firstRunDataLog(imageButtonBBuilding);
				imageButtonCBuildingArray = firstRunDataLog(imageButtonCBuilding);
				imageButtonDBuildingArray = firstRunDataLog(imageButtonDBuilding);
				imageButtonFBuildingArray = firstRunDataLog(imageButtonFBuilding);
				imageButtonLBuildingArray = firstRunDataLog(imageButtonLBuilding);
				imageButtonStudentCenterArray = firstRunDataLog(imageButtonStudentCenter);
				/*
				ImageButton imageButtonABuilding;
				ImageButton imageButtonBBuilding;
				ImageButton imageButtonCBuilding;
				ImageButton imageButtonDBuilding;
				ImageButton imageButtonFBuilding;
				ImageButton imageButtonLBuilding;
				ImageButton imageButtonStudentCenter;
				*/
				
				oldXVal = backGroundImageScaleAndValArray[1];///
				oldYVal = backGroundImageScaleAndValArray[3];
				firstRun = false;
			}
			viewsAutoScaleAndGroup(imageViewBackGround, redDot, redDotArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonABuilding, imageButtonABuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonBBuilding, imageButtonBBuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonCBuilding, imageButtonCBuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonDBuilding, imageButtonDBuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonFBuilding, imageButtonFBuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonLBuilding, imageButtonLBuildingArray);
			viewsAutoScaleAndGroup(imageViewBackGround, imageButtonStudentCenter, imageButtonStudentCenterArray);

			/*
			Log.d("H/W view", view.getWidth()+"=W - H="+view.getHeight());
			Log.d("Height/Width", height +"=W - H="+width);
			Log.d("xScale/yScal", backGroundImageScaleAndValArray[1]+"=W - H="+backGroundImageScaleAndValArray[3]);
			*/
			oldXScale = backGroundImageScaleAndValArray[0];
			oldYScale = backGroundImageScaleAndValArray[2];
			oldXVal = backGroundImageScaleAndValArray[1];
			oldYVal = backGroundImageScaleAndValArray[3];
			return true;
		}
		private void viewsAutoScaleAndGroup( View backGroundView,View childView, float[] viewArray) {
			double changeInX = backGroundImageScaleAndValArray[1] -oldXVal; 
			double changeInY = backGroundImageScaleAndValArray[3] -oldYVal;
			double width = imageViewBackGround.getWidth()*backGroundImageScaleAndValArray[0];
			double height = imageViewBackGround.getHeight()*backGroundImageScaleAndValArray[2];
			if(oldXScale != backGroundImageScaleAndValArray[0] || oldYScale != backGroundImageScaleAndValArray[2]){//0 = viewXOffSet //1 = viewYOffSet // 2 = oldX // 3 = oldY
				childView.setX((float) (backGroundImageScaleAndValArray[1]+(viewArray[0]*width)) );
				childView.setY((float) (backGroundImageScaleAndValArray[3]+(viewArray[1]*height)) );
				childView.setX((float) (childView.getX()+(((childView.getWidth()*backGroundImageScaleAndValArray[0])-childView.getWidth())/2)));
				childView.setY((float) (childView.getY()+(((childView.getWidth()*backGroundImageScaleAndValArray[2])-childView.getHeight())/2)));
				childView.setScaleX((float) backGroundImageScaleAndValArray[0]);
				childView.setScaleY((float) backGroundImageScaleAndValArray[2]);
			}else{
				childView.setX((float) (childView.getX()+(changeInX)));
				childView.setY((float) (childView.getY()+(changeInY)));
			}	
			
			viewArray[2] = childView.getX();
			viewArray[3] = childView.getY();
		}
		/** backGroundImageScaleAndVal
		 *  0 = xSale
		 *  1 = xVale
		 *  2 = yScale
		 *  3 = yVale
		 */
		private void backGroundImageScaleAndVal(double[] backGroundImageScaleAndValArray, String[] strMatrix) {
			backGroundImageScaleAndValArray[0] = Double.parseDouble(strMatrix[1]);
			backGroundImageScaleAndValArray[1] = Double.parseDouble(strMatrix[5]);
			backGroundImageScaleAndValArray[2] = Double.parseDouble(strMatrix[9]);
			backGroundImageScaleAndValArray[3] = Double.parseDouble(strMatrix[11]);
		}

		public float[] firstRunDataLog(View view){
			float[] viewArray = new float[6];//0 = viewXOffSet //1 = viewYOffSet // 2 = X // 3 = Y // 4 = oldX // 5 = oldY
			viewArray[0] = view.getX()/imageViewBackGround.getWidth();
			viewArray[1] = view.getY()/imageViewBackGround.getHeight();		
			viewArray[2] = view.getX();
			viewArray[3] = view.getY();
			viewArray[4] = view.getX();
			viewArray[5] = view.getY();
			
			return viewArray;
		}
	
}
}
