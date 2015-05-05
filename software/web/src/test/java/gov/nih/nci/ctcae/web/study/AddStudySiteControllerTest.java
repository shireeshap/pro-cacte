package gov.nih.nci.ctcae.web.study;

import static edu.nwu.bioinformatics.commons.testing.CoreTestCase.assertEqualArrays;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.ControllersUtils;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class AddStudySiteControllerTest extends AbstractWebTestCase {

    private AddStudySiteController controller;

    private StudyController studyController;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        controller = new AddStudySiteController();
        studyController = new CreateStudyController();
        studyController.setStudyRepository(studyRepository);
        studyController.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        controller.setStudyRepository(studyRepository);
        studyController.setUserRepository(userRepository);
        ((StudyDetailsTab)studyController.getFlow().getTab(0)).setUserRepository(userRepository);
        request.setMethod("GET");
    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequest() throws Exception {

        login("cca.cca@demo.com");
        studyController.handleRequest(request, response);
        StudyCommand command = ControllersUtils.getStudyCommand(request);
        assertNotNull("command must present in session", command);
        studyRepository.addStudySite(command.getStudy());
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("index must be present", modelAndView.getModelMap().get("index"));
    }
}