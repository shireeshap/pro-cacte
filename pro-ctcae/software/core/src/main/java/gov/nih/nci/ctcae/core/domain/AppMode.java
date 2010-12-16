package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

/**
 * @author mehul gulati
 * Date: Oct 27, 2010
 */

public enum AppMode implements CodedEnum<String> {

    HOMEWEB("Web"),

    IVRS("IVRS/Automated Telephone"),

    HOMEBOOKLET("Paper Form"),

    CLINICWEB("Web"),

    CLINICBOOKLET("Paper Form");

    private final String displayText;

    AppMode(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);
    }

    public  static AppMode getByCode(String code){
        return getByClassAndCode(AppMode.class, code);
    }

    public String getCode() {
        return getDisplayName();
    }

    public String getDisplayName() {
        return displayText;
    }

    public String getName() {
        return this.name();
    }


    @Override
    public String toString() {
        return displayText;
    }
}
