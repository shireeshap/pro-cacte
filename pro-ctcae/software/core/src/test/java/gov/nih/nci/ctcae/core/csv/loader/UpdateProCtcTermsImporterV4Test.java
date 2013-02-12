package gov.nih.nci.ctcae.core.csv.loader;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuery;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;

public class UpdateProCtcTermsImporterV4Test extends TestDataManager{
	
	public static ProCtc proCtc;
	public static ProCtcQuestionRepository proCtcQuestionRepository;
	
	public void testUpdateProCtcTermsImporterV4whenQuestionsNotPresent() throws IOException{
        
		UpdateProCtcTermsImporterV4 updateProCtcTerms = new UpdateProCtcTermsImporterV4();
		deleteProCtcTermsInTestData();
		proCtc = getProCtc();
        updateProCtcTerms.setProCtcQuestionRepository(proCtcQuestionRepository);
        updateProCtcTerms.setCtcTermRepository(ctcTermRepository);
        updateProCtcTerms.setProCtcTermRepository(proCtcTermRepository);
        updateProCtcTerms.setProCtcRepository(proCtcRepository);
        
        ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
        List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertTrue(proCtcQuestions.isEmpty());
        assertTrue(proCtcTerms.isEmpty());
        
        updateProCtcTerms.updateProCtcTerms(proCtc);
        proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertFalse(proCtcQuestions.isEmpty());
        assertFalse(proCtcTerms.isEmpty());
        System.out.println("Total number of questions after are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms after are :" + proCtcTerms.size());
        
	}
	
	public void testUpdateProCtcTermsImporterV4whenQuestionsArePresent() throws IOException{
        
		deleteProCtcTermsInTestData();
		UpdateProCtcTermsImporterV4 updateProCtcTerms = new UpdateProCtcTermsImporterV4();
		proCtc = getProCtc();
        updateProCtcTerms.setProCtcQuestionRepository(proCtcQuestionRepository);
        updateProCtcTerms.setCtcTermRepository(ctcTermRepository);
        updateProCtcTerms.setProCtcTermRepository(proCtcTermRepository);
        updateProCtcTerms.setProCtcRepository(proCtcRepository);
        updateProCtcTerms.updateProCtcTerms(proCtc);
        
        ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
        List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertFalse(proCtcQuestions.isEmpty());
        assertFalse(proCtcTerms.isEmpty());
        System.out.println("Total number of questions before are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms before are :" + proCtcTerms.size());
        
        updateProCtcTerms.updateProCtcTerms(proCtc);
        proCtcQuestions.clear();
        proCtcTerms.clear();
        proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        System.out.println("Total number of questions (whenQuestionsArePresent) after are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms (whenQuestionsArePresent) after are :" + proCtcTerms.size());
        
        assertEquals(121, proCtcQuestions.size());
        assertEquals(78, proCtcTerms.size());
	}
	
	private ProCtc getProCtc(){
		ProCtc proCtc;
		
		ProCtcQuery proCtcQuery = new ProCtcQuery();
		proCtcQuery.filterByProCtcVersion("4.0");
		proCtc = proCtcRepository.findSingle(proCtcQuery);
		if(proCtc == null){
			proCtc = new ProCtc();
	        proCtc.setProCtcVersion("4.0");
	        proCtc.setReleaseDate(new Date());
	        proCtc = proCtcRepository.save(proCtc);
		}
		return proCtc;
	}
	
	public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository){
		UpdateProCtcTermsImporterV4Test.proCtcQuestionRepository = proCtcQuestionRepository;
	}
	
	public ProCtcQuestionRepository getProCtcQuestionRepository(GenericRepository genericRepository){
		ProCtcQuestionRepository proCtcQuestionRepository = new ProCtcQuestionRepository();
		proCtcQuestionRepository.setGenericRepository(genericRepository);
		return proCtcQuestionRepository;
	}
}
