package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class ReleaseFormControllerTest extends WebTestCase {
    private ReleaseFormController controller;
    private WebControllerValidator validator;
    private FinderRepository finderRepository;
    private StudyRepository studyRepository;
    private StudyCrf studyCrf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new ReleaseFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        studyRepository = registerMockFor(StudyRepository.class);
        validator = new WebControllerValidatorImpl();
        controller.setFinderRepository(finderRepository);
        controller.setStudyRepository(studyRepository);
        controller.setWebControllerValidator(validator);

        studyCrf = new StudyCrf();
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, Integer.valueOf(1))).andReturn(studyCrf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof StudyCrf);

    }

    public void testPostRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, Integer.valueOf(1))).andReturn(studyCrf);
        replayMocks();
        controller.handleRequest(request, response);
        verifyMocks();
        resetMocks();

        request.setMethod("POST");
        expect(studyRepository.save(studyCrf.getStudy())).andReturn(studyCrf.getStudy());
        validator.validate(request, studyCrf, isA(BindException.class));
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);

        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");

        assertNotNull("must find command object", command);
        assertTrue(command instanceof StudyCrf);


    }
}
