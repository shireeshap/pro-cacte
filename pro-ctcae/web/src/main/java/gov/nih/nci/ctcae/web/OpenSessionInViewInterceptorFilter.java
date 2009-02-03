package gov.nih.nci.ctcae.web;

import gov.nih.nci.cabig.ctms.web.filters.ContextRetainingFilterAdapter;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * A filter which implements the Open Session In View pattern.  Different
 * from the one built into Spring because this one delegates to an instance of
 * {@link org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor} configured in the application context.
 * This permits the use of the same interceptor for deployed code & unit tests.
 * 
 * @author Rhett Sutphin
 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewFilter
 */
public class OpenSessionInViewInterceptorFilter extends ContextRetainingFilterAdapter {
	
	/** The interceptor bean name. */
	private String interceptorBeanName;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.ctms.web.filters.ContextRetainingFilterAdapter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		setInterceptorBeanName(filterConfig.getInitParameter("interceptorBeanName"));
	}

	/**
	 * Do filter.
	 * 
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * 
	 * @see org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion
	 */
	@Override
	public void doFilter(
		ServletRequest request, ServletResponse response, FilterChain chain
	) throws IOException, ServletException {
		log.debug("Opening session for request");
		OpenSessionInViewInterceptor interceptor
			= (OpenSessionInViewInterceptor) getApplicationContext().getBean(getInterceptorBeanName());
		WebRequest webRequest = new ServletWebRequest((HttpServletRequest) request);
		interceptor.preHandle(webRequest);
		try {
			chain.doFilter(request, response);
			interceptor.postHandle(webRequest, null);
		} finally {
			interceptor.afterCompletion(webRequest, null);
			log.debug("Session closed");
		}
	}


	/**
	 * Gets the interceptor bean name.
	 * 
	 * @return the interceptor bean name
	 */
	public String getInterceptorBeanName() {
		return interceptorBeanName;
	}

	/**
	 * Sets the interceptor bean name.
	 * 
	 * @param interceptorBeanName the new interceptor bean name
	 */
	public void setInterceptorBeanName(String interceptorBeanName) {
		this.interceptorBeanName = interceptorBeanName;
	}
}
