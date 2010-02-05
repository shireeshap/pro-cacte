package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Harsh Agarwal
 * @since Nov 24, 2008
 */
public class SearchParticipantControllerTest extends WebTestCase {
    private SearchStudyController searchParticipantController;
    private ParticipantCommand participantCommand;
    private ModelAndView modelAndView;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        searchParticipantController = new SearchStudyController();

    }

    public void testHandleRequestInternal() throws Exception {
        modelAndView = searchParticipantController.handleRequestInternal(request, response);
        assertEquals("participant/searchParticipant",modelAndView.getViewName());
    }



}