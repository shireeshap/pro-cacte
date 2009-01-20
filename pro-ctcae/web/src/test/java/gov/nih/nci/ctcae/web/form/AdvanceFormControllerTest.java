package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Jan, 20, 2009
 */
public class AdvanceFormControllerTest extends WebTestCase {
    private AdvanceFormController controller;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AdvanceFormController();
    }

    public void testFormBackingObject() throws Exception {
        ModelAndView modelAndView = controller.handleRequest(request, response);
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CreateFormCommand);
        assertTrue("must be advance form creation", ((CreateFormCommand) command).getAdvance());

    }


}