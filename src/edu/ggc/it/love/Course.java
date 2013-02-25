package edu.ggc.it.love;

import java.util.Calendar;

public class Course {
	private String subject;
	private int courseId;
	private int section;
	private String days;
	private Calendar beginDate;
	private Calendar endDate;
	
	public Course(String subject, int courseId, int section, String days, Calendar beginDate,
			Calendar endDate){
		this.subject = subject;
		this.courseId = courseId;
		this.section = section;
		this.days = days;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
}
