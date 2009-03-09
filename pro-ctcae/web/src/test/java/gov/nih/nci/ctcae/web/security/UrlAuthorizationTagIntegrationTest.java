package gov.nih.nci.ctcae.web.security;

import javax.servlet.jsp.tagext.Tag;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class UrlAuthorizationTagIntegrationTest extends UrlAuthorizationIntegrationTestCase {

    private UrlAuthorizationTag urlAuthorizationTag;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        urlAuthorizationTag = new UrlAuthorizationTag();
        urlAuthorizationTag.setPageContext(pageContext);

        urlAuthorizationTag.setUrl("/pages/form/manageForm");
    }

    public void testAuthorize() throws Exception {

        assertEquals(Tag.EVAL_BODY_INCLUDE, urlAuthorizationTag.doStartTag());
    }
}
