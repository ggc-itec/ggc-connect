package edu.ggc.it.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;
import edu.ggc.it.R;

public class ImageTouchCBuildingActivity extends Activity{
	
	private Matrix matrix = new Matrix();
	private Matrix oldMatrix = new Matrix();
	Context context = this;

	public enum State {
		NONE, DRAGGING, ZOOMING
	};

	private State mode = State.NONE;

	private PointF start = new PointF();
	private PointF middle = new PointF();
	float oldDistance = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ggc_c_building_map);
		ImageView view = (ImageView) findViewById(R.id.imageView_ggc_c_Building_map);
		view.setOnTouchListener(new TouchListener());
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
			return true;
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
	}
}


