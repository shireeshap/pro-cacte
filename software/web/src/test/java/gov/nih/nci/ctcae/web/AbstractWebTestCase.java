package gov.nih.nci.ctcae.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

/**
 * @author
 */
public abstract class AbstractWebTestCase extends TestDataManager {
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;
    protected ControllerTools controllerTools;

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        Resource r = new FileSystemResource("");
        String path = null;
		try {
			path = r.getFile().getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-dwr.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-security.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-common.xml");
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-validator.xml");
//        list.add(String.format("file:" + path + "/web/src/main/webapp/WEB-INF/spring-servlet.xml"));
        return list.toArray(new String[]{});
    }
    
    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        servletContext = new MockServletContext();
        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(servletContext);
        request.setSession(session);
        response = new MockHttpServletResponse();
        controllerTools = new ControllerTools();
        new ReportResultsHelper().setGenericRepository(genericRepository);
    }
}