package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

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
    PRESENT("Present/Absent", new String[]{"Yes", "No"}),

    /**
     * The AMOUNT.
     */
    AMOUNT("Amount", new String[]{"Not at all", "A little bit", "Somewhat", "Quite a bit", "Very much"}),

    /**
     * The SEVERITY.
     */
    SEVERITY("Severity", new String[]{"None", "Mild", "Moderate", "Severe", "Very severe"}),

    /**
     * The INTERFERENCE.
     */
    INTERFERENCE("Interference", new String[]{"Not at all", "A little bit", "Somewhat", "Quite a bit", "Very much"}),

    /**
     * The FREQUENCY.
     */
    FREQUENCY("Frequency", new String[]{"Never", "Rarely", "Occasionally", "Frequently", "Almost Constantly"});


    /**
     * The display text.
     */
    private final String displayText;
    private final String[] validValues;


    ProCtcQuestionType(String displayText, String[] validValues) {
        this.displayText = displayText;
        this.validValues = validValues;
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
        if (!StringUtils.isBlank(displayName)) {
            for (ProCtcQuestionType proCtcQuestionType : ProCtcQuestionType.values()) {
                if (proCtcQuestionType.getDisplayName().equals(displayName)) {
                    return proCtcQuestionType;
                }
            }
        }
        return null;
    }

    public String[] getValidValues() {
        return validValues;
    }

    public static ArrayList<ProCtcQuestionType> getAllDisplayTypes() {
        ArrayList<ProCtcQuestionType> proCtcQuestionTypes = new ArrayList<ProCtcQuestionType>();

        proCtcQuestionTypes.add(ProCtcQuestionType.FREQUENCY);
        proCtcQuestionTypes.add(ProCtcQuestionType.INTERFERENCE);
        proCtcQuestionTypes.add(ProCtcQuestionType.SEVERITY);
        proCtcQuestionTypes.add(ProCtcQuestionType.AMOUNT);

        return proCtcQuestionTypes;
    }

    public String getDesc() {
        return getDisplayName();
    }

}