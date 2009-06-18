package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class LoginController extends AbstractController {
    ClinicalStaffRepository clinicalStaffRepository;

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
        }

        ClinicalStaffQuery query = new ClinicalStaffQuery();
        query.filterByUserName(user.getUsername());
        ClinicalStaff clinicalStaff = clinicalStaffRepository.findSingle(query);

        if (clinicalStaff == null) {
            throw new CtcAeSystemException("User is neither a participant nor a clinical staff - " + user.getUsername());
        }

        ModelAndView mv = new ModelAndView("home");
        mv.addObject("notifications", user.getUserNotifications());
        return mv;
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}