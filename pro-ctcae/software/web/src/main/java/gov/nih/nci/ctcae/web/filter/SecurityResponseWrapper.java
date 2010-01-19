package gov.nih.nci.ctcae.web.filter;

import javax.servlet.http.*;

public final class SecurityResponseWrapper extends HttpServletResponseWrapper {

    public SecurityResponseWrapper(HttpServletResponse servletResponse) {
        super(servletResponse);
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (cookie != null) {
            if (!cookie.getSecure()) {
                cookie.setSecure(true);
            }
        }
        super.addCookie(cookie);    //To change body of overridden methods use File | Settings | File Templates.
    }

    
}