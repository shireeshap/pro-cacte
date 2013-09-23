package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;

import java.io.IOException;
import java.util.List;

public class ProCtcTermsImporterV4Test extends TestDataManager{
	
	public static ProCtc proCtc;
	
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
		proCtcTermImporter.setProCtcRepository(proCtcRepository);
		proCtcTermImporter.setProCtcTermRepository(proCtcTermRepository);
		ProCtc proctc = proCtcTermImporter.loadProCtcTerms(true);
		proCtcRepository.save(proctc);
        
        proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
        assertFalse(proCtcQuestions.isEmpty());
        assertFalse(proCtcTerms.isEmpty());
        System.out.println("Total number of questions after are :" + proCtcQuestions.size());
        System.out.println("Total number of ProCtcTerms after are :" + proCtcTerms.size());
        assertEquals(121, proCtcQuestions.size());
        assertEquals(79, proCtcTerms.size());
	}
	
	
}
