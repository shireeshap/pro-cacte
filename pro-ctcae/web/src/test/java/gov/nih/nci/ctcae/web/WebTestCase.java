package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.AbstractTestCase;
import gov.nih.nci.ctcae.web.ControllerTools;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

/**
 * @author
 */
public abstract class WebTestCase extends AbstractTestCase {
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;
    protected ControllerTools controllerTools;
    protected final Log log = LogFactory.getLog(getClass());

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new MockServletContext();
        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(servletContext);
        request.setMethod("POST");
        request.setSession(session);
        response = new MockHttpServletResponse();
        controllerTools = new ControllerTools();

    }

//    protected void login(Object currentLoggedInUser) {
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(currentLoggedInUser, "password");
//        SecurityContextHolder.getContext().setAuthentication(token);
//    }
}
