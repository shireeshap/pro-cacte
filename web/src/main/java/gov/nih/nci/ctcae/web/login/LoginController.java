package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.web.ControllersUtils;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class LoginController extends AbstractController {
    ClinicalStaffRepository clinicalStaffRepository;
    UserRepository userRepository;
    StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository;

    protected LoginController() {
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");

        }

        User user = (User) auth.getPrincipal();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
                if (ControllersUtils.isRequestComingFromMobile(request)) {
                    return new ModelAndView(new RedirectView("../mobile/inbox"));
                } else {
                    return new ModelAndView(new RedirectView("participant/participantInbox"));
                }
            }
            if (userRole.getRole().equals(Role.ADMIN)) {
                return new ModelAndView("home");
            }

        }

        ModelAndView mv = new ModelAndView("home");
        user = userRepository.findById(user.getId());
        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);

        if (clinicalStaff == null) {
            throw new CtcAeSystemException("User must be one of these - Clinical Staff, Participant, Admin - " + user.getUsername());
        }

        mv.addObject("notifications", getNotifications(user));
        mv.addObject("overdue", getOverdueAndUpcomingSchedules(clinicalStaff).get(0));
        mv.addObject("upcoming", getOverdueAndUpcomingSchedules(clinicalStaff).get(1));
        return mv;
    }

    private List<UserNotification> getNotifications(User user) {
        return user.getUserNotifications();
    }

    public List<List<StudyParticipantCrfSchedule>> getOverdueAndUpcomingSchedules(ClinicalStaff clinicalStaff) {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
        query.filterByClinicalStaffId(clinicalStaff.getId());
        List<StudyOrganizationClinicalStaff> socs = studyOrganizationClinicalStaffRepository.find(query);
        ArrayList<List<StudyParticipantCrfSchedule>> out = new ArrayList<List<StudyParticipantCrfSchedule>>();
        ArrayList<StudyParticipantCrfSchedule> overdue = new ArrayList<StudyParticipantCrfSchedule>();
        ArrayList<StudyParticipantCrfSchedule> upcoming = new ArrayList<StudyParticipantCrfSchedule>();
        out.add(overdue);
        out.add(upcoming);
        Date today = new Date();
        Date week = DateUtils.addDaysToDate(today, 6);
        Date yesterday = DateUtils.addDaysToDate(today, -1);

        for (StudyOrganizationClinicalStaff staff : socs) {
            for (StudyParticipantAssignment studyParticipantAssignment : staff.getStudyOrganization().getStudyParticipantAssignments()) {
                for (StudyParticipantCrf spc : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    for (StudyParticipantCrfSchedule spcs : spc.getStudyParticipantCrfSchedules()) {
                        if (today.after(spcs.getDueDate())) {
                            overdue.add(spcs);
                        }
                        if (spcs.getStartDate().after(yesterday) && spcs.getStartDate().before(week)) {
                            upcoming.add(spcs);
                        }
                    }
                }
            }
        }
        return out;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setStudyOrganizationClinicalStaffRepository(StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository) {
        this.studyOrganizationClinicalStaffRepository = studyOrganizationClinicalStaffRepository;
    }
}