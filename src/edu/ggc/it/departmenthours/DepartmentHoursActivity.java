package edu.ggc.it.departmenthours;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;

import edu.ggc.it.R;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;

/**
 * Class: DeptHoursMainActivity
 * 
 * This class is a list activity that lists the hours of operation of GGC's
 * departments, links to their webpages, and states if the department is
 * currently open or closed.
 */
public class DepartmentHoursActivity extends ListActivity {

	private List<Department> departments;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_depthours_main);

		departments = new ArrayList<Department>();
		context = this;

		new DeptHoursTask().execute();
		String alertMessage = "Department hours may NOT be entirely accurate! \n Please visit GGC.edu and follow GGC on Twitter @GeorgiaGwinnett for the latest news!";
		Toast notification  = Toast.makeText(getApplicationContext(), alertMessage, Toast.LENGTH_LONG);
		notification.setGravity(Gravity.CENTER, 0, 0);
		notification.show();	
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Uri uri = Uri.parse(departments.get(position).getUrl());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		
	}

	/**
	 * This class executes to populate the list and views of each item.
	 */
	private class DeptHoursTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				XmlResourceParser parser = getResources().getXml(
						R.xml.departments);
				int eventType = parser.getEventType();
				while (eventType != XmlResourceParser.END_DOCUMENT) {
					if (eventType == XmlResourceParser.START_TAG
							&& parser.getName().equalsIgnoreCase("department")) {
						Department d = new Department();
						d.setName(parser.getAttributeValue(null, "name"));
						d.setUrl(parser.getAttributeValue(null, "url"));

						List<HoursForDayOfWeek> hoursOfOperation = new ArrayList<HoursForDayOfWeek>();
						eventType = parser.next();

						if (parser.getName().equalsIgnoreCase(
								"hoursofoperation")) {
							eventType = parser.next();
							for (int i = 1; i <= 7; i++) {
								HoursForDayOfWeek day = new HoursForDayOfWeek();
								day.setDayOfWeek(parser.getAttributeIntValue(
										null, "dayOfWeek", 1));
								day.setOpeningTime(parser.getAttributeIntValue(
										null, "openingTime", 0));
								day.setClosingTime(parser.getAttributeIntValue(
										null, "closingTime", 0));
								hoursOfOperation.add(day);
								eventType = parser.next();
								eventType = parser.next();
							}
							d.setHoursOfOperation(hoursOfOperation);
						}

						departments.add(d);
					} else {
						eventType = parser.next();
					}
				}
			} catch (MalformedURLException e) {
				Log.e("GGC-CONNECT", "MalformedURL", e);
			} catch (XmlPullParserException e) {
				Log.e("GGC-CONNECT", "XmlPULLParser", e);
			} catch (IOException e) {
				Log.e("GGC-CONNECT", "IO", e);
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			DepartmentAdapter adapter = new DepartmentAdapter(context,
					departments);
			setListAdapter(adapter);
		}
	}
}