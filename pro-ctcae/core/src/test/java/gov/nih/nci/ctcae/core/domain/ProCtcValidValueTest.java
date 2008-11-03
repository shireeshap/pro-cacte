package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class ProCtcValidValueTest extends TestCase {
	private ProCtcValidValue proCtcValidValue;

	public void testConstructor() {
		proCtcValidValue = new ProCtcValidValue();
		assertNull(proCtcValidValue.getValue());
	}

	public void testGetterAndSetter() {
		proCtcValidValue = new ProCtcValidValue();
		proCtcValidValue.setValue("Severe");

		assertEquals("Severe", proCtcValidValue.getValue());
	}

	public void testEqualsAndHashCode() {
		ProCtcValidValue anotherProCtcValidValue = null;
		
		assertEquals(anotherProCtcValidValue, proCtcValidValue);
		proCtcValidValue = new ProCtcValidValue();
		assertFalse(proCtcValidValue.equals(anotherProCtcValidValue));
		
		anotherProCtcValidValue = new ProCtcValidValue();
		assertEquals(anotherProCtcValidValue, proCtcValidValue);
		assertEquals(anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());

		proCtcValidValue.setValue("Severe");
		assertFalse(proCtcValidValue.equals(anotherProCtcValidValue));

		anotherProCtcValidValue.setValue("Severe");
		assertEquals(anotherProCtcValidValue.hashCode(), proCtcValidValue.hashCode());
		assertEquals(anotherProCtcValidValue, proCtcValidValue);

	}

}