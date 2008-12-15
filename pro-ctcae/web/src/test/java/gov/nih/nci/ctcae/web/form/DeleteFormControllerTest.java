package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * @author Mehul Gulati
 * Date: Dec 15, 2008
 */
public class DeleteFormControllerTest extends WebTestCase {
    private DeleteFormController controller;
    private WebControllerValidator validator;
    private FinderRepository finderRepository;
    private CRFRepository crfRepository;
    private StudyCrf studyCrf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new DeleteFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setFinderRepository(finderRepository);
        controller.setCrfRepository(crfRepository);
        controller.setWebControllerValidator(validator);



        studyCrf = new StudyCrf();
        studyCrf.setCrf(new CRF());
        studyCrf.setStudy(new Study());
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, 1)).andReturn(studyCrf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof StudyCrf);
    }

    public void testPostRequest() throws Exception {

        request.setMethod("POST");
        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, 1)).andReturn(studyCrf);
        crfRepository.delete(isA(CRF.class));
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);

        verifyMocks();
        Map model = modelAndView.getModel();
        assertTrue("view must be instance of redirect view", modelAndView.getView() instanceof RedirectView);
        Object command = model.get("command");
        assertNull("must not find command object", command);


    }

}
