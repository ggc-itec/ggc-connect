package edu.ggc.it.departmenthours;

import java.util.Calendar;
import java.util.List;

/** Class: Department
 * 
 * This class represents a department at GGC, including its name,
 * a list of its opening and closing times for each day of the week,
 * and a string URL to its webpage.
 */
public class Department
{
	private String name;
	private List<HoursForDayOfWeek> hoursOfOperation;
	private String url;
	
	/**
	 * Default constructor
	 */
	public Department()
	{
		this("", null, "");
	}
	
	/**
	 * Constructs a department with the specified name, hours of operation,
	 * and url to its webpage
	 * @param name the department's name
	 * @param hoursOfOperation a list of the department's hours of operation
	 * @param url the url of the department's webpage
	 */
	public Department(String name,
			List<HoursForDayOfWeek> hoursOfOperation, String url)
	{
		this.name = name;
		this.hoursOfOperation = hoursOfOperation;
		this.url = url;
	}

	/**
	 * Gets the current day of the week from the list of
	 * the department's hours of operation
	 * @return the current day of the week
	 */
	public HoursForDayOfWeek getCurrentDay()
	{
		Calendar currentDateTime = Calendar.getInstance();
		for (HoursForDayOfWeek day : hoursOfOperation)
		{
			if (day.getDayOfWeek() == currentDateTime.get(Calendar.DAY_OF_WEEK))
			{
				return day;
			}
		}
		return null;
	}
	
	/**
	 * Returns if the department is currently open
	 * @return true if the current time is within the hours of operation, false otherwise
	 */
	public boolean isOpen()
	{
		Calendar currentDateTime = Calendar.getInstance();
		HoursForDayOfWeek day = getCurrentDay();
		int currentTime = currentDateTime.get(Calendar.HOUR_OF_DAY) * 100 + currentDateTime.get(Calendar.MINUTE);
		return (currentTime >= day.getOpeningTime() && day.getClosingTime() > currentTime);
	}
	
	/**
	 * Gets the name of the department
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Gets the list of the hours of operation for each day of the week
	 * @return the hours of operation
	 */
	public List<HoursForDayOfWeek> getHoursOfOperation()
	{
		return hoursOfOperation;
	}
	
	/**
	 * Gets the URL of the department's webpage
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * Sets the name of the department
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Sets the hours of operation
	 * @param hoursOfOperation the hours of operation to set
	 */
	public void setHoursOfOperation(List<HoursForDayOfWeek> hoursOfOperation)
	{
		this.hoursOfOperation = hoursOfOperation;
	}

	/**
	 * Sets the url of the department's webpage
	 * @param url the url to set
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
}
