package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.util.Collection;

/**
 * @author mehul
 */

public class ParticipantIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private Participant participant, inValidParticipant;


    private void saveParticipant() {
        participant = new Participant();
        participant.setFirstName("John");
        participant.setLastName("Dow");
        participant.setAssignedIdentifier("1234");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(defaultStudySite);
        studyParticipantAssignment.setStudyParticipantIdentifier("abc");
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
    }

    public void testSaveParticipant() {
        saveParticipant();

        assertNotNull(participant.getId());

    }

    public void testValidationExceptionForSavingInValidParticipant() {
        inValidParticipant = new Participant();

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

}
