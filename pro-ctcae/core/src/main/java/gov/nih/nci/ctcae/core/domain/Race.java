package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

public enum Race {
	
ASIAN("Asian"), WHITE("White"), BLACK("Black or African American"), AMERICANINDIAN(
			"American Indian or Alaska Native"), NATIVEHAWAII(
			"Native Hawaiian or Other PacificIslander"), NOTREPORTED(
			"Not Reported"), UNKNOWN("Unknown");

	private final String displayText;

	Race(String displayText) {
		this.displayText = displayText;
	}

	@Override
	public String toString() {
		return displayText;
	}
	public String getDisplayText() {
		return displayText;
	}
	public static ArrayList<Race> getAllRaces(){
		ArrayList<Race> races = new ArrayList<Race>();
		for (Race value : Race.values()) {
			races.add(value);
		}
		return races;
	}
}
