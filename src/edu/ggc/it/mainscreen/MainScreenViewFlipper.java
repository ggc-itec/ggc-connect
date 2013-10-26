package edu.ggc.it.mainscreen;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import edu.ggc.it.R;

/**
 * Created by gregwesterfield on 10/13/13.
 */
public class MainScreenViewFlipper extends LinearLayout implements View.OnTouchListener
{

    /**
     * Count of index buttons. Default is 3
     */
    public static int countIndexes = 3;

    /**
     * Button Layout
     */
    LinearLayout buttonLayout;

    /**
     * Index button images
     */
    ImageView[] indexButtons;

    /**
     * Views for the Flipper
     */
    View[] views;

    /**
     * Flipper instance
     */
    ViewFlipper flipper;

    /**
     * X coordinate for touch down
     */
    float downX;

    /**
     * X coordinate for touch up
     */
    float upX;

    /**
     * Current index
     */
    int currentIndex = 0;


    public MainScreenViewFlipper(Context context)
    {
        super(context);
        init(context);
    }

    public MainScreenViewFlipper(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    /**
     * Initialize
     *
     * @param context
     */
    public void init(Context context)
    {
        setBackgroundColor(0xffbfbfbf);

        // Layout inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_screen_view, this, true);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setOnTouchListener(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 50;

        indexButtons = new ImageView[countIndexes];

        views = new View[countIndexes];
        views[0] = new MainScreenViewOne(context);
        views[1] = new MainScreenViewTwo(context);
        views[2] = new MainScreenViewThree(context);

        for(int i = 0; i < countIndexes; i++) {
            indexButtons[i] = new ImageView(context);

            if (i == currentIndex) {
                indexButtons[i].setImageResource(R.drawable.green);
            } else {
                indexButtons[i].setImageResource(R.drawable.white);
            }

            indexButtons[i].setPadding(10, 10, 10, 10);
            buttonLayout.addView(indexButtons[i], params);

            flipper.addView(views[i]);
        }
    }

    /**
     * onTouch event handling
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        if(v != flipper) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
        } else if(event.getAction() == MotionEvent.ACTION_UP){
            upX = event.getX();

            if( upX < downX ) {  // in case of right direction

                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_left_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_left_out));

                if (currentIndex < (countIndexes-1)) {
                    currentIndex++;
                    flipper.setDisplayedChild(currentIndex);
                    updateIndexes();
                } else if (currentIndex == (countIndexes-1)) {
                    currentIndex=0;
                    flipper.setDisplayedChild(currentIndex);
                    updateIndexes();
                }
            } else if (upX > downX){ // in case of left direction

                flipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_right_in));
                flipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
                        R.anim.push_right_out));

                if (currentIndex > 0) {
                    currentIndex--;
                    flipper.setDisplayedChild(currentIndex);
                    updateIndexes();
                } else if (currentIndex==0) {
                    currentIndex=2;
                    flipper.setDisplayedChild(currentIndex);
                    updateIndexes();
                }
            }
        }
        return true;
    }

    /**
     * Update the display of index buttons
     */
    private void updateIndexes()
    {
        for(int i = 0; i < countIndexes; i++) {
            if (i == currentIndex) {
                indexButtons[i].setImageResource(R.drawable.green);
            } else {
                indexButtons[i].setImageResource(R.drawable.white);
            }
        }
    }
}
