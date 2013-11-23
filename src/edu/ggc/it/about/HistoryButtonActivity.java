package edu.ggc.it.about;

import edu.ggc.it.R;
import android.os.Bundle;
import android.app.Activity;

/**
 * Class: HistoryButtonActivity
 * 
 * Provide some historical facts about GGC.
 *
 */
public class HistoryButtonActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// sets the content view to the history layout
		setContentView(R.layout.activity_ggc_history);
	}
}
