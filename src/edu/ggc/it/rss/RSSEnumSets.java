package edu.ggc.it.rss;

public class RSSEnumSets
{
    /**
     * Defines the tags present in the XML document from the Rss Feed
     * 
     * @author crystalist
     * 
     */
    public enum RSSTag
    {
	ITEM("item"), TITLE("title"), LINK("link"), DESCRIPTION("description"), PUBLISH_DATE(
		"pubDate");

	private final String detail;

	private RSSTag(String s)
	{
	    detail = s;
	}

	public String tag()
	{
	    return detail;
	}
    }

    /**
     * An enum set that holds the URLs to the GGC news and events rss feeds
     * 
     * @author Derek I made this to keep from putting long URLs in other classes
     *         specifically in Main or any other class that uses these rss URLs
     */
    public enum RSS_URL
    {
	// Current GGC news URL
	NEWS("http://www.ggc.edu/about-ggc/news/News?format=rss", "News"),

	// Current GGC events URL
	EVENTS("http://www.ggc.edu/student-life/events-calendar/events-calendar/rss", "Events");

	private final String url;
	private final String title;

	private RSS_URL(String url, String title)
	{
	    this.url = url;
	    this.title = title;
	}

	public String URL()
	{
	    return url;
	}

	public String title()
	{
	    return title;
	}
    }
}
