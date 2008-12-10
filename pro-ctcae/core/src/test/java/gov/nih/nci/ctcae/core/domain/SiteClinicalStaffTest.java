package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Mehul Gulati
 *         Date: Oct 17, 2008
 */

public class SiteClinicalStaffTest extends TestCase {

	private SiteClinicalStaff siteClinicalStaff;

	public void testGetterAndSetter() {
		siteClinicalStaff = new SiteClinicalStaff();
		siteClinicalStaff.setStatusCode("ab");
		siteClinicalStaff.setStatusDate(new Date());

		assertEquals("ab", siteClinicalStaff.getStatusCode());
		assertEquals(new Date(), siteClinicalStaff.getStatusDate());
	}

	public void testEqualsAndHashCode() {

		SiteClinicalStaff anothersiteClinicalStaff = null;
		assertEquals(anothersiteClinicalStaff, siteClinicalStaff);

		siteClinicalStaff = new SiteClinicalStaff();
		assertFalse(siteClinicalStaff.equals(anothersiteClinicalStaff));

		anothersiteClinicalStaff = new SiteClinicalStaff();
		assertEquals(anothersiteClinicalStaff, siteClinicalStaff);
		assertEquals(anothersiteClinicalStaff.hashCode(), siteClinicalStaff.hashCode());

		siteClinicalStaff.setStatusCode("ab");
		assertFalse(siteClinicalStaff.equals(anothersiteClinicalStaff));

		anothersiteClinicalStaff.setStatusCode("ab");
		assertEquals(anothersiteClinicalStaff, siteClinicalStaff);
		assertEquals(anothersiteClinicalStaff.hashCode(), siteClinicalStaff.hashCode());

		Date date = new Date();
		siteClinicalStaff.setStatusDate(date);
		assertFalse(siteClinicalStaff.equals(anothersiteClinicalStaff));

		anothersiteClinicalStaff.setStatusDate(date);
		assertEquals(anothersiteClinicalStaff, siteClinicalStaff);
		assertEquals(anothersiteClinicalStaff.hashCode(), siteClinicalStaff.hashCode());

	}
}
