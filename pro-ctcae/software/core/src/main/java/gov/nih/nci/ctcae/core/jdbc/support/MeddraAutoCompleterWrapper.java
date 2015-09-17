package gov.nih.nci.ctcae.core.jdbc.support;

/**
 * @author Amey
 * MeddraAutoCompleterWrapper class
 * Used as a value object for holding meddra low level term which matches to search_string using fuzzy matching algorithm.
 */
public class MeddraAutoCompleterWrapper {
	private String meddraTerm;
	private String soundexRank;
	private String distance;

    public String getMeddraTerm() {
        return meddraTerm;
    }

    public void setMeddraTerm(String meddraTerm) {
        this.meddraTerm = meddraTerm;
    }

    public String getSoundexRank() {
        return soundexRank;
    }

    public void setSoundexRank(String soundexRank) {
        this.soundexRank = soundexRank;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
	public String toString() {
		return meddraTerm;
	}
}