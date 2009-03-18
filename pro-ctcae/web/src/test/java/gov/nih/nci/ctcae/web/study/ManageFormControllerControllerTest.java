package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.ManageFormController;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public class ManageFormControllerControllerTest extends WebTestCase {

    private ManageFormController controller;
    private Study study;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ManageFormController();
        controller.setStudyRepository(studyRepository);

        study = new Study();
        request.setMethod("GET");

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequestIfNoCRF() throws Exception {


        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertNotNull(modelAndView);
        assertTrue("moel must be empty", modelAndView.getModel().keySet().isEmpty());


    }

    public void testHandleRequestForSelectedNullCRF() throws Exception {

        request.addParameter("studyId", "1");
        expect(studyRepository.findById(1)).andReturn(null);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
        assertTrue("moel must be empty", modelAndView.getModel().keySet().isEmpty());

    }

    public void testHandleRequestForSelectedNotNullCRF() throws Exception {

        request.addParameter("studyId", "1");
        expect(studyRepository.findById(1)).andReturn(study);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
        assertEquals("study  must present in command", study, modelAndView.getModel().get("study"));

    }
}