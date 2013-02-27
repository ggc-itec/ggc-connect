package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
	double[] matrixDataOfBackground;
	double[] gpsLocation;
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
		redDot = (ImageView) findViewById(R.id.imageView_Red_Dot);
		imageViewBackGround = (ImageView) findViewById(R.id.imageView_GGC_Full_Map_Small);
		imageViewBackGround.setOnTouchListener(new TouchListener());
		setUpGPS();
		setUpImageButtons();
		setImageButtonsLocation();
		oldXScale=1;
		oldXScale=1;
		firstRun =true;
		matrixDataOfBackground = new double[4];
		gpsLocation = new double[2];
		gpsLocation[0] = 0.0;
		gpsLocation[1] = 0.0;
	}
	
	private void setImageButtonsLocation() {
		imageButtonABuilding.setX(400);
		imageButtonABuilding.setY(750);
		imageButtonBBuilding.setX(500);
		imageButtonBBuilding.setY(460);
		imageButtonCBuilding.setX(450);
		imageButtonCBuilding.setY(400);
		imageButtonStudentCenter.setX(360);
		imageButtonStudentCenter.setY(423);
		imageButtonLBuilding.setX(390);
		imageButtonLBuilding.setY(525);
		imageButtonFBuilding.setX(450);
		imageButtonFBuilding.setY(875);
		imageButtonDBuilding.setX(400);
		imageButtonDBuilding.setY(950);	
		redDot.setX(-15);
		redDot.setY(-14);
	}

	/**
	 *  setUpImageButtons sets up all of the ImageButtons, sets their background color to TRANSPARENT, and adds and {@link GGCOnClickListener} to each
	 *  of the ImageButtons. 
	 */
	private void setUpImageButtons() {
		
		GGCOnClickListener ggcOnClickListener = new GGCOnClickListener();	
		//A
		imageButtonABuilding = (ImageButton) findViewById(R.id.imageButtonABulling);
		//imageButtonABuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonABuilding.setOnClickListener(ggcOnClickListener);
		//B
		imageButtonBBuilding = (ImageButton) findViewById(R.id.imageButtonBBuilding);
		//imageButtonBBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonBBuilding.setOnClickListener(ggcOnClickListener);
		//C
		imageButtonCBuilding = (ImageButton) findViewById(R.id.imageButtonCBuilding);
		//imageButtonCBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonCBuilding.setOnClickListener(ggcOnClickListener);
		//D
		imageButtonDBuilding = (ImageButton) findViewById(R.id.imageButtonDBuilding);
		//imageButtonDBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonDBuilding.setOnClickListener(ggcOnClickListener);
		//F
		imageButtonFBuilding = (ImageButton) findViewById(R.id.imageButtonFBuilding);
		//imageButtonFBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonFBuilding.setOnClickListener(ggcOnClickListener);
		//L
		imageButtonLBuilding = (ImageButton) findViewById(R.id.imageButtonLBuilding);
		//imageButtonLBuilding.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonLBuilding.setOnClickListener(ggcOnClickListener);
		//Student Center
		imageButtonStudentCenter = (ImageButton) findViewById(R.id.imageButtonStudentCenter);
		//imageButtonStudentCenter.setBackgroundColor(Color.TRANSPARENT);	
		imageButtonStudentCenter.setOnClickListener(ggcOnClickListener);
	}// TODO Go back and have all of the setBackgroundColor s in an method so they can turn on and off.
	
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
			if(view.getId() == R.id.imageButtonABulling){ 
				startActivity(new Intent(context, ImageTouchABuildingActivity.class));
			}else if(view.getId() == R.id.imageButtonBBuilding ){ 
				startActivity(new Intent(context, ImageTouchBBuildingActivity.class));
			}else if(view.getId() == R.id.imageButtonCBuilding ){ 
				startActivity(new Intent(context, ImageTouchCBuildingActivity.class));
			}else  if(view.getId() == R.id.imageButtonDBuilding ){ 
				startActivity(new Intent(context, ImageTouchDBuildingActivity.class));
			}else  if(view.getId() == R.id.imageButtonFBuilding ){ 
				startActivity(new Intent(context, ImageTouchFBuildingActivity.class));
			}else  if(view.getId() == R.id.imageButtonLBuilding ){ 
				startActivity(new Intent(context, ImageTouchLBuildingActivity.class));
			}else  if(view.getId() == R.id.imageButtonStudentCenter){ 
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
			lat = lat.substring(0,7);
			gpsLocation[0] = Double.parseDouble(lat);
			String lon = longitude+"";
			lon = lon.substring(0,7);
			gpsLocation[1] = Double.parseDouble(lon);
			/*
			if (Double.parseDouble(lat) == 33.98 && Double.parseDouble(lon) == -84.00  ) {
				redDot.setX(50);
				redDot.setY(50);
			}
			*/
			Toast.makeText(context, "GPS lati " +gpsLocation[0]+" long "+ gpsLocation[1] , Toast.LENGTH_LONG).show();
			
			Log.d("GPS Data", "GPS latitude=" +latitude+ " longitude="+longitude+" latSubstring="+lat+" lonSubstring="+lon+ " X = "+imageViewBackGround.getWidth()+" Y = "+ imageViewBackGround.getHeight());
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
			backGroundImageScaleAndVal(matrixDataOfBackground,strMatrix);
			if(firstRun == true){
				redDotArray = firstRunDataLog(redDot);
				imageButtonABuildingArray = firstRunDataLog(imageButtonABuilding);
				imageButtonBBuildingArray = firstRunDataLog(imageButtonBBuilding);
				imageButtonCBuildingArray = firstRunDataLog(imageButtonCBuilding);
				imageButtonDBuildingArray = firstRunDataLog(imageButtonDBuilding);
				imageButtonFBuildingArray = firstRunDataLog(imageButtonFBuilding);
				imageButtonLBuildingArray = firstRunDataLog(imageButtonLBuilding);
				imageButtonStudentCenterArray = firstRunDataLog(imageButtonStudentCenter);
				
				oldXVal = matrixDataOfBackground[1];// current x val
				oldYVal = matrixDataOfBackground[3];// current y val
				firstRun = false;
			}
			viewsAutoScaleAndGroup( redDot, redDotArray);
			viewsAutoScaleAndGroup( imageButtonABuilding, imageButtonABuildingArray);
			viewsAutoScaleAndGroup( imageButtonBBuilding, imageButtonBBuildingArray);
			viewsAutoScaleAndGroup( imageButtonCBuilding, imageButtonCBuildingArray);
			viewsAutoScaleAndGroup( imageButtonDBuilding, imageButtonDBuildingArray);
			viewsAutoScaleAndGroup( imageButtonFBuilding, imageButtonFBuildingArray);
			viewsAutoScaleAndGroup( imageButtonLBuilding, imageButtonLBuildingArray);
			viewsAutoScaleAndGroup( imageButtonStudentCenter, imageButtonStudentCenterArray);

			/*
			Log.d("H/W view", view.getWidth()+"=W - H="+view.getHeight());
			Log.d("Height/Width", height +"=W - H="+width);
			Log.d("xScale/yScal", backGroundImageScaleAndValArray[1]+"=W - H="+backGroundImageScaleAndValArray[3]);
			*/
			oldXScale = matrixDataOfBackground[0];
			oldYScale = matrixDataOfBackground[2];
			oldXVal = matrixDataOfBackground[1];
			oldYVal = matrixDataOfBackground[3];
			return true;
		}
		private void viewsAutoScaleAndGroup( View childView, float[] viewArray) {
			double changeInX = matrixDataOfBackground[1] -oldXVal; // baseImage + baseImage Martix[] = scale groups
			double changeInY = matrixDataOfBackground[3] -oldYVal;
			double width = imageViewBackGround.getWidth()*matrixDataOfBackground[0];
			double height = imageViewBackGround.getHeight()*matrixDataOfBackground[2];
			
			//this is where the GPS data needs to enter-face with the view.
			if(childView.equals(redDot)){
				Log.d("childView", childView.toString() + " "+ childView.getId() + " "+childView );
				// xOffSet viewArray[0] =
				// yOffSet viewArray[1] =
				// x)Offset = view.getX()/backGround.getWidth(); 
				// view.getX() = 
				
				double pixPerSecLon = 44.6973;
				double pixPerSecLat = 44.6961;
				double pixPerMeterLon = imageViewBackGround.getWidth()/1000; //p/m
				double pixPerMeterLat = imageViewBackGround.getHeight()/1700;// p/m
				double xOfCBuildingPix = imageViewBackGround.getWidth()*0.54342; 
				double yOfCBuildingPix = imageViewBackGround.getHeight()*0.36336;
				double cBuildingLat = 33.980; // lat = x
				double cBuildingLon = -84.006;// lon = y
				double locationX = xOfCBuildingPix+(((gpsLocation[0] - cBuildingLat)*10000)* pixPerMeterLat);// answer in Pix 
				double locationY = yOfCBuildingPix+(((gpsLocation[1] - cBuildingLon)*10000)* pixPerMeterLon);// answer in Pix 
									//cBuilding				 GPS                       M		 Pix
				double xPixVal = locationX;
				double yPixVal = locationY;
				
				Log.d("Test", "locationX="+ locationX + " locationY="+locationY +" xPixVal="+xPixVal +" yPixVal="+yPixVal);

				viewArray[0] = (float) (xPixVal/imageViewBackGround.getWidth()); // Scale conversion may taken out after testing 
				viewArray[1] = (float) (yPixVal/imageViewBackGround.getHeight());
			}
			
			if(oldXScale != matrixDataOfBackground[0] || oldYScale != matrixDataOfBackground[2]){//0 = viewXOffSet //1 = viewYOffSet // 2 = oldX // 3 = oldY
				childView.setX((float) (matrixDataOfBackground[1]+(viewArray[0]*width)) );
				childView.setY((float) (matrixDataOfBackground[3]+(viewArray[1]*height)) );
				childView.setX((float) (childView.getX()+(((childView.getWidth()*matrixDataOfBackground[0])-childView.getWidth())/2)));
				childView.setY((float) (childView.getY()+(((childView.getWidth()*matrixDataOfBackground[2])-childView.getHeight())/2)));
				childView.setScaleX((float) matrixDataOfBackground[0]);
				childView.setScaleY((float) matrixDataOfBackground[2]);
			}else{
				childView.setX((float) (childView.getX()+(changeInX)));
				childView.setY((float) (childView.getY()+(changeInY)));
			}	
			
			viewArray[2] = childView.getX();
			viewArray[3] = childView.getY();
		}
		/** backGroundImageScaleAndVal
		 *  0 = xScale
		 *  1 = xVale
		 *  2 = yScale
		 *  3 = yVale
		 */
		private void backGroundImageScaleAndVal(double[] backGroundImageScaleAndValArray, String[] strMatrix) {
			backGroundImageScaleAndValArray[0] = Double.parseDouble(strMatrix[1]); // 0 = xScale
			backGroundImageScaleAndValArray[1] = Double.parseDouble(strMatrix[5]); // 1 = xVale
			backGroundImageScaleAndValArray[2] = Double.parseDouble(strMatrix[9]); // 2 = yScale
			backGroundImageScaleAndValArray[3] = Double.parseDouble(strMatrix[11]);// 3 = yVale
		}

		public float[] firstRunDataLog(View view){
			float[] viewArray = new float[6];
			viewArray[0] = view.getX()/imageViewBackGround.getWidth(); //0 = viewXOffSet 
			viewArray[1] = view.getY()/imageViewBackGround.getHeight(); //1 = viewYOffSet 
			viewArray[2] = view.getX();// 2 = X 	
			viewArray[3] = view.getY();// 3 = Y 
			viewArray[4] = view.getX();// 4 = oldX 
			viewArray[5] = view.getY();// 5 = oldY
			
			return viewArray;
		}
	
}
}
