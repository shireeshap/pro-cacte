package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

public enum Ethnicity {
	PLEASESELECT("Please Select"),
	HISPANIC("Hispanic or Latino"),
	NONHISPANIC("Not Hispanic or Latino"),
	NOTREPORTED("Not Reported"),
	UNKNOWN("Unknown");
	
	private final String displayText;
	
	Ethnicity(String displayText){
		this.displayText = displayText;
	}
	
	@Override
	public String toString(){
		return displayText;
	}
	
	public static ArrayList<Ethnicity> getAllEthnicities(){
		ArrayList<Ethnicity> ethnicities = new ArrayList<Ethnicity>();
		for (Ethnicity value : Ethnicity.values()) {
			ethnicities.add(value);
		}
		return ethnicities;
	}
}
