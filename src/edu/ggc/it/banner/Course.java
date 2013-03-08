package edu.ggc.it.banner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Course {
	private String subject;
	private String courseName;
	private int courseId;
	private int section;
	private int crn;
	private Map<String, List<Meeting>> meetings;
	
	public Course(String subject, String courseName, int courseId, int section, int crn, Map<String, List<Meeting>> meetings){
		this.subject = subject;
		this.courseName = courseName;
		this.courseId = courseId;
		this.section = section;
		this.crn = crn;
		this.meetings = meetings;
	}
	
	public String toString(){
		return String.format("%s - %d - %s %d - %02d", courseName, crn, subject, courseId, section);
	}
}
