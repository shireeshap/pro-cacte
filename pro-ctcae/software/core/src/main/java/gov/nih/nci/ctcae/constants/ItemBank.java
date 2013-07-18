package gov.nih.nci.ctcae.constants;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.register;
import static gov.nih.nci.cabig.ctms.domain.EnumHelper.sentenceCasedName;
import gov.nih.nci.cabig.ctms.domain.CodedEnum;

public enum ItemBank implements CodedEnum<String> {
	
	PROCTCAE("PRO-CTCAE"), EQ5D3L("EQ5D-3L"), EQ5D5L("EQ5D-5L");

    private String code;

    private ItemBank(String code) {
        this.code = code;
        register(this);
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return sentenceCasedName(this);
    }

    public String getName() {
        return name();
    }

    public static ItemBank getByCode(String code) {
        return getByClassAndCode(ItemBank.class, code);
    }

}
