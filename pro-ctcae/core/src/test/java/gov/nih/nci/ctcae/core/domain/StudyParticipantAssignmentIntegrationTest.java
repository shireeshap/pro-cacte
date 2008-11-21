package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantAssignmentRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

/**
 * @author Vinay Kumar
 * @crated Nov 21, 2008
 */
public class StudyParticipantAssignmentIntegrationTest extends AbstractJpaIntegrationTestCase {

    private StudyParticipantAssignmentRepository studyParticipantAssignmentRepository;

    private StudyParticipantAssignment assignment;

    private StudyRepository studyRepository;
    private Study study;

    private StudySite nciStudySite;
    private Organization nci;
    private OrganizationRepository organizationRepository;

    private ParticipantRepository participantRepository;
    private Participant participant;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        nci = Fixture.createOrganization("National Cancer Institute", "NCI");


        nci = organizationRepository.save(nci);


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


    }


    public void setOrganizationRepository(final OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public void setParticipantRepository(final ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void setStudyRepository(final StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setStudyParticipantAssignmentRepository(final StudyParticipantAssignmentRepository studyParticipantAssignmentRepository) {
        this.studyParticipantAssignmentRepository = studyParticipantAssignmentRepository;
    }
}
