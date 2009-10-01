package gov.nih.nci.ctcae.core.rules;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: Harsh
 * Date: Apr 14, 2009
 * Time: 5:30:38 PM
 */
public class ProCtcAEFactResolver {
    protected static final Log logger = LogFactory.getLog(ProCtcAEFactResolver.class);

    public boolean assertFact(CRF crf, ProCtcQuestionType proCtcQuestionType, ProCtcValidValue proCtcValidValue, String questionType, String operator, String validValue) {


        if (!proCtcQuestionType.getCode().toUpperCase().equals(questionType.toUpperCase())) {
            return false;
        }
        TreeSet<ProCtcValidValue> values = getComparisonValues(crf).get(proCtcQuestionType);
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
        logger.debug("Assert.." + "," + crf + "," + proCtcQuestionType + "," + proCtcValidValue + "," + questionType + "," + operator + "," + validValue + " --- " + returnValue);
        return returnValue;
    }

    public static HashMap<ProCtcQuestionType, TreeSet<ProCtcValidValue>> getComparisonValues(CRF crf) {
        TreeSet<ProCtcValidValue> severityOptions = new TreeSet<ProCtcValidValue>(new DisplayOrderComparator());
        TreeSet<ProCtcValidValue> interferenceOptions = new TreeSet<ProCtcValidValue>(new DisplayOrderComparator());
        TreeSet<ProCtcValidValue> presentOptions = new TreeSet<ProCtcValidValue>(new DisplayOrderComparator());
        TreeSet<ProCtcValidValue> amountOptions = new TreeSet<ProCtcValidValue>(new DisplayOrderComparator());
        TreeSet<ProCtcValidValue> frequencyOptions = new TreeSet<ProCtcValidValue>(new DisplayOrderComparator());

        for (CrfPageItem crfPageItem : crf.getAllCrfPageItems()) {
            switch (crfPageItem.getProCtcQuestion().getProCtcQuestionType()) {
                case SEVERITY:
                    for (ProCtcValidValue validValue : crfPageItem.getProCtcQuestion().getValidValues()) {
                        severityOptions.add(validValue);
                    }
                    break;
                case INTERFERENCE:
                    for (ProCtcValidValue validValue : crfPageItem.getProCtcQuestion().getValidValues()) {
                        interferenceOptions.add(validValue);
                    }
                    break;
                case AMOUNT:
                    for (ProCtcValidValue validValue : crfPageItem.getProCtcQuestion().getValidValues()) {
                        amountOptions.add(validValue);
                    }
                    break;
                case FREQUENCY:
                    for (ProCtcValidValue validValue : crfPageItem.getProCtcQuestion().getValidValues()) {
                        frequencyOptions.add(validValue);
                    }
                    break;
                case PRESENT:
                    for (ProCtcValidValue validValue : crfPageItem.getProCtcQuestion().getValidValues()) {
                        presentOptions.add(validValue);
                    }
                    break;
            }
        }

        HashMap<ProCtcQuestionType, TreeSet<ProCtcValidValue>> hashMap = new HashMap<ProCtcQuestionType, TreeSet<ProCtcValidValue>>();
        hashMap.put(ProCtcQuestionType.AMOUNT, amountOptions);
        hashMap.put(ProCtcQuestionType.FREQUENCY, frequencyOptions);
        hashMap.put(ProCtcQuestionType.PRESENT, presentOptions);
        hashMap.put(ProCtcQuestionType.SEVERITY, severityOptions);
        hashMap.put(ProCtcQuestionType.INTERFERENCE, interferenceOptions);

        return hashMap;
    }

}
