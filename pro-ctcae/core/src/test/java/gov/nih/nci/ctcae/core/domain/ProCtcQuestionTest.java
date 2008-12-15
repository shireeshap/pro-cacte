package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

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
		proCtcQuestion.setQuestionText("How is the pain?");

		assertEquals("How is the pain?", proCtcQuestion.getQuestionText());
	}

	public void testGetFormatedquestionText() {
		proCtcQuestion = new ProCtcQuestion();
		assertTrue(StringUtils.isBlank(proCtcQuestion.getFormattedQuestionText()));
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.FREQUENCY);
		ProCtcTerm term = new ProCtcTerm();
		term.setCtepTerm("Pain");
		proCtcQuestion.setProCtcTerm(term);

		assertEquals("Over the past week, how OFTEN did you have Pain", proCtcQuestion.getFormattedQuestionText());
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.SEVERITY);
		assertEquals("Over the past week, what was the WORST SEVERITY of your Pain", proCtcQuestion.getFormattedQuestionText());
		proCtcQuestion.setProCtcQuestionType(ProCtcQuestionType.INTERFERENCE);
		assertEquals("Over the past week, how much has the Pain INTERFERED with your daily activities", proCtcQuestion.getFormattedQuestionText());
	}

	public void testEqualsAndHashCode() {
		ProCtcQuestion anotherProCtcQuestion = null;
		assertEquals(anotherProCtcQuestion, proCtcQuestion);
		proCtcQuestion = new ProCtcQuestion();
		assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));

		anotherProCtcQuestion = new ProCtcQuestion();
		assertEquals(anotherProCtcQuestion, proCtcQuestion);
		assertEquals(anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());

		proCtcQuestion.setQuestionText("How is the pain?");
		assertFalse(proCtcQuestion.equals(anotherProCtcQuestion));
		anotherProCtcQuestion.setQuestionText("How is the pain?");
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

	public void testEqualsAndHashCodeMustNotConsiderId() {
		ProCtcQuestion anotherProCtcQuestion = new ProCtcQuestion();

		proCtcQuestion = new ProCtcQuestion();

		proCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.setId(2);
		assertEquals("must not consider id", anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
		assertEquals(anotherProCtcQuestion, proCtcQuestion);

	}


	public void testEqualsAndHashCodeMustNotConsiderValidValues() {
		ProCtcQuestion anotherProCtcQuestion = new ProCtcQuestion();
		proCtcQuestion = new ProCtcQuestion();

		proCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.addValidValue(new ProCtcValidValue());
		assertEquals("must not consider valid values", anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
		assertEquals(anotherProCtcQuestion, proCtcQuestion);

	}

	public void testEqualsAndHashCodeMustNotConsiderCrfItems() {
		ProCtcQuestion anotherProCtcQuestion = new ProCtcQuestion();
		proCtcQuestion = new ProCtcQuestion();

		proCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.setQuestionText("How is the pain?");
		anotherProCtcQuestion.getCrfItems().add(new CrfItem());
		assertEquals("must not consider crf items", anotherProCtcQuestion.hashCode(), proCtcQuestion.hashCode());
		assertEquals(anotherProCtcQuestion, proCtcQuestion);

	}

}