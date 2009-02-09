package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Vinay Kumar
 */

public class SiteClinicalStaffRoleTest extends TestCase {

    private SiteClinicalStaff siteClinicalStaff;

    private SiteClinicalStaffRole siteClinicalStaffRole, duplicateSiteClinicalStaffRole;
    protected Date statusDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        siteClinicalStaff = new SiteClinicalStaff();

        siteClinicalStaffRole = new SiteClinicalStaffRole();
        duplicateSiteClinicalStaffRole = new SiteClinicalStaffRole();
        duplicateSiteClinicalStaffRole.setRoleStatus(RoleStatus.IN_ACTIVE);


    }


    public void testEqualsAndHashCode() {

        SiteClinicalStaffRole anotherSiteClinicalStaffRole = null;
        siteClinicalStaffRole = null;
        assertEquals(siteClinicalStaffRole, anotherSiteClinicalStaffRole);

        siteClinicalStaffRole = new SiteClinicalStaffRole();
        assertFalse(siteClinicalStaffRole.equals(anotherSiteClinicalStaffRole));

        anotherSiteClinicalStaffRole = new SiteClinicalStaffRole();
        assertEquals(anotherSiteClinicalStaffRole, siteClinicalStaffRole);
        assertEquals(anotherSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());

        siteClinicalStaffRole.setRole(Role.ADMINISTRATOR);
        assertFalse(anotherSiteClinicalStaffRole.equals(siteClinicalStaffRole));

        anotherSiteClinicalStaffRole.setRole(Role.ADMINISTRATOR);
        assertEquals(anotherSiteClinicalStaffRole, siteClinicalStaffRole);
        assertEquals(anotherSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());


        siteClinicalStaffRole.setSiteClinicalStaff(siteClinicalStaff);
        assertFalse(anotherSiteClinicalStaffRole.equals(siteClinicalStaffRole));

        anotherSiteClinicalStaffRole.setSiteClinicalStaff(siteClinicalStaff);
        assertEquals(anotherSiteClinicalStaffRole, siteClinicalStaffRole);
        assertEquals(anotherSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());

    }

    public void testEqualsAndHashCodeMustNotConsiderStatusCode() {

        assertEquals("must not consider status code", duplicateSiteClinicalStaffRole, siteClinicalStaffRole);
        assertEquals("must not consider status code", duplicateSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());


    }

    public void testEqualsAndHashCodeMustNotConsiderStatusDate() {
        siteClinicalStaffRole.setStatusDate(new Date());
        assertEquals("must not consider status date", duplicateSiteClinicalStaffRole, siteClinicalStaffRole);
        assertEquals("must not consider status date", duplicateSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());
        duplicateSiteClinicalStaffRole.setStatusDate(new Date());
        assertEquals("must not consider status code", duplicateSiteClinicalStaffRole.hashCode(), siteClinicalStaffRole.hashCode());


    }

}