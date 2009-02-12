package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class StudySiteTest extends AbstractTestCase {

    private StudySite studySite;

    private StudySiteClinicalStaff studySiteClinicalStaff, duplicateStudySiteClinicalStaff;
    ClinicalStaffAssignment clinicalStaffAssignment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studySite = new StudySite();
        studySite.setOrganization(Fixture.NCI);
        studySite.setStudy(new Study());
        clinicalStaffAssignment = new ClinicalStaffAssignment();
        //clinicalStaffAssignment.setOrganization(Fixture.NCI);


        studySiteClinicalStaff = new StudySiteClinicalStaff();
        studySiteClinicalStaff.setSiteClinicalStaff(clinicalStaffAssignment);

        duplicateStudySiteClinicalStaff = new StudySiteClinicalStaff();
        duplicateStudySiteClinicalStaff.setSiteClinicalStaff(clinicalStaffAssignment);
    }

//    public void testAddStudySiteClinicalStaffThrowsException() {
//        clinicalStaffAssignment.setOrganization(Fixture.DUKE);
//        try {
//            studySite.addStudySiteClinicalStaff(studySiteClinicalStaff);
//            fail(("site clinical staff does not belongs to NCI. So user must not be able to assign it to NCI study site"));
//        } catch (CtcAeSystemException e) {
//
//        }
//
//    }

//    public void testToString() {
//        assertEquals("", studySite.toString());
//
//    }

    public void testMustNotAddDuplicateStudySiteClinicalStaff() {

        studySite.addStudySiteClinicalStaff(studySiteClinicalStaff);
        studySite.addStudySiteClinicalStaff(studySiteClinicalStaff);
        studySite.addStudySiteClinicalStaff(duplicateStudySiteClinicalStaff);

        assertEquals("must not add duplicate study site clinical staff", 1, studySite.getStudySiteClinicalStaffs().size());
    }
}
