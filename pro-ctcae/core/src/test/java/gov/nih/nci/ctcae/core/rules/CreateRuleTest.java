package gov.nih.nci.ctcae.core.rules;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

import java.util.ArrayList;
import java.util.List;


public class CreateRuleTest extends AbstractRulesTest {


    public void testDeleteRuleSet() throws Exception {

        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(ruleSet);
        ruleEngineService.deleteRuleSet(packageName);
        ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNull(ruleSet);
    }

    public void testCreateRuleSet() throws Exception {

        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNull(ruleSet);
        ruleSet = new RuleSet();

        ruleSet.setName(packageName);
        ruleSet.setStatus("Draft");
        ruleSet.setDescription("First Test RuleSet");
        ruleSet.setSubject(subject);
        ruleSet.setCoverage("Not Enabled");

        assertEquals(0, ruleSet.getImport().size());
        ruleAuthoringService.createRuleSet(ruleSet);
        RuleSet savedRuleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(savedRuleSet);
    }

    public void testAddRule() {
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
//        Rule rule = proCtcAERulesService.addRule(ruleSet, "Test Rule");
        //rule.set
//        ruleAuthoringService.createRule(rule);
    }


}