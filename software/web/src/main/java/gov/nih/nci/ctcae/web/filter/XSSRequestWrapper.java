package gov.nih.nci.ctcae.web.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Amey
 * Sanitize the HttpRequest to remove all the potential patterns in URL to prevent Cross-Site Scripting (XSS) attack.
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper{
	Log logger = LogFactory.getLog(this.getClass().getName());

	public XSSRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	 private static Pattern[] patterns = new Pattern[]{
	        // Script fragments
	        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
	        //img
	        Pattern.compile("<img[\r\n]*(.*?)>", Pattern.CASE_INSENSITIVE),
	        // src='...'
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // lonely script tags
	        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // eval(...)
	        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // expression(...)
	        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // javascript:...
	        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
	        // vbscript:...
	        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
	        // onload(...)=...
	        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
	    };

	    @Override
	    public String[] getParameterValues(String parameter) {
	        String[] values = super.getParameterValues(parameter);
	 
	        if (values == null) {
	            return null;
	        }
	 
	        int count = values.length;
	        String[] encodedValues = new String[count];
	        for (int i = 0; i < count; i++) {
	            encodedValues[i] = stripXSS(values[i]);
	        }
	 
	        return encodedValues;
	    }
	 
	    @Override
	    public String getParameter(String parameter) {
	        String value = super.getParameter(parameter);
	        return stripXSS(value);
	    }
	 
	    @Override
	    public String getHeader(String name) {
	        String value = super.getHeader(name);
	        return stripXSS(value);
	    }
	    
	    @Override
	    public StringBuffer getRequestURL() {
	    	String value = super.getRequestURL().toString();
	        return new StringBuffer(stripXSS(value));
	    }
	    
	    @Override
	    public String getRequestURI() {
	    	String value = super.getRequestURI();
	        return stripXSS(value);
	    }
	    
	    @Override
	    public String getPathInfo() {
	    	String value = super.getPathInfo();
	    	return stripXSS(value);
	    }
	    
	    private String stripXSS(String value) {
	        if (value != null) {
	        	value = new HTMLInputFilter().filter(value);
	        	try {
					value = URLDecoder.decode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					logger.error("XSSRequestWrapper: Error in decoding Url: " + e.getMessage());
				}
	 
	            // Avoid null characters
	            value = value.replaceAll("\0", "");
	            
	            // Remove all sections that match a pattern
	            for (Pattern scriptPattern : patterns){
	                value = scriptPattern.matcher(value).replaceAll("");
	            }
	        }
	        return value;
	    }

}
