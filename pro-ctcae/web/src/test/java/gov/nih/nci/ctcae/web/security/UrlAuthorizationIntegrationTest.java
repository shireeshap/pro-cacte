package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.cabig.ctms.CommonsSystemException;
import gov.nih.nci.cabig.ctms.tools.spring.ControllerUrlResolver;
import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.Controller;

import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class UrlAuthorizationIntegrationTest extends AbstractWebIntegrationTestCase {

    private UrlAuthorizationCheck urlAuthorizationCheck;
    private ControllerUrlResolver urlResolver;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        urlResolver = (ControllerUrlResolver) webApplicationContext.getBean("urlResolver");

        Map controllerBeans = webApplicationContext.getBeansOfType(Controller.class);

        assertFalse("must find controller beans", controllerBeans.isEmpty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication.getAuthorities());
        for (Object controllerBean : controllerBeans.keySet()) {
            try {
                String url = urlResolver.resolve(String.valueOf(controllerBean)).getUrl(true);

                assertTrue(String.format("user %s can not access url:%s ", authentication.getName(), url), urlAuthorizationCheck.authorize(url));
            } catch (CommonsSystemException e) {

            }
        }
    }

    public void testAuthorize() throws Exception {

        assertTrue(urlAuthorizationCheck.authorize("/pages/form/manageForm"));
    }

    @Required
    public void setUrlAuthorizationCheck(UrlAuthorizationCheck urlAuthorizationCheck) {
        this.urlAuthorizationCheck = urlAuthorizationCheck;
    }
}