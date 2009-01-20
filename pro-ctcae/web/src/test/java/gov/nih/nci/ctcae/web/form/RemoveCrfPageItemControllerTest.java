package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class RemoveCrfPageItemControllerTest extends WebTestCase {

    private RemoveCrfPageItemController controller;

    private FinderRepository finderRepository;
    private ProCtcQuestion proCtcQuestion;
    private CreateFormCommand command;
    private ProCtcTerm proCtcTerm;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new RemoveCrfPageItemController();
        finderRepository = registerMockFor(FinderRepository.class);
        controller.setFinderRepository(finderRepository);
        proCtcQuestion = new ProCtcQuestion();
        proCtcQuestion.setId(1);
        command = new CreateFormCommand();
        proCtcTerm = new ProCtcTerm();
        proCtcTerm.addProCtcQuestion(proCtcQuestion);


    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequestIfQuestionIdIsWrong() throws Exception {
        request.addParameter("questionId", new String[]{"1"});
        request.addParameter("crfPageNumber", new String[]{"1"});

        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(null);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        assertNull("must return null because no question is present for given id", modelAndView);
    }

    public void testHandleRequestIfQuestionIdIsCorrect() throws Exception {
        command.addProCtcTerm(proCtcTerm);

        request.getSession().setAttribute(BasicFormController.class.getName() + ".FORM." + "command", this.command);

        request.addParameter("questionId", new String[]{"1"});
        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(proCtcQuestion);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        assertNotNull("must not return null because there is one question  for given id", modelAndView);

        command = ControllersUtils.getFormCommand(request);
        assertTrue("must remove proctc question", command.getCrf().getAllCrfPageItems().isEmpty());
    }


}