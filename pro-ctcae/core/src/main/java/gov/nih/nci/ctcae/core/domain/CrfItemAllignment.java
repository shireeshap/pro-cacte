package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author Vinay Kumar
 * @date 11/17/2008
 */
public enum CrfItemAllignment implements CodedEnum<String> {

    VERTICAL("Vertical"),
    HORIZONTAL("Horizontal");


    private final String displayText;

    CrfItemAllignment(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    public static CrfItemAllignment getByCode(String code) {
        return getByClassAndCode(CrfItemAllignment.class, code);
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