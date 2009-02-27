package gov.nih.nci.ctcae.web.security;

import gov.nih.nci.ctcae.web.AbstractWebIntegrationTestCase;
import gov.nih.nci.ctcae.web.MockSection;

import javax.servlet.jsp.tagext.Tag;

/**
 * @author Vinay Kumar
 * @crated Feb 26, 2009
 */
public class SectionAuthorizationTagIntegrationTest extends AbstractWebIntegrationTestCase {

    private SectionAuthorizationTag sectionAuthorizationTag;

    private MockSection section;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        sectionAuthorizationTag = new SectionAuthorizationTag();
        sectionAuthorizationTag.setPageContext(pageContext);

        section = new MockSection();
        section.setMainUrl("/pages/form/manageForm");
        sectionAuthorizationTag.setSection(section);
    }

    public void testAuthorize() throws Exception {

        assertEquals(Tag.EVAL_BODY_INCLUDE, sectionAuthorizationTag.doStartTag());
    }
}
