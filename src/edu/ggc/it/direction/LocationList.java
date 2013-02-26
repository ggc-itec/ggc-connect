package edu.ggc.it.direction;

import java.util.ArrayList;
/**
 * This class aims to create a list of location and feed for the spinner.
 * @author Thai Pham
 * @version 0.1
 *
 */
public class LocationList {
	//Create a new arraylist
	private ArrayList<Location> locationList;
	/**
	 * Constructor to create a new LocationList
	 */
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
	
	/**
	 * This method is to get the size of arraylist
	 * @return
	 */
	public int getSize(){
		return locationList.size();
	}
	
	/**
	 * This method is to get the name of a location in location list
	 * @param i: position of element want to get
	 * @return name of the location which has position i in the location list
	 */
	public String getElement(int i){
		return locationList.get(i).getName();
	}
	
	/**
	 * This method is to get the arraylist of only the name of location, to display in the spinner
	 * @return list of locations' name
	 */
	public ArrayList<String> getNameList(){
		ArrayList<String> nameList = new ArrayList<String>();
		for(int i = 0; i < locationList.size(); i++){
			nameList.add(locationList.get(i).getName());
		}
		return nameList;
	}
	
	/**
	 * This method is to get the building name of a location in the location list
	 * @param i: position of a place want to get the building name
	 * @return name of the building where the place is on
	 */
	public String getBuilding(int i){
		return locationList.get(i).getBuilding();
	}
	
	/**
	 * This method is to get the instruction to find the place on map
	 * @param i: position of the location in location list
	 * @return instruction for the place which has position is i
	 */
	public String getInstruction(int i){
		return locationList.get(i).getInstruction();
	}
}
