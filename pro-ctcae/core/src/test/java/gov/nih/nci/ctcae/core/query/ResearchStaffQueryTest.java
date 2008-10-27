package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Mehul Gulati
 * Date: Oct 27, 2008
 */
public class ResearchStaffQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
        researchStaffQuery.filterByResearchStaffFirstName("john");
        assertEquals("SELECT r from ResearchStaff r WHERE lower(r.firstName) LIKE :firstName order by r.id",
                researchStaffQuery.getQueryString());
    }

    public void testFilterByFirstName() throws Exception {
        ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
        researchStaffQuery.filterByResearchStaffFirstName("john");
        assertEquals("SELECT r from ResearchStaff r WHERE lower(r.firstName) LIKE :firstName order by r.id",
                researchStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", researchStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", researchStaffQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", researchStaffQuery.getParameterMap().get("firstName"), "%john%");

    }
     public void testFilterByLastName() throws Exception {
        ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
        researchStaffQuery.filterByResearchStaffLastName("dow");
        assertEquals("SELECT r from ResearchStaff r WHERE lower(r.lastName) LIKE :lastName order by r.id",
                researchStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", researchStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", researchStaffQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", researchStaffQuery.getParameterMap().get("lastName"), "%dow%");

    }

     public void testFilterByFullName() throws Exception {
       ResearchStaffQuery researchStaffQuery = new ResearchStaffQuery();
       researchStaffQuery.filterByResearchStaffFirstName("John");
       researchStaffQuery.filterByResearchStaffLastName("Dow");
        assertEquals("SELECT r from ResearchStaff r WHERE lower(r.lastName) LIKE :lastName AND lower(r.firstName) LIKE :firstName order by r.id",
                researchStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", researchStaffQuery.getParameterMap().size(), 2);

}
}
