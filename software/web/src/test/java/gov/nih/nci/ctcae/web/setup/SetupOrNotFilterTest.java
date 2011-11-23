package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.web.WebTestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;

import static org.easymock.EasyMock.expect;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupOrNotFilterTest extends WebTestCase {
    private FilterChain filterChain;
    private SetupOrNotFilter filter;
    private SetupStatus setupStatus;

    public void setUp() throws Exception {
        super.setUp();
        WebApplicationContext applicationContext = registerMockFor(WebApplicationContext.class);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        filterChain = registerMockFor(FilterChain.class);

        filter = new SetupOrNotFilter();
        setupStatus = registerMockFor(SetupStatus.class);

        expect(applicationContext.getBean("setupStatus")).andReturn(setupStatus);
        replayMocks();
        filter.init(new MockFilterConfig(servletContext));
        verifyMocks();
        resetMocks();

        request.setContextPath("/proctcae");
    }

    public void testRedirectsToSetupWhenNecessary() throws Exception {
        expect(setupStatus.isSetupNeeded()).andReturn(true);
        replayMocks();

        filter.doFilter(request, response, filterChain);
        verifyMocks();

        assertNotNull(response.getRedirectedUrl());
        assertEquals("/proctcae/setup/initial", response.getRedirectedUrl());
    }

    public void testFallThroughWhenNotNecessary() throws Exception {
        filterChain.doFilter(request, response);
        expect(setupStatus.isSetupNeeded()).andReturn(false);
        replayMocks();

        filter.doFilter(request, response, filterChain);
        verifyMocks();
    }
}

