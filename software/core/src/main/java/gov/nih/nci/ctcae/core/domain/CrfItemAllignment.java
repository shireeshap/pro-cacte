package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.domain.CodedEnum;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

import static gov.nih.nci.cabig.ctms.domain.CodedEnumHelper.getByClassAndCode;

//
/**
 * The Enum CrfItemAllignment.
 *
 * @author Vinay Kumar
 * @date 11/17/2008
 */
public enum CrfItemAllignment implements CodedEnum<String> {

    /**
     * The VERTICAL.
     */
    VERTICAL("Vertical"),

    /**
     * The HORIZONTAL.
     */
    HORIZONTAL("Horizontal");


    /**
     * The display text.
     */
    private final String displayText;

    /**
     * Instantiates a new crf item allignment.
     *
     * @param displayText the display text
     */
    CrfItemAllignment(String displayText) {
        this.displayText = displayText;
        CodedEnumHelper.register(this);

    }


    /**
     * Gets the by code.
     *
     * @param code the code
     * @return the by code
     */
    public static CrfItemAllignment getByCode(String code) {
        return getByClassAndCode(CrfItemAllignment.class, code);
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