package gov.nih.nci.ctcae.web.study;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public class SearchStudyControllerTest extends WebTestCase {

    private SearchStudyController controller;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        controller = new SearchStudyController();


    }

    public void testHandleRequest() throws Exception {

        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertNotNull("study search criteria must be present", modelAndView.getModelMap().get("searchCriteria"));
    }
}
