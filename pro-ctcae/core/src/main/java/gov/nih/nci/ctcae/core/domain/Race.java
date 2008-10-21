package gov.nih.nci.ctcae.core.domain;

public enum Race {
	ASIAN("Asian"), WHITE("White"), BLACK("Black or African American"), AMERICANINDIAN(
			"American Indian or Alaska Native"), NATIVEHAWAII(
			"Native Hawaiian or Other PacificIslander"), NOTREPORTED(
			"Not Reported"), UNKNOWN("Unknown");

	private final String displayText;

	Race(String displayText) {
		this.displayText = displayText;
	}

	public String toString() {
		return displayText;
	}
}
