package edu.ggc.it.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import edu.ggc.it.rss.RSSEnumSets.RSSTag;
import android.content.Context;
import android.widget.Toast;

public class RSSParser
{
    private RSSDataContainer container;
    private Context context;
    private URL url;
    private XmlPullParserFactory factory;
    private XmlPullParser parser;

    /**
     * Constructor
     * 
     * @param container		reference to RSSDataContainer uses this class
     * @param context		Context of activity
     */
    public RSSParser(RSSDataContainer container, Context context)
    {
	this.container = container;
	this.context = context;
	setUpConnections();
    }

    /**
     * Set up connection instances
     */
    private void setUpConnections()
    {
	try
	{
	    url = new URL(container.getURL());
	    factory = XmlPullParserFactory.newInstance();
	    factory.setNamespaceAware(false);
	    parser = factory.newPullParser();
	    parser.setInput(url.openConnection().getInputStream(), "UTF-8");
	} catch (MalformedURLException e)
	{
	    Toast.makeText(context, "Bad URL", Toast.LENGTH_LONG).show();
	} catch (XmlPullParserException e)
	{
	    Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
	} catch (IOException e)
	{
	    Toast.makeText(context, "Unable to contact server", Toast.LENGTH_LONG).show();
	}
    }

    /**
     * Parses information from RSS feed and adds to RSSDataContainer object
     */
    public void parseRSS()
    {
	try {
	    boolean insideItem = false;
	    int eventType = parser.getEventType();
	    while (eventType != XmlPullParser.END_DOCUMENT)
	    {
		String name = parser.getName();
		if (eventType == XmlPullParser.START_TAG)
		{
		    if (name.equalsIgnoreCase(RSSTag.ITEM.toString()))
		    {
			insideItem = true;
		    }
		    else if (name.equalsIgnoreCase(RSSTag.TITLE.toString()))
		    {
			if (insideItem)
			    container.add(parser.nextText(), RSSTag.TITLE);
		    }
		    else if (name.equalsIgnoreCase(RSSTag.LINK.toString()))
		    {
			if (insideItem)
			    container.add(parser.nextText(), RSSTag.LINK);
		    }
		    else if (name.equalsIgnoreCase(RSSTag.DESCRIPTION.toString()))
		    {
			if (insideItem)
			    container.add(parser.nextText(), RSSTag.DESCRIPTION);
		    }
		    else if (name.equalsIgnoreCase(RSSTag.PUBLISH_DATE.toString()))
		    {
			if (insideItem)
			    container.add(parser.nextText(), RSSTag.PUBLISH_DATE);
		    }
		}
		else if (eventType == XmlPullParser.END_TAG && name.equalsIgnoreCase(RSSTag.ITEM.toString()))
		{
		    insideItem = false;
		}
		eventType = parser.next();
	    }//end while loop
	} catch (XmlPullParserException e){
	    Toast.makeText(context, "An error occured", Toast.LENGTH_LONG).show();
	} catch (IOException e){
	    Toast.makeText(context, "Unable to contact server", Toast.LENGTH_LONG).show();
	}
    }
}
