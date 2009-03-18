package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.validation.BindException;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class CreateParticipantControllerTest extends WebTestCase {
    private CreateParticipantController createParticipantController;
    private ParticipantCommand participantCommand;
    private BindException errors;
    private ParticipantRepository participantRepository;
    private OrganizationRepository organizationRepository;
    private ArrayList<Organization> organizations;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createParticipantController = new CreateParticipantController();
        participantCommand = (ParticipantCommand) createParticipantController.formBackingObject(request);
        errors = registerMockFor(BindException.class);
        participantRepository = registerMockFor(ParticipantRepository.class);
        organizationRepository = registerMockFor(OrganizationRepository.class);


        createParticipantController.setParticipantRepository(participantRepository);
        createParticipantController.setOrganizationRepository(organizationRepository);
        createParticipantController.setStudyRepository(studyRepository);

        Organization organization = Fixture.createOrganization("TEST", "TEST");
        Study study = Fixture.createStudyWithStudySite("Test", "Test", "Test", organization);
        organizations = new ArrayList<Organization>();
        organizations.add(Fixture.NCI);
    }

    public void testConstructor() {
        //	assertEquals("participant/createParticipant", createParticipantController.getFormView());
    }

    public void testFormBackingObject() throws Exception {
        assertNotNull(participantCommand);
        assertNotNull(participantCommand.getParticipant());
        assertEquals(0, participantCommand.getOrganizationId());
        assertNull(participantCommand.getSiteName());
        assertNull(participantCommand.getStudySites());
    }

    public void testReferenceData() throws Exception {
        replayMocks();
        //	Map data = createParticipantController.referenceData(request, participantCommand, errors);
        verifyMocks();

//		assertNotNull(data.get("genders"));
//		assertNotNull(data.get("ethnicities"));
//		assertNotNull(data.get("races"));
//		assertNotNull(data.get("studysites"));

    }

}