package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;

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
        CrfItem item1 = new CrfItem();
        item1.setDisplayOrder(1);

        CrfItem item2 = new CrfItem();
        item2.setDisplayOrder(2);
        item2.setResponseRequired(true);

        CrfItem item3 = new CrfItem();
        item3.setDisplayOrder(3);

        CrfItem item4 = new CrfItem();
        item4.setDisplayOrder(4);

        StudyParticipantCrfItem studyParticipantCrfItem1 = new StudyParticipantCrfItem();
        studyParticipantCrfItem1.setCrfItem(item1);
        studyParticipantCrfItem1.setProCtcValidValue(new ProCtcValidValue());

        StudyParticipantCrfItem studyParticipantCrfItem2 = new StudyParticipantCrfItem();
        studyParticipantCrfItem2.setCrfItem(item2);

        StudyParticipantCrfItem studyParticipantCrfItem3 = new StudyParticipantCrfItem();
        studyParticipantCrfItem3.setCrfItem(item3);

        StudyParticipantCrfItem studyParticipantCrfItem4 = new StudyParticipantCrfItem();
        studyParticipantCrfItem4.setCrfItem(item4);


        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem1);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem2);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem3);
        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem4);


    }

    public void testConstructor() {
        assertEquals("form/submitForm", controller.getFormView());
        assertEquals("form/confirmFormSubmission", controller.getSuccessView());
        assertEquals("form/reviewFormSubmission", controller.getReviewView());
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
        EasyMock.expectLastCall().times(4);

        expect(finderRepository.findById(StudyParticipantCrfSchedule.class, studyParticipantCrfSchedule.getId())).andReturn(studyParticipantCrfSchedule);
        EasyMock.expectLastCall().times(4);

        replayMocks();

        //first run
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        assertEquals(controller.getFormView(), modelAndView.getViewName());

        //second run (error for null value in mandatory question)
        //command.setCurrentIndex(1);
        command.setDirection("continue");
        modelAndView = controller.handleRequest(request, response);
        ModelMap m = modelAndView.getModelMap();
        BeanPropertyBindingResult r = (BeanPropertyBindingResult)m.get("org.springframework.validation.BindingResult.command");
        assertEquals(1, r.getAllErrors().size());


        //third run (review form if hit continue on last question)
        //command.setCurrentIndex(command.getTotalQuestions() - 1);
        command.setDirection("continue");
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        assertEquals(controller.getReviewView(), modelAndView.getViewName());

        //fourth run
        command.setDirection("save");
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
        assertEquals("Form was submitted successfully", command.getFlashMessage());

        //fifth run
        command.setDirection("");
        studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
        modelAndView = controller.handleRequest(request, response);
        model = modelAndView.getModel();
        assertEquals(controller.getSuccessView(), modelAndView.getViewName());
        assertEquals("You have already submitted this form. Below are the responses.", command.getFlashMessage());


        verifyMocks();

    }
}