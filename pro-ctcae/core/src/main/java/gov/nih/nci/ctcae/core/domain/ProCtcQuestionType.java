package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author Vinay Kumar
 */
public enum ProCtcQuestionType implements CodedEnum<String> {

    PRESENT("Present/Not Present"),
    AMOUNT("Amount"),
    SEVERITY("Severity"),
    INTERFERENCE("Interference"),
    FREQUENCY("Frequency");


    private final String displayText;

    ProCtcQuestionType(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    public static ProCtcQuestionType getByCode(String code) {
        return getByClassAndCode(ProCtcQuestionType.class, code);
    }

    public String getCode() {
        return getDisplayName();
    }

    public String getDisplayName() {
        return displayText;
    }


    @Override
    public String toString() {
        return displayText;
    }

    public static ProCtcQuestionType getByDisplayName(String displayName) {
        for (ProCtcQuestionType proCtcQuestionType : ProCtcQuestionType.values()) {

            if (proCtcQuestionType.getDisplayName().equals(displayName)) {
                return proCtcQuestionType;
            }
        }
        return null;
    }

}