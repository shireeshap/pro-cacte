package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.ProctcTermTypeBasedCategoryEnum;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.ProctcaeGradeMappingVersionQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermIntegrationTest extends TestDataManager {

    private ProCtcTerm proProCtcTerm;
    private ProctcaeGradeMappingVersion pgmv;

    @Override
    protected void onSetUpInTransaction() throws Exception {
    	super.onSetUpInTransaction();
    	pgmv = getDefaultProctcaeGradeMappingVersion();
    }
     

    
    public void testDeleteNotSupported() {
        try {
            proCtcTermRepository.delete(new ProCtcTerm());
            fail("Expecting UnsupportedOperationException: delete is not supported for ProCtcTerm.");
        } catch (CtcAeSystemException e) {
        	assertTrue(true);
        }
    }
    

    public void testFindAndInitialize() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository.find(proCtcTermQuery);
        ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();

        proProCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
        assertEquals(proProCtcTerm.getCtcTerm().getCtepCode(), firstProProCtcTerm.getCtcTerm().getCtepCode());
        assertEquals(proProCtcTerm.getCtcTerm().getCtepTerm(), firstProProCtcTerm.getCtcTerm().getCtepTerm());
        assertEquals(proProCtcTerm.getCtcTerm().getSelect(), firstProProCtcTerm.getCtcTerm().getSelect());
        assertEquals(proProCtcTerm.getProCtcTermVocab().getTermEnglish(), firstProProCtcTerm.getProCtcTermVocab().getTermEnglish());
        assertEquals(proProCtcTerm, firstProProCtcTerm);
    }

    public void testFindAndInitializeById() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);
        ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();


        ProCtcTerm proCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
        assertEquals(proCtcTerm, firstProProCtcTerm);
    }

    public void testFilterByCtcTermHavingQuestionsOnly() {
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.filterByCtcTermHavingQuestionsOnly();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);

        assertFalse("must find atleast one ctc term", ctcTerms.isEmpty());
        for (ProCtcTerm proCtcTerm : ctcTerms) {
            assertFalse("must find atleast one question for each term", proCtcTerm.getProCtcQuestions().isEmpty());

        }
    }

    public void testFindByQuery() {
        int size = jdbcTemplate
                .queryForInt("select count(*) from PRO_CTC_TERMS");
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        proCtcTermQuery.setMaximumResults(size + 1000);
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository
                .find(proCtcTermQuery);

        assertFalse(ctcTerms.isEmpty());
        assertEquals(size, ctcTerms.size());
    }
    
    public void testSaveProCtcTermWithGradeMappings(){
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        Collection<? extends ProCtcTerm> ctcTerms = proCtcTermRepository.find(proCtcTermQuery);
    	ProCtcTerm firstProProCtcTerm = ctcTerms.iterator().next();
    	proProCtcTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
    	
    	List<ProctcaeGradeMapping> pcgmList = buildPcgmList(proProCtcTerm);
    	proProCtcTerm.getProCtcGradeMappings().clear();
    	proProCtcTerm.addAllProCtcGradeMappings(pcgmList);
    	proCtcTermRepository.save(proProCtcTerm);
    	commitAndStartNewTransaction();
    	ProCtcTerm reloadedTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
    	
    	assertEquals(4, reloadedTerm.getProCtcGradeMappings().size());
    	for(ProctcaeGradeMapping pcgm : pcgmList){
    		assertTrue(reloadedTerm.getProCtcGradeMappings().contains(pcgm));
    	}
    	proProCtcTerm.getProCtcGradeMappings().clear();
    	proCtcTermRepository.save(proProCtcTerm);
    	commitAndStartNewTransaction();
    	
    	reloadedTerm = proCtcTermRepository.findById(firstProProCtcTerm.getId());
    	assertEquals(reloadedTerm.getProCtcGradeMappings().size(), 0);
    }

    
    public void testGetTypeBasedCategoryS(){
    	Integer proCtcSysId = 10003;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_S, category);
    }
    
    public void testGetTypeBasedCategoryAMT(){
    	Integer proCtcSysId = 10028;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_AMT, category);
    }
    
    public void testGetTypeBasedCategoryPA(){
    	Integer proCtcSysId = 10032;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_PA, category);
    }
    
    
    public void testGetTypeBasedCategoryFS(){
    	Integer proCtcSysId = 10055;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_FS, category);
    }
    
    public void testGetTypeBasedCategoryFI(){
    	Integer proCtcSysId = 10027;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_FI, category);
    }

    
    public void testGetTypeBasedCategorySI(){
    	Integer proCtcSysId = 10079;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_SI, category);
    }
    
    public void testGetTypeBasedCategoryFSI(){
    	Integer proCtcSysId = 10005;
    	ProCtcTerm proctcTerm = proCtcTermRepository.findBySystemId(proCtcSysId);
    	
    	ProctcTermTypeBasedCategoryEnum category = proctcTerm.getTypeBasedCategory();
    	assertEquals(ProctcTermTypeBasedCategoryEnum.CATEGORY_FSI, category);
    }
    

	private List<ProctcaeGradeMapping> buildPcgmList(ProCtcTerm term) {
		List<ProctcaeGradeMapping> pcgmList = new ArrayList<ProctcaeGradeMapping>();
		
		ProctcaeGradeMappingVersion proCtcGradeMappingVersion = pgmv;
		pcgmList.add(new ProctcaeGradeMapping(0, true, ProctcaeGradeMapping.ZERO, proCtcGradeMappingVersion, term));
		pcgmList.add(new ProctcaeGradeMapping(1, true, ProctcaeGradeMapping.PRESENT_CLINICIAN_ASSESS, proCtcGradeMappingVersion, term));

		pcgmList.add(new ProctcaeGradeMapping(1, false, ProctcaeGradeMapping.ONE, proCtcGradeMappingVersion, term));
		pcgmList.add(new ProctcaeGradeMapping(1, 2, 0, ProctcaeGradeMapping.TWO, proCtcGradeMappingVersion, term));
		
		return pcgmList;
	}


}
