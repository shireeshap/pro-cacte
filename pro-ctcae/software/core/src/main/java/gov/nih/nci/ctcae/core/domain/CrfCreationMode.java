package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;
import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum CrfCreationMode.
 *
 * @author Vinay Kumar
 */
public enum CrfCreationMode implements CodedEnum<String> {

    /**
     * The BASIC.
     */
    BASIC("Basic"),

    /**
     * The ADVANCE.
     */
    ADVANCE("Advance");

    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new crf creation mode.
     *
     * @param displayText the display text
     */
    CrfCreationMode(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static CrfCreationMode getByCode(String code) {
        return getByClassAndCode(CrfCreationMode.class, code);
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