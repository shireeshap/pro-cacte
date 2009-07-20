package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.UserQuery;

import java.util.Collection;

/**
 * @author mehul
 */

public class ParticipantIntegrationTest extends TestDataManager {

    private Participant participant;


    private void saveParticipant() {
        ParticipantQuery pq = new ParticipantQuery();
        pq.filterByUsername("1");
        genericRepository.delete(genericRepository.findSingle(pq));
        UserQuery uq = new UserQuery();
        uq.filterByUserName("1");
        genericRepository.delete(genericRepository.findSingle(uq));
        commitAndStartNewTransaction();
        participant = Fixture.createParticipant("John", "Dow", "1234");
        participant.getUser().setUsername("1");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(StudyTestHelper.getDefaultStudy().getLeadStudySite());
        studyParticipantAssignment.setStudyParticipantIdentifier("abc");
        studyParticipantAssignment.setArm(StudyTestHelper.getDefaultStudy().getArms().get(0));
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
    }

    public void testSaveParticipant() {
        saveParticipant();

        assertNotNull(participant.getId());

    }

    public void testValidationExceptionForSavingInValidParticipant() {
        Participant inValidParticipant = new Participant();

        try {
            inValidParticipant = participantRepository.save(inValidParticipant);
        } catch (CtcAeSystemException e) {
            logger.info("expecting this");
        }

        try {
            inValidParticipant.setFirstName("John");
            participantRepository.save(inValidParticipant);
            fail();

        } catch (CtcAeSystemException e) {
            logger.info("expecting this.. last name is missing");
        }

    }

    public void testFindById() {
        saveParticipant();

        Participant existingParticipant = participantRepository
                .findById(participant.getId());
        assertEquals(participant.getFirstName(), existingParticipant
                .getFirstName());
        assertEquals(participant.getLastName(), existingParticipant
                .getLastName());
    }

    public void testFindByFirstName() {
        saveParticipant();

        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantFirstName("J");

        Collection<? extends Participant> participants = participantRepository
                .find(participantQuery);
        assertFalse(participants.isEmpty());
        int size = jdbcTemplate
                .queryForInt("select count(*) from participants participants where lower(participants.first_name ) like '%j%'");

        assertEquals(size, participants.size());

        for (Participant participant : participants) {
            assertTrue(participant.getFirstName().toLowerCase().contains("j"));
        }

    }

    public void testFindByLastName() {
        saveParticipant();

        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantLastName("D");

        Collection<? extends Participant> participants = participantRepository
                .find(participantQuery);
        assertFalse(participants.isEmpty());
        int size = jdbcTemplate
                .queryForInt("select count(*) from participants participants where lower(participants.last_name ) like '%d%'");

        assertEquals(size, participants.size());

        for (Participant participant : participants) {
            assertTrue(participant.getLastName().toLowerCase().contains("d"));
        }

    }

    public void testFindByUserId() {
        saveParticipant();
        login(participant.getUser().getUsername());

        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByUsername(participant.getUser().getUsername());
        Collection<? extends Participant> participants = participantRepository
                .find(participantQuery);
        assertFalse(participants.isEmpty());
        assertEquals(1, participants.size());
    }

}
