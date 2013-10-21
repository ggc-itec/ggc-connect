package edu.ggc.it.rss;

import java.util.ArrayList;
import java.util.List;

import edu.ggc.it.rss.RSSEnumSets.RSSTaskDetail;

/**
 * This is a container class that has a unique URL associated with it
 * This class is instantiated in RSSActivity
 * Passed and filled in RSSTask
 * Then passed to RSSAdapter
 * Originally this package had multiple references to the below ArrayLists
 * I just put the ArrayLists in one class and now just pass references to this class
 * This encapsulates the rss data into one class
 * 
 * @author Derek
 *
 */
public class RSSDataContainer 
{
	private final String rssUrl;
	private List<String> titles = new ArrayList<String>();
	private List<String> links = new ArrayList<String>();
	private List<String> publishedDates = new ArrayList<String>();
	private List<String> descriptions = new ArrayList<String>();
	
	public RSSDataContainer(String rssUrl)
	{
		this.rssUrl = rssUrl;
	}
	
	/**
	 * Add data to the appropriate ArrayList based on the passed RSSTaskDetail
	 * This method is used in RSSTask.doInBackground(Void... args)
	 * @param data
	 * 			the string information added to ArrayLists
	 * @param tag
	 * 			the tag associated with the passed data
	 */
	@SuppressWarnings("incomplete-switch")
	public void add(String data, RSSTaskDetail tag)
	{
		switch (tag) 
		{
			case TITLE:
				titles.add(data);
				break;
			case LINK:
				links.add(data);
				break;
			case PUBLISH_DATE:
				publishedDates.add(data);
				break;
			case DESCRIPTION:
				descriptions.add(data);
				break;
		}
	}
	
	/**
	 * @return the URL associated with this container
	 */
	public String getURL()
	{
		return rssUrl;
	}
	
	/**
	 * @return size of titles ArrayList
	 */
	public int getTitlesSize()
	{
		return titles.size();
	}
	
	/*
	 * The getters for each of the arrays
	 * Returns string at specified index
	 */
	public String getTitleAtIndex(int index)
	{
		return titles.get(index);
	}
	
	public String getLinkAtIndex(int index)
	{
		return links.get(index);
	}
	
	public String getPublishedDatesAtIndex(int index)
	{
		return publishedDates.get(index);
	}
	
	public String getDescriptionsAtIndex(int index)
	{
		return descriptions.get(index);
	}
}


