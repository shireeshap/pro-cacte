package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.*;
import gov.nih.nci.ctcae.core.query.StudyQuery;

import java.util.List;

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
//    private String studyId = "123-12-DEFAULT";
    private String studyId = "123-12";

    public StudyTestHelper(StudyRepository studyRepository, OrganizationClinicalStaffRepository organizationClinicalStaffRepository, ClinicalStaffRepository clinicalStaffRepository, OrganizationRepository organizationRepository) {
        this.studyRepository = studyRepository;
        oth = new OrganizationTestHelper(organizationRepository);
        csth = new ClinicalStaffTestHelper(organizationClinicalStaffRepository, clinicalStaffRepository, organizationRepository);
        deleteStandardStudies();
    }

    private void deleteStandardStudies() {

        StudyQuery query = new StudyQuery();
        query.filterByAssignedIdentifierExactMatch(studyId);
        List<Study> studies = (List<Study>) studyRepository.find(query);
        for (Study study : studies) {
            studyRepository.delete(study);
        }
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
        study.setAssignedIdentifier(studyId);
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
        sSiteMskcc = ls;

    }

    private void secondTab_StudySites(Study study) {
        sSiteDuke.setOrganization(oth.getDUKE());
        study.addStudySite(sSiteDuke);
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

        //DUKE
        StudyOrganizationClinicalStaff sitePiDuke = new StudyOrganizationClinicalStaff();
        sitePiDuke.setRole(Role.SITE_PI);
        OrganizationClinicalStaff ocs1 = csth.findOrganizationClinicalStaffByNciIdentifier("AABERNETHY");
        sitePiDuke.setOrganizationClinicalStaff(ocs1);
        sitePiDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(sitePiDuke);

        StudyOrganizationClinicalStaff siteCraDuke = new StudyOrganizationClinicalStaff();
        siteCraDuke.setRole(Role.SITE_CRA);
        OrganizationClinicalStaff ocs2 = csth.findOrganizationClinicalStaffByNciIdentifier("CDAVIS");
        siteCraDuke.setOrganizationClinicalStaff(ocs2);
        siteCraDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(siteCraDuke);

        StudyOrganizationClinicalStaff physicianDuke = new StudyOrganizationClinicalStaff();
        physicianDuke.setRole(Role.TREATING_PHYSICIAN);
        OrganizationClinicalStaff ocs3 = csth.findOrganizationClinicalStaffByNciIdentifier("KBUECKERS");
        physicianDuke.setOrganizationClinicalStaff(ocs3);
        physicianDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(physicianDuke);

        StudyOrganizationClinicalStaff nurseDuke = new StudyOrganizationClinicalStaff();
        nurseDuke.setRole(Role.NURSE);
        OrganizationClinicalStaff ocs4 = csth.findOrganizationClinicalStaffByNciIdentifier("AWILLIAMS");
        nurseDuke.setOrganizationClinicalStaff(ocs4);
        nurseDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(nurseDuke);

        //MSKCC
        StudyOrganizationClinicalStaff sitePiMskcc = new StudyOrganizationClinicalStaff();
        sitePiMskcc.setRole(Role.SITE_PI);
        OrganizationClinicalStaff ocs5 = csth.findOrganizationClinicalStaffByNciIdentifier("LARCHER");
        sitePiMskcc.setOrganizationClinicalStaff(ocs5);
        sitePiMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(sitePiMskcc);

        StudyOrganizationClinicalStaff siteCraMskcc = new StudyOrganizationClinicalStaff();
        siteCraMskcc.setRole(Role.SITE_CRA);
        OrganizationClinicalStaff ocs6 = csth.findOrganizationClinicalStaffByNciIdentifier("HTODD");
        siteCraMskcc.setOrganizationClinicalStaff(ocs6);
        siteCraMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(siteCraMskcc);

        StudyOrganizationClinicalStaff physicianMskcc = new StudyOrganizationClinicalStaff();
        physicianMskcc.setRole(Role.TREATING_PHYSICIAN);
        OrganizationClinicalStaff ocs7 = csth.findOrganizationClinicalStaffByNciIdentifier("JHENNAGIR");
        physicianMskcc.setOrganizationClinicalStaff(ocs7);
        physicianMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(physicianMskcc);

        StudyOrganizationClinicalStaff nurseMskcc = new StudyOrganizationClinicalStaff();
        nurseMskcc.setRole(Role.NURSE);
        OrganizationClinicalStaff ocs8 = csth.findOrganizationClinicalStaffByNciIdentifier("DOPLAND");
        nurseMskcc.setOrganizationClinicalStaff(ocs8);
        nurseMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(nurseMskcc);
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
