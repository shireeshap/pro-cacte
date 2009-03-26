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
public enum RuleStatus implements CodedEnum<String> {

    DRAFT("Draft");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */
    RuleStatus(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static RuleStatus getByCode(String code) {
        return getByClassAndCode(RuleStatus.class, code);
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
    public static RuleStatus getByDisplayName(String displayName) {
        for (RuleStatus roleStatus : RuleStatus.values()) {

            if (roleStatus.getDisplayName().equals(displayName)) {
                return roleStatus;
            }
        }
        return null;
    }

}