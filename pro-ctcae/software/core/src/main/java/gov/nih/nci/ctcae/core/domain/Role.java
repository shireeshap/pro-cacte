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

    PI("PI", RoleType.STUDY_LEVEL,"Overall PI"),
    ADMIN("ADMIN", RoleType.ADMIN, "Admin"),
    LEAD_CRA("LEAD_CRA", RoleType.STUDY_LEVEL,"Lead CRA"),
    ODC("ODC", RoleType.STUDY_LEVEL,"Overall Data Coordinator"),
    CCA("CCA", RoleType.STUDY_LEVEL,"Coordinating Centre Administrator"),


    SITE_PI("SITE_PI", RoleType.STUDY_SITE_LEVEL, "Site PI"),
    SITE_CRA("SITE_CRA", RoleType.STUDY_SITE_LEVEL,"Site CRA"),

    TREATING_PHYSICIAN("TREATING_PHYSICIAN", RoleType.STUDY_SITE_LEVEL,"Treating Physician"),
    NURSE("NURSE", RoleType.STUDY_SITE_LEVEL,"Nurse"),


    PARTICIPANT("PARTICIPANT", RoleType.PARTICIPANT_LEVEL,"Participant");


    /**
     * The display text.
     */
    private final String displayText;
    private final RoleType roleType;
    private final String screenText;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */

    Role(String displayText, RoleType roleType,String screenText) {
        this.displayText = displayText;
        this.roleType = roleType;
        this.screenText = screenText;

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

    public String getScreenText() {
        return screenText;
    }
}