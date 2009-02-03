package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Enum Race.
 */
public enum Race {

    /** The ASIAN. */
    ASIAN("Asian"), /** The WHITE. */
 WHITE("White"), /** The BLACK. */
 BLACK("Black or African American"), /** The AMERICANINDIAN. */
 AMERICANINDIAN(
            "American Indian or Alaska Native"), 
 /** The NATIVEHAWAII. */
 NATIVEHAWAII(
            "Native Hawaiian or Other PacificIslander"), 
 /** The NOTREPORTED. */
 NOTREPORTED(
            "Not Reported"), 
 /** The UNKNOWN. */
 UNKNOWN("Unknown");

    /** The display text. */
    private final String displayText;

    /**
     * Instantiates a new race.
     * 
     * @param displayText the display text
     */
    Race(String displayText) {
        this.displayText = displayText;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return displayText;
    }

    /**
     * Gets the display text.
     * 
     * @return the display text
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Gets the all races.
     * 
     * @return the all races
     */
    public static ArrayList<Race> getAllRaces() {
        ArrayList<Race> races = new ArrayList<Race>();
        for (Race value : Race.values()) {
            races.add(value);
        }
        return races;
    }
}
