package gov.nih.nci.ctcae.web.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Jan 15, 2010
 * Time: 9:51:07 AM
 * To change this template use File | Settings | File Templates.
 */

public class SecurityFilter implements Filter {
    private static final String HEADER_PRAGMA = "Pragma";

    private static final String HEADER_EXPIRES = "Expires";

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private FilterConfig filterConfig = null;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //Do not allow caching of SSL pages
        disableCaching(response);
        //Disable WebDAV, or disallow unneeded HTTP methods
        disallowUnneededHttpMethods(response);
        if (uriContainsIllegalCharacters(request)) {
            response.sendError(403);
            return;
        }

        chain.doFilter(new SecurityRequestWrapper(request), new SecurityResponseWrapper(response));
    }

    private boolean uriContainsIllegalCharacters(HttpServletRequest request) {
        String uri = request.getRequestURI();
        Pattern p = Pattern.compile("[^a-zA-Z0-9?/=\\.\\-_]");
        Matcher m = p.matcher(uri);
        if (m.find()) {
            return true;
        }

        if (request.getMethod().toUpperCase().equals("GET")) {
            Enumeration parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String param = (String) parameterNames.nextElement();
                String paramValue = request.getParameter(param);
                if (!StringUtils.isBlank(paramValue)) {
                    m = p.matcher(paramValue);
                    if (m.find()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void disableCaching(HttpServletResponse response) {
        disallowUnneededHttpMethods(response);
        response.addHeader(HEADER_PRAGMA, "no-cache");
        response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
        response.setDateHeader(HEADER_EXPIRES, 1L);
    }

    private void disallowUnneededHttpMethods(HttpServletResponse response) {
        response.setHeader("ALLOW", "GET, HEAD, POST, PUT, TRACE, OPTIONS");
    }


    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
