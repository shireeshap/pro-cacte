package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class AddStudySiteControllerTest extends WebTestCase {

    private AddStudySiteController controller;

    private StudyController studyController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new AddStudySiteController();
        studyController = new CreateStudyController();
        studyController.setStudyRepository(studyRepository);
        studyController.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        controller.setStudyRepository(studyRepository);


    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {
        replayMocks();
        studyController.handleRequest(request, response);
        verifyMocks();
        resetMocks();
        StudyCommand command = ControllersUtils.getStudyCommand(request);
        assertNotNull("command must present in session", command);
        studyRepository.addStudySite(command.getStudy());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("index must be present", modelAndView.getModelMap().get("index"));

        verifyMocks();
    }
}