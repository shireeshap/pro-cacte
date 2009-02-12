package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment;
import gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignmentRole;
import gov.nih.nci.ctcae.web.WebTestCase;

/**
 * @author Mehul Gulati
 *         Date: Nov 5, 2008
 */
public class ClinicalStaffCommandTest extends WebTestCase {
    private ClinicalStaffCommand clinicalStaffCommand;
    private ClinicalStaffAssignment clinicalStaff1Assignment;
    private ClinicalStaffAssignment clinicalStaff2Assignment;
    private ClinicalStaffAssignment clinicalStaff3Assignment;
    private ClinicalStaffAssignment clinicalStaff4Assignment;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        clinicalStaffCommand = new ClinicalStaffCommand();
        clinicalStaff1Assignment = new ClinicalStaffAssignment();
//        clinicalStaff1Assignment.setOrganization(Fixture.createOrganization("org1", "nci1"));
//
//        clinicalStaff2Assignment = new ClinicalStaffAssignment();
//        clinicalStaff2Assignment.setOrganization(Fixture.createOrganization("org2", "nci2"));
//
//        clinicalStaff3Assignment = new ClinicalStaffAssignment();
//        clinicalStaff3Assignment.setOrganization(Fixture.createOrganization("org3", "nci3"));
//
//        clinicalStaff4Assignment = new ClinicalStaffAssignment();
//        clinicalStaff4Assignment.setOrganization(Fixture.createOrganization("org4", "nci4"));

    }

    public void testEmptyConstructor() {
        assertNotNull("must instantiate site", clinicalStaffCommand.getClinicalStaff());
    }

    public void testRemoveClinicalStaffAssignments() {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff1Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff2Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff3Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff4Assignment);
        assertEquals("must be 4 clinical staff sites", 4, clinicalStaff.getClinicalStaffAssignments().size());

        clinicalStaffCommand.setClinicalStaffAssignmentIndexToRemove(String.valueOf(2));

        clinicalStaffCommand.apply();

        assertEquals("must remove 3 clinical staff sites", 3, clinicalStaff.getClinicalStaffAssignments().size());

        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff1Assignment));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff2Assignment));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff4Assignment));


    }

    public void testRemoveClinicalStaffAssignmentRole() {
        ClinicalStaff clinicalStaff = clinicalStaffCommand.getClinicalStaff();
        clinicalStaff1Assignment.addClinicalStaffAssignmentRole(new ClinicalStaffAssignmentRole());
        clinicalStaff1Assignment.addClinicalStaffAssignmentRole(new ClinicalStaffAssignmentRole());
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff1Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff2Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff3Assignment);
        clinicalStaff.addClinicalStaffAssignment(clinicalStaff4Assignment);

        ClinicalStaffAssignment clinicalStaffAssignment = clinicalStaff.getClinicalStaffAssignments().get(0);
        assertEquals("must be 4 clinical staff sites", 4, clinicalStaff.getClinicalStaffAssignments().size());
        assertEquals("must have 2 clinical staff site roles", 2, clinicalStaffAssignment.getClinicalStaffAssignmentRoles().size());

        // clinicalStaffCommand.setObjectsIdsToRemove("1,3");

        clinicalStaffCommand.setClinicalStaffAssignmentRoleIndexToRemove("0-1");
        clinicalStaffCommand.apply();

        assertEquals("must not remove any clinical staff sites", 4, clinicalStaff.getClinicalStaffAssignments().size());
        clinicalStaffAssignment = clinicalStaff.getClinicalStaffAssignments().get(0);

        assertEquals("must remove 1 clinical staff site roles", 1, clinicalStaffAssignment.getClinicalStaffAssignmentRoles().size());

        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff1Assignment));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff2Assignment));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff3Assignment));
        assertTrue("must preserve order of clinical staff sites", clinicalStaff.getClinicalStaffAssignments().contains(clinicalStaff4Assignment));


    }


}
