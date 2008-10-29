package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

public enum Gender {
	PLEASESELECT("Please Select"),
	MALE("Male"),
	FEMALE("Female"),
	UNKNOWN("Unknown");
	
	private final String displayText;
	
	Gender(String displayText){
		this.displayText = displayText;
	}
	
	@Override
	public String toString(){
		return displayText;
	}
	
	public static ArrayList<Gender> getAllGenders(){
		ArrayList<Gender> genders = new ArrayList<Gender>();
		for (Gender value : Gender.values()) {
			genders.add(value);
		}
		return genders;
	}
}
