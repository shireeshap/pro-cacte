package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.Date;


/**
 * @author Vinay Kumar
 */

public class ClinicalStaffAssignemntRoleTest extends TestCase {

    private ClinicalStaffAssignment clinicalStaffAssignment;

    private ClinicalStaffAssignmentRole clinicalStaffAssignmentRole, duplicateClinicalStaffAssignmentRole;
    protected Date statusDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clinicalStaffAssignment = new ClinicalStaffAssignment();

        clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        duplicateClinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        duplicateClinicalStaffAssignmentRole.setRoleStatus(RoleStatus.IN_ACTIVE);


    }


    public void testEqualsAndHashCode() {

        ClinicalStaffAssignmentRole anotherClinicalStaffAssignmentRole = null;
        clinicalStaffAssignmentRole = null;
        assertEquals(clinicalStaffAssignmentRole, anotherClinicalStaffAssignmentRole);

        clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        assertFalse(clinicalStaffAssignmentRole.equals(anotherClinicalStaffAssignmentRole));

        anotherClinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        assertEquals(anotherClinicalStaffAssignmentRole, clinicalStaffAssignmentRole);
        assertEquals(anotherClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());

        clinicalStaffAssignmentRole.setRole(Role.ADMINISTRATOR);
        assertFalse(anotherClinicalStaffAssignmentRole.equals(clinicalStaffAssignmentRole));

        anotherClinicalStaffAssignmentRole.setRole(Role.ADMINISTRATOR);
        assertEquals(anotherClinicalStaffAssignmentRole, clinicalStaffAssignmentRole);
        assertEquals(anotherClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());


        clinicalStaffAssignmentRole.setClinicalStaffAssignment(clinicalStaffAssignment);
        assertFalse(anotherClinicalStaffAssignmentRole.equals(clinicalStaffAssignmentRole));

        anotherClinicalStaffAssignmentRole.setClinicalStaffAssignment(clinicalStaffAssignment);
        assertEquals(anotherClinicalStaffAssignmentRole, clinicalStaffAssignmentRole);
        assertEquals(anotherClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());

    }

    public void testEqualsAndHashCodeMustNotConsiderStatusCode() {

        assertEquals("must not consider status code", duplicateClinicalStaffAssignmentRole, clinicalStaffAssignmentRole);
        assertEquals("must not consider status code", duplicateClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());


    }

    public void testEqualsAndHashCodeMustNotConsiderStatusDate() {
        clinicalStaffAssignmentRole.setStatusDate(new Date());
        assertEquals("must not consider status date", duplicateClinicalStaffAssignmentRole, clinicalStaffAssignmentRole);
        assertEquals("must not consider status date", duplicateClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());
        duplicateClinicalStaffAssignmentRole.setStatusDate(new Date());
        assertEquals("must not consider status code", duplicateClinicalStaffAssignmentRole.hashCode(), clinicalStaffAssignmentRole.hashCode());


    }

}