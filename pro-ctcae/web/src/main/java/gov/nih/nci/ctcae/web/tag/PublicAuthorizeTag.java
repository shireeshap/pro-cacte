package gov.nih.nci.ctcae.web.tag;


import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

//
/**
 * The Class PublicAuthorizeTag.
 *
 * @author
 */
public class PublicAuthorizeTag extends TagSupport {

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

        boolean isAuthenticated = authorizeUser();
        if (!isAuthenticated) {
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