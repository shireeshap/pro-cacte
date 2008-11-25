package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.easymock.EasyMock;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Nov 4, 2008
 */
public class EditParticipantControllerTest extends WebTestCase {
    private EditParticipantController editParticipantController;
    private ParticipantCommand participantCommand;
    private ParticipantRepository participantRepository;
    private Participant participant;
    private BindException errors;
    private FinderRepository finderRepository;
    private Study study, study1;
    WebControllerValidator webControllerValidator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        editParticipantController = new EditParticipantController();
        participantRepository = registerMockFor(ParticipantRepository.class);
        errors = registerMockFor(BindException.class);
        finderRepository = registerMockFor(FinderRepository.class);
        webControllerValidator = registerMockFor(WebControllerValidator.class);

        editParticipantController.setParticipantRepository(participantRepository);
        editParticipantController.setFinderRepository(finderRepository);
        editParticipantController.setWebControllerValidator(webControllerValidator);

        participant = Fixture.createParticipant("test", "test", "id");
        participant.setId(1);

        study = Fixture.createStudyWithStudySite("short", "long", "id", Fixture.createOrganization("test", "test"));
        study.setId(2);
        study1 = Fixture.createStudyWithStudySite("short", "long", "id", Fixture.createOrganization("test", "test"));
        study1.setId(3);

        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(study.getStudySites().get(0));
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant.getStudyParticipantAssignments().get(0).getStudySite().getOrganization().setId(1);
        participantCommand = new ParticipantCommand();

    }

    public void testConstructor() {
        assertEquals("participant/editParticipant", editParticipantController.getFormView());
    }

    public void testFormBackingObject() throws Exception {
        request.setParameter("participantId", participant.getId().toString());

        expect(participantRepository.findById(new Integer(participant.getId()))).andReturn(participant);
        replayMocks();
        participantCommand = (ParticipantCommand) editParticipantController.formBackingObject(request);
        verifyMocks();

        assertNotNull(participantCommand);
        assertNotNull(participantCommand.getParticipant());
        assertEquals(1, participantCommand.getSiteId());
        assertEquals("test", participantCommand.getSiteName());
        assertNull(participantCommand.getStudyId());
    }

    public void testOnSumbit() throws Exception {

        request.setParameter("participantId", participant.getId().toString());
        request.setMethod("POST");

        participantCommand.setStudyId(new int[]{3});
        ArrayList l = new ArrayList();
        l.add(study1.getStudySites().get(0));

        expect(finderRepository.find(isA(StudyOrganizationQuery.class))).andReturn(l);
        expect(participantRepository.save(isA(Participant.class))).andReturn(participantCommand.getParticipant());
        replayMocks();
        ModelAndView modelAndView = editParticipantController.onSubmit(request, response, participantCommand, errors);
        verifyMocks();
        
        assertEquals(editParticipantController.getSuccessView(),modelAndView.getViewName());
    }


    public void testBindAndValidate() throws Exception {

        webControllerValidator.validate(request, participantCommand, errors);
        errors.reject("studyId", "Please select at least one study.") ;

        replayMocks();
        editParticipantController.onBindAndValidate(request, participantCommand, errors);
        verifyMocks();

    }

}