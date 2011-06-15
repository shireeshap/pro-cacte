package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserNotification;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.web.ControllersUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class LoginController extends AbstractController {
    ClinicalStaffRepository clinicalStaffRepository;
    UserRepository userRepository;
    ParticipantRepository participantRepository;
	StudyOrganizationClinicalStaffRepository studyOrganizationClinicalStaffRepository;

    protected LoginController() {
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String load = request.getParameter("load");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");
        }

        User user = (User) auth.getPrincipal();
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
            	//use LocaleContextHolder to set the display using the language selected on the login screen
            	String lang = LocaleContextHolder.getLocale().getLanguage();
            	//use getParticipantsPreferredLanguage to set the display using the users preferred language
            	//String lang = getParticipantsPreferredLanguage(user);
                if (ControllersUtils.isRequestComingFromMobile(request)) {
                    return new ModelAndView(new RedirectView("../mobile/inbox"));
                } else {
                    return new ModelAndView(new RedirectView("participant/participantInbox?lang="+lang));
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
            List<List<StudyParticipantCrfSchedule>> schedules = getOverdueAndUpcomingSchedules(clinicalStaff, load);
            if (load!=null && load.equals("all")){
                load="less";
            } else {
                load="all";
            }
            mv.addObject("overdue", schedules.get(0));
            mv.addObject("upcoming", schedules.get(1));
            mv.addObject("load", load);
        }
        return mv;
    }

    /**
     * Gets the participants preferred language to be used for display based on participants preference

     *
     * @param user the user
     * @return the participants preferred language
     */
    private String getParticipantsPreferredLanguage(User user) {
    	String lang = "en";
        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByUsername(user.getUsername());
        Collection<Participant> participantCollection = participantRepository.find(participantQuery);
        List<StudyParticipantAssignment> studyParticipantAssignments = null; 
        StudyParticipantAssignment studyParticipantAssignment = null;
        if(participantCollection !=null && !participantCollection.isEmpty()){
        	Object[] pArray = participantCollection.toArray();
        	studyParticipantAssignments = ((Participant)pArray[0]).getStudyParticipantAssignments();
        	if(studyParticipantAssignments != null && studyParticipantAssignments.size() > 0){
        		studyParticipantAssignment = studyParticipantAssignments.get(0);
        	}
        }
        
        if(studyParticipantAssignment != null){
        	if(studyParticipantAssignment.getHomeWebLanguage() != null){
        		if(studyParticipantAssignment.getHomeWebLanguage().equalsIgnoreCase("SPANISH")){
            		lang = "es";
            	}
            } else if(studyParticipantAssignment.getIvrsLanguage().equalsIgnoreCase("SPANISH")){
            	//only look for ivrsLang if homeWebLang is null.
            		lang = "es";
            }
        }
        	
        return lang;
	}

	private List<UserNotification> getNotifications(User user) {
        return user.getUserNotifications();
    }

    public List<List<StudyParticipantCrfSchedule>> getOverdueAndUpcomingSchedules(ClinicalStaff clinicalStaff, String load) {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
        int pastCount = 10;
        int upcomingCount = 10;
        if (load != null && load.equals("all")) {
            pastCount = 100;
            upcomingCount = 100;
        }
        query.filterByClinicalStaffId(clinicalStaff.getId());
        List<StudyOrganizationClinicalStaff> socs = studyOrganizationClinicalStaffRepository.find(query);
        ArrayList<List<StudyParticipantCrfSchedule>> out = new ArrayList<List<StudyParticipantCrfSchedule>>();
        Set<StudyParticipantCrfSchedule> overdue = new HashSet<StudyParticipantCrfSchedule>();
        Set<StudyParticipantCrfSchedule> upcoming = new HashSet<StudyParticipantCrfSchedule>();
//        out.add(overdue);
//        out.add(upcoming);
        Date today = new Date();
        Date week = DateUtils.addDaysToDate(today, 6);
        Date yesterday = DateUtils.addDaysToDate(today, -1);

        for (StudyOrganizationClinicalStaff staff : socs) {
            for (StudyParticipantAssignment studyParticipantAssignment : staff.getStudyOrganization().getStudyParticipantAssignments()) {
                for (StudyParticipantCrf spc : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    for (StudyParticipantCrfSchedule spcs : spc.getStudyParticipantCrfSchedules()) {
                        if (spcs.getStatus().equals(CrfStatus.PASTDUE)) {
                            if (pastCount > 0) {
                                overdue.add(spcs);
                                pastCount--;
                            }
                        }
                        if (spcs.getStartDate().after(yesterday) && spcs.getStartDate().before(week)) {
                            if (upcomingCount > 0) {
                                upcoming.add(spcs);
                                upcomingCount--;
                            }
                        }
                    }
                }
            }
        }
        List sortedUpcoming = new ArrayList<StudyParticipantCrfSchedule>(upcoming);
        Collections.sort(sortedUpcoming, new ParticipantDisplayNameComparator());
        List sortedOverdue = new ArrayList<StudyParticipantCrfSchedule>(overdue);
        Collections.sort(sortedOverdue, new ParticipantDisplayNameComparator());
        out.add(sortedOverdue);
        out.add(sortedUpcoming);
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
    
    @Required
	public void setParticipantRepository(ParticipantRepository participantRepository) {
		this.participantRepository = participantRepository;
	}
}