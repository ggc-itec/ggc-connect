package edu.ggc.it.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import edu.ggc.it.rss.RSSEnumSets.RSSTaskDetail;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Any class that has a reference to RSSTask MUST
 * implement or pass a reference to a class that implements the RSSTaskComplete interface
 * A RSSDataContainer is passed that will be filled by this task
 * Types for Params, Progress and Result are all void since the task is very simple
 */
public class RSSTask extends AsyncTask<Void, Void, Void>
{
	private RSSTaskComplete task;
	private RSSDataContainer container;
	
	/**
	 * Constructor
	 * @param task
	 * 			a reference to a class that implements RSSTaskComplete
	 * @param container
	 * 			a RSSDataContainer object the container is filled in this class
	 */
	public RSSTask(RSSTaskComplete task, RSSDataContainer container)
	{
		this.task = task;
		this.container = container;
	}

	/**
	 * Populate RSSDataContiner with information from rss feed
	 */
	@Override
	protected Void doInBackground(Void... arg0) 
	{
		try {
			URL url = new URL(container.getURL());
			XmlPullParserFactory factory = XmlPullParserFactory
					.newInstance();
			factory.setNamespaceAware(false);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(url.openConnection().getInputStream(), "UTF-8");

			// get the title, link and description for all valid "item"
			boolean insideItem = false;
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					
					if (parser.getName().equalsIgnoreCase(RSSTaskDetail.ITEM.toString())) {
						insideItem = true;
					} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.TITLE.getValue())) {
						if (insideItem)
							container.add(parser.nextText(), RSSTaskDetail.TITLE);
					} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.LINK.getValue())) {
						if (insideItem)
							container.add(parser.nextText(), RSSTaskDetail.LINK);
					} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.DESCRIPTION.getValue())) {
						if (insideItem)
							container.add(parser.nextText(), RSSTaskDetail.DESCRIPTION);
					} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.PUBLISH_DATE.getValue())) {
						if (insideItem)
							container.add(parser.nextText(), RSSTaskDetail.PUBLISH_DATE);
					}
				} else if (eventType == XmlPullParser.END_TAG
						&& parser.getName().equalsIgnoreCase(RSSTaskDetail.ITEM.toString())) {
					insideItem = false;
				}
				eventType = parser.next();
			}
		} catch (MalformedURLException e) {
		    Log.e("GGC-CONNECT", "MalformedURL", e);
		} catch (XmlPullParserException e) {
			Log.e("GGC-CONNECT","XmlPULLParser", e);
		} catch (IOException e) {
			Log.e("GGC-CONNECT","IO",e);
		}
		return null;
	}
	
	/**
	 * Makes a callback to the RSSTaskComplete object
	 */
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		task.taskComplete();
	}
}
