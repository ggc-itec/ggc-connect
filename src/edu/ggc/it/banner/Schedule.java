package edu.ggc.it.banner;

import java.util.ArrayList;
import java.util.List;

public class Schedule
{
	private List<Section> courses;
	private static final int DEFAULT_COURSES = 4;
	
	public Schedule()
    {
		courses = new ArrayList<Section>(DEFAULT_COURSES);
	}
	
	public void addCourse(Section course)
    {
		courses.add(course);
	}
	
	public List<Section> getCourses()
    {
		return new ArrayList<Section>(courses);
	}
}
