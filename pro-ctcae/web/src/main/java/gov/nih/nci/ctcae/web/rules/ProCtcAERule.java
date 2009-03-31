package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * User: Harsh
 * Date: Mar 31, 2009
 * Time: 12:46:06 PM
 */
public class ProCtcAERule {

    private List<String> symptoms;
    private List<String> questiontypes;
    private List<String> operators;
    private List<String> values;
    private List<String> notifications;


    public ProCtcAERule() {
        symptoms = new ArrayList<String>();
        questiontypes = new ArrayList<String>();
        operators = new ArrayList<String>();
        values = new ArrayList<String>();
        notifications = new ArrayList<String>();
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getQuestiontypes() {
        return questiontypes;
    }

    public void setQuestiontypes(List<String> questiontypes) {
        this.questiontypes = questiontypes;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<String> notifications) {
        this.notifications = notifications;
    }

    public static ProCtcAERule getProCtcAERule(Rule rule) {
        ProCtcAERule proCtcAERule = new ProCtcAERule();
        Condition condition = rule.getCondition();
        List<Column> columns = condition.getColumn();
        Column firstColumn = columns.get(0); //We should have only one column
        String strSymptoms = firstColumn.getIdentifier();
        proCtcAERule.setSymptoms(new ArrayList(StringUtils.commaDelimitedListToSet(strSymptoms)));
        for (FieldConstraint fieldConstraint : firstColumn.getFieldConstraint()) {
            proCtcAERule.getQuestiontypes().add(fieldConstraint.getFieldName());
            LiteralRestriction firstRestriction = fieldConstraint.getLiteralRestriction().get(0);
            proCtcAERule.getOperators().add(firstRestriction.getEvaluator());
            proCtcAERule.getValues().add(firstRestriction.getValue().get(0));
        }

        for (String action : rule.getAction()) {
            proCtcAERule.getNotifications().add(action);
        }

        return proCtcAERule;
    }
}
