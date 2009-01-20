package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Jan 13, 2008
 */
public class AddConditionalQuestionControllerTest extends WebTestCase {

    private AddConditionalQuestionController controller;

    private CreateFormCommand command;
    protected FinderRepository finderRepository;
    private ProCtcQuestion proCtcQuestion;
    private ProCtcValidValue proCtcValidValue1;
    private ProCtcValidValue proCtcValidValue2;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddConditionalQuestionController();
        finderRepository = registerMockFor(FinderRepository.class);
        command = new CreateFormCommand();
        controller.setFinderRepository(finderRepository);


        proCtcQuestion = new ProCtcQuestion();
        proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("value1");
        proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value2");
        command.getCrf().addCrfPage(proCtcQuestion);

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }


    public void testHandleRequest() throws Exception {
        request.getSession().setAttribute(BasicFormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("questionId", new String[]{"1"});
        request.addParameter("selectedValidValues", new String[]{"3,4"});
        expect(finderRepository.findAndInitializeProCtcQuestion(1)).andReturn(proCtcQuestion);
        expect(finderRepository.findById(ProCtcValidValue.class, 3)).andReturn(proCtcValidValue1);
        expect(finderRepository.findById(ProCtcValidValue.class, 4)).andReturn(proCtcValidValue2);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        CRF crf = createFormCommand.getCrf();
        CrfPageItem crfPageItem = crf.getCrfPageItemByQuestion(proCtcQuestion);
        assertNotNull("crf page item must not be null", crfPageItem);
        assertFalse("must add rules", crfPageItem.getCrfPageItemDisplayRules().isEmpty());
        assertEquals("must add 2 riles", 2, crfPageItem.getCrfPageItemDisplayRules().size());

    }


}