package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author Mehul Gulati
 * Date: Nov 6, 2008
 */
public class CrfAjaxFacadeIntegrationTest extends AbstractWebIntegrationTestCase {

    private CrfAjaxFacade crfAjaxFacade;
    protected Map parameterMap;
    private List<CRF> crfs;

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
        int origId = 1;
		List<ProCtcQuestion> questions = getProCtcQuestionsForProCtcTerm(proCtcTerms
				.get(origId).getTermEnglish(SupportedLanguageEnum.ENGLISH));
   
        Integer id = proCtcTerms.get(origId).getId();
        System.out.println("ProCtcTerm is :"+ proCtcTerms.get(1));
        List<String> attributes = crfAjaxFacade.getAttributesForSymptom(id);
        System.out.println("Attribute size: "+attributes.size());
        System.out.println("Attribute 1: "+attributes.get(0));
        assertEquals(questions.size(), attributes.size());
        assertEquals(questions.get(0).getProCtcQuestionType().getDisplayName(), attributes.get(0));
        System.out.println("testGetSymptomsForCrf_GetAttributesForSymptom complete..");


    }
    
    public void testSearchCrfs(){
    	crfs = new ArrayList<CRF>();
    	crfs = crfAjaxFacade.searchCrfs(new String[]{}, 0, 25, "version", "asc", Long.valueOf(25));
    	assertEquals(3, crfs.size());
    	
    	String searchByTitle = "PRO Form 1";
    	crfs = crfAjaxFacade.searchCrfs(new String[]{searchByTitle}, 0, 25, "version", "asc", Long.valueOf(25));
    	assertEquals( 1, crfs.size());
    	assertEquals(searchByTitle, crfs.get(0).getTitle());
    }
    
    public void testResultCount(){
    	Long count;
    	String searchByTitle = "PRO Form 1";
    	
    	count = crfAjaxFacade.resultCount(null);
    	assertEquals(3, count.longValue());
    	
    	count = crfAjaxFacade.resultCount(new String[]{searchByTitle});
    	assertEquals(1, count.longValue());
    }
    
    public void testGetReducedCrfs() throws Exception{
    	crfs = crfAjaxFacade.searchCrf(62);
    	assertNotNull(crfs.get(0).getStudy());
    	assertNotNull(crfs.get(0).getEffectiveStartDate());
    	assertNotNull(crfs.get(0).getTitle());
    	assertNotNull(crfs.get(0).getId());
    	
    	assertNotNull(crfs.get(1).getStudy());
    	assertNotNull(crfs.get(1).getEffectiveStartDate());
    	assertNotNull(crfs.get(1).getTitle());
    	assertNotNull(crfs.get(1).getId());
    	
    	crfs = crfAjaxFacade.getReducedCrfs(62);
    	assertNull(crfs.get(0).getStudy());
    	assertNull(crfs.get(0).getEffectiveStartDate());
    	assertNotNull(crfs.get(0).getTitle());
    	assertNotNull(crfs.get(0).getId());
    	
    	assertNull(crfs.get(1).getStudy());
    	assertNull(crfs.get(1).getEffectiveStartDate());
    	assertNotNull(crfs.get(1).getTitle());
    	assertNotNull(crfs.get(1).getId());
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
