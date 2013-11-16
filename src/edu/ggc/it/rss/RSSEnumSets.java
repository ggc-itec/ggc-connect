package edu.ggc.it.rss;

public class RSSEnumSets
{
	/**
	 * Defines the tags present in the XML document from the Rss Feed
	 * @author crystalist
	 *
	 */
	public enum RSSTaskDetail 
	{
		ITEM("item"),
		TITLE("title"),
		LINK("link"),
		DESCRIPTION("description"),
		PUBLISH_DATE("pubDate");		
		
		private final String detail;       

		private RSSTaskDetail(String s) 
		{
			detail = s;
	    }

		 public String getValue(){return detail;}
	}
	
	/**
	 * An enum set that holds the URLs to the GGC news and events rss feeds
	 * @author Derek
	 * I made this to keep from putting long URLs in other classes
	 * specifically in Main or any other class that uses these rss URLs
	 */
	public enum RSS_URL 
	{
		//Current GGC news URL
		NEWS("http://www.ggc.edu/about-ggc/news/News?format=rss"),
		
		//Current GGC events URL
		EVENTS("http://www.ggc.edu/student-life/events-calendar/events-calendar/rss");
		
		private final String url;
		
		private RSS_URL(String url)
		{
			this.url = url;
		}
		
		public String toString()
		{
			return url;
		}
	}
}
