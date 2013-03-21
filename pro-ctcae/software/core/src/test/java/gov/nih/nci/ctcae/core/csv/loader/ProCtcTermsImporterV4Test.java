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

public class ProCtcTermsImporterV4Test extends TestDataManager{
	
	public static ProCtc proCtc;
	public static ProCtcQuestionRepository proCtcQuestionRepository;
	
	public void testLoadProCtcTerms() throws IOException{
        
		deleteProCtcTermsInTestData();
		
        ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
        List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertTrue(proCtcQuestions.isEmpty());
        assertTrue(proCtcTerms.isEmpty());
        System.out.println("Total number of questions before are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms before are :" + proCtcTerms.size());
        
        ProCtcTermsImporterV4 proCtcTermImporter = new ProCtcTermsImporterV4();
		proCtcTermImporter.setCtcTermRepository(ctcTermRepository);
		ProCtc proctc = proCtcTermImporter.loadProCtcTerms(true);
		proCtcRepository.save(proctc);
        
        proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertFalse(proCtcQuestions.isEmpty());
        assertFalse(proCtcTerms.isEmpty());
        System.out.println("Total number of questions after are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms after are :" + proCtcTerms.size());
        assertEquals(123, proCtcQuestions.size());
        assertEquals(80, proCtcTerms.size());
	}
	
	
	public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository){
		ProCtcTermsImporterV4Test.proCtcQuestionRepository = proCtcQuestionRepository;
	}
}
