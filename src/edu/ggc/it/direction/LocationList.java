package edu.ggc.it.direction;

import java.util.ArrayList;

public class LocationList {
	private ArrayList<Location> locationList;
	public LocationList(){	
		locationList = new ArrayList<Location>();
		locationList.add(new Location("Please choose a place you are looking! ","","Welcome to GGC!"));
		locationList.add(new Location("Food Court: Quizno,...","A","On the main GGC map, you will see this building in L shape at right middle of the map."));
		locationList.add(new Location("Food court: Star Buck,...","B","On the main GGC map, you will see this building in C shape in center of the map."));
		locationList.add(new Location("Campus Police","A","On the main GGC map, you will see this building in L shape at right middle of the map. Get in this building and go through all the hall way, you will see the room 1520."));
		locationList.add(new Location("Testing Center","A","On the main GGC map, you will see this building in L shape at right middle of the map. Go to the front entrance of this building, you will see it."));
		locationList.add(new Location("Parking Decal","A","On the main GGC map, you will see this building in L shape at right middle of the map. Go to the front entrance, you will see it on your left."));
		locationList.add(new Location("Academic Enhancement Center(AEC)","L","In the middle of GGC map, you will see a beautiful glass building. Go to the second floor of this building, it is on your right."));
		locationList.add(new Location("Writing Lab, Math Lab (AEC)","B","On the main GGC map, you will see this building in C shape in center of the map. Go to the second floor, turn left and find the room 1450."));
		locationList.add(new Location("Cisco Auditorium","C", "This building is next to the B building which is in C shape in center of GGC map. Go to right door and you will see it at room 1260"));
		locationList.add(new Location("Financial Aid","D","It is on the most right middle of the GGC map. "));
		locationList.add(new Location("Book Store","E","It is on middle of the GGC map, next to the Library. Go to the second floor, it is on your right."));
		//Add some more here...
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
	
	public ArrayList<String> getNameList(){
		ArrayList<String> nameList = new ArrayList<String>();
		for(int i = 0; i < locationList.size(); i++){
			nameList.add(locationList.get(i).getName());
		}
		return nameList;
	}
	
	public String getBuilding(int i){
		return locationList.get(i).getBuilding();
	}
	
	public String getInstruction(int i){
		return locationList.get(i).getInstruction();
	}
}
