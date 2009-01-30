package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class AddStudySiteControllerTest extends WebTestCase {

    private AddStudySiteController controller;

    private CreateStudyController createStudyController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddStudySiteController();
        createStudyController = new CreateStudyController();


    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {

        createStudyController.handleRequest(request, response);
        Object command = ControllersUtils.getStudyCommand(request);
        assertNotNull("command must present in session", command);

        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("index must be present", modelAndView.getModelMap().get("index"));


    }
}