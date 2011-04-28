package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @since Dec 18, 2008
 */
public class AddCrfComponentControllerTest extends WebTestCase {

    private AddCrfComponentController controller;

    private ProCtcTermRepository proCtcTermRepository;
    private ProCtcTerm proCtcTerm;
    private CreateFormCommand command;
    private ProCtcQuestion firstQuestion;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddCrfComponentController();
        proCtcTermRepository = registerMockFor(ProCtcTermRepository.class);
        controller.setProCtcTermRepository(proCtcTermRepository);
        proCtcTerm = new ProCtcTerm();
        command = new CreateFormCommand();

        firstQuestion = new ProCtcQuestion();
        firstQuestion.setId(11);
        firstQuestion.setQuestionText("sample question1", SupportedLanguageEnum.ENGLISH);

        proCtcTerm.addProCtcQuestion(firstQuestion);
    }

    public void testAddProCtcTermForBasicMode() throws Exception {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("proCtcTermId", new String[]{"1"});
        request.addParameter("componentType", new String[]{AddCrfComponentController.PRO_CTC_TERM_COMPONENT});
        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        assertEquals("must return view for new crf page", "form/ajax/oneCrfPageSection", modelAndView.getViewName());
        assertNotNull("must return crf page", modelAndView.getModel().get("crfPage"));

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        assertFalse("must add crf page", crf.getCrfPagesSortedByPageNumber().isEmpty());
        assertEquals("must add only one page", 1, crf.getCrfPagesSortedByPageNumber().size());


    }

    public void testAddProCtcTermAgainForBasicMode() throws Exception {

        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("proCtcTermId", new String[]{"1"});
        request.addParameter("componentType", new String[]{AddCrfComponentController.PRO_CTC_TERM_COMPONENT});
        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        resetMocks();

        expect(proCtcTermRepository.findById(1)).andReturn(proCtcTerm);
        replayMocks();
        modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        assertEquals("must return view for new pro ctc term", "form/ajax/oneProCtcTermSection", modelAndView.getViewName());
        assertNotNull("must return crf page items", modelAndView.getModel().get("crfPageItems"));

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

//    public void testHandleRequestIfQuestionIdIsWrong() throws Exception {
//        request.addParameter("proCtcTermId", new String[]{"1"});
//        request.addParameter("crfPageNumber", new String[]{"1"});
//        expect(proCtcTermRepository.find(1)).andReturn(null);
//        replayMocks();
//        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
//        verifyMocks();
//        assertNull("must return null because no question is present for given id", modelAndView);
//    }
//
//    public void testHandleRequestIfQuestionIdIsCorrect() throws Exception {
//        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
//        command.addCrfPage();
//        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);
//
//        request.addParameter("proCtcTermId", new String[]{"1"});
//        request.addParameter("crfPageNumber", new String[]{"0"});
//        expect(proCtcTermRepository.find(1)).andReturn(proCtcTerm);
//        replayMocks();
//        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
//        verifyMocks();
//        assertNotNull("must not return null because there is one proCtcTerm  for given id", modelAndView);
//        assertNotNull("must return crfPageItems", modelAndView.getModel().get("crfPageItems"));
//    }
//
//    public void testHandleRequestIfCrfPageIsNotSelected() throws Exception {
//        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);
//
//        request.addParameter("proCtcTermId", new String[]{"1"});
//        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
//        View view = modelAndView.getView();
//        assertTrue("must forward to add one crf page", view instanceof RedirectView);
//        String url = ((RedirectView) view).getUrl();
//        assertEquals("addOneCrfPage?subview=subview&proCtcTermId=1", url);
//    }
}