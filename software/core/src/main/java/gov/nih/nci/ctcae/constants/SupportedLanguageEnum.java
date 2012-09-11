package gov.nih.nci.ctcae.constants;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.register;
import static gov.nih.nci.cabig.ctms.domain.EnumHelper.sentenceCasedName;
import gov.nih.nci.cabig.ctms.domain.CodedEnum;

public enum SupportedLanguageEnum implements CodedEnum<String> {
    ENGLISH("English"), SPANISH("Spanish");

    private String code;

    private SupportedLanguageEnum(String code) {
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

    public static SupportedLanguageEnum getByCode(String code) {
        return getByClassAndCode(SupportedLanguageEnum.class, code);
    }
}