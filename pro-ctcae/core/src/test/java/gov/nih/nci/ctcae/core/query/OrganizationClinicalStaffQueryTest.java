package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.User;
import junit.framework.TestCase;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * @author Vinay Kumar
 */

public class OrganizationClinicalStaffQueryTest extends TestCase {


    public void testFilterByFirstNameOrLastNameOrNciIdentifier() {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByFirstNameOrLastNameOrNciIdentifier("test");
        assertEquals("SELECT scs from OrganizationClinicalStaff scs WHERE (lower(scs.clinicalStaff.firstName) LIKE :firstName or lower(scs.clinicalStaff.lastName) LIKE :lastName or lower(scs.clinicalStaff.nciIdentifier) LIKE :nciIdentifier) order by scs.clinicalStaff.firstName",query.getQueryString());
    }

}