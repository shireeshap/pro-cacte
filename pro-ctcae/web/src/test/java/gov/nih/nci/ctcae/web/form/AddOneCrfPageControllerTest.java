package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Jan 13, 2008
 */
public class AddOneCrfPageControllerTest extends WebTestCase {

    private AddOneCrfPageController controller;

    private ProCtcTermRepository proCtcTermRepository;
    private ProCtcTerm proCtcTerm;
    private CreateFormCommand command;
    protected FinderRepository finderRepository;
    private ProCtcQuestion proCtcQuestion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddOneCrfPageController();
        finderRepository = registerMockFor(FinderRepository.class);
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        controller.setProCtcTermRepository(proCtcTermRepository);
        proCtcTerm = new ProCtcTerm();
        command = new CreateFormCommand();
        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
        controller.setFinderRepository(finderRepository);


        proCtcQuestion = new ProCtcQuestion();
    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }


    public void testHandleRequestIfAddingQuestionBySelectingPage() throws Exception {
        request.getSession().setAttribute(AdvanceFormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("questionId", new String[]{"1"});
        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(proCtcQuestion);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add only one page", 1, crf.getCrfPagesSortedByPageNumber().size());

    }

    public void testHandleRequestIfAddingProCtcTermBySelectingPage() throws Exception {
        request.getSession().setAttribute(AdvanceFormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("proCtcTermId", new String[]{"1"});
        expect(proCtcTermRepository.findAndInitializeTerm(1)).andReturn(proCtcTerm);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add only one page", 1, crf.getCrfPagesSortedByPageNumber().size());

    }


    public void testHandleRequestIfCrfPageIsNotSelected() throws Exception {
        request.getSession().setAttribute(AdvanceFormController.class.getName() + ".FORM." + "command", command);

        ModelAndView modelAndView = controller.handleRequestInternal(request, response);

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add only one page", 1, crf.getCrfPagesSortedByPageNumber().size());

    }
}