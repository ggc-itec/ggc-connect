package edu.ggc.it.direction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import edu.ggc.it.R;
/**
 * This class is used to override the imageview to make it draggable and zoomable
 * @author From the internet
 *
 */
public class TouchImageView extends ImageView {
	// Longitude of destination that user wants to hit
	double longitudeDes;
	// Latitude of destination that user wants to hit
    double latitudeDes;
	// Longitude where user current at
    double longitudeUser;
    // Latitude where user current at
	double latitudeUser;

    Matrix matrix;
    // We can be in one of these 3 states
    final int NONE = 0;
    final int DRAG = 1;
    final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 3f;
    float[] m;

    int viewWidth, viewHeight;
    final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;
    Context context;
    
    Bitmap mUser;
    Bitmap mMarker;
    

    /**
     * Constructor to create a new touchimageview with given context
     * @param context
     */
    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
        mMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.here);
        mUser = BitmapFactory.decodeResource(context.getResources(), R.drawable.you);
    }
    
    /**
     * Constructor to create a new touchimageview with given context and attribute
     * @param context
     */
    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
        mMarker = BitmapFactory.decodeResource(context.getResources(), R.drawable.here);
        mUser = BitmapFactory.decodeResource(context.getResources(), R.drawable.you);
    }
    /**
     * Get and define the constructing of image
     * @param context
     */
    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {
            /**
             * @Override: to clarify actions of users
             */
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                PointF curr = new PointF(event.getX(), event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	last.set(curr);
                        start.set(last);
                        mode = DRAG;
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK)
                            performClick();
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }
                setImageMatrix(matrix);
                invalidate();
                
                return true; // indicate event was handled
            }
        });
    }
    
    
    /**
     * This routine is used to set the maximum size of image that can be zoom in
     * @param x
     */
    public void setMaxZoom(float x) {
        maxScale = x;
    }
    
    public void setOriginalSize() {
    	saveScale = 1f;
    }
    
    /**
     * Listener to get information when users do zoom in or out
     *
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        /**
         * @Override
         * when users zoom the map
         */
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

    /**
     * define the matrix to make sure the view does not move
     */
    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    /**
     * fix the image inside a view
     * @param trans
     * @param viewSize
     * @param contentSize
     * @return
     */
    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }
    
    /**
     * Fix if the view is move out of screen
     * @param delta
     * @param viewSize
     * @param contentSize
     * @return
     */
    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        // Rescales image on rotation
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
        	float scale;
            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();
            
            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
    
    /**
     * This method is used to set the coordinators of destination where user wants to hit
     * @param lat: latitude of destination
     * @param lon: longitude of destination
     */
    public void setDesCoordinator(double lat, double lon){
    	longitudeDes = lon;
    	latitudeDes=lat;
    }
    
    /**
     * This method is used to set the current coordinators of user position on the map
     * @param lat: latitude where user is
     * @param lon: longitude where user is
     */
    public void setUserCoordinator(double lat, double lon){
    	longitudeUser = lon;
    	latitudeUser=lat;
    }
    
    /**
     * Override onDraw method
     * This method is used to add  marker to the main map:
     * One is the marker of destination where user wants to hit
     * The other is the marker of user on the map
     */
    protected void onDraw(Canvas c) {
        //Let the image of the campus map be drawn first
        super.onDraw(c);
 
        float[] pDes = new float[2];
        float[] pUser = new float[2];

        pDes[0]=(float) ((longitudeDes + 84.01209) * 100000*1188/1437);
        pDes[1]=(float) (((33.98565 - latitudeDes) * 100000)-15);
        
        pUser[0]=(float) ((longitudeUser + 84.01209) * 100000*1188/1437);
        pUser[1]=(float) (((33.98565 - latitudeUser) * 100000)-15);
        matrix.mapPoints(pDes);
        matrix.mapPoints(pUser);
        c.drawBitmap(mMarker, pDes[0],pDes[1], null);
        c.drawBitmap(mUser, pUser[0],pUser[1], null);
    }

}