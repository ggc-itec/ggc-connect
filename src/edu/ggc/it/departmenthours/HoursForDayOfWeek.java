package edu.ggc.it.departmenthours;

/** Class: HoursForDayOfWeek
 * 
 * This class represents the opening and closing times of a department
 * in military time, 0 to 2300, for a single day of the week,
 * Sunday (1) through Saturday (7).
 */
public class HoursForDayOfWeek
{
	
	private int dayOfWeek;
	private int openingTime;
	private int closingTime;
	
	/**
	 * Default constructor
	 */
	public HoursForDayOfWeek()
	{
		this(0, 0, 0);
	}
	
	/**
	 * Constructor specifying the day of the week and the opening and closing times
	 * @param dayOfWeek the day of the week, with a value of 1 (Sunday) to 7 (Saturday)
	 * @param openingTime the opening time
	 * @param closingTime the closing time
	 */
	public HoursForDayOfWeek(int dayOfWeek, int openingTime, int closingTime)
	{
		this.dayOfWeek = dayOfWeek;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}

	/**
	 * Converts the specified military time to standard time
	 * @param militaryTime the time to convert to standard time
	 * @return a string of the standard time
	 */
	private static String toStandardTime(int militaryTime)
	{
		int hourOfDay = militaryTime / 100;
		int minuteOfHour = militaryTime % 100;
		String minutes = "";
		String standardTime = "";
		
		if(minuteOfHour < 10)
		{
			minutes += "0" + minuteOfHour;
		}
		else
		{
			minutes += minuteOfHour;
		}
		
		if (hourOfDay % 12 == 0)
		{
			standardTime = "12:" + minutes;
		}
		else
		{
			standardTime = (hourOfDay % 12) + ":" + minutes;
		}
		
		if (hourOfDay <= 12)
		{
			standardTime += " AM";
		}
		else
		{
			standardTime += " PM";
		}
		
		return standardTime;
	}
	
	/**
	 * Returns the opening and closing times as an html formatted string
	 * @return the times as an html string
	 */
	public String toHtmlString()
	{
		return "<html>Opens: " + toStandardTime(openingTime) + "<br>" +
				"Closes: " + toStandardTime(closingTime) + "</html>";
	}
	
	/**
	 * Returns the day of the week, a value of 1 (Sunday) through 7 (Saturday)
	 * @return the day of the week
	 */
	public int getDayOfWeek()
	{
		return dayOfWeek;
	}

	/**
	 * Returns the opening time in military hours
	 * @return the opening time
	 */
	public int getOpeningTime()
	{
		return openingTime;
	}

	/**
	 * Returns the closing time in military hours
	 * @return the closing time
	 */
	public int getClosingTime()
	{
		return closingTime;
	}
	
	/**
	 * Sets the day of the week
	 * @param dayOfWeek the day of the week to set
	 */
	public void setDayOfWeek(int dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * Sets the opening time
	 * @param openingTime the opening time to set
	 */
	public void setOpeningTime(int openingTime)
	{
		this.openingTime = openingTime;
	}

	/**
	 * Sets the closing time
	 * @param closingTime the closing time to set
	 */
	public void setClosingTime(int closingTime)
	{
		this.closingTime = closingTime;
	}
}