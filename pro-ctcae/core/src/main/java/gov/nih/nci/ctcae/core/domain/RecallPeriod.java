package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author Vinay Kumar
 */
public enum RecallPeriod implements CodedEnum<String> {

    WEEKLY("over the past 7 days"),
    MONTHLY("over the past 30 days"),
    LAST_CANCER_TREATMENT("since your last cancer treatment");

    private final String displayText;

    RecallPeriod(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    public static RecallPeriod getByCode(String code) {
        return getByClassAndCode(RecallPeriod.class, code);
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