package edu.ggc.it.banner;

import java.util.Date;

public class Meeting {
	private String location;
	private String instructor;
	private Date beginDate;
	private Date endDate;
	private Date beginTime;
	private Date endTime;

	public Meeting(String location, String instructor, Date beginDate, Date endDate, Date beginTime, Date endTime){
		this.location = location;
		this.instructor = instructor;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
}
