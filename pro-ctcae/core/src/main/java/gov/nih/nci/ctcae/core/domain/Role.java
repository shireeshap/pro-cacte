package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

import java.util.ArrayList;
import java.util.List;

//
/**
 * The Enum Role.
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
    PARTICIPANT("Participant"),
    CRA("CRA"),
    PHYSICAN("Physican"),
    ADMINISTRATOR("Administrator");


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

    public static List<Role> getStudySpeceficRoles(Role role) {
        List<Role> roles = new ArrayList<Role>();
        if (role.equals(Role.CRA) || role.equals(Role.STUDY_CRA) || role.equals(Role.SITE_CRA)) {
            roles.add(Role.STUDY_CRA);
            roles.add(Role.SITE_CRA);

        }
        if (role.equals(Role.PHYSICAN) || role.equals(Role.SITE_PI) || role.equals(Role.PI) || role.equals(Role.SITE_INVESTIGATOR)) {
            roles.add(Role.SITE_PI);
            roles.add(Role.PI);
            roles.add(Role.SITE_INVESTIGATOR);
        }
        return roles;
    }


    public static List<Role> getStudyLevelRole() {
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.CRA);
        roles.add(Role.PHYSICAN);
        return roles;

    }
}