package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class LoginController extends AbstractController {
    UserRepository userRepository;
	
	public static final int MAX_RESULTS_DISPLAYED = 25;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");
        }

        User user = (User) auth.getPrincipal();
        RequestContextUtils.getLocale(request);
        String lang  = "en";
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
            	//use LocaleContextHolder to set the display using the language selected on the login screen
            	if(request.getSession().getAttribute("lang") != null){
            		lang = request.getSession().getAttribute("lang").toString();
            	}
            	//Use Locale value of the requesting page to set the language.
            	if(RequestContextUtils.getLocale(request)!= null){
            		lang = RequestContextUtils.getLocale(request).toString();
            	}
            	//use getParticipantsPreferredLanguage to set the display using the users preferred language
                return new ModelAndView(new RedirectView("participant/participantInbox?lang="+lang));
            } else {
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.ENGLISH);
            }
            if (userRole.getRole().equals(Role.ADMIN)) {
                return new ModelAndView("home");
            }
        }

        ModelAndView mv = new ModelAndView("home");
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
        for (OrganizationClinicalStaff organizationClinicalStaff : clinicalStaff.getOrganizationClinicalStaffs()) {
            for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaff.getStudyOrganizationClinicalStaff()) {
                if (studyOrganizationClinicalStaff.getRoleStatus().equals(RoleStatus.ACTIVE) && studyOrganizationClinicalStaff.getStatusDate().before(today)) {
                    Role role = studyOrganizationClinicalStaff.getRole();
                    switch(role){  
                    
                    case SITE_CRA:
                    case SITE_PI: siteLevelRole = true;
                    			  break;
                    	
                    case NURSE:
                    case TREATING_PHYSICIAN: nurseLevelRole = true;
                    						 break;
                    	
                    case ODC: odc = true;
                    		  		break;
                    
                    case LEAD_CRA:
                    case PI: studyLevelRole = true;
                    		 break;
                    
                    default :
                    		 Log.warn("User is not a SITE_CRA, SITE_PI, NURSE, TREATING_PHYSICIAN, ODC, LEAD_CRA, PI");
                    		 break;
                    }
                }
            }
        }
        mv.addObject("siteLevelRole", siteLevelRole);
        mv.addObject("studyLevelRole", studyLevelRole);
        mv.addObject("nurseLevelRole", nurseLevelRole);
        mv.addObject("odc", odc);
        return mv;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}