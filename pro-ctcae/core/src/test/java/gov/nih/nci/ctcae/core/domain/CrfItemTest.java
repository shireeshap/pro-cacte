package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import gov.nih.nci.ctcae.core.domain.CrfItem;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */
public class CrfItemTest extends TestCase {
	private CrfItem crfItem;

	public void testConstructor() {
		crfItem = new CrfItem();
		assertEquals(0, crfItem.getDisplayOrder());
	}

	public void testGetterAndSetter() {
		crfItem = new CrfItem();
		crfItem.setDisplayOrder(1);

		assertEquals(1, crfItem.getDisplayOrder());
	}

	public void testEqualsAndHashCode() {

		CrfItem anothercrfItem = null;
		assertEquals(anothercrfItem, crfItem);

		crfItem = new CrfItem();
		assertFalse(crfItem.equals(anothercrfItem));

		anothercrfItem = new CrfItem();
		assertEquals(anothercrfItem, crfItem);
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());

		crfItem.setDisplayOrder(1);
		assertFalse(crfItem.equals(anothercrfItem));

		anothercrfItem.setDisplayOrder(1);
		assertEquals(anothercrfItem.hashCode(), crfItem.hashCode());
		assertEquals(anothercrfItem, crfItem);

	}

}