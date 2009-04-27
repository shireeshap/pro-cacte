package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class ParticipantInboxControllerTest extends WebTestCase {
    private ParticipantInboxController controller;
    private ParticipantRepository participantRepository;
    Participant participant;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ParticipantInboxController();
        participantRepository = registerMockFor(ParticipantRepository.class);

        controller.setParticipantRepository(participantRepository);

        participant = Fixture.createParticipant("test", "test", "test");
        participant.setId(1);
    }

    public void testConstructor() {
        assertEquals("participant/participantInbox", controller.getFormView());
        assertEquals(gov.nih.nci.ctcae.core.domain.Participant.class, controller.getCommandClass());
    }

    public void testFormBackingObject() throws Exception {
        request.setParameter("participantId", "1");
        expect(participantRepository.findById(participant.getId())).andReturn(participant);
        replayMocks();

        Participant p = (Participant) controller.formBackingObject(request);
        verifyMocks();

        assertEquals(participant, p);
    }
}