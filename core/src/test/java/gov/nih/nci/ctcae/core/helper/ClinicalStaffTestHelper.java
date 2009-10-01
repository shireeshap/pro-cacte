package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.OrganizationClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.repository.secured.OrganizationClinicalStaffRepository;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class ClinicalStaffTestHelper {

    private static OrganizationClinicalStaffRepository organizationClinicalStaffRepository;
    private static ClinicalStaffRepository clinicalStaffRepository;

    private ClinicalStaffTestHelper() {

    }

    public static void initialize() {
        organizationClinicalStaffRepository = TestDataManager.organizationClinicalStaffRepository;
        clinicalStaffRepository = TestDataManager.clinicalStaffRepository;
    }

    public static void createDefaultClinicalStaff() {
        Organization duke = OrganizationTestHelper.getDUKE();
        Organization mskcc = OrganizationTestHelper.getMSKCC();

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

    public static OrganizationClinicalStaff findOrganizationClinicalStaffByNciIdentifier(String nciIdentifier) {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByExactMatchNciIdentifier(nciIdentifier);
        return organizationClinicalStaffRepository.findSingle(query);
    }

    private static ClinicalStaff findClinicalStaffByNCIIdentifier(String nciIdentifier) {
        ClinicalStaffQuery query = new ClinicalStaffQuery();
        query.filterByNciIdentifier(nciIdentifier);
        return clinicalStaffRepository.findSingle(query);
    }

    public static ClinicalStaff createClinicalStaff(final String firstName, final String lastName, final String nciIdentifier, final Organization organization) {
        ClinicalStaff clinicalStaff = findClinicalStaffByNCIIdentifier(nciIdentifier);
        if (clinicalStaff == null) {
            clinicalStaff = new ClinicalStaff();
            clinicalStaff.setFirstName(firstName);
            clinicalStaff.setLastName(lastName);
            clinicalStaff.setNciIdentifier(nciIdentifier);
            clinicalStaff.setEmailAddress(firstName + "." + lastName + "@demo.com");
            clinicalStaff.setPhoneNumber("234-432-2499");
            addUserToClinicalStaff(clinicalStaff);
            OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
            organizationClinicalStaff.setOrganization(organization);
            clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);
            clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
        }
        return clinicalStaff;
    }

    private static void addUserToClinicalStaff(ClinicalStaff clinicalStaff) {
        clinicalStaff.setUser(new User());
        clinicalStaff.getUser().setPassword(TestDataManager.DEFAULT_PASSWORD);
    }

    public static ClinicalStaff getDefaultClinicalStaff() {
        return findClinicalStaffByNCIIdentifier("MOLSEN");
    }
}