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
public enum ProCtcQuestionType implements CodedEnum<String> {

    /**
     * The PRESENT.
     */
    PRESENT("Present/Not Present"),

    /**
     * The AMOUNT.
     */
    AMOUNT("Amount"),

    /**
     * The SEVERITY.
     */
    SEVERITY("Severity"),

    /**
     * The INTERFERENCE.
     */
    INTERFERENCE("Interference"),

    /**
     * The FREQUENCY.
     */
    FREQUENCY("Frequency");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new pro ctc question type.
     *
     * @param displayText the display text
     */
    ProCtcQuestionType(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static ProCtcQuestionType getByCode(String code) {
        return getByClassAndCode(ProCtcQuestionType.class, code);
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
     * Gets the by display name.
     *
     * @param displayName the display name
     * @return the by display name
     */
    public static ProCtcQuestionType getByDisplayName(String displayName) {
        for (ProCtcQuestionType proCtcQuestionType : ProCtcQuestionType.values()) {

            if (proCtcQuestionType.getDisplayName().equals(displayName)) {
                return proCtcQuestionType;
            }
        }
        return null;
    }

}