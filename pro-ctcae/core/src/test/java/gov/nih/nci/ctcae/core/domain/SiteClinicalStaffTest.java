package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Mehul Gulati
 *         Date: Oct 17, 2008
 */

public class SiteClinicalStaffTest extends TestCase {

    private SiteClinicalStaff siteClinicalStaff;

    private SiteClinicalStaffRole siteClinicalStaffRole, duplicateSiteClinicalStaffRole;
    protected Date statusDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        siteClinicalStaff = new SiteClinicalStaff();
        siteClinicalStaff.setStatusCode("ab");
        statusDate = new Date();
        siteClinicalStaff.setStatusDate(statusDate);

        siteClinicalStaffRole = new SiteClinicalStaffRole();
        duplicateSiteClinicalStaffRole = new SiteClinicalStaffRole();
        duplicateSiteClinicalStaffRole.setRoleStatus(RoleStatus.IN_ACTIVE);


    }

    public void testGetterAndSetter() {

        assertEquals("ab", siteClinicalStaff.getStatusCode());
        assertEquals(statusDate, siteClinicalStaff.getStatusDate());

    }

    public void testRemoveSiteClinicalStaffRoleThrowsException() {
        try {
            siteClinicalStaff.addSiteClinicalStaffRole(siteClinicalStaffRole);
            siteClinicalStaff.getSiteClinicalStaffRoles().remove(0);
            fail(("Remove method is not supported"));
        } catch (UnsupportedOperationException e) {

        }

    }


    public void testMustNotAddDuplicateSiteClinicalStaffRole() {

        siteClinicalStaff.addSiteClinicalStaffRole(siteClinicalStaffRole);
        siteClinicalStaff.addSiteClinicalStaffRole(duplicateSiteClinicalStaffRole);
        siteClinicalStaff.addSiteClinicalStaffRole(siteClinicalStaffRole);
        assertEquals("must not add duplicate  site clinical staff roles", 1, siteClinicalStaff.getSiteClinicalStaffRoles().size());
    }

    public void testEqualsAndHashCode() {

        SiteClinicalStaff anothersiteClinicalStaff = null;
        siteClinicalStaff = null;
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
