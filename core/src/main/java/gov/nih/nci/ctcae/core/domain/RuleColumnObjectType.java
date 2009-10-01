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
public enum RuleColumnObjectType implements CodedEnum<String> {

    SYMPTOM(ProCtcTerm.class.getCanonicalName()),
    QUESTION_TYPE(ProCtcQuestionType.class.getCanonicalName());

    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new ROLE.
     *
     * @param displayText the display text
     */
    RuleColumnObjectType(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static RuleColumnObjectType getByCode(String code) {
        return getByClassAndCode(RuleColumnObjectType.class, code);
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
    public static RuleColumnObjectType getByDisplayName(String displayName) {
        for (RuleColumnObjectType roleStatus : RuleColumnObjectType.values()) {

            if (roleStatus.getDisplayName().equals(displayName)) {
                return roleStatus;
            }
        }
        return null;
    }


}