package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

// TODO: Auto-generated Javadoc
/**
 * The Enum RecallPeriod.
 * 
 * @author Vinay Kumar
 */
public enum RecallPeriod implements CodedEnum<String> {

    /** The WEEKLY. */
    WEEKLY("over the past 7 days"),
    
    /** The MONTHLY. */
    MONTHLY("over the past 30 days"),
    
    /** The LAS t_ cance r_ treatment. */
    LAST_CANCER_TREATMENT("since your last cancer treatment");

    /** The display text. */
    private final String displayText;

    /**
     * Instantiates a new recall period.
     * 
     * @param displayText the display text
     */
    RecallPeriod(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     * 
     * @param code the code
     * 
     * @return the by code
     */
    public static RecallPeriod getByCode(String code) {
        return getByClassAndCode(RecallPeriod.class, code);
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