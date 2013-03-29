package gov.nih.nci.ctcae.core.domain;

import java.util.List;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class MeddraQuestionTest extends TestDataManager{
	private static MeddraQuestion meddraQuestion;
	private static MeddraQuestion otherMeddraQuestion;
	private static MeddraQuestionVocab meddraQuestionVocab;
	private static MeddraQuestionVocab otherMeddraQuestionVocab;
	private static String QUESTION_TEXT_ENGLISH = "English question text";
	private static String QUESTION_TEXT_SPANISH = "Spanish question text";
	
	public void testGetterAndSetterForMeddraQuestion(){
		meddraQuestion = new MeddraQuestion();
		meddraQuestion.setLowLevelTerm(fetchLowLevelTerm().get(0));
		meddraQuestion.setDisplayOrder(11);

		assertEquals(Integer.valueOf(11), meddraQuestion.getDisplayOrder());
		assertEquals(fetchLowLevelTerm().get(0), meddraQuestion.getLowLevelTerm());
	}
	
	public void testGetterAndSetterForMeddraQuestionVocab(){
		meddraQuestionVocab = new MeddraQuestionVocab();
		meddraQuestionVocab.setQuestionTextEnglish(QUESTION_TEXT_ENGLISH);
		meddraQuestionVocab.setQuestionTextSpanish(QUESTION_TEXT_SPANISH);
		
		assertEquals(QUESTION_TEXT_ENGLISH, meddraQuestionVocab.getQuestionTextEnglish());
		assertEquals(QUESTION_TEXT_SPANISH, meddraQuestionVocab.getQuestionTextSpanish());
	}
	
	public void testEqualsAndHashCodeForMeddraQuestion(){
		meddraQuestion = new MeddraQuestion();
		meddraQuestion.setLowLevelTerm(fetchLowLevelTerm().get(0));
		meddraQuestion.setDisplayOrder(11);
		
		otherMeddraQuestion = new MeddraQuestion();
		otherMeddraQuestion.setDisplayOrder(11);
		
		// expect assertion to be false as lowLevelTerm, proCtcQuestionType and meddraQuestionVocab as not equal
		assertFalse(otherMeddraQuestion.equals(meddraQuestion));
		assertNotSame(otherMeddraQuestion.hashCode(), meddraQuestion.hashCode());
		
		otherMeddraQuestion.setLowLevelTerm(fetchLowLevelTerm().get(0));
		meddraQuestion.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
		// expect assertion to be false as proCtcQuestionType and meddraQuestionVocab as not equal
		assertFalse(otherMeddraQuestion.equals(meddraQuestion));
		assertNotSame(otherMeddraQuestion.hashCode(), meddraQuestion.hashCode());
		
		otherMeddraQuestion.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
		// expect assertion to be true
		assertTrue(otherMeddraQuestion.equals(meddraQuestion));
		assertEquals(otherMeddraQuestion.hashCode(), meddraQuestion.hashCode());
	}
	
	public void testEqualsAndHashCodeForMeddraQuestionVocab(){
		meddraQuestionVocab = new MeddraQuestionVocab();
		meddraQuestionVocab.setQuestionTextEnglish(QUESTION_TEXT_ENGLISH);
		meddraQuestionVocab.setQuestionTextSpanish(QUESTION_TEXT_SPANISH);
		
		otherMeddraQuestionVocab = new MeddraQuestionVocab();

		// expect assertion to be false as questionTextEnglish is not equal
		assertFalse(otherMeddraQuestionVocab.equals(meddraQuestionVocab));
		assertNotSame(otherMeddraQuestionVocab.hashCode(), meddraQuestionVocab.hashCode());
		
		otherMeddraQuestionVocab.setQuestionTextEnglish(QUESTION_TEXT_ENGLISH);
		// expect assertion to be true as questionTextEnglish is equal (questionTextSpanish is not considered in equals method)
		assertTrue(otherMeddraQuestionVocab.equals(meddraQuestionVocab));
		assertEquals(otherMeddraQuestionVocab.hashCode(), meddraQuestionVocab.hashCode());
	}
	
	
	private List<LowLevelTerm> fetchLowLevelTerm(){
		return hibernateTemplate.find("from LowLevelTerm");
	}
	
}
