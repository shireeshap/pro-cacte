package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.cabig.ctms.web.filters.ContextRetainingFilterAdapter;
import gov.nih.nci.ctcae.core.SetupStatus;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vinay Kumar
 * @crated Mar 18, 2009
 */
public class SetupOrNotFilter extends ContextRetainingFilterAdapter {
    private SetupStatus status;

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        status = (SetupStatus) getApplicationContext().getBean("setupStatus");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (status.isSetupNeeded()) {
            log.debug("Initial setup required.  Redirecting.");
            try {
                new RedirectView("/setup/initial", true).render(null, (HttpServletRequest) request, (HttpServletResponse) response);
            } catch (Exception e) {
                throw new ServletException("Redirect view rending failed", e);
            }
        } else {
            log.debug("Initial setup complete.  Proceeding.");
            chain.doFilter(request, response);
        }
    }
}

