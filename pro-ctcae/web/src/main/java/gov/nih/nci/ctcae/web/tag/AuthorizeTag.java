package gov.nih.nci.ctcae.web.tag;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

//
/**
 * The Class AuthorizeTag.
 *
 * @author
 */
public class AuthorizeTag extends TagSupport {

    /**
     * The logger.
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Display or hide certain portions of a JSP based on users access level for the application code with which the JSP is being invoked.
     *
     * @return int to satisfy the tag interface
     * @throws javax.servlet.jsp.JspException if entity provided is of unsupported type
     * @throws JspException                   the jsp exception
     */
    public int doStartTag() throws JspException {

        boolean isAuthorized = authorizeUser();
        if (isAuthorized) {
            return Tag.EVAL_BODY_INCLUDE;
        }


        return Tag.SKIP_BODY;

    }


    /**
     * Authorize user.
     *
     * @return true, if successful
     * @throws JspException the jsp exception
     */
    private boolean authorizeUser() throws JspException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getPrincipal() != null : false;


    }


}
