package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcQuestionRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Jan 13, 2008
 */
public class RemoveConditionsControllerTest extends WebTestCase {

    private RemoveConditionsController controller;

    private CreateFormCommand command;
    protected FinderRepository finderRepository;
    private ProCtcQuestion proCtcQuestion;
    private ProCtcValidValue proCtcValidValue1;
    private ProCtcValidValue proCtcValidValue2;
    private ProCtcValidValue proCtcValidValue;
    private ArrayList<ProCtcValidValue> proCtcValidValues;
    private ProCtcQuestionRepository proCtcQuestionRepository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new RemoveConditionsController();
        finderRepository = registerMockFor(FinderRepository.class);
        command = new CreateFormCommand();
        proCtcQuestionRepository = registerMockFor(ProCtcQuestionRepository.class);
        controller.setProCtcQuestionRepository(proCtcQuestionRepository);


        proCtcQuestion = new ProCtcQuestion();
        proCtcValidValue1 = new ProCtcValidValue();
        proCtcValidValue1.setValue("value1");
        proCtcValidValue1.setId(3);
        proCtcValidValue2 = new ProCtcValidValue();
        proCtcValidValue2.setValue("value2");
        proCtcValidValue2.setId(4);
        command.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);

        command.getCrf().addCrfPage(proCtcQuestion);

        proCtcValidValue = new ProCtcValidValue();
        proCtcValidValue.setValue("value0");
        proCtcValidValue.setId(0);


        proCtcValidValues = new ArrayList<ProCtcValidValue>();
        proCtcValidValues.add(proCtcValidValue1);
        proCtcValidValues.add(proCtcValidValue2);
    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }


    public void testHandleRequestForRemovingConditionsOnAQuestion() throws Exception {
        CRF crf = command.getCrf();
        CrfPageItem crfPageItem = crf.getCrfPageItemByQuestion(proCtcQuestion);
        crfPageItem.addCrfPageItemDisplayRules(proCtcValidValues);
        assertEquals("must add 2 rules", 2, crfPageItem.getCrfPageItemDisplayRules().size());

        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("questionId", new String[]{"1"});
        request.addParameter("proCtcValidValueId", new String[]{"1,6,4"});
        expect(proCtcQuestionRepository.findById(1)).andReturn(proCtcQuestion);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();

        CreateFormCommand createFormCommand = ControllersUtils.getFormCommand(request);

        crf = createFormCommand.getCrf();
        crfPageItem = crf.getCrfPageItemByQuestion(proCtcQuestion);
        assertNotNull("crf page item must not be null", crfPageItem);
        assertFalse("must remove 1 rule", crfPageItem.getCrfPageItemDisplayRules().isEmpty());
        assertEquals("must have 1 rule only because 1 rule has been removed", 1, crfPageItem.getCrfPageItemDisplayRules().size());

    }


}