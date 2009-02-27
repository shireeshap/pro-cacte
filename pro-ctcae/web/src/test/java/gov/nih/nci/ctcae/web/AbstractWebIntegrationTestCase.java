package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public abstract class AbstractWebIntegrationTestCase extends AbstractHibernateIntegrationTestCase {

    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;

    protected MockPageContext pageContext;

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

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, createWebApplicationContextForServlet("spring"));
        pageContext = new MockPageContext(servletContext);


    }

    private XmlWebApplicationContext createWebApplicationContextForServlet(String servletName) {
        ApplicationContext parent = getApplicationContext();
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setParent(parent);
        context.setServletContext(servletContext);
        //context.setConfigLocations(new String[]{"web/WEB-INF/" + servletName + "-servlet.xml"});

        //context.refresh();
        return context;
    }

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-dwr.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-security.xml");
        return list.toArray(new String[]{});


    }
}
