package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * The Enum IvrsCallStatus.
 *
 * @author Vinay Gangoli
 */
public enum IvrsCallStatus implements CodedEnum<String> {

	//indicates the status prior to being added to the JMS queue
	PENDING("Pending"),
	//indicates the status after being added to the JMS queue
    SCHEDULED("Scheduled"),
	//indicates the status on successfully completion of the call
    COMPLETED("Completed"),
    //indicates a cancelled call
    CANCELLED("Cancelled"),
    //indicates a failed call
    FAILED("Failed"),
    //covers cases which don't fall into any of the above
    NOTAPPLICABLE("N/A");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new Ivrs Call status.
     *
     * @param displayText the display text
     */
    IvrsCallStatus(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static IvrsCallStatus getByCode(String code) {
        return getByClassAndCode(IvrsCallStatus.class, code);
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