package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class StudyParticipantAssignmentTest extends AbstractTestCase {

    private StudySite studySite, anotherStudySite;
    private StudyParticipantAssignment studyParticipantAssignment;

    private StudySiteClinicalStaff studySiteClinicalStaff;

    private StudyParticipantClinicalStaff studyParticipantClinicalStaff, duplicateStudyParticipantClinicalStaff;
    private SiteClinicalStaff siteClinicalStaff;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studySite = new StudySite();
        studySite.setOrganization(Fixture.NCI);
        studySite.setStudy(new Study());

        anotherStudySite = new StudySite();
        anotherStudySite.setOrganization(Fixture.DUKE);
        anotherStudySite.setStudy(new Study());

        siteClinicalStaff = new SiteClinicalStaff();
        siteClinicalStaff.setOrganization(Fixture.NCI);

        studySiteClinicalStaff = new StudySiteClinicalStaff();
        studySiteClinicalStaff.setSiteClinicalStaff(siteClinicalStaff);
        studySite.addStudySiteClinicalStaff(studySiteClinicalStaff);

        studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        studyParticipantClinicalStaff.setStudySiteClinicalStaff(studySiteClinicalStaff);

        duplicateStudyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        duplicateStudyParticipantClinicalStaff.setStudySiteClinicalStaff(studySiteClinicalStaff);

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
    }

    public void testAddStudyParticipantClinicalStaffThrowsException() {
        studySiteClinicalStaff.setStudySite(anotherStudySite);
        try {
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            fail(("site clinical staff does not belongs to NCI. So user must not be able to assign it to NCI study site"));
        } catch (CtcAeSystemException e) {

        }

    }


    public void testMustNotAddDuplicateStudySiteClinicalStaff() {

        studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
        studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
        studyParticipantAssignment.addStudyParticipantClinicalStaff(duplicateStudyParticipantClinicalStaff);


        assertEquals("must not add duplicate study participant clinical staff", 1, studyParticipantAssignment.getStudyParticipantClinicalStaffs().size());
    }
}