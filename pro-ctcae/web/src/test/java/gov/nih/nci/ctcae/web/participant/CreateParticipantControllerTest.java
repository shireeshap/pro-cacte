package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CreateParticipantControllerTest extends WebTestCase {
	private CreateParticipantController createParticipantController;
	private ParticipantCommand participantCommand;
	private BindException errors;
	private ParticipantRepository participantRepository;
	private FinderRepository finderRepository;
	private OrganizationRepository organizationRepository;
	private StudyRepository studyRepository;
	private ArrayList<Organization> organizations;


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createParticipantController = new CreateParticipantController();
		participantCommand = (ParticipantCommand) createParticipantController.formBackingObject(request);
		errors = registerMockFor(BindException.class);
		participantRepository = registerMockFor(ParticipantRepository.class);
		finderRepository = registerMockFor(FinderRepository.class);
		organizationRepository = registerMockFor(OrganizationRepository.class);
		studyRepository = registerMockFor(StudyRepository.class);


		createParticipantController.setFinderRepository(finderRepository);
		createParticipantController.setParticipantRepository(participantRepository);
		createParticipantController.setOrganizationRepository(organizationRepository);

		Organization organization = Fixture.createOrganization("TEST", "TEST");
		Study study = Fixture.createStudyWithStudySite("Test", "Test", "Test", organization);
		organizations = new ArrayList<Organization>();
		organizations.add(nci);
	}

	public void testConstructor() {
		assertEquals("participant/createParticipant", createParticipantController.getFormView());
	}

	public void testFormBackingObject() throws Exception {
		assertNotNull(participantCommand);
		assertNotNull(participantCommand.getParticipant());
		assertEquals(0, participantCommand.getSiteId());
		assertNull(participantCommand.getSiteName());
		assertNull(participantCommand.getStudyId());
	}

	public void testReferenceData() throws Exception {
		expect(organizationRepository.findOrganizationsForStudySites()).andReturn(organizations);
		replayMocks();
		Map data = createParticipantController.referenceData(request, participantCommand, errors);
		verifyMocks();

		assertNotNull(data.get("genders"));
		assertNotNull(data.get("ethnicities"));
		assertNotNull(data.get("races"));
		assertNotNull(data.get("studysites"));

	}

}