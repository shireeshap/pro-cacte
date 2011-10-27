package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        //Resource r = new FileSystemResource("");
        //try {
            //String path = r.getFile().getCanonicalPath();
            //System.out.println(path);
            //context.setConfigLocations(new String[]{String.format("file:" + path + "/web/src/main/webapp/WEB-INF/%s-servlet.xml", servletName)});
        //} catch (IOException e) {
        //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       // }

        //context.refresh();
        return context;
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
