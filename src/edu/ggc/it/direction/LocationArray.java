package edu.ggc.it.direction;

import java.util.ArrayList;
/**
 * This class aims to create a list of location and feed for the spinner.
 * @author Thai Pham
 * @version 0.1
 *
 */
public class LocationArray {
	//Create a new arraylist
	private ArrayList<Location> locationList;
	/**
	 * Constructor to create a new LocationList
	 */
	public LocationArray(){	
		locationList = new ArrayList<Location>();
		locationList.add(new Location("Please choose a place you are looking! ","",0.0,0.0,"                         Welcome to GGC! \n              Your position is the red point. \n Your target is the white point with red text.\n"));
		locationList.add(new Location("Academic Enhancement Center(AEC)","L",33.97959,-84.00454,"In the middle of GGC map, you will see a beautiful glass building. Go to the second floor of this building, it is on your right."));
		locationList.add(new Location("AEC: Writing Lab and Math Lab","B",33.98095,-84.00526,"On the main GGC map, you will see this building in C shape in center of the map. Go to the second floor, turn left and find the room 2450."));
		locationList.add(new Location("Baseball Field","BB",33.98402,-84.00212,"It is on the top of the GGC map, beside the soccer field."));
		locationList.add(new Location("Book Store","E",33.97939,-84.00553,"It is on middle of the GGC map, next to the Library. Go to the second floor, it is on your right."));
		locationList.add(new Location("Campus Police","A",33.97999,-84.00096,"On the main GGC map, you will see this building in L shape at right middle of the map. Get in this building and go through all the hall way, you will see the room 1520."));
		locationList.add(new Location("Cisco Auditorium","C",33.98040,-84.00622, "This building is next to the B building which is in C shape in center of GGC map. Go to right door and you will see it at room 1260"));
		locationList.add(new Location("Financial Aid","D",33.97970,-83.99837,"It is on the most right middle of the GGC map. "));
		locationList.add(new Location("Food Court: Wow Cafe & Wingery, Quiznos","A",33.97999,-84.00096,"On the main GGC map, you will see this building in L shape at right middle of the map."));
		locationList.add(new Location("Food court: GGCity Cafe, Star Buck","B",33.98095,-84.00526,"On the main GGC map, you will see this building in C shape in center of the map."));
		locationList.add(new Location("Georgia Gwinnett College ROTC","ROTC",33.98032,-84.00914,"It is on middle left of the GGC map, beside the soccerfields, and building C. It is named Dorm on the map."));
		locationList.add(new Location("Jazzman's Cafe & Bakery","L",33.97959,-84.00454,"In the middle of GGC map, you will see a beautiful glass building. Go to this building, you will see it at right the front door on your right."));
		locationList.add(new Location("Parking Decal","A",33.97999,-84.00096,"On the main GGC map, you will see this building in L shape at right middle of the map. Go to the front entrance, you will see it on your left."));
		locationList.add(new Location("Registrar","D",33.97950, -83.99831,"It is on the most right middle of the GGC map. "));
		locationList.add(new Location("Soccer Fields","SF",33.97884,-84.00848,"It is on the bottom left of the GGC map, beside the Dorm, and next to the student's parking."));
		locationList.add(new Location("Student Parking Deck","PD",33.98162,-83.99993,"It is on the middle right of the GGC map, it is named Parking Deck on the map."));
		locationList.add(new Location("Student Parking","PK",33.97860,-84.01110,"It is on the most left of the GGC map, next to the soccer field."));
		locationList.add(new Location("Testing Center","A",33.97999,-84.00096,"On the main GGC map, you will see this building in L shape at right middle of the map. Go to the front entrance of this building, you will see it."));
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
	
	/**
	 * This method is to get the latitude of a place on map
	 * @param i: position of the location in location list
	 * @return latitude of the place which has position is i
	 */
	public double getLatitude(int i){
		return locationList.get(i).getLatitude();
	}
	
	/**
	 * This method is to get the latitude of a place on map
	 * @param i: position of the location in location list
	 * @return latitude of the place which has position is i
	 */
	public double getLongitude(int i){
		return locationList.get(i).getLongitude();
	}
}
