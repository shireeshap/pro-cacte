package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class StudyTestHelper extends GenerateTestDataTest {

    OrganizationTestHelper oth;
    ClinicalStaffTestHelper csth;
    StudySite sSiteDuke = new StudySite();
    StudySite sSiteMskcc = new StudySite();
    private StudyRepository studyRepository;

    public StudyTestHelper(StudyRepository studyRepository, OrganizationClinicalStaffRepository organizationClinicalStaffRepository, ClinicalStaffRepository clinicalStaffRepository, OrganizationRepository organizationRepository) {
        this.studyRepository = studyRepository;
        oth = new OrganizationTestHelper(organizationRepository);
        csth = new ClinicalStaffTestHelper(organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
    }

    public void createStandardStudy() {
        Study study = new Study();
        firstTab_GeneralInfo(study);
        secondTab_StudySites(study);
        thirdTab_OverallStudyStaff(study);
        fourthTab_SiteClinicalStaff();
        addCCA();

        studyRepository.save(study);
    }

    private void firstTab_GeneralInfo(Study study) {
        study.setShortTitle("Collection of patient-reported symptoms and performance status via the internet");
        study.setLongTitle("Collection of patient-reported symptoms and performance status via the internet");
        study.setAssignedIdentifier("123-12");
        study.setDescription("Collection of patient-reported symptoms and performance status via the internet");

        DataCoordinatingCenter dcc = new DataCoordinatingCenter();
        dcc.setOrganization(oth.getDUKE());
        study.setDataCoordinatingCenter(dcc);

        StudySponsor ss = new StudySponsor();
        ss.setOrganization(oth.getCALGB());
        study.setStudySponsor(ss);

        FundingSponsor fs = new FundingSponsor();
        fs.setOrganization(oth.getNCI());
        study.setFundingSponsor(fs);

        LeadStudySite ls = new LeadStudySite();
        ls.setOrganization(oth.getMSKCC());
        study.setLeadStudySite(ls);

    }

    private void secondTab_StudySites(Study study) {
        sSiteDuke.setOrganization(oth.getDUKE());
        study.addStudySite(sSiteDuke);

        sSiteMskcc.setOrganization(oth.getMSKCC());
        study.addStudySite(sSiteMskcc);

    }

    private void thirdTab_OverallStudyStaff(Study study) {
        StudyOrganizationClinicalStaff odc = new StudyOrganizationClinicalStaff();
        odc.setRole(Role.ODC);
        odc.setOrganizationClinicalStaff(csth.findOrganizationClinicalStaffByNciIdentifier("MOLSEN"));
        study.getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(odc);

        StudyOrganizationClinicalStaff lCra = new StudyOrganizationClinicalStaff();
        lCra.setRole(Role.LEAD_CRA);
        lCra.setOrganizationClinicalStaff(csth.findOrganizationClinicalStaffByNciIdentifier("LSIT"));
        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(lCra);

        StudyOrganizationClinicalStaff pi = new StudyOrganizationClinicalStaff();
        pi.setRole(Role.PI);
        pi.setOrganizationClinicalStaff(csth.findOrganizationClinicalStaffByNciIdentifier("EBASCH"));
        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(pi);

    }

    private void fourthTab_SiteClinicalStaff() {
        StudyOrganizationClinicalStaff sitePi = new StudyOrganizationClinicalStaff();
        sitePi.setRole(Role.SITE_PI);
        OrganizationClinicalStaff ocs1 = csth.findOrganizationClinicalStaffByNciIdentifier("AABERNETHY");
        sitePi.setOrganizationClinicalStaff(ocs1);
        sitePi.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(sitePi);

        StudyOrganizationClinicalStaff siteCra = new StudyOrganizationClinicalStaff();
        siteCra.setRole(Role.SITE_CRA);
        OrganizationClinicalStaff ocs2 = csth.findOrganizationClinicalStaffByNciIdentifier("CDAVIS");
        siteCra.setOrganizationClinicalStaff(ocs2);
        siteCra.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(siteCra);

        StudyOrganizationClinicalStaff physician = new StudyOrganizationClinicalStaff();
        physician.setRole(Role.TREATING_PHYSICIAN);
        OrganizationClinicalStaff ocs3 = csth.findOrganizationClinicalStaffByNciIdentifier("LARCHER");
        physician.setOrganizationClinicalStaff(ocs3);
        physician.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(physician);

        StudyOrganizationClinicalStaff nurse = new StudyOrganizationClinicalStaff();
        nurse.setRole(Role.NURSE);
        OrganizationClinicalStaff ocs4 = csth.findOrganizationClinicalStaffByNciIdentifier("HTODD");
        nurse.setOrganizationClinicalStaff(ocs4);
        nurse.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(nurse);
    }

    private void addCCA() {
        StudyOrganizationClinicalStaff cca = new StudyOrganizationClinicalStaff();
        cca.setRole(Role.CCA);
        OrganizationClinicalStaff ocs5 = csth.findOrganizationClinicalStaffByNciIdentifier("CCCA");
        cca.setOrganizationClinicalStaff(ocs5);
        cca.setStudyOrganization(sSiteMskcc);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(cca);

    }
}
