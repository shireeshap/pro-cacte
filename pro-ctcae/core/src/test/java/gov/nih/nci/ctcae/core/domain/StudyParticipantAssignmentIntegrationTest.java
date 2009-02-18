package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Nov 21, 2008
 */
public class StudyParticipantAssignmentIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

    private StudyParticipantAssignment assignment;

    private Study study;

    private StudySite nciStudySite;
    private Organization nci;

    private Participant participant;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        OrganizationQuery query = new OrganizationQuery();
        query.setMaximumResults(10);
        List<Organization> organizations = new ArrayList<Organization>(organizationRepository.find(query));

        nci = organizations.get(0);


        nciStudySite = new StudySite();
        nciStudySite.setOrganization(nci);


        study = Fixture.createStudy("study short title", "study long title", "assigned identifier");
        study.addStudySite(nciStudySite);

        study = studyRepository.save(study);

        assertNotNull(study.getId());
        assertNotNull(study.getId());

        participant = new Participant();
        participant.setFirstName("John");
        participant.setLastName("Dow");
        participant.setAssignedIdentifier("1234");
        participant = participantRepository.save(participant);
        assertNotNull(participant.getId());
        assertNotNull(participant.getId());


        assignment = new StudyParticipantAssignment();
        assignment.setStudyParticipantIdentifier("id001");
        assignment.setParticipant(participant);
        assignment.setStudySite(nciStudySite);


    }

    public void testSaveAssignment() {
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantAssignmentRepository.save(assignment);
        assertNotNull(studyParticipantAssignment.getId());
        assertNotNull(studyParticipantAssignment.getId());
        assertEquals(studyParticipantAssignment.getStudyParticipantIdentifier(), assignment.getStudyParticipantIdentifier());
        assertEquals(nciStudySite, studyParticipantAssignment.getStudySite());
        assertEquals(participant, studyParticipantAssignment.getParticipant());

        studyParticipantAssignment = studyParticipantAssignmentRepository.findById(assignment.getId());
        assertEquals(studyParticipantAssignment, assignment);


    }


    public void setStudyParticipantAssignmentRepository(final StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }
}
