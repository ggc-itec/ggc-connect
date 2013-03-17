package edu.ggc.it.map;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
	private Bitmap pic;
	private Bitmap aBuilding;
	private Bitmap bBuilding;
	private Bitmap cBuilding;
	private Bitmap dBuilding;
	private Bitmap fBuilding;
	private Bitmap lBuilding;
	private Bitmap sBuilding;
	private Bitmap redDot;
	private ArrayList<Float> canvasX;
	private ArrayList<Float> canvasY;
	private float newX, newY;
	private float mScaleFactor = 0.25f; //1.f
	private float scaledHeight;
	private float scaledWidth;
	private float redDotX = (float)(scaledWidth*(700/1400.0));
	private float redDotY = (float)(scaledHeight*(560/1120.0)) ;
	private boolean firstRun = true;



	public MapView(Context context) {
		super(context);
		this.context = context;
		setDrawingCacheEnabled(true);
		pic = BitmapFactory.decodeResource(getResources(),R.drawable.ggc_map);
		aBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		bBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		cBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		dBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		fBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		lBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		sBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);

		scaleGestureDetector = new ScaleGestureDetector(context,new ScaleListener());
		gestureDetector = new GestureDetector(context, new GestureListener());
		newX = 0;
		newY = 0;
		canvasX = new ArrayList<Float>();
		canvasY = new ArrayList<Float>();
		setCanvasXY(newX, newY);
	}
	/*
	 * The setCanvasXY and getCanvasX/ getCanvasY where made to help keep track of where
	 * start of background image is.
	 * 
	 */
	private void setCanvasXY(float x, float y){
		canvasX.add(x);
		canvasY.add(y);	
	}
	
	private float getCanvasX(){
		float xSum = 0;
		for(float f: canvasX){
			xSum+=f;
		}
		return xSum;
	}
	
	private float getCanvasY(){
		float ySum = 0;
		for(float f: canvasY){
			ySum+=f;
		}
		return ySum;
	}
	
	public void setRedDotXY(float metersLatiOffSet, float metersLongOffSet){
		redDot = null;
		redDot = BitmapFactory.decodeResource(getResources(), R.drawable.red_dot);
		
		float pixPerMLati = scaledWidth/1700;
		float pixPerMLon = scaledHeight/1000;
		
		redDotX = (pixPerMLati*metersLatiOffSet)+(scaledWidth*(510/1400));
		redDotY = (pixPerMLon*metersLongOffSet)+(scaledHeight*(513/1120));
		
		scaleReferenceHashMap.remove("redDot_X");
		scaleReferenceHashMap.remove("redDot_Y");
		makeRedDot();
		invalidate();
	}
	
	private void makeScaleReferenceHashMap(){
		scaleReferenceHashMap = new HashMap<String,Float>();
		
		scaleReferenceHashMap.put("A_BUILDING_X",(float) ( (scaledWidth*(952/1400.0)) -(aBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("A_BUILDING_Y",(float) ( (scaledHeight*(568/1120.0)) -(aBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("B_BUILDING_X",(float)(((pic.getWidth()/2)-bBuilding.getWidth()/2)-(pic.getWidth()/14)));
		scaleReferenceHashMap.put("B_BUILDING_Y",(float)(((pic.getHeight()/2)-bBuilding.getHeight()/2)-(pic.getHeight()/14)));
		
		scaleReferenceHashMap.put("C_BUILDING_X",(float) ( (scaledWidth*(510/1400.0)) -(cBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("C_BUILDING_Y",(float) ( (scaledHeight*(550/1120.0)) -(cBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("D_BUILDING_X",(float) ( (scaledWidth*(1165/1400.0)) -(dBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("D_BUILDING_Y",(float) ( (scaledHeight*(611/1120.0)) -(dBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("F_BUILDING_X",(float) ( (scaledWidth*(1094/1400.0)) -(fBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("F_BUILDING_Y",(float) ( (scaledHeight*(536/1120.0)) -(fBuilding.getHeight()/2)));
		
		scaleReferenceHashMap.put("L_BUILDING_X",(float) ( (scaledWidth*(670/1400.0)) -(lBuilding.getWidth()/2)));
		scaleReferenceHashMap.put("L_BUILDING_Y",(float) ( (scaledHeight*(623/1120.0)) -(lBuilding.getHeight()/2)));
		
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
		   float xOffSet = getCanvasX();
		   float yOffSet = getCanvasY();
		   methodBasedButtonActionListener(xOffSet,yOffSet, touchX, touchY);
		}

		if (ev.getPointerCount() == 1) {
		    gestureDetector.onTouchEvent(ev);
		} else {
			scaleGestureDetector.onTouchEvent(ev);
		}
		return true;
	}

	private void methodBasedButtonActionListener(float xOffSet, float yOffSet, float touchX, float touchY) {
		   ///A
		   float aBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_X")*mScaleFactor);
		   float aBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_X")+aBuilding.getWidth())*mScaleFactor);
				   
		   float aBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_Y")*mScaleFactor);
		   float aBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_Y")+aBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= aBuildingXMin && touchX <= aBuildingXMax){   
			   if(touchY >= aBuildingYMin && touchY <= aBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchABuildingActivity.class);
				   context.startActivity(i);		
			   }
		   }
		   ///B
		   float bBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("B_BUILDING_X")*mScaleFactor);
		   float bBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("B_BUILDING_X")+bBuilding.getWidth())*mScaleFactor);
				   
		   float bBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("B_BUILDING_Y")*mScaleFactor);
		   float bBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("B_BUILDING_Y")+bBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= bBuildingXMin && touchX <= bBuildingXMax){   
			   if(touchY >= bBuildingYMin && touchY <= bBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchBBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }	
		   ///C
		   float cBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("C_BUILDING_X")*mScaleFactor);
		   float cBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("C_BUILDING_X")+cBuilding.getWidth())*mScaleFactor);
				   
		   float cBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("C_BUILDING_Y")*mScaleFactor);
		   float cBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("C_BUILDING_Y")+cBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= cBuildingXMin && touchX <= cBuildingXMax){   
			   if(touchY >= cBuildingYMin && touchY <= cBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchCBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///D
		   float dBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("D_BUILDING_X")*mScaleFactor);
		   float dBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("D_BUILDING_X")+dBuilding.getWidth())*mScaleFactor);
				   
		   float dBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("D_BUILDING_Y")*mScaleFactor);
		   float dBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("D_BUILDING_Y")+dBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= dBuildingXMin && touchX <= dBuildingXMax){   
			   if(touchY >= dBuildingYMin && touchY <= dBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchDBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///F
		   float fBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("F_BUILDING_X")*mScaleFactor);
		   float fBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("F_BUILDING_X")+fBuilding.getWidth())*mScaleFactor);
				   
		   float fBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("F_BUILDING_Y")*mScaleFactor);
		   float fBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("F_BUILDING_Y")+fBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= fBuildingXMin && touchX <= fBuildingXMax){   
			   if(touchY >= fBuildingYMin && touchY <= fBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchFBuildingActivity.class);
				   context.startActivity(i);
			   }
		   }
		   ///L
		   float lBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("L_BUILDING_X")*mScaleFactor);
		   float lBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("L_BUILDING_X")+lBuilding.getWidth())*mScaleFactor);
				   
		   float lBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("L_BUILDING_Y")*mScaleFactor);
		   float lBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("L_BUILDING_Y")+lBuilding.getWidth())*mScaleFactor);
				   
		   if(touchX >= lBuildingXMin && touchX <= lBuildingXMax){   
			   if(touchY >= lBuildingYMin && touchY <= lBuildingYMax){
				   Context context = getContext();
				   Intent i = new Intent(context, ImageTouchLBuildingActivity.class);
				   context.startActivity(i);
				}
		   }
		   ///S
		   float sBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("S_BUILDING_X")*mScaleFactor);
		   float sBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("S_BUILDING_X")+sBuilding.getWidth())*mScaleFactor);
				   
		   float sBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("S_BUILDING_Y")*mScaleFactor);
		   float sBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("S_BUILDING_Y")+sBuilding.getWidth())*mScaleFactor);
				   
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
		canvas.translate(newX, newY);
		canvas.drawBitmap(pic, 0, 0,null); 	
		
		
		scaledWidth = pic.getScaledWidth(canvas);
		scaledHeight = pic.getScaledHeight(canvas);
		
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
			newX = newX-distanceX;
			newY = newY-distanceY;
			setCanvasXY(-distanceX, -distanceY);// scrolling and zooming are throwing off canvasXY data
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
