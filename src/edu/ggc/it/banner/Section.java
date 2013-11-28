package edu.ggc.it.banner;

import java.io.Serializable;
import java.util.List;

public class Section implements Serializable
{
	private static final long serialVersionUID = 4676656216027877660L;

	public static final String KEY = "edu.ggc.it.banner.Section";
	
	private Course course;
	private String term;
	private int section;
	private int crn;
	private List<Meeting> meetings;
	
	public Section(Course course, String term, int section, int crn, List<Meeting> meetings)
    {
		this.course = course;
		this.term = term;
		this.section = section;
		this.crn = crn;
		this.meetings = meetings;
	}
	
	public Course getCourse()
    {
		return course;
	}
	
	public String getTerm()
    {
		return term;
	}
	
	public int getCRN()
    {
		return crn;
	}
	
	public int getSection()
    {
		return section;
	}
	
	public List<Meeting> getMeetings()
    {
		return meetings;
	}
	
	public String toString()
    {
		return String.format("%s - %d - %s %s - %02d", course.getName(), crn, course.getSubject(), course.getId(), section);
	}
}
