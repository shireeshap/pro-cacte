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
public class EditParticipantControllerTest extends WebTestCase {
    private EditParticipantController editParticipantController;
    private ParticipantCommand participantCommand;
    private ParticipantRepository participantRepository;
    Participant participant ;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        editParticipantController = new EditParticipantController();
        participantRepository = registerMockFor(ParticipantRepository.class);

        editParticipantController.setParticipantRepository(participantRepository);
        participant = Fixture.createParticipant("test","test","id");
        participant.setId(1);

    }

    public void testConstructor() {
        assertEquals("participant/editParticipant", editParticipantController.getFormView());
    }

    public void testFormBackingObject() throws Exception {
        request.setParameter("participantId", participant.getId().toString());

        expect(participantRepository.findById(new Integer(participant.getId()))).andReturn(participant);
        replayMocks();
        participantCommand = (ParticipantCommand)editParticipantController.formBackingObject(request);
        verifyMocks();

        assertNotNull(participantCommand);
        assertNotNull(participantCommand.getParticipant());
        assertEquals(0, participantCommand.getSiteId());
        assertNull(participantCommand.getSiteName());
        assertNull(participantCommand.getStudyId());
    }
}