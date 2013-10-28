package edu.ggc.it.banner;

import java.io.Serializable;
import java.util.Date;

/* Class: Meeting
 * This class provides methods (getters) that return various information about classes that
 * are offered. Information includes location, instructor, beginning date, end date, time class
 * starts, time class ends, days of classes, and the class type.
 */

public class Meeting implements Serializable{
	private static final long serialVersionUID = 6543419763151671413L;
	private String location;
	private Instructor instructor;
	private Date beginDate;
	private Date endDate;
	private Date beginTime;
	private Date endTime;
	private String days;
	private String type;
	
	/* Constructor which receives information about classes
	 * @param: location - holds information about the location of the class
	 * @param: days - holds information about days the class meets
	 * @param: type - holds information about the type of class it is
	 * @param: instructor - holds information about the instructor who teaches the class
	 * @param: beginDate - holds information about the day the class starts
	 * @param: endDate - holds information about the day the class ends
	 * @param: beginTime - holds information about the time the class starts
	 * @param: endTime - holds information about the time the class ends
	 */
	
	public Meeting(String location, String days, String type, Instructor instructor, Date beginDate,
			Date endDate, Date beginTime, Date endTime){
		this.location = location;
		this.days = days;
		this.type = type;
		this.instructor = instructor;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	
	/* Method: getLocation
	 * @return: location of the class
	 * @param: none
	 */
	
	public String getLocation(){
		return location;
	}
	
	/* Method: getDays
	 * @return: days that the class meets
	 * @param: none
	 */
	
	public String getDays(){
		return days;
	}
	
	/* Method: getType
	 * @return: type of the class
	 * @param: none
	 */
	
	public String getType(){
		return type;
	}
	
	/* Method: getInstructor
	 * @return: name of the instructor teaching the class
	 * @param: none
	 */
	
	public Instructor getInstructor(){
		return instructor;
	}
	
	/* Method: getBeginDate
	 * @return: day that the class starts
	 * @param: none
	 */
	
	public Date getBeginDate(){
		return beginDate;
	}
	
	/* Method: getEndDate
	 * @return: day that the class ends
	 * @param: none
	 */
	
	public Date getEndDate(){
		return endDate;
	}
	
	/* Method: getBeginTime
	 * @return: time that the class starts
	 * @param: none
	 */
	
	public Date getBeginTime(){
		return beginTime;
	}
	
	/* Method: getEndTime
	 * @return: time that the class ends
	 * @param: none
	 */
	
	public Date getEndTime(){
		return endTime;
	}
}
