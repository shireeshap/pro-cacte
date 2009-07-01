package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Date;

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
        String username = StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser().getUsername();
        login(username);
        LoginController controller = new LoginController();
        controller.setClinicalStaffRepository(clinicalStaffRepository);
        controller.setUserRepository(userRepository);
        controller.setStudyOrganizationClinicalStaffRepository(studyOrganizationClinicalStaffRepository);
        ModelAndView mv = controller.handleRequestInternal(request, response);
        assertEquals("home", mv.getViewName());
        List<UserNotification> noti = (List<UserNotification>) mv.getModel().get("notifications");
        assertNotNull(noti);
        assertEquals(0, noti.size());
        List<StudyParticipantCrfSchedule> over = (List<StudyParticipantCrfSchedule>) mv.getModel().get("overdue");
        assertNotNull(over);
        for (StudyParticipantCrfSchedule spc : over) {
            assertTrue(spc.getStartDate().before(new Date()));
        }
        List<StudyParticipantCrfSchedule> up = (List<StudyParticipantCrfSchedule>) mv.getModel().get("upcoming");
        assertNotNull(up);
        for (StudyParticipantCrfSchedule spc : up) {
            assertTrue(spc.getStartDate().after(DateUtils.addDaysToDate(new Date(), -1)));
        }

        username = StudyTestHelper.getLeadSiteStaffByRole(Role.SITE_CRA).getUser().getUsername();
        login(username);
        mv = controller.handleRequestInternal(request, response);
        assertEquals("home", mv.getViewName());
        noti = (List<UserNotification>) mv.getModel().get("notifications");
        assertNotNull(noti);
//        assertEquals(2, noti.size());
        over = (List<StudyParticipantCrfSchedule>) mv.getModel().get("overdue");
        assertNotNull(over);
        for (StudyParticipantCrfSchedule spc : over) {
            assertTrue(spc.getStartDate().before(new Date()));
        }
        up = (List<StudyParticipantCrfSchedule>) mv.getModel().get("upcoming");
        assertNotNull(up);
        for (StudyParticipantCrfSchedule spc : up) {
            assertTrue(spc.getStartDate().after(DateUtils.addDaysToDate(new Date(), -1)));
        }

    }

}