package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.domain.User;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class MethodAuthorizationIntegrationTestCase extends AbstractHibernateIntegrationTestCase {

    protected PrivilegeAuthorizationCheck privilegeAuthorizationCheck;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


    }

    public void authorizeUser(User user, List<String> allowedUrls) {

        login(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertFalse("user must have authorities", authentication.getAuthorities().length == 0);

//        for (String url : allowedUrls) {
//
//            assertTrue(String.format("user %s can not access url:%s ", authentication.getName(), url), urlAuthorizationCheck.authorize(url));
//            allUrls.remove(url);
//        }
//
//
//        //user must not see other urls;
//        for (String url : allUrls) {
//            assertFalse(String.format("user %s must not access url:%s ", authentication.getName(), url), urlAuthorizationCheck.authorize(url));
//        }

    }


    @Required
    public void setPrivilegeAuthorizationCheck(PrivilegeAuthorizationCheck privilegeAuthorizationCheck) {
        this.privilegeAuthorizationCheck = privilegeAuthorizationCheck;
    }

}
