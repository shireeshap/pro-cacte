package gov.nih.nci.ctcae.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Provides caching
 *
 * */
public class CachingFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(CachingFilter.class.getName());
    private String expiryDate;

    protected boolean acceptsEncoding(HttpServletRequest request, String name) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent.contains("Firefox")
                || userAgent.contains("MSIE 7.0")
                || userAgent.contains("MSIE 8.0");
    }


    public void init(FilterConfig filterConfig) throws ServletException {
        //set far future expiry date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 40);
        expiryDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(calendar.getTime());

        LOG.info(String.format("Initializing caching filter with expiry date of %s ", expiryDate));

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletResponse instanceof HttpServletResponse && servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String path = httpServletRequest.getServletPath();
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setHeader("Expires", expiryDate);
            httpServletResponse.setHeader("Cache-Control", String.format("public,max-age=1576800000"));  //max-age=60 * 60 * 24 * 365 * 50  (50 years)
           // httpServletResponse.setHeader();
            LOG.debug("expiration date set for path: " + path + " = " + expiryDate);
        } else {
            LOG.info(String.format("servlet response %s and request %s are not of type HttpServletResponse " +
                    "and HttpServletRequest so skipping caching", servletResponse, servletRequest));

        }
        // chain.doFilter() should be called after writing the header. If it is
        // called _before_ writing the header
        filterChain.doFilter(servletRequest, servletResponse);

    }

    public void destroy() {

    }
}

