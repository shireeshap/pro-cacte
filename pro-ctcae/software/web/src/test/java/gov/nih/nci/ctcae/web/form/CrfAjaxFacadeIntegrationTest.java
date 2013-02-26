package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

/**
 * @author Mehul Gulati
 * Date: Nov 6, 2008
 */
public class CrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private CrfAjaxFacade crfAjaxFacade;
    protected Map parameterMap;

    public void testSearchCRF() throws Exception {
        List<CRF> list = crfAjaxFacade.searchCrf(StudyTestHelper.getDefaultStudy().getId());
        assertNotNull(list);
        assertTrue(list.size()>0);
    }

    public void testGetSymptomsForCrf_GetAttributesForSymptom() throws Exception {
    	
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        List<ProCtcTerm> proCtcTerms = crfAjaxFacade.getSymptomsForCrf(crf.getId());
        assertEquals(10, proCtcTerms.size());
                
        Integer id = proCtcTerms.get(1).getId();
        List<ProCtcQuestionType> questionTypes = fetchProCtcQuestionTypes(proCtcTerms.get(1));
        System.out.println("ProCtcTerm is :"+ proCtcTerms.get(1));
        List<String> attributes = crfAjaxFacade.getAttributesForSymptom(id);
        System.out.println("Attribute size: "+attributes.size());
        System.out.println("Attribute 1: "+attributes.get(0));
        assertEquals(questionTypes.size(), attributes.size());
        assertEquals(questionTypes.get(0).getDisplayName(), attributes.get(0));


    }
    
    @Required
    public void setCRFAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

    public List<ProCtcQuestionType> fetchProCtcQuestionTypes(ProCtcTerm proCtcTerm){
    	return hibernateTemplate.find("select question.proCtcQuestionType from ProCtcQuestion question where question.proCtcTerm = ?", new Object[]{proCtcTerm});
    }
}
