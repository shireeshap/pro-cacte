package gov.nih.nci.ctcae.web.rules;

import org.drools.repository.RulesRepository;
import gov.nih.nci.ctcae.web.rules.AbstractRulesTest;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;

import java.util.List;
import java.util.ArrayList;

import com.semanticbits.rules.brxml.RuleSet;
import com.semanticbits.rules.brxml.Rule;


public class CommonRulesTest extends AbstractRulesTest {

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
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        if (ruleSet == null) {
            createRuleSet();
        }
    }

    private void createRuleSet() {
        RuleSet ruleSet = new RuleSet();

        ruleSet.setName(packageName);
        ruleSet.setStatus("Draft");
        ruleSet.setDescription("First Test RuleSet");
        ruleSet.setSubject(subject);
        ruleSet.setCoverage("Not Enabled");

        assertEquals(0, ruleSet.getImport().size());
        ruleAuthoringService.createRuleSet(ruleSet);

    }

    private void deleteRuleSet() throws Exception {
        ruleEngineService.deleteRuleSet(packageName);
    }


    public void testAddRule() throws Exception {

        deleteRuleSet();
        assertNull(ruleAuthoringService.getRuleSet(packageName, true));
        createRuleSet();

        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(ruleSet);
        CreateFormCommand createFormCommand = new CreateFormCommand();
        createFormCommand.createRules(ruleSet, "MyRule", symptoms, questiontypes, operators, values, notifications);

        assertTrue(true);

    }

    public void testExportRuleSet() throws Exception {
        ruleEngineService.exportRule(packageName, "c:\\etc\\ctcae");
    }

    public void testGetRules(){
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertEquals(1, ruleSet.getRule().size());
    }

}