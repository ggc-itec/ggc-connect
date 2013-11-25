package edu.ggc.it.rss;

import edu.ggc.it.rss.RSSEnumSets.RSS_URL;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Any class that has a reference to RSSTask MUST implement or pass a reference
 * to a class that implements the RSSTaskComplete interface
 * 
 * @param String
 * @progress
 */
public class RSSTask extends AsyncTask<RSS_URL, Void, RSSDataContainer[]>
{
    private RSSTaskComplete task;
    private Context context;
    private ProgressDialog dialog;
    private boolean showDialog;

    /**
     * Constructor
     * 
     * @param task
     *            a reference to a class that implements RSSTaskComplete
     * @param context
     *            Context of activity using this class
     * @param showDialog
     *            if true ProgressDialog will be showed
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
     * Creates a RSSDataContainer object that is filled and returned
     * 
     * @param rssURL
     *            an array of strings passed when RSSTask.execute(String)
     */
    @Override
    protected RSSDataContainer[] doInBackground(RSS_URL... rssURL)
    {
	RSSDataContainer[] container = new RSSDataContainer[rssURL.length];
	for(int i = 0; i < rssURL.length; i++)
	{
	    container[i] = new RSSDataContainer(rssURL[i]);
	    container[i].fill(context);
	}
	return container;
    }

    /**
     * Calls the RSSTaskComplete objects taskcomplete() method
     * 
     * @param container
     *            the container returned by doInBackground()
     */
    protected void onPostExecute(RSSDataContainer[] container)
    {
	if (showDialog)
	{
	    if (dialog.isShowing())
	    {
		dialog.dismiss();
	    }
	}
	task.taskComplete(container);
    }
}
