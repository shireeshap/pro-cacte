package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.query.Query;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @crated Nov 25, 2008
 */
public class AddQuestionByParticipantControllerTest extends WebTestCase {
    private AddQuestionByParticipantController controller;
    private WebControllerValidator validator;
    private FinderRepository finderRepository;
    private GenericRepository genericRepository;

    private StudyParticipantCrfSchedule studyParticipantCrfSchedule;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddQuestionByParticipantController();
        finderRepository = registerMockFor(FinderRepository.class);
        genericRepository = registerMockFor(GenericRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setFinderRepository(finderRepository);
        controller.setGenericRepository(genericRepository);
        controller.setWebControllerValidator(validator);


    }

    public void testConstructor() {
        assertEquals("form/addQuestionForParticipant", controller.getFormView());
        assertEquals("form/reviewFormSubmission", controller.getSuccessView());
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", new SubmitFormCommand());

        expect(finderRepository.find(isA(Query.class))).andReturn(new ArrayList());
        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();

        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof SubmitFormCommand);

        //assertNotNull(((SubmitFormCommand) command).getProCtcQuestions());
    }

    public void testPostRequest() throws Exception {
        request.setMethod("POST");
        SubmitFormCommand submitFormCommand = new SubmitFormCommand();
        submitFormCommand.setStudyParticipantCrfSchedule(new StudyParticipantCrfSchedule());
        //submitFormCommand.setDirection("continue");
        request.getSession().setAttribute(SubmitFormController.class.getName() + ".FORM." + "command", submitFormCommand);
        request.setParameter("questionsByParticipants", new String[]{"1"});
        request.setParameter("answer1", "2");


        expect(finderRepository.find(isA(Query.class))).andReturn(new ArrayList());
        expect(finderRepository.findById(ProCtcQuestion.class, new Integer("1"))).andReturn(new ProCtcQuestion());
        expect(finderRepository.findById(ProCtcValidValue.class, new Integer("2"))).andReturn(new ProCtcValidValue());
        expect(genericRepository.save(isA(CrfItem.class))).andReturn(new CrfItem());
        expect(genericRepository.save(isA(StudyParticipantCrfItem.class))).andReturn(new StudyParticipantCrfItem());
        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);

    }
}