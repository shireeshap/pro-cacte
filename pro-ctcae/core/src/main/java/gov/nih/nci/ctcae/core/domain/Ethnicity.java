package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

//
/**
 * The Enum Ethnicity.
 */
public enum Ethnicity {

    /**
     * The HISPANIC.
     */
    HISPANIC("Hispanic or Latino"),

    /**
     * The NONHISPANIC.
     */
    NONHISPANIC("Not Hispanic or Latino"),

    /**
     * The NOTREPORTED.
     */
    NOTREPORTED("Not Reported"),

    /**
     * The UNKNOWN.
     */
    UNKNOWN("Unknown");

    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new ethnicity.
     *
     * @param displayText the display text
     */
    Ethnicity(String displayText) {
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
     * Gets the all ethnicities.
     *
     * @return the all ethnicities
     */
    public static ArrayList<Ethnicity> getAllEthnicities() {
        ArrayList<Ethnicity> ethnicities = new ArrayList<Ethnicity>();
        for (Ethnicity value : Ethnicity.values()) {
            ethnicities.add(value);
        }
        return ethnicities;
    }
}
