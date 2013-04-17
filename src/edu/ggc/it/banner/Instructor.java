package edu.ggc.it.banner;

import java.io.Serializable;

public class Instructor implements Serializable{
	private static final long serialVersionUID = -486753106973769596L;
	private String name;
	private String email;
	
	public Instructor(String name, String email){
		this.name = name;
		this.email = email;
	}
	
	public String getName(){
		return name;
	}
	
	public String getEmail(){
		return email;
	}
}
