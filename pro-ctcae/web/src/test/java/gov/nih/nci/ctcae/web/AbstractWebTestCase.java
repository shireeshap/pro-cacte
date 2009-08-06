package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;
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