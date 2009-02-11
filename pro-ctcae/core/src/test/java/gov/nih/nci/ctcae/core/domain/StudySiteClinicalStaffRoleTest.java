package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Vinay Kumar
 */

public class StudySiteClinicalStaffRoleTest extends TestCase {

    private StudySiteClinicalStaff studySiteClinicalStaff;

    private StudySiteClinicalStaffRole studySiteClinicalStaffRole, duplicateStudySiteClinicalStaffRole;
    protected Date statusDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        studySiteClinicalStaff = new StudySiteClinicalStaff();

        studySiteClinicalStaffRole = new StudySiteClinicalStaffRole();
        duplicateStudySiteClinicalStaffRole = new StudySiteClinicalStaffRole();
        duplicateStudySiteClinicalStaffRole.setRoleStatus(RoleStatus.IN_ACTIVE);


    }


    public void testEqualsAndHashCode() {

        StudySiteClinicalStaffRole anotherStudySiteClinicalStaffRole = null;
        studySiteClinicalStaffRole = null;
        assertEquals(studySiteClinicalStaffRole, anotherStudySiteClinicalStaffRole);

        studySiteClinicalStaffRole = new StudySiteClinicalStaffRole();
        assertFalse(studySiteClinicalStaffRole.equals(anotherStudySiteClinicalStaffRole));

        anotherStudySiteClinicalStaffRole = new StudySiteClinicalStaffRole();
        assertEquals(anotherStudySiteClinicalStaffRole, studySiteClinicalStaffRole);
        assertEquals(anotherStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());

        studySiteClinicalStaffRole.setRole(Role.ADMINISTRATOR);
        assertFalse(anotherStudySiteClinicalStaffRole.equals(studySiteClinicalStaffRole));

        anotherStudySiteClinicalStaffRole.setRole(Role.ADMINISTRATOR);
        assertEquals(anotherStudySiteClinicalStaffRole, studySiteClinicalStaffRole);
        assertEquals(anotherStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());


        studySiteClinicalStaffRole.setStudySiteClinicalStaff(studySiteClinicalStaff);
        assertFalse(anotherStudySiteClinicalStaffRole.equals(studySiteClinicalStaffRole));

        anotherStudySiteClinicalStaffRole.setStudySiteClinicalStaff(studySiteClinicalStaff);
        assertEquals(anotherStudySiteClinicalStaffRole, studySiteClinicalStaffRole);
        assertEquals(anotherStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());

    }

    public void testEqualsAndHashCodeMustNotConsiderStatusCode() {

        assertEquals("must not consider status code", duplicateStudySiteClinicalStaffRole, studySiteClinicalStaffRole);
        assertEquals("must not consider status code", duplicateStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());


    }

    public void testEqualsAndHashCodeMustNotConsiderStatusDate() {
        studySiteClinicalStaffRole.setStatusDate(new Date());
        assertEquals("must not consider status date", duplicateStudySiteClinicalStaffRole, studySiteClinicalStaffRole);
        assertEquals("must not consider status date", duplicateStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());
        duplicateStudySiteClinicalStaffRole.setStatusDate(new Date());
        assertEquals("must not consider status code", duplicateStudySiteClinicalStaffRole.hashCode(), studySiteClinicalStaffRole.hashCode());


    }

}