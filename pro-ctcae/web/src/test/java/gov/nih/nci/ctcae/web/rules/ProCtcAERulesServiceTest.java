package gov.nih.nci.ctcae.web.rules;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.helper.CrfTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Arrays;

import com.semanticbits.rules.brxml.RuleSet;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Jun 19, 2009
 * Time: 11:26:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProCtcAERulesServiceTest extends TestDataManager {
    CRF crf;
    Study study;
    ProCtcAERulesService proCtcAERulesService;


    @Override
    protected String[] getConfigLocations() {
        String[] locations = super.getConfigLocations();
        ArrayList<String> l = new ArrayList<String>(Arrays.asList(locations));
        l.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-rules-services.xml");
        return l.toArray(new String[]{});
    }

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        if (crf == null) {
            study = StudyTestHelper.getDefaultStudy();
            crf = study.getCrfs().get(0);
        }
    }

    public void testGetExistingRuleSetForCrf() {
        assertNull(proCtcAERulesService.getExistingRuleSetForCrf(crf));
    }

    public void testDeleteExistingAndGetNewRuleSetForCrf() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);
        assertNotNull(ruleSet);
        assertEquals("gov.nih.nci.ctcae.rules.form.study_" + study.getId() + ".form_" + crf.getId(), ruleSet.getName());
    }

    public void testCreateRule() throws Exception {
        RuleSet ruleSet = ProCtcAERulesService.deleteExistingAndGetNewRuleSetForCrf(crf);

    }
    @Required
    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }
}
