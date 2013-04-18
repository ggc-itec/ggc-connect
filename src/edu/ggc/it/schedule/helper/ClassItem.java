package edu.ggc.it.schedule.helper;

/**
 * This is a class object to represent a class/course schedule item.
 * 
 * It was created to make it easier to work with updating and adding to the schedule.
 * @author Raj
 *
 */
public class ClassItem {
	
	private String className;
	private String section;
	private int startTimeHour;
	private int startTimeMinute;
	private int endTimeHour;
	private int endTimeMinute;
	private String buildingLocation;
	private String roomLocation;
	private boolean onMonday;
	private boolean onTuesday;
	private boolean onWednesday;
	private boolean onThursday;
	private boolean onFriday;
	private boolean onSaturday;
	
	public ClassItem() {
		this.className = "";
		this.section = "";
		
		// set to unreasonable amount for initialization
		this.startTimeHour = 24;
		this.startTimeMinute = 60;
		this.endTimeHour = 24;
		this.endTimeMinute = 60;
		
		this.buildingLocation = "";
		this.roomLocation = "";
		this.onMonday = false;
		this.onTuesday = false;
		this.onWednesday = false;
		this.onThursday = false;
		this.onFriday = false;
		this.onSaturday = false;
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getSection() {
		return this.section;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public int getStartTimeHour() {
		return this.startTimeHour;
	}
	
	public void setStartTimeHour(int startTimeHour) {
		this.startTimeHour = startTimeHour;
	}
	
	public int getStartTimeMinute() {
		return this.startTimeMinute;
	}
	
	public void setStartTimeMinute(int startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}
	
	public int getEndTimeHour() {
		return this.endTimeHour;
	}
	
	public void setEndTimeHour(int endTimeHour) {
		this.endTimeHour = endTimeHour;
	}
	
	public int getEndTimeMinute() {
		return this.endTimeMinute;
	}
	
	public void setEndTimeMinute(int endTimeMinute) {
		this.endTimeMinute = endTimeMinute;
	}
	
	public String getBuildingLocation() {
		return this.buildingLocation;
	}
	
	public void setBuildingLocation(String buildingLocation) {
		this.buildingLocation = buildingLocation;
	}
	
	public String getRoomLocation() {
		return this.roomLocation;
	}
	
	public void setRoomLocation(String roomLocation) {
		this.roomLocation = roomLocation;
	}
	
	public boolean isOnMonday() {
		return this.onMonday;
	}
	
	public void setOnMonday(boolean onMonday) {
		this.onMonday = onMonday;
	}
	
	public boolean isOnTuesday() {
		return this.onTuesday;
	}
	
	public void setOnTuesday(boolean onTuesday) {
		this.onTuesday = onTuesday;
	}
	
	public boolean isOnWednesday() {
		return this.onWednesday;
	}
	
	public void setOnWednesday(boolean onWednesday) {
		this.onWednesday = onWednesday;
	}
	
	public boolean isOnThursday() {
		return this.onThursday;
	}
	
	public void setOnThursday(boolean onThursday) {
		this.onThursday = onThursday;
	}
	
	public boolean isOnFriday() {
		return this.onFriday;
	}
	
	public void setOnFriday(boolean onFriday) {
		this.onFriday = onFriday;
	}
	
	public boolean isOnSaturday() {
		return this.onSaturday;
	}
	
	public void setOnSaturday(boolean onSaturday) {
		this.onSaturday = onSaturday;
	}
}
