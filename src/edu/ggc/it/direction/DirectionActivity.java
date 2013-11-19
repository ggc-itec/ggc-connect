package edu.ggc.it.direction;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import edu.ggc.it.R;

/**
 * This class is an activity class
 * 
 * @author Thai Pham
 * @version 0.1
 * 
 */
public class DirectionActivity extends Activity
{
	private TouchImageView img;
	private Context context;
	private TextView instructionText;
	private LocationManager lm;
	private UserLocationListener locationListener;
	private double latitude;
	private double longitude;
	private double latitudeDes;
	private double longitudeDes;
	private LocationArray locationList;
	private Spinner spin;
	private String spinVal;
	private ArrayAdapter<String> spinAdapter;

	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_direction);
		context = this;
		checkGPS();
		locationList = new LocationArray();
		spin = (Spinner) findViewById(R.id.spinnerText);
		spin.setOnItemSelectedListener(new MyItemSelectedListener());
		instructionText = (TextView) findViewById(R.id.instruction_text);
		img = (TouchImageView) findViewById(R.id.imageMap);
		img.setMaxZoom(4f);
		spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				locationList.getNameList());
		spin.setAdapter(spinAdapter);
	}

	/**
	 * This function aims to release all objects to NULL in order to save memory
	 */
	public void onBackPressed()
    {
		super.onBackPressed();
		Drawable d = img.getDrawable();

		if (d != null) {
			d.setCallback(null);
        }

		img.setImageDrawable(null);
		img = null;

		if (locationListener != null) {
			lm.removeUpdates(locationListener);
        }

		lm = null;
		spinAdapter = null;
		spin = null;
	}

	/**
	 * This function aims to update user's location when it changes, to correct
	 * location of users on the map
	 */
	public void updateLocation()
    {
		img.setUserCoordinator(latitude, longitude);
		img.invalidate();
	}

	/**
	 * This method is to check if the GPS on users' device is on or off. If it
	 * is off, allow users turn on
	 */
	public void checkGPS()
    {
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		} else {
			lm = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			lm.getProvider(LocationManager.GPS_PROVIDER);
			locationListener = new UserLocationListener();
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
					locationListener);
		}
	}

	private class UserLocationListener implements LocationListener
    {
		@Override
		public void onLocationChanged(Location location) {
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			updateLocation();
		}

		@Override
		public void onProviderDisabled(String provider) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}

	private void buildAlertMessageNoGps()
    {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
    {
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
    {
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 3);
		} catch (IOException ex) {
			ex.getMessage();
		}
		String zipCode = "";
		for (int i = 0; i < addresses.size(); i++) {
			Address address = addresses.get(i);
			if (address.getPostalCode() != null)
				zipCode = address.getPostalCode();
		}
		String url = "http://www.weather.com/weather/today/" + zipCode;
		if (item.getItemId() == R.id.weather) {
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			} catch (Exception e) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			}
		}
		return true;
	}

	public class MyItemSelectedListener implements OnItemSelectedListener
    {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
        {
			spinVal = locationList.getInstruction(position);
			String bld = locationList.getBuilding(position);

			if (bld.length() == 0) {
				instructionText.setText(spinVal);
				img.setImageResource(R.drawable.thai_ggc_map);
			} else {
				if (bld.length() == 1) {
					instructionText
							.setText("It is located on building " + bld + "\nInstruction: Your " +
                                    "location is Yellow dot, Destination is Red dot\n" + "    " + spinVal);
				} else {
					instructionText
							.setText("\nInstruction: Your location is Yellow dot, Destination is Red dot\n"
									+ "    " + spinVal);
				}
			}

			latitudeDes = locationList.getLatitude(position);
			longitudeDes = locationList.getLongitude(position);
			img.setDesCoordinator(latitudeDes, longitudeDes);
			img.setOriginalSize();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	}
}
