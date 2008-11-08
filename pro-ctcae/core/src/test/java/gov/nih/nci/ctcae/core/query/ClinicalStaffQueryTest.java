package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Mehul Gulati
 *         Date: Oct 16, 2008
 */
public class ClinicalStaffQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        assertEquals("SELECT i from ClinicalStaff i WHERE lower(i.firstName) LIKE :firstName order by i.id",
                clinicalStaffQuery.getQueryString());

    }

    public void testFilterByFirstName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        assertEquals("SELECT i from ClinicalStaff i WHERE lower(i.firstName) LIKE :firstName order by i.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("firstName"), "%john%");
    }

    public void testFilterByLastName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffLastName("Dow");
        assertEquals("SELECT i from ClinicalStaff i WHERE lower(i.lastName) LIKE :lastName order by i.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("lastName"), "%dow%");
    }

    public void testFilterByFullName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        clinicalStaffQuery.filterByClinicalStaffLastName("Dow");
        assertEquals("SELECT i from ClinicalStaff i WHERE lower(i.lastName) LIKE :lastName AND lower(i.firstName) LIKE :firstName order by i.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 2);

    }

}
