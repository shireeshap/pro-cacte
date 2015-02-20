package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Harsh Agarwal
 *         Date June 10, 2009
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
        assertEquals("participant/participantInbox?lang=en", ((RedirectView) mv.getView()).getUrl());
    }

    public void testAdminLogin() throws Exception {
        login(SYSTEM_ADMIN);
        LoginController controller = new LoginController();
        
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertEquals("home", mv.getViewName());
    }

    /**
     * Test participant mobile login. Changed the assert from
     * <../mobile/inbox> to:<participant/participantInbox?lang=en> as mobile mode is not supported any more.
     * PRKC-1114
     *
     * @throws Exception the exception
     */
    public void testParticipantMobileLogin() throws Exception {
        request.addHeader("x-wap-profile", "wap");
        login(ParticipantTestHelper.getDefaultParticipant().getUser().getUsername());
        LoginController controller = new LoginController();
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertTrue(mv.getView() instanceof RedirectView);
        assertEquals("participant/participantInbox?lang=en", ((RedirectView) mv.getView()).getUrl());

        request.addHeader("user-agent", "Windows CE");
        mv = controller.handleRequestInternal(request, response);
        assertTrue(mv.getView() instanceof RedirectView);
        assertEquals("participant/participantInbox?lang=en", ((RedirectView) mv.getView()).getUrl());
    }

    public void testClinicalStaffLogin() throws Exception {
        String username = StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser().getUsername();
        login(username);
        LoginController controller = new LoginController();
        controller.setUserRepository(userRepository);
        ModelAndView mv = controller.handleRequestInternal(request, response);
        
        assertEquals("home", mv.getViewName());
        //Ensure Site CRA is loaded as a site level role.
        assertEquals(true, mv.getModelMap().get("siteLevelRole"));
        
        assertEquals(false,mv.getModelMap().get("studyLevelRole"));
        assertEquals(false,mv.getModelMap().get("studyLevelRole"));
        assertEquals(false,mv.getModelMap().get("odc"));
    }

}