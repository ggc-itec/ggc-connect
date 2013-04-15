package edu.ggc.it.map;

import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import edu.ggc.it.R;

public class MapView extends View {
	Activity activity;
	Context context;
	private ScaleGestureDetector scaleGestureDetector;
	GestureDetector gestureDetector;
	HashMap<String, Float> scaleReferenceHashMap;
	private Bitmap backGroundImage;
	private Bitmap aBuilding;
	private Bitmap bBuilding;
	private Bitmap cBuilding;
	private Bitmap dBuilding;
	private Bitmap fBuilding;
	private Bitmap lBuilding;
	private Bitmap sBuilding;
	private Bitmap redDot;
	private float canvasX, canvasY;
	private float mScaleFactor = 0.25f; //1.f
	private float scaledHeight;
	private float scaledWidth;
	private float redDotX = (float)(scaledWidth*(700/1400.0));
	private float redDotY = (float)(scaledHeight*(560/1120.0)) ;
	private boolean firstRun = true;


/**
 * MapView extends View and is a class that is intend to show a map background then when provided with GPS data, in the from of meters off set from the 
 * center of the map, show the current location of the device. MapView also has custom buttons and is able to be scaled and scrolled with out changing the
 * relative position of all of images/buttons. MapView is designed to be used in conjunction with {@link MapActivity} which provides the meters off set 
 * that this class uses to draw the redDot location icon in the appropriate place. The drawing for this class is handle in the in onDraw method however there
 * are other methods that are used to organize, maintain, and update the relative positions of the images/buttons.
 * @author Andrew F. Lynch
 * @param context
 */
	public MapView(Context context) {
		super(context);
		this.context = context;
		setDrawingCacheEnabled(true);
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inScaled = false;
		// set up all of the images with the exception of the redDot image. The redDot image gets initialized the first time setRedDotXY() is called.
		backGroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.map_map, options);
		aBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_a_button, options);
		bBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_b_button, options);
		cBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_c_button, options);
		dBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_d_button, options);
		fBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_f_button, options);
		lBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_l_button, options);
		sBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.map_student_center_button, options);
		scaleGestureDetector = new ScaleGestureDetector(context,new ScaleListener());
		gestureDetector = new GestureDetector(context, new GestureListener());
		// sets canvasX and canvasY to the starting point of the background image.
		setCanvasXY(0, 0);	
	}

	/*
	 * The setCanvasXY and getCanvasX/getCanvasY where made to help keep track of where
	 * start of background image is.
	 * 
	 */
	private void setCanvasXY(float x, float y){
		canvasX = x;
		canvasY = y;
	}
	
	/**
	 * This method changes the relative position of the redDot also the first this method is run it initializes redDot.
	 * @param metersLongOffSet
	 * @param metersLatiOffSet
	 */
	public void setRedDotXY(float metersLongOffSet, float  metersLatiOffSet){
		redDot = null;
		redDot = BitmapFactory.decodeResource(getResources(), R.drawable.red_dot);
	
		float pixPerMLon = (float) (scaledWidth/1700.0);
		float pixPerMLati = (float) (scaledHeight/1000.0);
		
		redDotX = (float)   ((pixPerMLon*metersLongOffSet)+(scaledWidth*(796.5/1400.0)));//X
		redDotY = (float)   ((pixPerMLati*metersLatiOffSet)+(scaledHeight*(580.5/1120.0)));
		Log.d("MapView", "mLatOff "+ metersLatiOffSet+" mLongOff "+ metersLongOffSet+ "pixPerMLati "+ pixPerMLati+" pixPerMLong "+ pixPerMLon);
		
		if (scaleReferenceHashMap.containsKey("redDot_X")){
			scaleReferenceHashMap.remove("redDot_X");
			scaleReferenceHashMap.remove("redDot_Y");
		}
		makeRedDot();
		invalidate();
	}
	
	private void makeScaleReferenceHashMap(){
		scaleReferenceHashMap = new HashMap<String,Float>();
		
		scaleReferenceHashMap.put("A_BUILDING_X",(float) ( (scaledWidth*(951/1400.0)) -(aBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("A_BUILDING_Y",(float) ( (scaledHeight*(560/1120.0)) -(aBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("B_BUILDING_X",(float) ( (scaledWidth*(586/1400.0)) -(aBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("B_BUILDING_Y",(float) ( (scaledHeight*(480/1120.0)) -(aBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("C_BUILDING_X",(float) ( (scaledWidth*(510/1400.0)) -(cBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("C_BUILDING_Y",(float) ( (scaledHeight*(540/1120.0)) -(cBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("D_BUILDING_X",(float) ( (scaledWidth*(1150/1400.0)) -(dBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("D_BUILDING_Y",(float) ( (scaledHeight*(600/1120.0)) -(dBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("F_BUILDING_X",(float) ( (scaledWidth*(1035/1400.0)) -(fBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("F_BUILDING_Y",(float) ( (scaledHeight*(820/1120.0)) -(fBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("L_BUILDING_X",(float) ( (scaledWidth*(670/1400.0)) -(lBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("L_BUILDING_Y",(float) ( (scaledHeight*(613/1120.0)) -(lBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("S_BUILDING_X",(float) ( (scaledWidth*(567/1400.0)) -(sBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("S_BUILDING_Y",(float) ( (scaledHeight*(640/1120.0)) -(sBuilding.getHeight()/2)));
		
	}
	
	private void makeRedDot(){// needs to be cleared from the HashMap
		scaleReferenceHashMap.put("redDot_X",(float) ((redDotX) -(redDot.getWidth()/2)));
		scaleReferenceHashMap.put("redDot_Y",(float) ((redDotY) -(redDot.getHeight()/2)));
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Both gestureDerector and ScaleGestureDetector are always getting MotionEven
		// this is causing scaling and scrolling to happen at the same time. 
		// TODO find away to fix this problem.
		
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
		   float touchX = ev.getX();
		   float touchY = ev.getY();  
		   methodBasedButtonActionListener(canvasX,canvasY, touchX, touchY);
		}

		if (ev.getPointerCount() == 1) {
		    gestureDetector.onTouchEvent(ev);
		} else {
			scaleGestureDetector.onTouchEvent(ev);
		}
		return true;
	}

	private void methodBasedButtonActionListener(float newX, float newY, float touchX, float touchY) {
		   ///A
		   float aBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_X")*mScaleFactor);
		   float aBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_X")+aBuilding.getWidth())*mScaleFactor);
				   
		   float aBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_Y")*mScaleFactor);
		   float aBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_Y")+aBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= aBuildingXMin && touchX <= aBuildingXMax){   
			   if(touchY >= aBuildingYMin && touchY <= aBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchABuildingActivity.class);
				   context.startActivity(i);		
			   }
		   }
		   ///B
		   float bBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("B_BUILDING_X")*mScaleFactor);
		   float bBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("B_BUILDING_X")+bBuilding.getWidth())*mScaleFactor);
				   
		   float bBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("B_BUILDING_Y")*mScaleFactor);
		   float bBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("B_BUILDING_Y")+bBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= bBuildingXMin && touchX <= bBuildingXMax){   
			   if(touchY >= bBuildingYMin && touchY <= bBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchBBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }	
		   ///C
		   float cBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("C_BUILDING_X")*mScaleFactor);
		   float cBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("C_BUILDING_X")+cBuilding.getWidth())*mScaleFactor);
				   
		   float cBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("C_BUILDING_Y")*mScaleFactor);
		   float cBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("C_BUILDING_Y")+cBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= cBuildingXMin && touchX <= cBuildingXMax){   
			   if(touchY >= cBuildingYMin && touchY <= cBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchCBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///D
		   float dBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("D_BUILDING_X")*mScaleFactor);
		   float dBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("D_BUILDING_X")+dBuilding.getWidth())*mScaleFactor);
				   
		   float dBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("D_BUILDING_Y")*mScaleFactor);
		   float dBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("D_BUILDING_Y")+dBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= dBuildingXMin && touchX <= dBuildingXMax){   
			   if(touchY >= dBuildingYMin && touchY <= dBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchDBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///F
		   float fBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("F_BUILDING_X")*mScaleFactor);
		   float fBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("F_BUILDING_X")+fBuilding.getWidth())*mScaleFactor);
				   
		   float fBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("F_BUILDING_Y")*mScaleFactor);
		   float fBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("F_BUILDING_Y")+fBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= fBuildingXMin && touchX <= fBuildingXMax){   
			   if(touchY >= fBuildingYMin && touchY <= fBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchFBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///L
		   float lBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("L_BUILDING_X")*mScaleFactor);
		   float lBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("L_BUILDING_X")+lBuilding.getWidth())*mScaleFactor);
				   
		   float lBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("L_BUILDING_Y")*mScaleFactor);
		   float lBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("L_BUILDING_Y")+lBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= lBuildingXMin && touchX <= lBuildingXMax){   
			   if(touchY >= lBuildingYMin && touchY <= lBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchLBuildingActivity.class);
				   context.startActivity(i);
				}
		   }
		   ///S
		   float sBuildingXMin = (newX*mScaleFactor)+(scaleReferenceHashMap.get("S_BUILDING_X")*mScaleFactor);
		   float sBuildingXMax = (newX*mScaleFactor)+((scaleReferenceHashMap.get("S_BUILDING_X")+sBuilding.getWidth())*mScaleFactor);
				   
		   float sBuildingYMin = (newY*mScaleFactor)+(scaleReferenceHashMap.get("S_BUILDING_Y")*mScaleFactor);
		   float sBuildingYMax = (newY*mScaleFactor)+((scaleReferenceHashMap.get("S_BUILDING_Y")+sBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= sBuildingXMin && touchX <= sBuildingXMax){   
			   if(touchY >= sBuildingYMin && touchY <= sBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchStudentCenterActivity.class);
				   context.startActivity(i);
				}
		   }
		   
	}
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(canvasX, canvasY);
		canvas.drawBitmap(backGroundImage, 0, 0,null); 	
		
		
		scaledWidth = backGroundImage.getScaledWidth(canvas);
		scaledHeight = backGroundImage.getScaledHeight(canvas);
		
		/*
		scaledWidth = canvas.getWidth();
		scaledHeight = canvas.getHeight();
		*/
		
		if(firstRun == true){
			makeScaleReferenceHashMap();
			firstRun = false;
		}
		canvas.drawBitmap(aBuilding,scaleReferenceHashMap.get("A_BUILDING_X"),scaleReferenceHashMap.get("A_BUILDING_Y"),null);
		canvas.drawBitmap(bBuilding,scaleReferenceHashMap.get("B_BUILDING_X"),scaleReferenceHashMap.get("B_BUILDING_Y"),null);
		canvas.drawBitmap(cBuilding,scaleReferenceHashMap.get("C_BUILDING_X"),scaleReferenceHashMap.get("C_BUILDING_Y"),null);
		canvas.drawBitmap(dBuilding,scaleReferenceHashMap.get("D_BUILDING_X"),scaleReferenceHashMap.get("D_BUILDING_Y"),null);
		canvas.drawBitmap(fBuilding,scaleReferenceHashMap.get("F_BUILDING_X"),scaleReferenceHashMap.get("F_BUILDING_Y"),null);
		canvas.drawBitmap(lBuilding,scaleReferenceHashMap.get("L_BUILDING_X"),scaleReferenceHashMap.get("L_BUILDING_Y"),null);
		canvas.drawBitmap(sBuilding,scaleReferenceHashMap.get("S_BUILDING_X"),scaleReferenceHashMap.get("S_BUILDING_Y"),null);
		if (redDot != null){
			canvas.drawBitmap(redDot,scaleReferenceHashMap.get("redDot_X"),scaleReferenceHashMap.get("redDot_Y"),null);
		}
		canvas.restore();
	}
	

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			canvasX = canvasX-distanceX;
			canvasY = canvasY-distanceY;
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
		};

	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			// Don't let the object get too small or too large.
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
			invalidate();
			return true;
		}
	}

}
