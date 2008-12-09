package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Collection;

/**
 * @author mehul
 */

public class ParticipantIntegrationTest extends AbstractHibernateIntegrationTestCase {

	private ParticipantRepository participantRepository;
	private Participant participant, inValidParticipant;

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction(); // To change body of overridden methods
		// use File | Settings | File Templates.
		login();

		participant = new Participant();
		participant.setFirstName("John");
		participant.setLastName("Dow");
		participant.setAssignedIdentifier("1234");
		participant = participantRepository.save(participant);

	}

	public void testSaveParticipant() {

		assertNotNull(participant.getId());

	}

	public void testValidationExceptionForSavingInValidParticipant() {
		inValidParticipant = new Participant();

		try {
			inValidParticipant = participantRepository.save(inValidParticipant);
		} catch (DataIntegrityViolationException e) {
			logger.info("expecting this");
		}

		try {
			inValidParticipant.setFirstName("John");
			inValidParticipant = participantRepository.save(inValidParticipant);
		} catch (JpaSystemException e) {
			fail();
			logger.info("expecting this.. last name is missing");
		}
		inValidParticipant = new Participant();
		inValidParticipant.setFirstName("John");
		inValidParticipant.setLastName("Dow");
		inValidParticipant.setAssignedIdentifier("1234");
		participantRepository.save(inValidParticipant);
		inValidParticipant = participantRepository.save(inValidParticipant);
		assertNotNull(inValidParticipant.getId());

	}

	public void testFindById() {

		Participant existingParticipant = participantRepository
			.findById(participant.getId());
		assertEquals(participant.getFirstName(), existingParticipant
			.getFirstName());
		assertEquals(participant.getLastName(), existingParticipant
			.getLastName());
	}

	public void testFindByFirstName() {

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

	public void setParticipantRepository(
		ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}
}
