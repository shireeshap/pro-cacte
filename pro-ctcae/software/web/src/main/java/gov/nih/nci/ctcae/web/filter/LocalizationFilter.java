package gov.nih.nci.ctcae.web.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;



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
    	//we only support en or es for now.
    	if(lang.equalsIgnoreCase("en") || 
    			lang.equalsIgnoreCase("es")){
        	//use LocaleContextHolder to set the display using the language selected on the login screen
        	LocaleContextHolder.setLocale(new Locale(lang));
    	}
    	
    	chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
