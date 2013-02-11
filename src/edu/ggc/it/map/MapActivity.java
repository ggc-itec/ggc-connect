package edu.ggc.it.map;

import edu.ggc.it.R;
import edu.ggc.it.R.layout;
import android.app.Activity;
import android.os.Bundle;

public class MapActivity extends Activity {
	
	/**
	 *  MapActivity has an image of GGC for the user to see.
	 *  TODO add GPS Object to this. That will handle all of
	 *  GPS data and return the latitude and longitude of the phone,
	 *  and then show a representation of the location of the user on the
	 *  map image of GGC.
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
	}

}
