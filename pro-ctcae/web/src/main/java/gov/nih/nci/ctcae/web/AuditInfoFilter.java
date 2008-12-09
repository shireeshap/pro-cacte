package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @author Vinay Kumar
 * @crated Dec 9, 2008
 */
public class AuditInfoFilter extends
	gov.nih.nci.cabig.ctms.web.filters.ContextRetainingFilterAdapter {

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
						 final FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication != null)
//
//		{
//			String username = SecurityContextHolder.getContext().getAuthentication().getName();
//			// String username = ApplicationSecurityManager.getUser(httpReq);
//			if (username != null) {
//				DataAuditInfo
//					.setLocal(new DataAuditInfo(
//						username, request.getRemoteAddr(), new Date(),
//						httpReq.getRequestURI()));
//			}
//		}

		DataAuditInfo
			.setLocal(new DataAuditInfo(
				"admin", request.getRemoteAddr(), new Date(),
				httpReq.getRequestURI()));

		chain.doFilter(request, response);
		edu.nwu.bioinformatics.commons.DataAuditInfo.setLocal(null);
	}
}

