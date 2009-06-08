package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.repository.OrganizationRepository;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class OrganizationTestHelper {
    private OrganizationRepository organizationRepository;

    public OrganizationTestHelper(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }


    public Organization getMSKCC() {
        return findOrganizationByNciInstituteCode("NY016");
    }

    public Organization getDUKE() {
        return findOrganizationByNciInstituteCode("NC010");
    }

    public Organization getCALGB() {
        return findOrganizationByNciInstituteCode("CALGB");
    }

    public Organization getNCI() {
        return findOrganizationByNciInstituteCode("NCI");
    }

    public Organization getWAKE() {
        return findOrganizationByNciInstituteCode("NC008");
    }

    public Organization findOrganizationByNciInstituteCode(String code) {
        OrganizationQuery query = new OrganizationQuery();
        query.filterByNciCodeExactMatch(code);
        Organization o = organizationRepository.findSingle(query);
        return o;
    }


}