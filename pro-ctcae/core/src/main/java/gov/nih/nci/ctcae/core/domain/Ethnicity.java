package gov.nih.nci.ctcae.core.domain;

public enum Ethnicity {
	HISPANIC("Hispanic or Latino"),
	NONHISPANIC("Not Hispanic or Latino"),
	NOTREPORTED("Not Reported"),
	UNKNOWN("Unknown");
	
	private final String displayText;
	
	Ethnicity(String displayText){
		this.displayText = displayText;
	}
	
	public String toString(){
		return displayText;
	}
}
