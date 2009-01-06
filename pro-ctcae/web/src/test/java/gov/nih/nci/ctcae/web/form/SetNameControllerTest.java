package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class SetNameControllerTest extends WebTestCase {

    private SetNameController controller;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SetNameController();


    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"POST"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {
        request.addParameter("crfTitle", "crfTitle");

        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("crfTitle must be present", "crfTitle", modelAndView.getModelMap().get("crfTitle"));


    }
}