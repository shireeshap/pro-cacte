package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

//
/**
 * The Enum Gender.
 */
public enum Gender {

    /**
     * The MALE.
     */
    MALE("Male"),

    /**
     * The FEMALE.
     */
    FEMALE("Female"),

    /**
     * The UNKNOWN.
     */
    UNKNOWN("Unknown");

    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new gender.
     *
     * @param displayText the display text
     */
    Gender(String displayText) {
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
     * Gets the all genders.
     *
     * @return the all genders
     */
    public static ArrayList<Gender> getAllGenders() {
        ArrayList<Gender> genders = new ArrayList<Gender>();
        for (Gender value : Gender.values()) {
            genders.add(value);
        }
        return genders;
    }

}
