package edu.ggc.it.rss;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message>{

	public static SimpleDateFormat FORMATTER = 
			new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	
	private String title;
	private URL link;
	private String description;
	private Date date;
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title) 
	{
		title = title.trim();
	}
	
	
	
	
	@Override
	public int compareTo(Message arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
