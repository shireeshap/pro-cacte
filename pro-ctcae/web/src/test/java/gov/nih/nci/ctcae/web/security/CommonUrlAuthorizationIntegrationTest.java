package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.cabig.ctms.CommonsSystemException;
import gov.nih.nci.cabig.ctms.tools.spring.ControllerUrlResolver;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class CommonUrlAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {


    public void testAuthorizeUserForAllUrl() {

        List<String> allowedUrls = new ArrayList();

        urlResolver = (ControllerUrlResolver) webApplicationContext.getBean("urlResolver");

        Map controllerBeans = webApplicationContext.getBeansOfType(Controller.class);

        assertFalse("must find controller beans", controllerBeans.isEmpty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication.getAuthorities());
        for (Object controllerBean : controllerBeans.keySet()) {
            try {
                String url = urlResolver.resolve(String.valueOf(controllerBean)).getUrl(true);

                allowedUrls.add(url);
            } catch (CommonsSystemException e) {

            }
        }
        authorizeUser(defaultUser, allowedUrls);
    }


    public void testAuthorizeForHomePage() throws Exception {

        assertTrue("all authenticated user must see home page", urlAuthorizationCheck.authorize("/pages/home"));
    }

    public void testAuthorizeForLogoutLink() throws Exception {

        assertTrue("all authenticated user must see logout link", urlAuthorizationCheck.authorize("/j_spring_security_logout"));
    }


}