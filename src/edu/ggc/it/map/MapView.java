package edu.ggc.it.map;

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
import edu.ggc.it.R;

public class MapView extends View {
	Activity activity;
	
	private ScaleGestureDetector scaleGestureDetector;
	private GestureDetector gestureDetector;
	private float newX, newY;
	private float mScaleFactor = 1.f;

	public MapView(Context context){
		super(context);
		setDrawingCacheEnabled(true);	
		setBackgroundColor(Color.YELLOW);
	    scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
	    // GestureDetector.SimpleOnGestureListener
	    gestureDetector = new GestureDetector(context,  new GestureListener());
	    newX = 0;
	    newY = 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
	    scaleGestureDetector.onTouchEvent(ev);
	    return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);

	    canvas.save();
	    canvas.scale(mScaleFactor, mScaleFactor);
	    canvas.translate(newX, newY);
		Bitmap pic = BitmapFactory.decodeResource(getResources(), R.drawable.ggc_building_b_map);
		canvas.drawBitmap(pic, canvas.getWidth()/2, canvas.getHeight()/2, null); 
	    canvas.restore();
	}
	
	private class GestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onDown(MotionEvent e) {// worng
			
			// google how to use gestureDetector onDown
	        
	        newY = e.getY();

	        // Don't let the object get too small or too large.
	        //mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

	        invalidate();
			
			
			
			return super.onDown(e);	
		}
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
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
