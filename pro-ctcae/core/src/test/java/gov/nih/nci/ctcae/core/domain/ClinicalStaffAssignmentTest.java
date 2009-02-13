package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.ListValues;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;


/**
 * @author Mehul Gulati
 *         Date: Oct 17, 2008
 */

public class ClinicalStaffAssignmentTest extends TestCase {

    private ClinicalStaffAssignment clinicalStaffAssignment;

    private ClinicalStaffAssignmentRole clinicalStaffAssignmentRole, duplicateClinicalStaffAssignmentRole;
    protected Date statusDate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clinicalStaffAssignment = new ClinicalStaffAssignment();
        clinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
        statusDate = new Date();
        clinicalStaffAssignment.setDomainObjectId(1);

        clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        duplicateClinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();
        duplicateClinicalStaffAssignmentRole.setRoleStatus(RoleStatus.IN_ACTIVE);


    }

    public void getStudySpeceficRoles() {
        ClinicalStaffAssignmentRole role = new ClinicalStaffAssignmentRole();
        role.setRole(Role.SITE_CRA);
        clinicalStaffAssignment.addClinicalStaffAssignmentRole(role);
        List<ListValues> listValueses = clinicalStaffAssignment.getStudySpeceficRoles();
        assertEquals(2, listValueses.size());
    }

    public void testGetterAndSetter() {

        assertEquals(Organization.class.getName(), clinicalStaffAssignment.getDomainObjectClass());
        assertEquals(Integer.valueOf(1), clinicalStaffAssignment.getDomainObjectId());

    }


    public void testMustNotAddDuplicateClinicalStaffAssignmentRole() {

        clinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);
        clinicalStaffAssignment.addClinicalStaffAssignmentRole(duplicateClinicalStaffAssignmentRole);
        clinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);
        assertEquals("must not add duplicate  site clinical staff roles", 1, clinicalStaffAssignment.getClinicalStaffAssignmentRoles().size());
    }

    public void testEqualsAndHashCode() {

        ClinicalStaffAssignment anothersiteClinicalStaffAssignment = null;
        clinicalStaffAssignment = null;
        assertEquals(anothersiteClinicalStaffAssignment, clinicalStaffAssignment);

        clinicalStaffAssignment = new ClinicalStaffAssignment();
        assertFalse(clinicalStaffAssignment.equals(anothersiteClinicalStaffAssignment));

        anothersiteClinicalStaffAssignment = new ClinicalStaffAssignment();
        assertEquals(anothersiteClinicalStaffAssignment, clinicalStaffAssignment);
        assertEquals(anothersiteClinicalStaffAssignment.hashCode(), clinicalStaffAssignment.hashCode());

        clinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
        assertFalse(clinicalStaffAssignment.equals(anothersiteClinicalStaffAssignment));

        anothersiteClinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
        assertEquals(anothersiteClinicalStaffAssignment, clinicalStaffAssignment);
        assertEquals(anothersiteClinicalStaffAssignment.hashCode(), clinicalStaffAssignment.hashCode());

        Date date = new Date();
        clinicalStaffAssignment.setDomainObjectId(1);
        assertFalse(clinicalStaffAssignment.equals(anothersiteClinicalStaffAssignment));

        anothersiteClinicalStaffAssignment.setDomainObjectId(1);
        assertEquals(anothersiteClinicalStaffAssignment, clinicalStaffAssignment);
        assertEquals(anothersiteClinicalStaffAssignment.hashCode(), clinicalStaffAssignment.hashCode());

    }

}
