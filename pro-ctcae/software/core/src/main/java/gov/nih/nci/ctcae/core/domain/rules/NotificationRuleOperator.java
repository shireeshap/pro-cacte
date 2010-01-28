package gov.nih.nci.ctcae.core.domain.rules;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;

//
/**
 * The Enum CrfStatus.
 *
 * @author Vinay Kumar
 */
public enum NotificationRuleOperator implements CodedEnum<String> {

    /**
     * The DRAFT.
     */
    EQUAL("is equal to"),

    /**
     * The RELEASED.
     */
    GREATER("is greater than"),

    /**
     * The INPROGRESS.
     */
    GREATER_EQUAL(" is greater than or equal to"),

    /**
     * The SCHEDULED.
     */
    LESS("is less than"),

    /**
     * The COMPLETED.
     */
    LESS_EQUAL("is less than or equal to");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new crf status.
     *
     * @param displayText the display text
     */
    NotificationRuleOperator(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static NotificationRuleOperator getByCode(String code) {
        return getByClassAndCode(NotificationRuleOperator.class, code);
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


}