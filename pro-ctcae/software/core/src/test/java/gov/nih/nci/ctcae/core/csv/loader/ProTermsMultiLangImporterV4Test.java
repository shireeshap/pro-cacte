package gov.nih.nci.ctcae.core.csv.loader;

import gov.nih.nci.ctcae.core.domain.ProCtc;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ProCtcQuestionQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;

import java.io.IOException;
import java.util.List;

public class ProTermsMultiLangImporterV4Test extends TestDataManager{
	
	public static ProCtc proCtc;
	public static ProCtcQuestionRepository proCtcQuestionRepository;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		deleteProCtcTermsInTestData();
	}
	
	public void testUpdateMultiLangProTerms() throws IOException{
        ProCtcTermsImporterV4 proCtcTermImporter = new ProCtcTermsImporterV4();
		proCtcTermImporter.setCtcTermRepository(ctcTermRepository);
		ProCtc proctc = proCtcTermImporter.loadProCtcTerms(true);
		proCtcRepository.save(proctc);
		
		ProCtcQuestionQuery proCtcQuestionQuery = new ProCtcQuestionQuery();
        List<ProCtcQuestion> proCtcQuestions = (List<ProCtcQuestion>) proCtcQuestionRepository.find(proCtcQuestionQuery);
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        List<ProCtcTerm> proCtcTerms = (List<ProCtcTerm>) proCtcTermRepository.find(proCtcTermQuery);
		
		assertEquals(proCtcQuestions.get(0).getProCtcQuestionVocab().getQuestionTextEnglish(), proCtcQuestions.get(0).getProCtcQuestionVocab().getQuestionTextSpanish());
		assertEquals(proCtcTerms.get(0).getProCtcTermVocab().getTermEnglish(), proCtcTerms.get(0).getProCtcTermVocab().getTermSpanish());
		
		ProTermsMultiLangImporterV4 proTermsMultiLangImporter = new ProTermsMultiLangImporterV4();
        proTermsMultiLangImporter.setProCtcQuestionRepository(proCtcQuestionRepository);
        proTermsMultiLangImporter.setProCtcTermRepository(proCtcTermRepository);
        proTermsMultiLangImporter.updateMultiLangProTerms();
        
        assertNotSame(proCtcQuestions.get(0).getProCtcQuestionVocab().getQuestionTextEnglish(), proCtcQuestions.get(0).getProCtcQuestionVocab().getQuestionTextSpanish());
		assertNotSame(proCtcTerms.get(0).getProCtcTermVocab().getTermEnglish(), proCtcTerms.get(0).getProCtcTermVocab().getTermSpanish());
	}
	
	
	public void setProCtcQuestionRepository(ProCtcQuestionRepository proCtcQuestionRepository){
		ProTermsMultiLangImporterV4Test.proCtcQuestionRepository = proCtcQuestionRepository;
	}
	
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
	}
}
