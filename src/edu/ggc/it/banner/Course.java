package edu.ggc.it.banner;

public class Course {
	private String subject;
	private String courseName;
	private String courseId;
	private String description;
	private double hours;
	
	public Course(String subj, String name, String id, String desc, double hours){
		subject = subj;
		courseName = name;
		courseId = id;
		description = desc;
		this.hours = hours;
	}
	
	public String getSubject(){
		return subject;
	}
	
	public String getName(){
		return courseName;
	}
	
	public String getId(){
		return courseId;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getShortDescription(int maxChars){
		String result = description;
		if (result.length() <= maxChars)
			return result;
		
		result = description.substring(0, maxChars);
		int wordBreak = result.lastIndexOf(" ");
		if (wordBreak == -1)
			wordBreak = result.length();
		result = result.substring(0, wordBreak) + "...";
		
		return result;
	}
	
	public double getCredits(){
		return hours;
	}
	
	public String toString(){
		return String.format("%s %s - %s", subject, courseId, courseName);
	}
}
