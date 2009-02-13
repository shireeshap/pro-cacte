package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Organization;
import junit.framework.TestCase;

/**
 * @author Mehul Gulati
 *         Date: Oct 16, 2008
 */
public class ClinicalStaffQueryTest extends TestCase {

    public void testQueryConstructor() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        assertEquals("SELECT cs from ClinicalStaff cs WHERE lower(cs.firstName) LIKE :firstName order by cs.id",
                clinicalStaffQuery.getQueryString());

    }

    public void testFilterByFirstName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        assertEquals("SELECT cs from ClinicalStaff cs WHERE lower(cs.firstName) LIKE :firstName order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("firstName"), "%john%");
    }

    public void testFilterByOrganizationId() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganizationAndRole(1);
        assertEquals("SELECT cs from ClinicalStaff cs left join cs.clinicalStaffAssignments as csa WHERE csa.domainObjectClass = :domainObjectClass AND csa.domainObjectId = :organizationId order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 2);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("organizationId"));
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("domainObjectClass"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("organizationId"), 1);
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("domainObjectClass"), Organization.class.getName());
    }

    public void testFilterByOrganizationIdAndSearchText() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganizationAndRole(1);
        clinicalStaffQuery.filterByFirstNameOrLastNameOrNciIdentifier("AbC");
        assertEquals("SELECT cs from ClinicalStaff cs left join cs.clinicalStaffAssignments as csa WHERE csa.domainObjectClass = :domainObjectClass " +
                "AND csa.domainObjectId = :organizationId AND (lower(cs.firstName) LIKE :firstName or lower(cs.lastName) LIKE :lastName or lower(cs.nciIdentifier) LIKE :nciIdentifier) order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 5);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("organizationId"));
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("domainObjectClass"));
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("nciIdentifier"));
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("lastName"));
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("firstName"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("organizationId"), 1);
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("domainObjectClass"), Organization.class.getName());
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("firstName"), "%abc%");
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("lastName"), "%abc%");
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("nciIdentifier"), "%abc%");
    }

    public void testFilterByNciIdentifier() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByNciIdentifier("John");
        assertEquals("SELECT cs from ClinicalStaff cs WHERE lower(cs.nciIdentifier) LIKE :nciIdentifier order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("nciIdentifier"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("nciIdentifier"), "%john%");
    }

    public void testFilterByLastName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffLastName("Dow");
        assertEquals("SELECT cs from ClinicalStaff cs WHERE lower(cs.lastName) LIKE :lastName order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 1);
        assertTrue("missing parameter name", clinicalStaffQuery.getParameterMap().containsKey("lastName"));
        assertEquals("wrong parameter value", clinicalStaffQuery.getParameterMap().get("lastName"), "%dow%");
    }

    public void testFilterByFullName() throws Exception {
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("John");
        clinicalStaffQuery.filterByClinicalStaffLastName("Dow");
        assertEquals("SELECT cs from ClinicalStaff cs WHERE lower(cs.lastName) LIKE :lastName AND lower(cs.firstName) LIKE :firstName order by cs.id",
                clinicalStaffQuery.getQueryString());
        assertEquals("wrong number of parameters", clinicalStaffQuery.getParameterMap().size(), 2);

    }

}
