package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author
 * @crated Oct 7, 2008
 */
public class OrganizationQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        assertEquals("wrong parsing for constructor",
                "SELECT o from Organization o order by o.id", organizationQuery
                        .getQueryString());

    }

    public void testFilterByTitle() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationName("a");
        assertEquals(
                "SELECT o from Organization o WHERE lower(o.name) LIKE :name order by o.id",
                organizationQuery.getQueryString());
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "name"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get("name"),
                "%a%");

        organizationQuery.filterByNciInstituteCode("b");
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 2);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%b%");

    }


    public void testFilterByNCICode() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciInstituteCode("a");
        assertEquals(
                "SELECT o from Organization o WHERE lower(o.nciInstituteCode) LIKE :nciInstituteCode order by o.id",
                organizationQuery.getQueryString());
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 1);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%a%");


    }

    public void testFilterByOrganizationNameOrNciInstituteCode() throws Exception {
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.filterByOrganizationNameOrNciInstituteCode("a");
        assertEquals(
                "SELECT o from Organization o WHERE (lower(o.name) LIKE :name or lower(o.nciInstituteCode) LIKE :nciInstituteCode) order by o.id",
                organizationQuery.getQueryString());
        assertEquals("wrong number of parameters", organizationQuery.getParameterMap().size(), 2);
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "nciInstituteCode"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get(
                "nciInstituteCode"), "%a%");
        assertTrue("missing paramenter name", organizationQuery.getParameterMap().containsKey(
                "name"));
        assertEquals("wrong parameter value", organizationQuery.getParameterMap().get("name"),
                "%a%");


    }
}