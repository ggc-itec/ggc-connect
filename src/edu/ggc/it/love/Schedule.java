package edu.ggc.it.love;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
	private List<Course> courses;
	private static final int DEFAULT_COURSES = 4;
	
	public Schedule(){
		courses = new ArrayList<Course>(DEFAULT_COURSES);
	}
	
	public void addCourse(Course course){
		courses.add(course);
	}
}
