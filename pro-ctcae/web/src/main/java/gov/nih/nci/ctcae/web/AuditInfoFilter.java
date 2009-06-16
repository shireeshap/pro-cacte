package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.cabig.ctms.web.filters.ContextRetainingFilterAdapter;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

//
/**
 * The Class AuditInfoFilter.
 *
 * @author Vinay Kumar
 * @since Dec 9, 2008
 */
public class AuditInfoFilter extends ContextRetainingFilterAdapter {

    /* (non-Javadoc)
      * @see gov.nih.nci.cabig.ctms.web.filters.FilterAdapter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
      */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)

        {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            // String username = ApplicationSecurityManager.getUser(httpReq);
            if (username != null) {
                DataAuditInfo
                        .setLocal(new DataAuditInfo(
                                username, request.getRemoteAddr(), new Date(),
                                httpReq.getRequestURI()));
            }
        } else {

            DataAuditInfo.setLocal(new DataAuditInfo("admin", request.getRemoteAddr(), new Date(),
                    httpReq.getRequestURI()));
        }
        chain.doFilter(request, response);
        DataAuditInfo.setLocal(null);
    }
}

