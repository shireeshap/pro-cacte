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

    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;

    private StudyParticipantClinicalStaff studyParticipantClinicalStaff, duplicateStudyParticipantClinicalStaff;
    private OrganizationClinicalStaff organizationClinicalStaff;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studySite = new StudySite();
        studySite.setOrganization(Fixture.NCI);
        studySite.setStudy(new Study());

        anotherStudySite = new StudySite();
        anotherStudySite.setOrganization(Fixture.DUKE);
        anotherStudySite.setStudy(new Study());

        organizationClinicalStaff = new OrganizationClinicalStaff();
        organizationClinicalStaff.setOrganization(Fixture.NCI);

        studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(organizationClinicalStaff);
        studySite.addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        studyParticipantClinicalStaff.setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        duplicateStudyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        duplicateStudyParticipantClinicalStaff.setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
    }

    public void testAddStudyParticipantClinicalStaffThrowsException() {
        studyOrganizationClinicalStaff.setStudyOrganization(anotherStudySite);
        try {
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            fail(("site clinical staff does not belongs to NCI. So user must not be able to assign it to NCI study site"));
        } catch (CtcAeSystemException e) {

        }

    }

    public void testAddStudyParticipantClinicalStaffThrowsExceptionIfStudySiteIsCoordinatingCenter() {
        studyOrganizationClinicalStaff.setStudyOrganization(new StudyCoordinatingCenter());
        try {
            studyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);
            fail(("study organization clinical staff deos not belong to a study site %s. it belongs to study coordinating center"));
        } catch (CtcAeSystemException e) {

        }

    }


}