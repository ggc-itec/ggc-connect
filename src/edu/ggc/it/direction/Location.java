package edu.ggc.it.direction;

public class Location {
	private String name;
	private double latitude;
	private double longitude;
	private String building;

	public Location(String n, double la, double lo, String bld){
		name = n;
		latitude = la;
		longitude = lo;
		building = bld;
	}
	
	public String getName(){
		return name;
	}
	
	public String getBuilding(){
		return building;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
}
