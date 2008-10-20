package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcTermTest extends TestCase {
	private ProCtcTerm proCtcTerm;

	public void testConstructor() {
		proCtcTerm = new ProCtcTerm();
		assertNull(proCtcTerm.getQuestionText());
	}

	public void testGetterAndSetter() {
		proCtcTerm = new ProCtcTerm();
		proCtcTerm.setQuestionText("How is the pain?");

		assertEquals("How is the pain?", proCtcTerm.getQuestionText());
	}

	public void testEqualsAndHashCode() {
		ProCtcTerm anotherProCtcTerm = null;
		assertEquals(anotherProCtcTerm, proCtcTerm);
		proCtcTerm = new ProCtcTerm();
		assertFalse(proCtcTerm.equals(anotherProCtcTerm));
		anotherProCtcTerm = new ProCtcTerm();
		assertEquals(anotherProCtcTerm, proCtcTerm);
		assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());

		proCtcTerm.setQuestionText("How is the pain?");
		assertFalse(proCtcTerm.equals(anotherProCtcTerm));

		anotherProCtcTerm.setQuestionText("How is the pain?");
		assertEquals(anotherProCtcTerm.hashCode(), proCtcTerm.hashCode());
		assertEquals(anotherProCtcTerm, proCtcTerm);

	}

}