package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import edu.ggc.it.R;

public class MapView extends View {
	Activity activity;
	Context context;
	private ScaleGestureDetector scaleGestureDetector;
	GestureDetector gestureDetector;
	private float newX, newY;
	private float mScaleFactor = 1.f;
	private Bitmap pic;
	public enum State {
		NONE, DRAGGING, ZOOMING
	};

	float oldDistance = 1f;
	double oldXVal;
	double oldYVal;
	double[] matrixDataOfBackground;
	double[] gpsLocation;
	float[] redDotArray;
	double yPixVal;
	double xPixVal;
	double heightAtCurrentScale;
	double widthAtCurrentScale;
	double oldXScale;
	double oldYScale;
	Boolean firstRun;

	public MapView(Context context) {
		super(context);
		this.context = context;
		setDrawingCacheEnabled(true);
		setBackgroundColor(Color.YELLOW);
		pic = BitmapFactory.decodeResource(getResources(),);
		scaleGestureDetector = new ScaleGestureDetector(context,new ScaleListener());
		gestureDetector = new GestureDetector(context, new GestureListener());
		newX = 0;
		newY = 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Both gestureDerector and ScaleGestureDetector are always getting MotionEven
		// this is causing scaling and scrolling to happen at the same time. 
		// TODO find away to fix this problem.

		//if (ev.getAction() == MotionEvent.ACTION_SCROLL) {
			gestureDetector.onTouchEvent(ev);
		//} else {
			scaleGestureDetector.onTouchEvent(ev);
		//}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.scale(mScaleFactor, mScaleFactor);
		canvas.translate(newX, newY);
		canvas.drawBitmap(pic, canvas.getWidth() / 2, canvas.getHeight() / 2,null); // Need some kind of constant
		canvas.restore();
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		
		/*
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		
			Toast.makeText(context, "onFling", 750).show();
			newX = newX+velocityX;
			newY = newY+velocityY;
			invalidate();
			return super.onFling(e1, e2, velocityX, velocityY);
		}
		*/
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			
			//Toast.makeText(context, "onScroll", 750).show();
			newX = newX-distanceX;
			newY = newY-distanceY;
			invalidate();
			return super.onScroll(e1, e2, distanceX, distanceY);
			
		};

		@Override
		public boolean onDown(MotionEvent e) {// worng
			
			//Toast.makeText(context, "onDown", 750).show();

			/*
			final int historySize = e.getHistorySize();
			final int pointerCount = e.getPointerCount();
			float firstX = 0, firstY = 0, newestX = 0, newestY = 0; 
			for (int h = 0; h < historySize; h++) {
				if (h == 0 || h == historySize - 1) {
					for (int p = 0; p < pointerCount; p++) {
					
						if(h == 0){
								firstX = e.getHistoricalX(p,h);
								firstY = e.getHistoricalY(p, h);
						}else if ( h == historySize-1 ){
								newestX = e.getHistoricalX(p,h);
								newestY = e.getHistoricalY(p,h);	
						}
						
					}
				}
				
			}
			
			newX = newX +(newestX - firstX);
			newY = newY +(newestY - firstY);
			
			Toast.makeText(context, "onDown", 500).show();
			Log.d("Test", "onDown");

			invalidate();
			*/
			return super.onDown(e);
		}
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

	/**
	 * MapActivity has an image of GGC that helps users know where they are.
	 */

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

}
