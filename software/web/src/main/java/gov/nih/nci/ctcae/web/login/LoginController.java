package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.StudyParticipantAssignmentQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class LoginController extends AbstractController {
    UserRepository userRepository;
    GenericRepository genericRepository;
    private static String ENGLISH = "ENGLISH";
    private static String SPANISH = "SPANISH";
    private static String IS_LOGIN = "isLogin";

	
	public static final int MAX_RESULTS_DISPLAYED = 25;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");
        }

        User user = (User) auth.getPrincipal();
        String lang = getDisplayLanguage(request, user);
        
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
            	//use getParticipantsPreferredLanguage to set the display using the users preferred language
                return new ModelAndView(new RedirectView("participant/participantInbox?lang="+lang));
            } else {
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.ENGLISH);
            }
            if (userRole.getRole().equals(Role.ADMIN)) {
            	ModelAndView mv = new ModelAndView("home");
            	if(user.hasRole(Role.LEAD_CRA)){
                    mv.addObject("studyLevelRole", true);
            	}
            	if (user.hasRole(Role.SITE_CRA)){
            		mv.addObject("siteLevelRole", true);
            	}
                return mv;
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
    
    private String getDisplayLanguage(HttpServletRequest request, User user){
    	 String lang = null;
         String isLoginFlow = request.getParameter(IS_LOGIN);
         if(!StringUtils.isEmpty(isLoginFlow)){
         	lang = getParticipantPrefferredLanguage(user.getId());
             if(StringUtils.isEmpty(lang)){
             	  lang = "en";
             } else {
             	if(ENGLISH.equals(lang)){
             		lang = "en";
             	} else if(SPANISH.equals(lang)) {
             		lang = "es";
             	}
             }
         } else {
         	//Use Locale value of the requesting page to set the language.
         	if(RequestContextUtils.getLocale(request)!= null){
         		lang = RequestContextUtils.getLocale(request).toString();
         	}
         }
         
         return lang;
    }
    
    public String getParticipantPrefferredLanguage(Integer userId) {
		StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();
		query.filterByUserId(userId);
		StudyParticipantAssignment spa = (StudyParticipantAssignment) genericRepository.findSingle(query);
		if(spa != null){
			return spa.getHomeWebLanguage();
		}
		return null;
	}

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
	public void setGenericRepository(GenericRepository genericRepository){
		this.genericRepository = genericRepository;
	}
}