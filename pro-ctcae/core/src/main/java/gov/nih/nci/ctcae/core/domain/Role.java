package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum Role.
 *
 * @author Vinay Kumar
 */
public enum Role implements CodedEnum<String> {

    PI("Prinicple Investigator", RoleType.STUDY_LEVEL),
    LEAD_CRA("Lead CRA", RoleType.STUDY_LEVEL),
    ODC("Overall Data Coordinator", RoleType.STUDY_LEVEL),
    CCA("Coordinating Center Administrator", RoleType.STUDY_LEVEL),


    SITE_PI("Site PI", RoleType.STUDY_SITE_LEVEL),
    SITE_CRA("Site CRA", RoleType.STUDY_SITE_LEVEL),

    TREATING_PHYSICIAN("Site Investigator/Treating Physician", RoleType.STUDY_SITE_LEVEL),

    RESEARCH_NURSE("Research Nurse", RoleType.STUDY_SITE_LEVEL),
    PARTICIPANT("Participant", RoleType.SITE_LEVEL);


    /**
     * The display text.
     */
    private final String displayText;
    private final RoleType roleType;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */

    Role(String displayText, RoleType roleType) {
        this.displayText = displayText;
        this.roleType = roleType;

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


    public String getDisplayText() {
        return displayText;
    }

    public RoleType getRoleType() {
        return roleType;
    }
}