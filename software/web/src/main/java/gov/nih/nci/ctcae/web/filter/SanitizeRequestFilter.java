package gov.nih.nci.ctcae.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Amey
 * For preventing Cross Site Scripting attack.
 * (Fixes in response to app-scan performed on Aug 25th, 2014.)
 */
public class SanitizeRequestFilter implements Filter{

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter) throws IOException, ServletException {
	    // Sanitize request with XSSRequestWrapper for preventing XSS attack. (Note: add useHttpOnly="true" in proctcae.xml to handle HttpOnly issue.)
		filter.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
