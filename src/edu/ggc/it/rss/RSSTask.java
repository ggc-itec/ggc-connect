package edu.ggc.it.rss;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Any class that has a reference to RSSTask MUST implement or pass a reference
 * to a class that implements the RSSTaskComplete interface
 * 
 * @param String
 * @progress
 */
public class RSSTask extends AsyncTask<RSSFeed, Void, Void>
{
    private static final String DEBUG_TAG = "RSSTask";
    
    private RSSTaskComplete task;
    private Context context;
    private boolean showDialog;
    private ProgressDialog dialog;
    
    /**
     * An interface to be used by any class that has an instance of RSSTask.
     * This method serves as a callback whenever onPostExecute() is called
     * @author Derek
     *
     */
    public interface RSSTaskComplete
    {
	/**
	 * This method is called when onPostExecute() is called
	 */
	void taskComplete();
    }

    /**
     * Constructor
     * 
     * @param task
     *            a reference to a class that implements RSSTaskComplete
     * @param context
     *            Context of activity using this class
     * @param showDialog
     *            if true ProgressDialog will be shown
     */
    public RSSTask(RSSTaskComplete task, Context context, boolean showDialog)
    {
	this.task = task;
	this.context = context;
	this.showDialog = showDialog;
	if (showDialog)
	    dialog = new ProgressDialog(context);
    }

    /**
     * This method is called before doInBackground() Sets the message and shows
     * the ProgressDialog
     */
    protected void onPreExecute()
    {
	if (showDialog)
	{
	    dialog.setMessage("Working...");
	    dialog.show();
	}
    }

    /**
     * Creates RSSParser then parses each of the RSSFeeds into the RSSDatabase
     * 
     * @param feeds
     *            an array of RSSFeed's passed when RSSTask.execute(RSSFeed) is called
     */
    @Override
    protected Void doInBackground(RSSFeed... feeds)
    {
	try 
	{
	    RSSParser parser = new RSSParser(context);
	    for(int i = 0; i < feeds.length; i++)
	    {
		parser.setUpConnections(feeds[i]);
		parser.parseFeed();
	    }
	}catch(XmlPullParserException e) {
	    Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
	    Log.e(DEBUG_TAG, "XmlPullParserException", e);
	}catch(IOException e) {
	    Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
	    Log.e(DEBUG_TAG, "IOException", e);
	}
	return null;
    }

    /**
     * Calls the RSSTaskComplete object's taskComplete() method
     * 
     * @param result
     */
    protected void onPostExecute(Void result)
    {
	if (showDialog)
	{
	    if (dialog.isShowing())
	    {
		dialog.dismiss();
	    }
	}
	task.taskComplete();
    }
}
