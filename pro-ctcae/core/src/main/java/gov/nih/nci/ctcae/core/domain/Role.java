package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum ProCtcQuestionType.
 *
 * @author Vinay Kumar
 */
public enum Role implements CodedEnum<String> {

    PI("Prinicple Investigator"),
    STUDY_CRA("Study CRA"),
    SITE_PI("Site PI"),
    SITE_CRA("Site CRA"),
    SITE_INVESTIGATOR("Site Investigator"),
    RESEARCH_NURSE("Research Nurse"),
    PARTICIPANT("Participant");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */
    Role(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static Role getByCode(String code) {
        return getByClassAndCode(Role.class, code);
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
    public static Role getByDisplayName(String displayName) {
        for (Role role : Role.values()) {

            if (role.getDisplayName().equals(displayName)) {
                return role;
            }
        }
        return null;
    }

}