package edu.ggc.it.map;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;
import edu.ggc.it.R;

public class MapView extends View {
	Activity activity;
	Context context;
	private ScaleGestureDetector scaleGestureDetector;
	GestureDetector gestureDetector;
	HashMap<String, Float> scaleReferenceHashMap;
	private Bitmap pic;
	private Bitmap aBuilding;
	private ArrayList<Float> canvasX;
	private ArrayList<Float> canvasY;
	private float newX, newY;
	private float mScaleFactor = 0.25f; //1.f


	public MapView(Context context) {
		super(context);
		this.context = context;
		setDrawingCacheEnabled(true);
		setBackgroundColor(Color.YELLOW);
		pic = BitmapFactory.decodeResource(getResources(),R.drawable.ggc_map);
		aBuilding = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_scale_gray_button);
		makeScaleReferenceHashMap();
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
	
	private void makeScaleReferenceHashMap(){
		scaleReferenceHashMap = new HashMap<String,Float>();
		scaleReferenceHashMap.put("A_BUILDING_X",(float)(((pic.getWidth()/2)-aBuilding.getWidth()/2)-(pic.getWidth()/14)));
		scaleReferenceHashMap.put("A_BUILDING_Y",(float)(((pic.getHeight()/2)-aBuilding.getHeight()/2)-(pic.getHeight()/14)) );
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
		   
		   float aBuildingXMin = (xOffSet*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_X")*mScaleFactor);
		   float aBuildingXMax = (xOffSet*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_X")+aBuilding.getWidth())*mScaleFactor);
				   
		   float aBuildingYMin = (yOffSet*mScaleFactor)+(scaleReferenceHashMap.get("A_BUILDING_Y")*mScaleFactor);
		   float aBuildingYMax = (yOffSet*mScaleFactor)+((scaleReferenceHashMap.get("A_BUILDING_Y")+aBuilding.getWidth())*mScaleFactor);
				   

		   if(touchX >= aBuildingXMin && touchX <= aBuildingXMax){   
			   if(touchY >= aBuildingYMin && touchY <= aBuildingYMax){
				   Toast.makeText(context, touchX+" "+touchY+"\n"+ aBuildingXMin+"~~"+aBuildingYMin, 1000).show();
			   }
		   }
		   

		}

		if (ev.getPointerCount() == 1) {
		    gestureDetector.onTouchEvent(ev);
		} else {
			scaleGestureDetector.onTouchEvent(ev);
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(newX, newY);
		canvas.drawBitmap(pic, 0, 0,null); 
		// need to set up scaling data bank
		canvas.drawBitmap(aBuilding,scaleReferenceHashMap.get("A_BUILDING_X"),scaleReferenceHashMap.get("A_BUILDING_Y"),null);
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
