package gov.nih.nci.ctcae.web.tag;

import junit.framework.TestCase;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.springframework.mock.web.MockPageContext;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * Testing the Authorize Tag
 *
 * @author Created on 11/21/2007
 */
public class AuthorizeTagTest extends TestCase {
    //~ Instance fields ================================================================================================

    private AuthorizeTag authorizeTag = new AuthorizeTag();
    private UsernamePasswordAuthenticationToken currentUser;
    private MockPageContext pageContext;


    protected void setUp() throws Exception {
        super.setUp();


        currentUser = new UsernamePasswordAuthenticationToken("abc", "123");

        SecurityContextHolder.getContext().setAuthentication(currentUser);
    }


    public void testOutputBodyWhenUseIsLoggedIn()
            throws JspException {

        assertEquals("authorized ", Tag.EVAL_BODY_INCLUDE, authorizeTag.doStartTag());


        assertEquals("authorized - COMPANY_MANAGER", Tag.EVAL_BODY_INCLUDE, authorizeTag.doStartTag());


    }


    public void testSkiptBodyWhenUserIsNotLoggedIn()
            throws JspException {
        SecurityContextHolder.getContext().setAuthentication(null);
        assertEquals("not authorized", Tag.SKIP_BODY, authorizeTag.doStartTag());


    }


    protected void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
    }

}

