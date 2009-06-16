package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 * @since Oct 7, 2008
 */
public class OrganizationTest extends TestCase {
	private Organization organization;

	public void testConstructor() {
		organization = new Organization();
		assertNull(organization.getName());
		assertNull(organization.getNciInstituteCode());
	}

	public void testGetterAndSetter() {
		organization = new Organization();
		organization.setName("National Cancer Institute");
		organization.setNciInstituteCode("NCI");

		assertEquals("National Cancer Institute", organization.getName());
		assertEquals("NCI", organization.getNciInstituteCode());
	}

	public void testToString() {
		organization = new Organization();
		organization.setName("National Cancer Institute");
		organization.setNciInstituteCode("NCI");

		assertEquals("National Cancer Institute ( NCI )", organization.toString());
	}

	public void testGetDisplayName() {
		organization = new Organization();
		organization.setName("National Cancer Institute");

		assertEquals("National Cancer Institute", organization.getDisplayName());
		organization.setNciInstituteCode("NCI");
		assertEquals("National Cancer Institute ( NCI )", organization.getDisplayName());

	}

	public void testEqualsAndHashCode() {
		Organization anotherOrganization = null;
		assertEquals(anotherOrganization, organization);
		organization = new Organization();
		assertFalse(organization.equals(anotherOrganization));
		anotherOrganization = new Organization();
		assertEquals(anotherOrganization, organization);
		assertEquals(anotherOrganization.hashCode(), organization.hashCode());


		organization.setName("National Cancer Institute");
		assertFalse(organization.equals(anotherOrganization));
		anotherOrganization.setName("National Cancer Institute");
		assertEquals(anotherOrganization.hashCode(), organization.hashCode());
		assertEquals(anotherOrganization, organization);

		organization.setNciInstituteCode("NCI");
		assertFalse(organization.equals(anotherOrganization));
		anotherOrganization.setNciInstituteCode("NCI");
		assertEquals(anotherOrganization.hashCode(), organization.hashCode());
		assertEquals(anotherOrganization, organization);

	}

}
