package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class ReleaseFormControllerTest extends WebTestCase {
    private ReleaseFormController controller;
    private WebControllerValidator validator;
    private CRFRepository crfRepository;

    private CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ReleaseFormController();
        crfRepository = registerMockFor(CRFRepository.class);
        validator = new WebControllerValidatorImpl();
        controller.setCrfRepository(crfRepository);
        controller.setWebControllerValidator(validator);

        crf = new CRF();
        crf.setStudy(new Study());
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(Integer.valueOf(1))).andReturn(crf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CRF);

    }

    public void testPostRequest() throws Exception {

        request.setMethod("POST");
        expect(crfRepository.findById(null)).andReturn(crf).anyTimes();
        expect(crfRepository.save(crf)).andReturn(crf).anyTimes();
        expect(crfRepository.updateStatusToReleased(crf)).andReturn(crf);

        replayMocks();

        ModelAndView modelAndView = controller.handleRequest(request, response);

        verifyMocks();
        Map model = modelAndView.getModel();
        assertTrue("view must be instance of redirect view", modelAndView.getView() instanceof RedirectView);
        Object command = model.get("command");
        assertNull("must not find command object", command);


    }
}
