package edu.ggc.it.love;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Course {
	private String subject;
	private int courseId;
	private int section;
	private int crn;
	private Map<String, List<TimeRange>> times;
	private Calendar beginDate;
	private Calendar endDate;
	
	public Course(String subject, int courseId, int section, Map<String, List<TimeRange>> times,
			Calendar beginDate, Calendar endDate){
		this.subject = subject;
		this.courseId = courseId;
		this.section = section;
		this.times = times;
		this.beginDate = beginDate;
		this.endDate = endDate;
	}
	
	public static class TimeRange{
		// these are stored as minutes from midnight
		private int begin;
		private int end;
		
		public TimeRange(int begin, int end){
			this.begin = begin;
			this.end = end;
		}
		
		public static int toMinutes(String time){
			// expects time in the format "HH:MI a.m."
			String[] parts = time.toUpperCase().split(" ");
			boolean morning = parts[1].charAt(0) == 'A';
			parts = parts[0].split(":");
			int hour = Integer.parseInt(parts[0]);
			int minute = Integer.parseInt(parts[1]);
			
			if (morning)
				hour += 12;
			if (hour > 23)
				hour = 0;
			
			return hour*60 + minute;
		}
	}
}
