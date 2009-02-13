package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.core.Fixture;

/**
 * @author Vinay Kumar
 * @crated Feb 9, 2009
 */
public class StudyParticipantAssignmentTest extends AbstractTestCase {

    private StudySite studySite, anotherStudySite;
    private StudyParticipantAssignment studyParticipantAssignment;


    private ClinicalStaffAssignment clinicalStaffAssignment;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studySite = new StudySite();
        studySite.setOrganization(Fixture.NCI);
        studySite.setStudy(new Study());

        anotherStudySite = new StudySite();
        anotherStudySite.setOrganization(Fixture.DUKE);
        anotherStudySite.setStudy(new Study());

        clinicalStaffAssignment = new ClinicalStaffAssignment();
        //clinicalStaffAssignment.setOrganization(Fixture.NCI);

        studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
    }


}