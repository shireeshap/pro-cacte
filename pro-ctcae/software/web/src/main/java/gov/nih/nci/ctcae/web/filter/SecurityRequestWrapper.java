package gov.nih.nci.ctcae.web.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class SecurityRequestWrapper extends HttpServletRequestWrapper {

    public SecurityRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);

    }

    private String cleanXSS(String value) {
        value = new HTMLInputFilter().filter(value);
//        value = value.replaceAll("<", "").replaceAll(">", "");
//        value = value.replaceAll("\\(", "").replaceAll("\\)", "");
//        value = value.replaceAll("'", "");
//        value = value.replaceAll(";", "");
//        value = value.replaceAll("&", "");
//        value = value.replaceAll("eval\\((.*)\\)", "");
//        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//        value = value.replaceAll("script", "");
        return value;
    }


    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    if (!cookie.getSecure()) {
                        cookie.setSecure(true);
                    }
                }
            }
        }
        return cookies;
    }

    @Override
    public Object getAttribute(String s) {
        Object attribute = super.getAttribute(s);
        if (attribute != null) {
            if (attribute instanceof String) {
                return cleanXSS((String) attribute);
            }
            if (attribute instanceof StringBuilder || attribute instanceof StringBuffer) {
                return cleanXSS(attribute.toString());
            }
        }
        return attribute;
    }
}