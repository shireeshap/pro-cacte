package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.form.CreateFormCommand;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.springframework.validation.BindException;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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