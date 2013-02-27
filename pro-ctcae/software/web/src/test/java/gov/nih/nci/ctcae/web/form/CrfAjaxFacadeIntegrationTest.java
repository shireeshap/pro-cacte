package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
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
    	System.out.println("testGetSymptomsForCrf_GetAttributesForSymptom starting..");
        CRF crf = StudyTestHelper.getDefaultStudy().getCrfs().get(0);
        List<ProCtcTerm> proCtcTerms = crfAjaxFacade.getSymptomsForCrf(crf.getId());
        assertEquals(10, proCtcTerms.size());
        
		List<ProCtcQuestion> questions = getProCtcQuestionsForProCtcTerm(proCtcTerms
				.get(0).getTermEnglish(SupportedLanguageEnum.ENGLISH));
   
        Integer id = proCtcTerms.get(1).getId();
        System.out.println("ProCtcTerm is :"+ proCtcTerms.get(1));
        List<String> attributes = crfAjaxFacade.getAttributesForSymptom(id);
        System.out.println("Attribute size: "+attributes.size());
        System.out.println("Attribute 1: "+attributes.get(0));
        assertEquals(questions.size(), attributes.size());
        assertEquals(questions.get(0).getProCtcQuestionType().getDisplayName(), attributes.get(0));
        System.out.println("testGetSymptomsForCrf_GetAttributesForSymptom complete..");


    }
    
    private List<ProCtcQuestion> getProCtcQuestionsForProCtcTerm(String proCtcTerm){
    	ProCtcQuestionQuery query = new ProCtcQuestionQuery();
    	query.filterByTerm(proCtcTerm);
    	return genericRepository.find(query);
    }
    
    @Required
    public void setCRFAjaxFacade(CrfAjaxFacade crfAjaxFacade) {
        this.crfAjaxFacade = crfAjaxFacade;
    }

}
