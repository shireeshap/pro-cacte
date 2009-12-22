package gov.nih.nci.ctcae.core.rules;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;

/**
 * User: Harsh
 * Date: Apr 14, 2009
 * Time: 4:44:43 PM
 */
public class QuestionTypeAndValueBeanForRules {
    ProCtcQuestionType proCtcQuestionType;
    ProCtcValidValue proCtcValidValue;

    public String getQuestionTypeAndValue() {
        return proCtcQuestionType.getCode() + "-" + proCtcValidValue.getValue();
    }
}
