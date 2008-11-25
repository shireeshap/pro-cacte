package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.easymock.EasyMock;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * @author Harsh Agarwal
 * @crated Nov 25, 2008
 */
public class SubmitFormControllerTest extends WebTestCase {
    private SubmitFormController controller;
    private WebControllerValidator validator;
    private FinderRepository finderRepository;
    private GenericRepository genericRepository;
    private SubmitFormCommand command;

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SubmitFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setFinderRepository(finderRepository);
        controller.setGenericRepository(genericRepository);
        controller.setWebControllerValidator(validator);

        studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();


    }

    public void testConstructor() {
        assertEquals("form/submitForm", controller.getFormView());
        assertEquals("form/confirmFormSubmission", controller.getSuccessView());
        assertEquals(SubmitFormCommand.class, controller.getCommandClass());
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("id", "1");
        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt("1"))).andReturn(studyParticipantCrfSchedule);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);

    }

    public void testPostRequest() throws Exception {

        request.setMethod("POST");
        command = new SubmitFormCommand();
        command.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + controller.getCommandName(), command);

        expect(genericRepository.save(studyParticipantCrfSchedule)).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(3);

        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId())).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(3);

        replayMocks();

        //first run
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        assertEquals(controller.getFormView(), modelAndView.getViewName());

        //second run
        studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
        assertEquals("You have already submitted this form. Below are the responses.", command.getFlashMessage());

        //third run
        command.setDirection("save");
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
        assertEquals("Form was submitted successfully", command.getFlashMessage());

        verifyMocks();

    }
}