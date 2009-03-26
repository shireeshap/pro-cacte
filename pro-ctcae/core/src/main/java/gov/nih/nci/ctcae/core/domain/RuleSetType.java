package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum RoleStatus .
 *
 * @author Harsh Agarwal
 */
public enum RuleSetType implements CodedEnum<String> {

    FORM_LEVEL("FormLevelRuleSet", "gov.nih.nci.ctcae.rules.form"),
    STUDY_SITE_LEVEL("StudySiteLevelRuleSet", "gov.nih.nci.ctcae.rules.studysite");


    /**
     * The display text.
     */
    private final String displayText;
    private final String packagePrefix;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */
    RuleSetType(String displayText, String packagePrefix) {
        this.displayText = displayText;
        this.packagePrefix = packagePrefix;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static RuleSetType getByCode(String code) {
        return getByClassAndCode(RuleSetType.class, code);
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
    public static RuleSetType getByDisplayName(String displayName) {
        for (RuleSetType roleStatus : RuleSetType.values()) {

            if (roleStatus.getDisplayName().equals(displayName)) {
                return roleStatus;
            }
        }
        return null;
    }

    public String getPackagePrefix(){
        return packagePrefix;
    }

}