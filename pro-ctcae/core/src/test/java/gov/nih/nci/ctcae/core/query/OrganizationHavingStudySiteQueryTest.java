package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author  Vinay Kumar
 * @crated Nov 21, 2008
 */
public class OrganizationHavingStudySiteQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        OrganizationHavingStudySiteQuery query = new OrganizationHavingStudySiteQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT distinct(ss.organization) from StudySite ss order by ss.organization.name", query
                        .getQueryString());

    }




}