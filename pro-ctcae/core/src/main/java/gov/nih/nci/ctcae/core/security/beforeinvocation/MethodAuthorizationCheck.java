package gov.nih.nci.ctcae.core.security.beforeinvocation;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;

/**
 * @author Vinay Kumar
 * @crated Mar 16, 2009
 */
public interface MethodAuthorizationCheck {
    boolean authorize(Authentication authentication, MethodInvocation methodInvocation) throws AccessDeniedException;
}
