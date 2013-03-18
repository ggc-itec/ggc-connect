package edu.ggc.it.direction;
/**
 * This class aims to define a location type. It will hold the name of a location, which building that location on,
 * and instruction how to find this on map.
 * @author Thai Pham 
 * @version 0.1
 *
 */
public class Location {
	//Name of location
	private String name;
	//Building that location is on
	private String building;
	//Instruction to find the location
	private String instruction;
	
	/**
	 * Constructor to create a new location
	 * @param n: to get the name
	 * @param bld: to get the building name
	 * @param instr: to get the instruction
	 */
	public Location(String n, String bld, String instr){
		name = n;
		building = bld;
		instruction = instr;
	}
	
	/**
	 * This method is to get the name of the location
	 * @return name of loction ing String
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * This method is to get the building name where a place on
	 * @return building name in String
	 */
	public String getBuilding(){
		return building;
	}

	/**
	 * This method is to get the instruction of how to find the location on the map
	 * @return instruction in String
	 */
	public String getInstruction(){
		return instruction;
	}
}
