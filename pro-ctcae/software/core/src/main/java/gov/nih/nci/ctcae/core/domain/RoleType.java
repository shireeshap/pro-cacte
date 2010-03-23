package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum RoleStatus .
 *
 * @author Vinay Kumar
 */
public enum RoleType implements CodedEnum {

    ADMIN, STUDY_LEVEL,
    SITE_LEVEL, STUDY_SITE_LEVEL, STUDY_COORDINATING_CENTER_LEVEL,
    PARTICIPANT_LEVEL;


    /**
     * Instantiates a new ROLE.
     */
    RoleType() {
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static RoleType getByCode(String code) {
        return getByClassAndCode(RoleType.class, code);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.domain.CodedEnum#getCode()
     */
    public String getCode() {
        return getDisplayName();
    }

    public String getDisplayName() {
        return this.toString();


    }


}