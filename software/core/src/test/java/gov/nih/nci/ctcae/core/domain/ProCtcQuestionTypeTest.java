package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */
public class ProCtcQuestionTypeTest extends TestCase {

	public void testStatus() {
		ProCtcQuestionType proCtcQuestionType = ProCtcQuestionType.FREQUENCY;
		assertEquals("Frequency", proCtcQuestionType.toString());
		proCtcQuestionType = ProCtcQuestionType.INTERFERENCE;
		assertEquals("Interference", proCtcQuestionType.toString());
		proCtcQuestionType = ProCtcQuestionType.SEVERITY;
		assertEquals("Severity", proCtcQuestionType.toString());
	}

	public void testGetByCode() {
		assertEquals(ProCtcQuestionType.FREQUENCY, ProCtcQuestionType.getByCode("Frequency"));
		assertEquals(ProCtcQuestionType.INTERFERENCE, ProCtcQuestionType.getByCode("Interference"));
		assertEquals(ProCtcQuestionType.SEVERITY, ProCtcQuestionType.getByCode("Severity"));

	}


}