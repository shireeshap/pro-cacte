package gov.nih.nci.ctcae.core.rules;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;

import java.util.ArrayList;
import java.util.List;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

/**
 * User: Harsh
 * Date: Jun 19, 2009
 * Time: 11:26:16 AM
 */
public class ProCtcAERulesServiceTest extends TestDataManager {

    CRF crf;
    Study study;
    List<String> symptoms;
    List<String> questiontypes;
    List<String> operators;
    List<String> values;
    List<String> notifications;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        if (crf == null) {
            study = StudyTestHelper.getDefaultStudy();
            crf = study.getCrfs().get(0);
        }
        symptoms = new ArrayList<String>();
        questiontypes = new ArrayList<String>();
        operators = new ArrayList<String>();
        values = new ArrayList<String>();
        notifications = new ArrayList<String>();
    }

    private void populateConditionData() {
        symptoms.add("Migrane");
        symptoms.add("Pain");

        questiontypes.add("Severity");
        operators.add("==");
        values.add("High");

        questiontypes.add("Frequency");
        operators.add(">");
        values.add("Rare");

        notifications.add("Nurse");
        notifications.add("Physician");

    }

    public void testGetExistingRuleSetForCrf() throws Exception {
        ProCtcAERulesService.deleteExistingRuleSetForCrf(crf);
        assertNull(ProCtcAERulesService.getRuleSetForCrf(crf, false));
    }

    public void testDeleteExistingAndGetNewRuleSetForCrf() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
        assertNotNull(ruleSet);
        assertEquals("gov.nih.nci.ctcae.rules.form.study_" + study.getId() + ".form_" + crf.getId(), ruleSet.getName());
    }

    public void testCreateRule() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
        assertEquals(0, ruleSet.getRule().size());
        populateConditionData();
        ProCtcAERulesService.createRule(ruleSet, symptoms, questiontypes, operators, values, notifications, "Y", false);
        ProCtcAERulesService.deployRuleSet(ruleSet);
        assertEquals(1, ruleSet.getRule().size());
    }

    public void testUpdateRule() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
        assertEquals(0, ruleSet.getRule().size());
        Rule rule = ProCtcAERulesService.createRule(ruleSet, symptoms, questiontypes, operators, values, notifications, "Y", false);
        assertEquals(1, ruleSet.getRule().size());
        ProCtcAERule proCtcAERule = ProCtcAERule.getProCtcAERule(rule);
        assertEquals(0, proCtcAERule.getSymptoms().size());

        populateConditionData();
        rule = ProCtcAERulesService.updateRule(rule.getId(), symptoms, questiontypes, operators, values, notifications, "Y");
        proCtcAERule = ProCtcAERule.getProCtcAERule(rule);
        assertEquals(2, proCtcAERule.getSymptoms().size());
    }

    public void testDeleteRule() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
        assertEquals(0, ruleSet.getRule().size());
        populateConditionData();
        Rule rule = ProCtcAERulesService.createRule(ruleSet, symptoms, questiontypes, operators, values, notifications, "Y", false);
        ProCtcAERulesService.deployRuleSet(ruleSet);
        assertEquals(1, ruleSet.getRule().size());
        String ruleId = rule.getId();

        ProCtcAERulesService.deleteRule(rule.getId(), ruleSet);
        ruleSet = ProCtcAERulesService.getRuleSetForCrf(crf,false);
        assertEquals(0, ruleSet.getRule().size());
        try {
            ProCtcAERulesService.getRuleAuthoringService().getRule(ruleId);
            fail("Should not find rule");
        } catch (Exception ex) {

        }
    }
}
