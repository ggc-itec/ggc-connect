package edu.ggc.it.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import edu.ggc.it.rss.RSSDatabase.RSSTable;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

/**
 * This is that class that handles parsing the rss feeds.
 * A lot of the methods are based on the Android training course on parsing XML data.
 * http://developer.android.com/training/basics/network-ops/xml.html
 * 
 * @author Derek
 *
 */
public class RSSParser
{
    private Context context;
    private ContentResolver resolver;
    private RSSFeed feed;
    private URL url;
    private XmlPullParserFactory factory;
    private XmlPullParser parser;
    
    /**
     * Defines the tags present in the XML document from the Rss Feed
     * 
     * @author crystalist
     * 
     */
    public enum RSSTag
    {
	ITEM("item"),
	TITLE("title"),
	LINK("link"),
	DESCRIPTION("description"),
	PUBLISH_DATE("pubDate"),
	OTHER("");

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
     * Constructor
     * 
     * @param context
     */
    public RSSParser(Context context)
    {
	this.context = context;
	this.resolver = context.getContentResolver();
    }

    /**
     * Set up connection instances. This method should be called before parseFeed().
     * 
     * @throws XmlPullParserException 
     * @throws IOException 
     */
    public void setUpConnections(RSSFeed feed) throws XmlPullParserException, IOException
    {
	this.feed = feed;
	try
	{
	    url = new URL(feed.URL());
	    factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(false);
	    parser = factory.newPullParser();
	    parser.setInput(url.openConnection().getInputStream(), "UTF-8");
	} catch (MalformedURLException e)
	{
	    Toast.makeText(context, "Bad URL", Toast.LENGTH_LONG).show();
	}
    }
    
    /**
     * Parses the RSSFeed. When it finds an "item" tag, the method readItem() is called.
     * This method should be called after setUpConnections().
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    public void parseFeed() throws XmlPullParserException, IOException
    {
	while(parser.next() != XmlPullParser.END_DOCUMENT)
	{
	    if(parser.getEventType() != XmlPullParser.START_TAG)
		continue;
	    
	    String name = parser.getName();
	    if(name.equalsIgnoreCase(RSSTag.ITEM.tag()))
	    {
		readItem();
	    }
	}
    }
    
    /**
     * Parses information within the "item" and puts it into a ContentValues.
     * This ContentValues is then added to the RSSDatabase by way of the RSSProvider.
     * ContentValues are basically a row within a table.
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void readItem() throws XmlPullParserException, IOException
    {
	ContentValues values = new ContentValues();
	while(parser.next() != XmlPullParser.END_TAG)
	{
	    if(parser.getEventType() != XmlPullParser.START_TAG)
		continue;
	    
	    RSSTag tag = getTag(parser.getName());
	    switch (tag)
	    {
	    	case TITLE:
	    	    values.put(RSSTable.COL_TITLE, readText());
	    	    break;
	    	case LINK:
	    	    values.put(RSSTable.COL_LINK, readText());
	    	    break;
	    	case DESCRIPTION:
	    	    values.put(RSSTable.COL_DESCRIPTION, readText());
	    	    break;
	    	case PUBLISH_DATE:
	    	    values.put(RSSTable.COL_PUB_DATE, readText());
	    	    break;
	    	default:
	    	    skip();
	    	    break;
	    }
	}
	values.put(RSSTable.COL_FEED, feed.title());
	resolver.insert(RSSProvider.CONTENT_URI, values);
    }
    
    /**
     * This method returns the String located in between the various tags in the rss feed.
     * 
     * @return result		The text located within a tag
     * @throws XmlPullParserException
     * @throws IOException
     */
    private String readText() throws XmlPullParserException, IOException
    {
	String result = "";
	if(parser.next() == XmlPullParser.TEXT)
	{
	    result = parser.getText();
	    parser.nextTag();
	}
	return result;
    }
    
    /**
     * This method is called when the parser finds a tag whose data won't get added to the RSSDatabase.
     * Pretty much a copy paste from the Android XML parsing training course.
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip() throws XmlPullParserException, IOException
    {
	if(parser.getEventType() != XmlPullParser.START_TAG)
	    throw new IllegalStateException();
	int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    
    /**
     * Returns an RSSTag based on the passed name.
     * If the name isn't one of the tags that will go into the database, then the RSSTag is set to OTHER.
     * 
     * @param name	The name of the tag
     * @return tag	The RSSTag associated with the passed name
     */
    private RSSTag getTag(String name)
    {
	RSSTag tag;
	if(name.equalsIgnoreCase(RSSTag.TITLE.tag()))
	    tag = RSSTag.TITLE;
	else if(name.equalsIgnoreCase(RSSTag.LINK.tag()))
	    tag = RSSTag.LINK;
	else if(name.equalsIgnoreCase(RSSTag.DESCRIPTION.tag()))
	    tag = RSSTag.DESCRIPTION;
	else if(name.equalsIgnoreCase(RSSTag.PUBLISH_DATE.tag()))
	    tag = RSSTag.PUBLISH_DATE;
	else
	    tag = RSSTag.OTHER;
	return tag;
    }
}
