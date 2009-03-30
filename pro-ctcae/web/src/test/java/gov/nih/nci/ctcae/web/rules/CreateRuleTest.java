package gov.nih.nci.ctcae.web.rules;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.ctcae.web.rules.AbstractRulesTest;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import gov.nih.nci.ctcae.web.WebTestCase;

import javax.servlet.http.HttpServletRequest;


public class CreateRuleTest extends AbstractRulesTest {
    List<String> symptoms = new ArrayList<String>();
    List<String> questiontypes = new ArrayList<String>();
    List<String> operators = new ArrayList<String>();
    List<String> values = new ArrayList<String>();
    List<String> notifications = new ArrayList<String>();

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        symptoms.add("Migrane");
        questiontypes.add("Severity");
        operators.add("gt");
        values.add("High");
        notifications.add("Nurse");

    }

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
        assertNotNull(ruleSet);
        CreateFormCommand createFormCommand = new CreateFormCommand();
        createFormCommand.createRules(ruleSet, "MyRule", symptoms, questiontypes, operators, values, notifications);
        assertTrue(true);

    }


}