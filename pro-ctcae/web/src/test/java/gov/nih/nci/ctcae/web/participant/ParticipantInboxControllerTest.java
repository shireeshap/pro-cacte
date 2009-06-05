package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

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
        List<Participant> participants = new ArrayList<Participant>();
        participants.add(participant);
        User user = new User();
        user.setUsername("test");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "test"));
        expect(participantRepository.find(isA(ParticipantQuery.class))).andReturn(participants);
        replayMocks();

        Participant p = (Participant) controller.formBackingObject(request);
        verifyMocks();

        assertEquals(participant, p);
    }
}