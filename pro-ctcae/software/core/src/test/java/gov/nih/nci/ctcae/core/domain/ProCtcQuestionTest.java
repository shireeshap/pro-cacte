package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcQuestionTest extends TestCase {
    private ProCtcQuestion proCtcQuestion;

    public void testConstructor() {
        proCtcQuestion = new ProCtcQuestion();
    }

    public void testGetterAndSetter() {
        proCtcQuestion = new ProCtcQuestion();
        proCtcQuestion.setQuestionText("How is the pain?", SupportedLanguageEnum.ENGLISH);

        assertEquals("How is the pain", proCtcQuestion.getQuestionText(SupportedLanguageEnum.ENGLISH));
    }


    public void testEqualsAndHashCode() {
        ProCtcQuestion anotherProCtcQuestion = null;
        assertEquals(anotherProCtcQuestion, proCtcQuestion);
        proCtcQuestion = new ProCtcQuestion();
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));

        anotherProCtcQuestion = new ProCtcQuestion();
        assertEquals(anotherProCtcQuestion, proCtcQuestion);
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());

        proCtcQuestion.setQuestionText("How is the pain?", SupportedLanguageEnum.ENGLISH);
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));
        anotherProCtcQuestion.setQuestionText("How is the pain?", SupportedLanguageEnum.ENGLISH);
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
        assertEquals(anotherProCtcQuestion, proCtcQuestion);

        proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));
        anotherProCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
        assertEquals(anotherProCtcQuestion, proCtcQuestion);

        ProCtcTerm proCtcTerm = new ProCtcTerm();
        proCtcQuestion.setProCtcTerm(proCtcTerm);
        assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));
        anotherProCtcQuestion.setProCtcTerm(proCtcTerm);
        assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
        assertEquals(anotherProCtcQuestion, proCtcQuestion);

    }
    
    public void testQuestionEnglishAndSpanishText(){
    	ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
    	String questionEnglishText1 = "How OFTEN did you have NAUSEA";
    	String questionEnglishText2 = "Did you have any RASH";
    	String questionSpanishText1 = "Con qué FRECUENCIA tuvo NÁUSEAS";
    	String questionSpanishText2 = "Tuvo algún SARPULLIDO";
    	
    	proCtcQuestion1.setQuestionText(questionEnglishText1);
    	ProCtcQuestionVocab proCtcQuestionVocab1 = proCtcQuestion1.getProCtcQuestionVocab();
    	assertEquals(proCtcQuestionVocab1.getQuestionTextEnglish(), questionEnglishText1);
    	assertEquals(proCtcQuestion1.getQuestionText(SupportedLanguageEnum.ENGLISH), questionEnglishText1);
    	assertEquals(proCtcQuestion1.getQuestionText(), questionEnglishText1);
    	assertNotSame(proCtcQuestionVocab1.getQuestionTextEnglish(), questionEnglishText2);
   
    	ProCtcQuestion proCtcQuestion2 = new ProCtcQuestion();
    	proCtcQuestion2.setQuestionText(questionSpanishText1, SupportedLanguageEnum.SPANISH);
    	ProCtcQuestionVocab proCtcQuestionVocab2 = proCtcQuestion2.getProCtcQuestionVocab();
    	assertEquals(proCtcQuestionVocab2.getQuestionTextSpanish(), questionSpanishText1);
    	assertEquals(proCtcQuestion2.getQuestionText(SupportedLanguageEnum.SPANISH), questionSpanishText1);
    	assertNotSame(proCtcQuestionVocab2.getQuestionTextSpanish(), questionSpanishText2);
    }

    public void testQuestionSymptoms(){
    	String termEnglishText1 = "Aching muscles";
    	String termSpanishText1 = "Dolor muscular";
    	
    	ProCtcTerm proCtcTerm1 = new ProCtcTerm();
    	ProCtcTermVocab proCtcTermVocab1 = new ProCtcTermVocab();
    	proCtcTermVocab1.setTermEnglish(termEnglishText1);
    	proCtcTermVocab1.setTermSpanish(termSpanishText1);
    	proCtcTerm1.setProCtcTermVocab(proCtcTermVocab1);
    	
    	ProCtcQuestion proCtcQuestion1 = new ProCtcQuestion();
    	proCtcQuestion1.setProCtcTerm(proCtcTerm1);
    	
    	assertEquals(proCtcQuestion1.getQuestionSymptom(), termEnglishText1);
    	assertEquals(proCtcQuestion1.getSymptomGender(),null);
    	
    }




}