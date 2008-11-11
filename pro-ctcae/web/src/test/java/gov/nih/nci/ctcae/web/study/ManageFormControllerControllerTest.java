package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
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
    private FinderRepository finderRepository;
    private StudyCrf studyCrf;
    private Study study;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ManageFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        controller.setFinderRepository(finderRepository);

        studyCrf = new StudyCrf();
        study = new Study();
        studyCrf.setStudy(study);
        request.setMethod("GET");

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequestIfNoStudyCrf() throws Exception {


        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertNotNull(modelAndView);
        assertTrue("moel must be empty", modelAndView.getModel().keySet().isEmpty());


    }

    public void testHandleRequestForSelectedNullStudyCrf() throws Exception {

        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, 1)).andReturn(null);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
        assertTrue("moel must be empty", modelAndView.getModel().keySet().isEmpty());

    }

    public void testHandleRequestForSelectedNotNullStudyCrf() throws Exception {

        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, 1)).andReturn(studyCrf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
        assertEquals("study  must present in command", study, modelAndView.getModel().get("study"));

    }
}