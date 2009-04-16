package gov.nih.nci.ctcae.web.rules;

import gov.nih.nci.ctcae.core.domain.*;

import java.util.List;
import java.util.ArrayList;

import com.semanticbits.rules.brxml.*;
import com.semanticbits.rules.objectgraph.FactResolver;


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
        symptoms.add("Pain");

        questiontypes.add("Severity");
        operators.add("==");
        values.add("High");

        questiontypes.add("Frequency");
        operators.add(">");
        values.add("Rare");

        notifications.add("Nurse");
        notifications.add("Physician");

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
        ruleSet.setImport(imports);
        assertEquals(1, ruleSet.getImport().size());
        ruleAuthoringService.createRuleSet(ruleSet);

    }

    public void testDeleteRuleSet() throws Exception {
//        packageName = "gov.nih.nci.ctcae.rules.studysite.study_3.form_1.studysite_15";
        ruleEngineService.deleteRuleSet(packageName);
    }


    public void testAddRule() throws Exception {

        testDeleteRuleSet();
        assertNull(ruleAuthoringService.getRuleSet(packageName, true));
        createRuleSet();

        ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(ruleSet);
        ProCtcAERulesService.createRule(ruleSet, "MyRule", symptoms, questiontypes, operators, values, notifications, "Y");
        ruleEngineService.exportRule(packageName, "c:\\etc\\ctcae");
        assertTrue(true);
        try {
            ruleEngineService.deployRuleSet(ruleSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testExportRuleSet() throws Exception {
        packageName = "gov.nih.nci.ctcae.rules.form.study_3.form_2";
        ruleEngineService.exportRule(packageName, "c:\\etc\\ctcae");
    }

    private List<Object> fireRules(List<Object> inputObjects, String bindURI) {

        List<Object> outputObjects = null;
        try {
            outputObjects = ruleExecutionService.fireRules(bindURI, inputObjects);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputObjects;
    }

    public void testExecuteRuleSet() throws Exception {


        List<Object> inputObjects = new ArrayList<Object>();
        ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertNotNull(ruleSet);

        ProCtcValidValue proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("High");

        ProCtcValidValue proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("Rare");
        inputObjects.add(proCtcValidValue2);
        inputObjects.add(proCtcValidValue1);


        inputObjects.add(ProCtcQuestionType.FREQUENCY);
        inputObjects.add(ProCtcQuestionType.SEVERITY);


        ProCtcTerm proCtcTerm = new ProCtcTerm();
        proCtcTerm.setTerm("Pain");
        inputObjects.add(proCtcTerm);

        FactResolver f = new FactResolver();
        inputObjects.add(f);

        List<Object> outputObjects = ProCtcAERulesService.fireRules(inputObjects, ruleSet.getName());
        System.out.println(outputObjects);
    }

    public void testGetRules() {
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertEquals(1, ruleSet.getRule().size());
    }

    public void testGetProCtcAERule() {
        RuleSet ruleSet = ruleAuthoringService.getRuleSet(packageName, true);
        assertEquals(1, ruleSet.getRule().size());

        ProCtcAERule proCtcAERule = ProCtcAERule.getProCtcAERule(ruleSet.getRule().get(0));

        assertEquals(2, proCtcAERule.getOperators().size());
        assertEquals(2, proCtcAERule.getSymptoms().size());
        assertEquals(2, proCtcAERule.getNotifications().size());
        assertEquals(2, proCtcAERule.getValues().size());
        assertEquals(2, proCtcAERule.getQuestiontypes().size());
        assertEquals("Y", proCtcAERule.getOverride());

    }
}