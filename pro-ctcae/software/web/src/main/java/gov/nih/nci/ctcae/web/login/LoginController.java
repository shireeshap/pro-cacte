package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        user.setNumberOfAttempts(0);
        user.setAccountNonExpired(true);
        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        if (clinicalStaff == null) {
            throw new CtcAeSystemException("User must be one of these - Clinical Staff, Participant, Admin - " + user.getUsername());
        }

        Date today = new Date();
        boolean siteLevelRole = false;
        boolean studyLevelRole = false;
        boolean nurseLevelRole = false;
        boolean odc = false;
        Set<CRF> topLevelCrfs = new HashSet<CRF>();
        Set<Study> allStudies = new TreeSet<Study>(new StudyDisplayNameComparator());
        for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaff.getStudyOrganizationClinicalStaff()) {
                if (studyOrganizationClinicalStaff.getRoleStatus().equals(RoleStatus.ACTIVE) && studyOrganizationClinicalStaff.getStatusDate().before(today)) {
                    Role role = studyOrganizationClinicalStaff.getRole();
                    if (role.equals(Role.SITE_CRA) || role.equals(Role.SITE_PI)) {
                        siteLevelRole = true;
                    }
                    if (role.equals(Role.NURSE) || role.equals(Role.TREATING_PHYSICIAN)) {
                        nurseLevelRole = true;
                    }
                    if (role.equals(Role.ODC)) {
                        odc = true;
                    }
                    if (role.equals(Role.LEAD_CRA) || role.equals(Role.PI)) {
                        studyLevelRole = true;
                    }
                    if (role.equals(Role.LEAD_CRA) || role.equals(Role.PI) || role.equals(Role.ODC)) {
                        Study study = studyOrganizationClinicalStaff.getStudyOrganization().getStudy();
                        List<CRF> crfs = study.getCrfs();
                        allStudies.add(study);
                        for (CRF crf : crfs) {
                            if (crf.getChildCrf() == null) {
                                topLevelCrfs.add(crf);
                            }
                        }
                    }
                }
            }
        }

        List sortedCrfs = new ArrayList<CRF>(topLevelCrfs);
        Collections.sort(sortedCrfs, new CrfActivityDateComparator());

        if (studyLevelRole) {
            mv.addObject("recentCrfs", sortedCrfs);
            mv.addObject("studyWithoutForm", allStudies);
        }
        mv.addObject("siteLevelRole", siteLevelRole);
        mv.addObject("studyLevelRole", studyLevelRole);
        mv.addObject("nurseLevelRole", nurseLevelRole);
        mv.addObject("odc", odc);
        mv.addObject("notifications", getNotifications(user));

        if (siteLevelRole) {
            List<List<StudyParticipantCrfSchedule>> schedules = getOverdueAndUpcomingSchedules(clinicalStaff);
            mv.addObject("overdue", schedules.get(0));
            mv.addObject("upcoming", schedules.get(1));
        }
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
                        if (today.after(spcs.getDueDate()) && !spcs.getStatus().equals(CrfStatus.COMPLETED)) {
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