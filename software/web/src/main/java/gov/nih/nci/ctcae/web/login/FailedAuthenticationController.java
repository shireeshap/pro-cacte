package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class FailedAuthenticationController extends AbstractController {
    UserRepository userRepository;

    protected FailedAuthenticationController() {
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new CtcAeSystemException("Cannot find not-null Authentication object. Make sure the user is logged in");

        }

        User user = (User) auth.getPrincipal();

        return null;
    }


    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}