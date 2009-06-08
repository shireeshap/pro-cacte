package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.*;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.repository.*;

import java.util.List;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class ClinicalStaffTestHelper {

    private OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    private ClinicalStaffRepository clinicalStaffRepository;
    private OrganizationRepository organizationRepository;

    public ClinicalStaffTestHelper(OrganizationClinicalStaffRepository organizationClinicalStaffRepository, ClinicalStaffRepository clinicalStaffRepository, OrganizationRepository organizationRepository) {
        this.organizationClinicalStaffRepository = organizationClinicalStaffRepository;
        this.clinicalStaffRepository = clinicalStaffRepository;
        this.organizationRepository = organizationRepository;

    }

    public void createDefaultClinicalStaff() {
        OrganizationTestHelper oth = new OrganizationTestHelper(organizationRepository);
        Organization duke = oth.getDUKE();
        Organization mskcc = oth.getMSKCC();

        createClinicalStaff("Meredith", "Olsen", "MOLSEN", duke);
        createClinicalStaff("Amy", "Abernethy", "AABERNETHY", duke);
        createClinicalStaff("Cathy", "Davis", "CDAVIS", duke);
        createClinicalStaff("Kerry", "Bueckers", "KBUECKERS", duke);
        createClinicalStaff("Angello", "Williams", "AWILLIAMS", duke);

        createClinicalStaff("Laura", "Sit", "LSIT", mskcc);
        createClinicalStaff("Ethan", "Basch", "EBASCH", mskcc);
        createClinicalStaff("Laura", "Archer", "LARCHER", mskcc);
        createClinicalStaff("Heather", "Todd", "HTODD", mskcc);
        createClinicalStaff("Josh", "Hennagir", "JHENNAGIR", mskcc);
        createClinicalStaff("Diane", "Opland", "DOPLAND", mskcc);

        createClinicalStaff("cca", "cca", "CCCA", duke);
    }

    public OrganizationClinicalStaff findOrganizationClinicalStaffByNciIdentifier(String nciIdentifier) {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByExactMatchNciIdentifier(nciIdentifier);
        OrganizationClinicalStaff o = organizationClinicalStaffRepository.findSingle(query);
        return o;
    }

    public ClinicalStaff createClinicalStaff(final String firstName, final String lastName, final String nciIdentifier, final Organization organization) {
        ClinicalStaffQuery query = new ClinicalStaffQuery();
        query.filterByNciIdentifier(nciIdentifier);
        List<ClinicalStaff> cs = (List<ClinicalStaff>) clinicalStaffRepository.find(query);
        if (cs == null || cs.size() == 0) {
            ClinicalStaff clinicalStaff = new ClinicalStaff();
            clinicalStaff.setFirstName(firstName);
            clinicalStaff.setLastName(lastName);
            clinicalStaff.setNciIdentifier(nciIdentifier);
            clinicalStaff.setEmailAddress(firstName + "." + lastName + "@demo.com");
            clinicalStaff.setPhoneNumber("234-432-2499");
            addUserToClinicalStaff(clinicalStaff);
            OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
            organizationClinicalStaff.setOrganization(organization);
            clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);
            clinicalStaffRepository.save(clinicalStaff);
            return clinicalStaff;
        }
        return cs.get(0);
    }

    private void addUserToClinicalStaff(ClinicalStaff clinicalStaff) {
        clinicalStaff.setUser(new User());
        clinicalStaff.getUser().setPassword(TestDataManager.DEFAULT_PASSWORD);
    }
}