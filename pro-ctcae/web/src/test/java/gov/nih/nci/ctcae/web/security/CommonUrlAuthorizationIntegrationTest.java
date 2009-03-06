package gov.nih.nci.ctcae.web.security;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class CommonUrlAuthorizationIntegrationTest extends UrlAuthorizationIntegrationTestCase {


    public void testAuthorizeForHomePage() throws Exception {

        assertTrue("all authenticated user must see home page", urlAuthorizationCheck.authorize("/pages/home"));
    }

    public void testAuthorizeForLogoutLink() throws Exception {

        assertTrue("all authenticated user must see logout link", urlAuthorizationCheck.authorize("/j_spring_security_logout"));
    }


}