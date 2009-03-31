package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.setup.SetupCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class LoginController extends AbstractController {

    protected LoginController() {
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");

        }

        User user = (User) auth.getPrincipal();
        if (user.getUserRoles().size() == 1) {
            if (user.getUserRoles().get(0).getRole().equals(Role.PARTICIPANT)) {
                return new ModelAndView(new RedirectView("participant/participantInbox"));
            }
        }

        return new ModelAndView("home");
    }

}