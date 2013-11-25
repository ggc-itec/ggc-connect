package edu.ggc.it.widget;

import edu.ggc.it.rss.RSSDataContainer;

/**
 * A singleton class to be used to handle global data in the widget package.
 * Holds the RSSDataContainer[] returned by RSSTask, an int is used to keep track
 * of the currently displayed rss feed
 * @author Derek
 *
 */
public class WidgetData
{
    /**
     * The singleton instance
     */
    private static WidgetData data;
    
    /**
     * The containers for the rss feeds
     */
    private RSSDataContainer[] containers;
    
    /**
     * The index of the current rss feed
     * 0 = News
     * 1 = Events
     * Note: this isn't the best implementation just the easiest at the time
     */
    private int current = 0;
    
    /**
     * The singleton constructor, just leave blank
     */
    private WidgetData(){}
    
    /**
     * Singleton getInstance() method
     * @return data		the instance of this class
     */
    public static WidgetData getInstance()
    {
	if(data == null)
	{
	    data = new WidgetData();
	}
	return data;
    }
    
    /**
     * Sets this class's containers object.
     * Since this is a singleton class this method needs to be called to set the containers object.
     * Preferably this method is called in WidgetUpdater's taskComplete() method
     * @param containers
     */
    public void setContainers(RSSDataContainer[] containers)
    {
	this.containers = containers;
    }
    
    /**
     * @return the currently displayed RSSDataContainer
     */
    public RSSDataContainer getCurrentContainer()
    {
	return containers[current];
    }
    
    /**
     * Switches the value of current based on the value of current at time this method is called.
     * This is the ternary operator. If current is equal to 0 then set current to 1, else set current to 0.
     * NOTE: If there more than 2 rss feeds this method will need to be changed as it can only handle 2 feeds
     */
    public void switchContainer()
    {
	current = (current == 0) ? 1 : 0;
    }
    
    /**
     * @return the amount of titles in the current RSSDataContainer
     */
    public int getCount()
    {
	return getCurrentContainer().getTitlesSize();
    }
    
    /**
     * @return the title of the current RSSDataContainers RSS_URL title
     */
    public String getTitle()
    {
	return getCurrentContainer().getRSSURL().title();
    }
}
