package gov.nih.nci.ctcae.core.query;

import junit.framework.TestCase;

/**
 * @author Vinay Kumar
 */

public class OrganizationClinicalStaffQueryTest extends TestCase {


    public void testFilterByFirstNameOrLastNameOrNciIdentifier() {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier("test");
        assertEquals("SELECT scs from OrganizationClinicalStaff scs WHERE (lower(scs.clinicalStaff.firstName) LIKE :firstName or lower(scs.clinicalStaff.lastName) LIKE :lastName or lower(scs.clinicalStaff.nciIdentifier) LIKE :nciIdentifier or lower(scs.clinicalStaff.firstName) || ' ' || lower(scs.clinicalStaff.lastName) LIKE :firstLastName) order by scs.clinicalStaff.firstName",query.getQueryString());
    }

}