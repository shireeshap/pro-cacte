package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum CrfStatus.
 *
 * @author Vinay Kumar
 */
public enum CrfStatus implements CodedEnum<String> {

    /**
     * The DRAFT.
     */
    DRAFT("Draft"),

    /**
     * The RELEASED.
     */
    RELEASED("Released"),

    /**
     * The INPROGRESS.
     */
    INPROGRESS("In-progress"),

    /**
     * The SCHEDULED.
     */
    SCHEDULED("Scheduled"),

    /**
     * The COMPLETED.
     */
    COMPLETED("Completed"),

    CANCELLED("Cancelled"),

    PASTDUE("Past-due");



    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new crf status.
     *
     * @param displayText the display text
     */
    CrfStatus(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static CrfStatus getByCode(String code) {
        return getByClassAndCode(CrfStatus.class, code);
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