package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.form.ManageFormController;
import gov.nih.nci.ctcae.web.form.CrfAjaxFacade;
import static org.easymock.EasyMock.expect;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class ManageFormControllerTest extends WebTestCase {

    private ManageFormController controller;
    private Study study;
    CrfAjaxFacade crfAjaxFacade;
    StudyAjaxFacade studyAjaxFacade;


    @Override
    protected void setUp() throws Exception {
        crfAjaxFacade = registerMockFor(CrfAjaxFacade.class);
        studyAjaxFacade = registerMockFor(StudyAjaxFacade.class);
        super.setUp();
        controller = new ManageFormController();
        controller.setStudyRepository(studyRepository);
        controller.setCrfAjaxFacade(crfAjaxFacade);
        controller.setStudyAjaxFacade(studyAjaxFacade);

        study = new Study();
        study.setId(1);
        request.setMethod("GET");

    }

    public void testSupportedMethod() {
        assertEqualArrays("only get is supported", new String[]{"GET"}, controller.getSupportedMethods());
    }

    public void testHandleRequestIfNoCRF() throws Exception {

        expect(studyAjaxFacade.matchStudy("%")).andReturn(new ArrayList<Study>());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
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
        expect(crfAjaxFacade.searchCrf(1)).andReturn(new ArrayList<CRF>());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
        assertEquals("study  must present in command", study, modelAndView.getModel().get("study"));

    }
}