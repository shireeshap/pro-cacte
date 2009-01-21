package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author Vinay Kumar
 */
public enum CrfCreationMode implements CodedEnum<String> {

    BASIC("Basic"),
    ADVANCE("Advance");

    private final String displayText;

    CrfCreationMode(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    public static CrfCreationMode getByCode(String code) {
        return getByClassAndCode(CrfCreationMode.class, code);
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