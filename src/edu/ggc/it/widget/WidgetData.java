package edu.ggc.it.widget;

import edu.ggc.it.rss.RSSDataContainer;

public class WidgetData
{
    private static WidgetData data;
    private RSSDataContainer[] containers;
    private int current = 0;
    
    private WidgetData(){}
    
    public static WidgetData getInstance()
    {
	if(data == null)
	{
	    data = new WidgetData();
	}
	return data;
    }
    
    public void setContainers(RSSDataContainer[] containers)
    {
	this.containers = containers;
    }
    
    public RSSDataContainer getContainer()
    {
	return containers[current];
    }
    
    public void setCurrent(int index)
    {
	current = index;
    }
    
    public int getCurrent()
    {
	return current;
    }
    
    public int getCount()
    {
	return containers[current].getTitlesSize();
    }
    
    public String getTitle()
    {
	return getContainer().getRSSURL().title();
    }
}
