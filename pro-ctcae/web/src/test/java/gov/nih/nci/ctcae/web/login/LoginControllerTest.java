package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.context.SecurityContextHolder;

/**
 * @author Harsh Agarwal
 * Date June 10, 2009
 */
public class LoginControllerTest extends AbstractWebTestCase {

    public void testExceptionIfNotLoggedIn() {
        SecurityContextHolder.getContext().setAuthentication(null);
        LoginController controller = new LoginController();
        try {
            controller.handleRequestInternal(request, response);
            fail("Expecting exception as the user is not logged in.");
        } catch (Exception e) {
        }
    }

    public void testParticipantLogin() throws Exception {
        login(ParticipantTestHelper.getDefaultParticipant().getUser().getUsername());
        LoginController controller = new LoginController();
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertTrue(mv.getView() instanceof RedirectView);
        assertEquals("participant/participantInbox", ((RedirectView) mv.getView()).getUrl());
    }

    public void testParticipantMobileLogin() throws Exception {
        request.addHeader("x-wap-profile", "wap");
        login(ParticipantTestHelper.getDefaultParticipant().getUser().getUsername());
        LoginController controller = new LoginController();
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertTrue(mv.getView() instanceof RedirectView);
        assertEquals("../mobile/inbox", ((RedirectView) mv.getView()).getUrl());

        request.addHeader("user-agent", "Windows CE");
        mv = controller.handleRequestInternal(request, response);
        assertTrue(mv.getView() instanceof RedirectView);
        assertEquals("../mobile/inbox", ((RedirectView) mv.getView()).getUrl());
    }

    public void testClinicalStaffLogin() throws Exception {
        login(ClinicalStaffTestHelper.getDefaultClinicalStaff().getUser().getUsername());
        LoginController controller = new LoginController();
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertEquals("home", mv.getViewName());
    }

}