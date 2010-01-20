package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AgarwalH
 * Date: Jan 12, 2010
 * Time: 12:37:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayQuestion {
    private Question question;


    public String getQuestionText() {
        return question.getQuestionText();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<ValidValue> getValidValues() {
        List<ValidValue> validValues = new ArrayList<ValidValue>();
        if (question instanceof ProCtcQuestion) {
            for (ValidValue validValue : ((ProCtcQuestion) question).getValidValues()) {
                validValues.add(validValue);
            }
        } else {
            if (question instanceof MeddraQuestion) {
                for (ValidValue validValue : ((MeddraQuestion) question).getValidValues()) {
                    validValues.add(validValue);
                }
            }
        }
        return validValues;
    }
}
