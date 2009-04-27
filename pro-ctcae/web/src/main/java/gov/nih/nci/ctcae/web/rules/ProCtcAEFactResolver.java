package gov.nih.nci.ctcae.web.rules;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.web.ListValues;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * User: Harsh
 * Date: Apr 14, 2009
 * Time: 5:30:38 PM
 */
public class ProCtcAEFactResolver {

    public boolean assertFact(CRF crf, ProCtcQuestionType proCtcQuestionType, ProCtcValidValue proCtcValidValue, String questionType, String operator, String validValue) {


        if (!proCtcQuestionType.getCode().toUpperCase().equals(questionType.toUpperCase())) {
            return false;
        }
        TreeSet<ProCtcValidValue> values = ListValues.getComparisonValues(crf).get(proCtcQuestionType);
        Iterator<ProCtcValidValue> it = values.iterator();
        boolean found = false;
        int inputValidValue = -1;
        while (it.hasNext()) {
            ProCtcValidValue currentValue = it.next();
            if (currentValue.getValue().toUpperCase().equals(proCtcValidValue.getValue().toUpperCase())) {
                found = true;
                inputValidValue = currentValue.getDisplayOrder();
                break;
            }
        }
        boolean returnValue = false;
        int compareTo = Integer.parseInt(validValue);
        if (found) {
            if (operator.equals("==")) {
                returnValue = inputValidValue == compareTo;
            }
            if (operator.equals("<")) {
                returnValue = inputValidValue < compareTo;
            }
            if (operator.equals(">")) {
                returnValue = inputValidValue > compareTo;
            }
            if (operator.equals("<=")) {
                returnValue = inputValidValue <= compareTo;
            }
            if (operator.equals(">=")) {
                returnValue = inputValidValue >= compareTo;
            }
        }
        System.out.println("Assert.." + "," + crf + "," + proCtcQuestionType + "," + proCtcValidValue + "," + questionType + "," + operator + "," + validValue + " --- " + returnValue);
        return returnValue;
    }

}
