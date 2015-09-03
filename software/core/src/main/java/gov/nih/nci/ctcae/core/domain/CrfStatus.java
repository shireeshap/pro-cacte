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
    OFFSTUDY("OffStudy"),
    PASTDUE("Past-due"),
    ONHOLD("On-hold"),
    NOTAPPLICABLE("N/A");
    
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
    
    public static CrfStatus getCrfStatus(String status){
		CrfStatus crfStatus = null;
		if(status != null){
			if(status.equals(CrfStatus.DRAFT.getName())){
				crfStatus = crfStatus.DRAFT;
			} else if(status.equals(CrfStatus.RELEASED.getName())){
				crfStatus = crfStatus.RELEASED;
			} else if(status.equals(CrfStatus.SCHEDULED.getName())){
				crfStatus = crfStatus.SCHEDULED;
			} else if(status.equals(CrfStatus.INPROGRESS.getName())){
				crfStatus = crfStatus.INPROGRESS;
			} else if(status.equals(CrfStatus.COMPLETED.getName())){
				crfStatus = crfStatus.COMPLETED;
			} else if(status.equals(CrfStatus.CANCELLED.getName())){
				crfStatus = crfStatus.CANCELLED;
			} else if(status.equals(CrfStatus.OFFSTUDY.getName())){
				crfStatus = crfStatus.OFFSTUDY;
			} else if(status.equals(CrfStatus.PASTDUE.getName())){
				crfStatus = crfStatus.PASTDUE;
			} else if(status.equals(CrfStatus.ONHOLD.getName())){
				crfStatus = crfStatus.ONHOLD;
			} else if(status.equals(CrfStatus.NOTAPPLICABLE.getName())){
				crfStatus = crfStatus.NOTAPPLICABLE;
			}			
		}
		return crfStatus;
	}
	
    public String getName() {
        return this.name();
    }


}