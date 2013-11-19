package edu.ggc.it.rss;

/**
 * An interface to be used by any class that has an instance of RSSTask
 * This method serves as a callback whenever onPostExecute() in RSSTask is called
 * @author Derek
 *
 */
public interface RSSTaskComplete 
{
	public void taskComplete(RSSDataContainer container);
}
