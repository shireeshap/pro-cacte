package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

import java.util.ArrayList;

//
/**
 * The Enum Gender.
 */
public enum Gender implements CodedEnum<String> {

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
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */
    Gender(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static Gender getByCode(String code) {
        return getByClassAndCode(Gender.class, code);
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cabig.ctms.domain.CodedEnum#getCode()
    */
    public String getCode() {
        return getDisplayName();
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cabig.ctms.domain.CodedEnum#getDisplayName()
    */
    public String getDisplayName() {
        return displayText;
    }


    /* (non-Javadoc)
    * @see java.lang.Enum#toString()
    */
    @Override
    public String toString() {
        return displayText;
    }

    /**
     * Gets the Role by display name.
     *
     * @param displayName the display name
     * @return the by display name
     */
    public static Gender getByDisplayName(String displayName) {
        for (Gender gender : Gender.values()) {

            if (gender.getDisplayName().equals(displayName)) {
                return gender;
            }
        }
        return null;
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
