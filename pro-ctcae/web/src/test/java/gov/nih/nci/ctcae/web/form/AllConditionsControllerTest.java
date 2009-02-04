package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Jan 13, 2008
 */
public class AllConditionsControllerTest extends WebTestCase {

    private AllConditionsController controller;

    private CreateFormCommand command;
    String questionsIds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AllConditionsController();
        command = new CreateFormCommand();

        command.getCrf().addProCtcTerm(proCtcTerm1);
    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }


    public void testHandleRequest() throws Exception {
        request.getSession().setAttribute(FormController.class.getName() + ".FORM." + "command", command);

        request.addParameter("questionsIds", new String[]{"1,-11,-12"});
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        verifyMocks();
        assertNotNull("must find crf page items", modelAndView.getModel().get("selectedCrfPageItems"));

        List<CrfPageItem> selectedCrfPageItems = (List<CrfPageItem>) modelAndView.getModel().get("selectedCrfPageItems");
        assertEquals("must find 1 crf items only", 1, selectedCrfPageItems.size());
    }


}