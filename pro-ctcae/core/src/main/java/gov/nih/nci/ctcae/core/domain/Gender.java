package gov.nih.nci.ctcae.core.domain;

public enum Gender {
	MALE("Male"),
	FEMALE("Female"),
	UNKNOWN("Unknown");
	
	private final String displayText;
	
	Gender(String displayText){
		this.displayText = displayText;
	}
	
	public String toString(){
		return displayText;
	}
}
