package gov.nih.nci.ctcae.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

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
    List<String> allowedParams = Arrays.asList("participant.user.confirmPassword", "participant.user.password", "participant.emailAddress");
    List<String> urlsToSanitizeForHttpPost = Arrays.asList("/proctcae/pages/participant/create");

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //Do not allow caching of SSL pages
       disableCaching(request,response);
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
        Pattern p = Pattern.compile("[^a-zA-Z0-9?'/=\\.\\-_\\[\\]%, ]");
        Matcher m = p.matcher(uri);
        
        if (m.find()) {
            return true;
        }

        Enumeration parameterNames = request.getParameterNames();
        if (request.getMethod().toUpperCase().equals("GET")) {
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
        } else if(request.getMethod().toUpperCase().equals("POST") && urlsToSanitizeForHttpPost.contains(uri)){
        	//not allowing % for post urls in urlsToSanitizeForHttpPost (barring allowedParams)
        	p = Pattern.compile("[^a-zA-Z0-9?'/=\\.\\-_\\[\\], ]");
            while (parameterNames.hasMoreElements()) {
                String param = (String) parameterNames.nextElement();
                String paramValue = request.getParameter(param);
                if (!StringUtils.isBlank(paramValue) && (!allowedParams.contains(param))) {
                    m = p.matcher(paramValue);
                    if (m.find()) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }


        private void disableCaching(HttpServletRequest request, HttpServletResponse response) {
            if (!StringUtils.containsIgnoreCase(request.getRequestURI(), ".css") &&
                    !StringUtils.containsIgnoreCase(request.getRequestURI(), ".js") &&
                    !StringUtils.containsIgnoreCase(request.getRequestURI(), "images")) {
                response.addHeader(HEADER_PRAGMA, "no-cache");
                response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
                response.setDateHeader(HEADER_EXPIRES, 1L);
            }
        }

        private void disallowUnneededHttpMethods(HttpServletResponse response) {
        response.setHeader("ALLOW", "GET, HEAD, POST, PUT, TRACE, OPTIONS");
    }


    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

}
