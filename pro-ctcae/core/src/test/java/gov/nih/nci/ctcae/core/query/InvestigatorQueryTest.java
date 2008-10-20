package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: tsneed
 * Date: Oct 16, 2008
 * Time: 4:58:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvestigatorQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        InvestigatorQuery investigatorQuery = new InvestigatorQuery();
        investigatorQuery.filterByInvestigatorFirstName("John");
        assertEquals("SELECT i from Investigator i WHERE lower(i.firstName) LIKE :firstName order by i.id",
                investigatorQuery.getQueryString());

    }

     public void testFilterByFirstName() throws Exception {
        InvestigatorQuery investigatorQuery = new InvestigatorQuery();
        investigatorQuery.filterByInvestigatorFirstName("John");
        assertEquals("SELECT i from Investigator i WHERE lower(i.firstName) LIKE :firstName order by i.id",
                investigatorQuery.getQueryString());
        assertEquals("wrong number of parameters", investigatorQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", investigatorQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", investigatorQuery.getParameterMap().get("firstName"), "%john%");
    }

     public void testFilterByLastName() throws Exception {
        InvestigatorQuery investigatorQuery = new InvestigatorQuery();
        investigatorQuery.filterByInvestigatorLastName("Dow");
        assertEquals("SELECT i from Investigator i WHERE lower(i.lastName) LIKE :lastName order by i.id",
                investigatorQuery.getQueryString());
        assertEquals("wrong number of parameters", investigatorQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", investigatorQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", investigatorQuery.getParameterMap().get("lastName"), "%dow%");
    }
    public void testFilterByFullName() throws Exception {
       InvestigatorQuery investigatorQuery = new InvestigatorQuery();
       investigatorQuery.filterByInvestigatorFirstName("John");
       investigatorQuery.filterByInvestigatorLastName("Dow");
        assertEquals("SELECT i from Investigator i WHERE lower(i.lastName) LIKE :lastName AND lower(i.firstName) LIKE :firstName order by i.id",
                investigatorQuery.getQueryString());
        assertEquals("wrong number of parameters", investigatorQuery.getParameterMap().size(), 2);

}

}
