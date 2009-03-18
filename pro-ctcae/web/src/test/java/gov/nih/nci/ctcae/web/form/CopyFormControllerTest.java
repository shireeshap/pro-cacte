package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Mehul Gulati
 *         Date: Dec 12, 2008
 */
public class CopyFormControllerTest extends WebTestCase {

    private CopyFormController controller;
    private CRFRepository crfRepository;

    private CRF crf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new CopyFormController();
        crfRepository = registerMockFor(CRFRepository.class);
        controller.setCrfRepository(crfRepository);

        crf = new CRF();
        crf.setStudy(new Study());

    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("crfId", "1");
        expect(crfRepository.findById(1)).andReturn(crf);
        expect(crfRepository.copy(isA(CRF.class))).andReturn(new CRF());
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
    }

}
