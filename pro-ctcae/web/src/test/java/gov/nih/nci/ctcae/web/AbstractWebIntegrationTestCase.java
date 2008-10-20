package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.AbstractJpaIntegrationTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * @author Vinay Kumar
 * @crated Oct 18, 2008
 */
public abstract class AbstractWebIntegrationTestCase extends AbstractJpaIntegrationTestCase {

    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected MockServletContext servletContext;
    protected MockHttpSession session;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        servletContext = new MockServletContext();
        session = new MockHttpSession(servletContext);
        request = new MockHttpServletRequest(servletContext);
        request.setMethod("POST");
        request.setSession(session);
        response = new MockHttpServletResponse();

    }

    @Override
    protected String[] getConfigLocations() {
        String[] configLocations = super.getConfigLocations();
        List<String> list = new ArrayList<String>(Arrays.asList(configLocations));
        list.add("classpath*:gov/nih/nci/ctcae/web/applicationContext-web-dwr.xml");
        return list.toArray(new String[]{});


    }
}
