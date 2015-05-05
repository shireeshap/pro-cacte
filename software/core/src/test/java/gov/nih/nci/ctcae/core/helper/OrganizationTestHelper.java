package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationRepository;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class OrganizationTestHelper {
    private static OrganizationRepository organizationRepository;

    public static void initialize() {
        organizationRepository = TestDataManager.organizationRepository;
    }


    public static Organization getMSKCC() {
        return findOrganizationByNciInstituteCode("NY016");
    }

    public static Organization getDUKE() {
        return findOrganizationByNciInstituteCode("NC010");
    }

    public static Organization getCALGB() {
        return findOrganizationByNciInstituteCode("CALGB");
    }

    public static Organization getNCI() {
        return findOrganizationByNciInstituteCode("NCI");
    }

    public static Organization getWAKE() {
        return findOrganizationByNciInstituteCode("NC008");
    }

    private static Organization findOrganizationByNciInstituteCode(String code) {
        OrganizationQuery query = new OrganizationQuery();
        query.filterByNciCodeExactMatch(code);
        Organization o = organizationRepository.findSingle(query);
        return o;
    }


}