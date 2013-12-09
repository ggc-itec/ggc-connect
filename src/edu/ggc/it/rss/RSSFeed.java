package edu.ggc.it.rss;

/**
 * The URL's for various rss feeds. Along with the URL itself a title is required.
 * 
 * @author Derek
 *
 */
public enum RSSFeed
{
    // Current GGC news URL
    NEWS("http://www.ggc.edu/about-ggc/news/News?format=rss", "GGC News"),

    // Current GGC events URL
    EVENTS("http://www.ggc.edu/student-life/events-calendar/events-calendar/rss", "GGC Events");

    private final String url;
    private final String title;

    private RSSFeed(String url, String title)
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
