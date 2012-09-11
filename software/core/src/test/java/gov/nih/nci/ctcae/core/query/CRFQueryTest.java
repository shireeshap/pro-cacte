package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author
 * @since Oct 7, 2008
 */
public class CRFQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        CRFQuery crfQuery = new CRFQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT o from CRF o order by o.id", crfQuery
                        .getQueryString());

    }


}
