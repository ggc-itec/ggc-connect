package edu.ggc.it.direction;

import java.util.ArrayList;

public class LocationList {
	private ArrayList<Location> locationList;
	public LocationList(){	
		locationList = new ArrayList<Location>();
		locationList.add(new Location("food",1.1,1.1,"building A, B, E, and L"));
		locationList.add(new Location("coffee",1.1,1.1,"building A, B, E, and L"));
		locationList.add(new Location("police",1.1,1.1,"building A @ 1520"));
		locationList.add(new Location("testing",1.1,1.1,"A building"));
		locationList.add(new Location("decal",1.1,1.1,"A building"));
		locationList.add(new Location("aec",1.1,1.1,"Library, or B building"));
		locationList.add(new Location("lab",1.1,1.1,"Library, or B building on the second floor"));
		locationList.add(new Location("lab",1.1,1.1,"1260 of C building"));
		locationList.add(new Location("finacial",1.1,1.1,"D building called admission building"));
		locationList.add(new Location("book store",1.1,1.1,"E building called Student Center"));
		locationList.add(new Location("book",1.1,1.1,"E building called Student Center"));
	}
	
	public String Find(String key){
		String str = "Ooops..I could find your place. Please make sure your typing is correct!";
		key = key.toLowerCase();
		for(Location i : locationList){
			//if(i.getName().equalsIgnoreCase(key))
			if((i.getName().equalsIgnoreCase(key))||(key.indexOf(i.getName())>=0))
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
