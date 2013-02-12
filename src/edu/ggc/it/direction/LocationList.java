package edu.ggc.it.direction;

import java.util.ArrayList;

public class LocationList {
	private ArrayList<Location> locationList;
	public LocationList(){	
		locationList = new ArrayList<Location>();
		locationList.add(new Location("library",1.1,1.1,"Building L"));
		locationList.add(new Location("book store",1.1,1.1,"Building E"));
		locationList.add(new Location("aec",1.1,1.1,"Building L and B"));
	}
	
	public String Find(String key){
		String str = "Please make sure your typing is correct!";
		for(Location i : locationList){
			if(i.getName().equalsIgnoreCase(key))
				str = i.getBuilding();
		}
		return str;
	}
	
	public int getSize(){
		return locationList.size();
	}
	
	public String getElement(int i){
		return locationList.get(i).getName();
	}
}
