package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.api.RuleAuthoringService;
import com.semanticbits.rules.api.RulesEngineService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.ArrayList;

import gov.nih.nci.ctcae.core.domain.*;

/**
 * User: Harsh
 * Date: Mar 24, 2009
 * Time: 1:52:31 PM
 */
public class ProCtcAERulesService {


    static RuleAuthoringService ruleAuthoringService;
    static RulesEngineService rulesEngineService;

    public static RuleSet getRuleSetForForm(CRF crf) {
        String packageName = RuleUtil.getPackageName(RuleSetType.FORM_LEVEL.getPackagePrefix(), crf.getStudy().getId().toString(), crf.getId().toString());
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        if (ruleSet == null) {
            ruleSet = new RuleSet();
            ruleSet.setName(packageName);
            ruleSet.setStatus(RuleStatus.DRAFT.getDisplayName());
            ruleSet.setDescription("RuleSet for Study: " + crf.getStudy().getShortTitle() + ", CRF: " + crf.getTitle());
            ruleSet.setSubject("Form Rules||" + crf.getStudy().getShortTitle() + "||" + crf.getTitle());
            ruleSet.setCoverage("Not Enabled");
            ruleAuthoringService.createRuleSet(ruleSet);
        }
        return ruleSet;
    }


    @Required
    public void setRuleAuthoringService(RuleAuthoringService ruleAuthoringService) {
        this.ruleAuthoringService = ruleAuthoringService;
    }

    @Required
    public void setRulesEngineService(RulesEngineService rulesEngineService) {
        ProCtcAERulesService.rulesEngineService = rulesEngineService;
    }

    public static void createRule(Rule rule) {
        ruleAuthoringService.createRule(rule);
    }

    public static void exportRuleSet(RuleSet ruleSet) throws Exception {
        rulesEngineService.exportRule(ruleSet.getName(), "c:\\etc\\ctcae");
    }

    public static void createRules(RuleSet ruleSet, String ruleName, List<String> symptoms, List<String> questiontypes, List<String> operators, List<String> values, List<String> notifications) {
        Rule rule = new Rule();
        MetaData metaData = new MetaData();
        metaData.setName(ruleName);
        metaData.setPackageName(ruleSet.getName());
        metaData.setDescription("");
        rule.setMetaData(metaData);
        Condition condition = new Condition();
        int j = 0;
        Column column = new Column();
        column.setObjectType(ProCtcTerm.class.getName());
        column.setIdentifier(symptoms.toString());
        for (String questiontype : questiontypes) {
            FieldConstraint fieldConstraint = new FieldConstraint();
            fieldConstraint.setFieldName(questiontype);
            LiteralRestriction literalRestriction = new LiteralRestriction();
            literalRestriction.setEvaluator(operators.get(j));
            List<String> myValues = new ArrayList<String>();
            myValues.add(values.get(j));
            literalRestriction.setValue(myValues);
            fieldConstraint.getLiteralRestriction().add(literalRestriction);
            column.getFieldConstraint().add(fieldConstraint);
            j++;
        }
        condition.getColumn().add(column);
        rule.setCondition(condition);
        rule.setAction(notifications);
        ruleSet.getRule().add(rule);
        createRule(rule);
    }


    public static void removeAllRulesFromRuleSet(RuleSet ruleSet) throws Exception {
        List<String> ruleNames = new ArrayList<String>();
        for (Rule rule : ruleSet.getRule()) {
            ruleNames.add(rule.getMetaData().getName());
        }

        for (String ruleName : ruleNames) {
            rulesEngineService.deleteRule(ruleSet.getName(), ruleName);
        }

    }
}
