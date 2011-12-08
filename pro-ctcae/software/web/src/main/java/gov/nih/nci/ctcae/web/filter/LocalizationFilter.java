package gov.nih.nci.ctcae.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;



/**
 * The Class LocalizationFilter.
 * Responsible for setting the locale at the time of login.
 * This happens before before spring creates a new session (for the session fixation problem) for the logged in user.
 * 
 */
public class LocalizationFilter implements Filter {
    private FilterConfig filterConfig = null;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

    	String lang = req.getParameter("lang");
    	lang = StringUtils.isBlank(lang) ? "en":lang;
    	
    	chain.doFilter(req, resp);
    	//we only support en or es for now.
    	if(lang.equalsIgnoreCase("en") || 
    			lang.equalsIgnoreCase("es")){
        	((HttpServletRequest)req).getSession().setAttribute("lang", lang);
    	} else {
    		((HttpServletRequest)req).getSession().setAttribute("lang", "en");
    	}
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
