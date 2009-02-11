package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;

//
/**
 * The Enum Gender.
 */
public enum NegativeAnswers {

    NEVER("Never"),
    NO("No"),
    NONE("None"),
    NOTATALL("Not at all");

    private final String displayText;

    NegativeAnswers(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }

    /**
     * Gets the display text.
     *
     * @return the display text
     */
    public String getDisplayText() {
        return displayText;
    }

    public static boolean contains(String answer) {
        for (NegativeAnswers value : NegativeAnswers.values()) {
            if (value.getDisplayText().toLowerCase().equals(answer.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}