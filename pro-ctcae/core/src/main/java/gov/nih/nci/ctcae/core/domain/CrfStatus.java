package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author Vinay Kumar
 */
public enum CrfStatus implements CodedEnum<String> {

    DRAFT("Draft"),
    RELEASED("Released"),
    INPROGRESS("In-progress"),
    SCHEDULED("Scheduled"),
    COMPLETED("Completed");

    private final String displayText;

    CrfStatus(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    public static CrfStatus getByCode(String code) {
        return getByClassAndCode(CrfStatus.class, code);
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


}