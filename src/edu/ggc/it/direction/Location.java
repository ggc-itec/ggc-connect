package edu.ggc.it.direction;

public class Location {
	private String name;
	private String building;
	private String instruction;

	public Location(String n, String bld, String instr){
		name = n;
		building = bld;
		instruction = instr;
	}
	
	public String getName(){
		return name;
	}
	
	public String getBuilding(){
		return building;
	}

	public String getInstruction(){
		return instruction;
	}
}
