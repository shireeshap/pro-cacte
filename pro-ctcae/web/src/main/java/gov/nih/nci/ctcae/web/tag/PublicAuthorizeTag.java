package gov.nih.nci.ctcae.web.tag;


import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author
 */
public class PublicAuthorizeTag extends TagSupport {

    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Display or hide certain portions of a JSP based on user’s access level for the application code with which the JSP is being invoked
     *
     * @throws javax.servlet.jsp.JspException if entity provided is of unsupported type
     * @returns int to satisfy the tag interface
     */
    public int doStartTag() throws JspException {

        boolean isAuthenticated = authorizeUser();
        if (!isAuthenticated) {
            return Tag.EVAL_BODY_INCLUDE;
        }


        return Tag.SKIP_BODY;

    }


    private boolean authorizeUser() throws JspException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getPrincipal() != null : false;


    }


}