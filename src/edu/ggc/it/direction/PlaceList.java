package edu.ggc.it.direction;

import java.util.ArrayList;

public class PlaceList {

	private ArrayList<String> placeList;
	
	public PlaceList(){
		placeList = new ArrayList<String>();
		placeList.add("Please choose a place you are looking for!");
		placeList.add("Option 1");
		placeList.add("Option 2");
		placeList.add("Option 3");
		placeList.add("Option 4");
		placeList.add("Option 5");
	}
	
	public ArrayList<String> getList(){
		return placeList;
	}
}
