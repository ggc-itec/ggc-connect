package edu.ggc.it.banner;

import java.util.Date;

public class Meeting {
	private String location;
	private Instructor instructor;
	private Date beginDate;
	private Date endDate;
	private Date beginTime;
	private Date endTime;
	private String days;
	private String type;

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
	
	public String getLocation(){
		return location;
	}
	
	public String getDays(){
		return days;
	}
	
	public String getType(){
		return type;
	}
	
	public Instructor getInstructor(){
		return instructor;
	}
	
	public Date getBeginDate(){
		return beginDate;
	}
	
	public Date getEndDate(){
		return endDate;
	}
	
	public Date getBeginTime(){
		return beginTime;
	}
	
	public Date getEndTime(){
		return endTime;
	}
}
