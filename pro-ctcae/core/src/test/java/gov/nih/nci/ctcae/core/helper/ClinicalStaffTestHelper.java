package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.*;
import gov.nih.nci.ctcae.core.helper.GenerateTestDataTest;
import gov.nih.nci.ctcae.core.repository.*;
import sun.security.krb5.internal.ktab.KeyTab;
import org.omg.CORBA.CharSeqHelper;

import java.util.List;
import java.util.Collection;

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
        createClinicalStaff("Laura", "Sit", "LSIT", mskcc);
        createClinicalStaff("Ethan", "Basch", "EBASCH", mskcc);
        createClinicalStaff("Amy", "Abernethy", "AABERNETHY", duke);
        createClinicalStaff("Cathy", "Davis", "CDAVIS", duke);
        createClinicalStaff("Laura", "Archer", "LARCHER", mskcc);
        createClinicalStaff("Heather", "Todd", "HTODD", mskcc);
        createClinicalStaff("cca", "cca", "CCCA", duke);
    }

    public OrganizationClinicalStaff findOrganizationClinicalStaffByNciIdentifier(String nciIdentifier) {
        OrganizationClinicalStaffQuery query = new OrganizationClinicalStaffQuery();
        query.filterByExactMatchNciIdentifier(nciIdentifier);
        OrganizationClinicalStaff o = organizationClinicalStaffRepository.findSingle(query);
        return o;
    }

    public ClinicalStaff createClinicalStaff(final String firstName, final String lastName, final String nciIdentifier, final Organization organization) {
        ClinicalStaff clinicalStaff = new ClinicalStaff();
        clinicalStaff.setFirstName(firstName);
        clinicalStaff.setLastName(lastName);
        clinicalStaff.setNciIdentifier(nciIdentifier);
        clinicalStaff.setEmailAddress(firstName + "." + lastName);
        clinicalStaff.setPhoneNumber("234-432-2499");
        addUserToClinicalStaff(clinicalStaff);
        OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
        organizationClinicalStaff.setOrganization(organization);
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);
        clinicalStaffRepository.save(clinicalStaff);
        return clinicalStaff;
    }

    private void addUserToClinicalStaff(ClinicalStaff clinicalStaff) {
        clinicalStaff.setUser(new User());
        clinicalStaff.getUser().setPassword(GenerateTestDataTest.DEFAULT_PASSWORD);
    }
}