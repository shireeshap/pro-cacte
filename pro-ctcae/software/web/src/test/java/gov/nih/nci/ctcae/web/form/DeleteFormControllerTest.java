package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidator;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.ArrayList;


/**
 * @author Mehul Gulati
 *         Date: Dec 15, 2008
 */
public class DeleteFormControllerTest extends WebTestCase {
    private DeleteFormController controller;
    private WebControllerValidator validator;
    private CRFRepository crfRepository;
    private StudyRepository studyRepository;
    private CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new DeleteFormController();
        crfRepository = registerMockFor(CRFRepository.class);
        studyRepository = registerMockFor(StudyRepository.class);
        validator = new WebControllerValidatorImpl();

        controller.setCrfRepository(crfRepository);
        controller.setStudyRepository(studyRepository);
        controller.setWebControllerValidator(validator);


        crf = new CRF();
        crf.setId(1);
        Study study = new Study();
        study.setId(1);
        crf.setStudy(study);
    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(1)).andReturn(crf);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        Map model = modelAndView.getModel();
        Object command = model.get("command");
        assertNotNull("must find command object", command);
        assertTrue(command instanceof CRF);
    }

}
