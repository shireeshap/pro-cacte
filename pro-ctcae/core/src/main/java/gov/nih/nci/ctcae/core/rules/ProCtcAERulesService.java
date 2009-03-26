package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.utils.RuleUtil;
import com.semanticbits.rules.api.RuleAuthoringService;
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


    RuleAuthoringService ruleAuthoringService;

    public RuleSet getRuleSetForForm(CRF crf) {
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
    public RuleAuthoringService getRuleAuthoringService() {
        return ruleAuthoringService;
    }

    @Required
    public void setRuleAuthoringService(RuleAuthoringService ruleAuthoringService) {
        this.ruleAuthoringService = ruleAuthoringService;
    }

    public static Rule addEmptyRule(CRF crf, RuleSet ruleSet) {
        Rule newRule = new Rule();
        MetaData metaData = new MetaData();
        metaData.setName("Rule " + ruleSet.getRule().size());
        newRule.setMetaData(metaData);

        Condition condition = newCondition();
        newRule.setCondition(condition);
        Column column = condition.getColumn().get(0);
        column.setObjectType(RuleColumnObjectType.SYMPTOM.getDisplayName());
//        column.setIdentifier();


        List<String> action = new ArrayList<String>();
        newRule.setAction(action);
        ruleSet.getRule().add(newRule);
        return newRule;
    }

    private static Condition newCondition() {
        Condition condition = new Condition();
        Column column = newColumn();
        condition.getColumn().add(column);
        return condition;
    }

    private static Column newColumn() {
        Column column = new Column();
        FieldConstraint fieldConstraint = newFieldConstraint();
        column.getFieldConstraint().add(fieldConstraint);
        return column;
    }

    private static FieldConstraint newFieldConstraint() {
        FieldConstraint fieldConstraint = new FieldConstraint();
        LiteralRestriction literalRestriction = new LiteralRestriction();
        fieldConstraint.getLiteralRestriction().add(literalRestriction);
        return fieldConstraint;
    }

}
