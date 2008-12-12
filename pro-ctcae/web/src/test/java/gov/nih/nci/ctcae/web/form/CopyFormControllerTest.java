package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.WebTestCase;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Mehul Gulati
 * Date: Dec 12, 2008
 */
public class CopyFormControllerTest extends WebTestCase {

    private CopyFormController controller;
    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    private StudyCrf studyCrf;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new CopyFormController();
        finderRepository = registerMockFor(FinderRepository.class);
        crfRepository = registerMockFor(CRFRepository.class);
        controller.setFinderRepository(finderRepository);
        controller.setCrfRepository(crfRepository);

        studyCrf = new StudyCrf();
        studyCrf.setCrf(new CRF());
        studyCrf.setStudy(new Study());

    }

    public void testGetRequest() throws Exception {
        request.setMethod("GET");
        request.addParameter("studyCrfId", "1");
        expect(finderRepository.findById(StudyCrf.class, 1)).andReturn(studyCrf);
        expect(crfRepository.save(isA(CRF.class))).andReturn(null);
        replayMocks();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        verifyMocks();
        assertNotNull(modelAndView);
    }

}
