package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.brxml.Column;
import com.semanticbits.rules.brxml.Condition;
import com.semanticbits.rules.brxml.Rule;
import com.semanticbits.rules.brxml.RuleAttribute;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Harsh
 * Date: Mar 31, 2009
 * Time: 12:46:06 PM
 */
public class ProCtcAERule {

    private String ruleId;
    private List<String> symptoms;
    private List<String> questiontypes;
    private List<String> operators;
    private List<String> values;
    private List<String> notifications;
    private String override = "N";

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

    public String getOverride() {
        return override;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    public static ProCtcAERule getProCtcAERule(Rule rule) {
        ProCtcAERule proCtcAERule = new ProCtcAERule();
        proCtcAERule.setRuleId(rule.getId());
        for (RuleAttribute ruleAttribute : rule.getRuleAttribute()) {
            if (ruleAttribute.getName().equals("override")) {
                proCtcAERule.setOverride(ruleAttribute.getValue());
            }
        }
        Condition condition = rule.getCondition();
        List<Column> columns = condition.getColumn();

        int cols = 0;
        for (Column column : columns) {
            if (column.getObjectType().equals(ProCtcTerm.class.getName())) {
                proCtcAERule.setSymptoms(column.getFieldConstraint().get(0).getLiteralRestriction().get(0).getValue());
            }
            if (column.getObjectType().equals(ProCtcQuestionType.class.getName())) {
                cols++;
            }
        }

        String[] arrQuestionTypes = new String[cols];
        String[] arrOperators = new String[cols];
        String[] arrValues = new String[cols];

        for (Column column : columns) {
            if (column.getObjectType().equals(ProCtcQuestionType.class.getName())) {
                String identifier = column.getIdentifier();
                int index = Integer.parseInt(identifier.substring("proCtcQuestionType".length()));
                arrQuestionTypes[index] = column.getFieldConstraint().get(0).getLiteralRestriction().get(0).getValue().get(0);
            }
            if (column.getObjectType().equals(ProCtcValidValue.class.getName())) {
                String identifier = column.getIdentifier();
                int index = Integer.parseInt(identifier.substring("proCtcValidValue".length()));
                arrValues[index] = column.getFieldConstraint().get(0).getLiteralRestriction().get(0).getValue().get(0);
                arrOperators[index] = column.getFieldConstraint().get(0).getLiteralRestriction().get(0).getEvaluator();
            }
        }

        proCtcAERule.setOperators(arrayToList(arrOperators));
        proCtcAERule.setQuestiontypes(arrayToList(arrQuestionTypes));
        proCtcAERule.setValues(arrayToList(arrValues));

        for (String action : rule.getAction()) {
            proCtcAERule.getNotifications().add(action);
        }

        return proCtcAERule;
    }

    private static List<String> arrayToList(String[] arr) {
        List<String> list = new ArrayList<String>();
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                list.add(arr[i]);
            }
        }
        return list;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
