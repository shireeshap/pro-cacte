package gov.nih.nci.ctcae.core.rules;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.semanticbits.rules.brxml.RuleSet;

/**
 * User: Harsh
 * Date: Jun 19, 2009
 * Time: 11:26:16 AM
 */
public class ProCtcAERulesServiceTest extends TestDataManager {
    public void testDummy() {

    }

//    CRF crf;
//    Study study;
//    List<String> symptoms = new ArrayList<String>();
//    List<String> questiontypes = new ArrayList<String>();
//    List<String> operators = new ArrayList<String>();
//    List<String> values = new ArrayList<String>();
//    List<String> notifications = new ArrayList<String>();
//
//
//    @Override
//    protected void onSetUpInTransaction() throws Exception {
//        super.onSetUpInTransaction();
//        if (crf == null) {
//            study = StudyTestHelper.getDefaultStudy();
//            crf = study.getCrfs().get(0);
//        }
//    }
//
//    public void testGetExistingRuleSetForCrf() {
//        assertNull(proCtcAERulesService.getExistingRuleSetForCrf(crf));
//    }
//
//    public void testDeleteExistingAndGetNewRuleSetForCrf() throws Exception {
//        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
//        assertNotNull(ruleSet);
//        assertEquals("gov.nih.nci.ctcae.rules.form.study_" + study.getId() + ".form_" + crf.getId(), ruleSet.getName());
//    }
//
//    public void testCreateRule() throws Exception {
//        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
//
//        symptoms.add("Migrane");
//        symptoms.add("Pain");
//
//        questiontypes.add("Severity");
//        operators.add("==");
//        values.add("High");
//
//        questiontypes.add("Frequency");
//        operators.add(">");
//        values.add("Rare");
//
//        notifications.add("Nurse");
//        notifications.add("Physician");
//        assertEquals(0, ruleSet.getRule().size());
//
//        ProCtcAERulesService.createRule(ruleSet, "My Test Rule", symptoms, questiontypes, operators, values, notifications, "Y");
//        ProCtcAERulesService.deployRuleSet(ruleSet);
//        assertEquals(1, ruleSet.getRule().size());
//    }
//
}
