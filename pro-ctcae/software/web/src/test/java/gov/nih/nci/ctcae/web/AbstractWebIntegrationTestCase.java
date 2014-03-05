package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.classextension.EasyMock;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * @author Vinay Kumar
 * @since Oct 18, 2008
 */
public abstract class AbstractWebIntegrationTestCase extends TestDataManager {

    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;
    protected MockPageContext pageContext;
    protected XmlWebApplicationContext webApplicationContext;
    protected Set<Object> mocks = new HashSet<Object>();

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        servletContext = new MockServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, "");


        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(servletContext);
        request.setMethod("POST");
        request.setSession(session);
        response = new MockHttpServletResponse();

        webApplicationContext = createWebApplicationContextForServlet("spring");
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
        pageContext = new MockPageContext(servletContext);

    }

    private XmlWebApplicationContext createWebApplicationContextForServlet(String servletName) {
        ApplicationContext parent = getApplicationContext();
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setParent(parent);
        context.setServletContext(servletContext);
        return context;
    }
    

    ////// MOCK REGISTRATION AND HANDLING
    public <T> T registerMockFor(Class<T> forClass) {
        return registered(EasyMock.createMock(forClass));
    }

    public void replayMocks() {
        for (Object mock : mocks) EasyMock.replay(mock);
    }

    public void verifyMocks() {
        for (Object mock : mocks) EasyMock.verify(mock);
    }

    public void resetMocks() {
        for (Object mock : mocks) EasyMock.reset(mock);
    }

    private <T> T registered(T mock) {
        mocks.add(mock);
        return mock;
    }

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-dwr.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-security.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-common.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-validator.xml");
        return list.toArray(new String[]{});
    }

}
