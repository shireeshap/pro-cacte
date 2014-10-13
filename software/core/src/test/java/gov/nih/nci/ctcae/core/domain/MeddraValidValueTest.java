package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

/**
 * @author AmeyS
 * Test cases for MeddraValidValue and MeddraValidValueVocab
 */
public class MeddraValidValueTest extends TestDataManager{
	private static MeddraValidValue meddraValidValue;
	private static MeddraValidValue otherMeddraValidValue;
	private static MeddraValidValueVocab meddraValidValueVocab;
	private static MeddraValidValueVocab otherMeddraValidValueVocab;
	private static MeddraQuestion meddraQuestion;
	private static String QUESTION_TEXT_ENGLISH = "English question text";
	private static String QUESTION_TEXT_SPANISH = "Spanish question text";
	private static String VALID_VALUE_VOCAB_TEXT_ENGLISH = "English valid value text";
	private static String VALID_VALUE_VOCAB_TEXT_SPANISH = "Spanish valid value text";
	
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		meddraQuestion = new MeddraQuestion();
		meddraQuestion.setQuestionText(QUESTION_TEXT_ENGLISH, SupportedLanguageEnum.ENGLISH);
		meddraQuestion.setQuestionText(QUESTION_TEXT_SPANISH, SupportedLanguageEnum.SPANISH);
	}
	
	public void testGetterAndSetterForMeddraValidValue(){
		meddraValidValue = new MeddraValidValue();
		meddraValidValue.setMeddraQuestion(meddraQuestion);
		meddraValidValue.setDisplayOrder(11);
		meddraValidValue.setMeddraValidValueVocab(null);
		
		assertEquals(Integer.valueOf(11), meddraValidValue.getDisplayOrder());
		assertEquals(meddraQuestion, meddraValidValue.getMeddraQuestion());
		assertNull(meddraValidValue.getMeddraValidValueVocab());
	}
	
	public void testGetterAndSetterForMeddraValidValueVocab(){
		meddraValidValueVocab = new MeddraValidValueVocab();
		meddraValidValueVocab.setValueEnglish(VALID_VALUE_VOCAB_TEXT_ENGLISH);
		meddraValidValueVocab.setValueSpanish(VALID_VALUE_VOCAB_TEXT_SPANISH);
		meddraValidValueVocab.setMeddraValidValue(null);
		
		assertEquals(VALID_VALUE_VOCAB_TEXT_ENGLISH, meddraValidValueVocab.getValueEnglish());
		assertEquals(VALID_VALUE_VOCAB_TEXT_SPANISH, meddraValidValueVocab.getValueSpanish());
		assertNull(meddraValidValueVocab.getMeddraValidValue());
	}
	
	public void testEqualsAndHashCodeForMeddraValidValue(){
		meddraValidValue = new MeddraValidValue();
		meddraValidValue.setMeddraQuestion(meddraQuestion);
		meddraValidValue.setDisplayOrder(11);
		
		otherMeddraValidValue = new MeddraValidValue();

		// expect the assertion to be false as displayOrder, meddraQuestion and meddraValidValueVocab are not equal 
		assertFalse(otherMeddraValidValue.equals(meddraValidValue));
		assertNotSame(otherMeddraValidValue.hashCode(), meddraValidValue.hashCode());
		
		otherMeddraValidValue.setDisplayOrder(11);
		// expect the assertion to be false as meddraQuestion and meddraValidValueVocab are not equal 
		assertFalse(otherMeddraValidValue.equals(meddraValidValue));
		assertNotSame(otherMeddraValidValue.hashCode(), meddraValidValue.hashCode());
		
		otherMeddraValidValue.setMeddraQuestion(meddraQuestion);
		// expect assertion to be true
		assertTrue(otherMeddraValidValue.equals(meddraValidValue));
		assertEquals(otherMeddraValidValue.hashCode(), meddraValidValue.hashCode());
	}
	
	public void testEqualsAndHashCodeForMeddraValidValueVocab(){
		meddraValidValueVocab = new MeddraValidValueVocab();
		meddraValidValueVocab.setValueEnglish(VALID_VALUE_VOCAB_TEXT_ENGLISH);
		
		otherMeddraValidValueVocab = new MeddraValidValueVocab();
		
		// expect assertion to be false as valueEnglish is not equal
		assertFalse(otherMeddraValidValueVocab.equals(meddraValidValueVocab));
		assertNotSame(otherMeddraValidValueVocab.hashCode(), meddraValidValueVocab.hashCode());
		
		otherMeddraValidValueVocab.setValueEnglish(VALID_VALUE_VOCAB_TEXT_ENGLISH);
		// expect assertion to be true 
		assertTrue(otherMeddraValidValueVocab.equals(meddraValidValueVocab));
		assertEquals(otherMeddraValidValueVocab.hashCode(), meddraValidValueVocab.hashCode());
	}

}
