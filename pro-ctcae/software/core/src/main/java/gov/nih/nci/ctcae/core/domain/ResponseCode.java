package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author mehul
 */
public enum ResponseCode implements CodedEnum<String> {

    FORCEDSKIP("-99"),
    MANUALSKIP("-55"),
    NotAsked("-2000");

    private final String displayText;

    ResponseCode(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);
    }

    public String getCode() {
        return getDisplayName();
    }

    public static ResponseCode getByCode(String code) {
        return getByClassAndCode(ResponseCode.class, code);
    }

    public String getDisplayName() {
        return displayText;
    }


    @Override
    public String toString() {
        return displayText;
    }

}
