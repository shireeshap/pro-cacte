package gov.nih.nci.ctcae.web.security;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Vinay Kumar
 * @since Feb 26, 2009
 */
public class UrlAuthorizationTag extends TagSupport {

    protected static final Log logger = LogFactory.getLog(UrlAuthorizationTag.class);


    private String url;

    //~ Methods ========================================================================================================

    public int doStartTag() throws JspException {

        if (StringUtils.isBlank(url)) {
            return Tag.SKIP_BODY;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "SecurityContextHolder did not return a non-null Authentication object, so skipping tag body");
            }

            return Tag.SKIP_BODY;
        }


        ApplicationContext applicationContext = getContext(pageContext);
        UrlAuthorizationCheck urlAuthorizationCheck = (UrlAuthorizationCheck) applicationContext.getBean("urlAuthorizationCheck");

        boolean authorize = urlAuthorizationCheck.authorize(url);
        if (authorize) {
            return Tag.EVAL_BODY_INCLUDE;
        } else {


            return Tag.SKIP_BODY;
        }


    }

    /**
     * Allows test cases to override where application context obtained from.
     *
     * @param pageContext so the <code>ServletContext</code> can be accessed as required by Spring's
     *                    <code>WebApplicationContextUtils</code>
     * @return the Spring application context (never <code>null</code>)
     */
    protected ApplicationContext getContext(PageContext pageContext) {
        ServletContext servletContext = pageContext.getServletContext();

        return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

