package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.StudyRepository;

import java.util.List;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class StudyTestHelper {

    private static StudySite sSiteDuke;
    private static StudySite sSiteMskcc;
    private static StudyRepository studyRepository;
    public static final String STANDARD_STUDY_ASSIGNED_ID = "123-12-DEFAULT";
    private static Study study;

    private StudyTestHelper() {
    }

    public static void initialize() {
        studyRepository = TestDataManager.studyRepository;
    }

    public static void createDefaultStudy() {
        deleteDefaultStudy();
        study = new Study();
        firstTab_GeneralInfo(study);
        secondTab_StudySites(study);
        thirdTab_OverallStudyStaff(study);
        fourthTab_SiteClinicalStaff();
        addCCA();

        study = studyRepository.save(study);
    }

    private static void firstTab_GeneralInfo(Study study) {
        study.setShortTitle("Collection of patient-reported symptoms and performance status via the internet");
        study.setLongTitle("Collection of patient-reported symptoms and performance status via the internet");
        study.setAssignedIdentifier(STANDARD_STUDY_ASSIGNED_ID);
        study.setDescription("Collection of patient-reported symptoms and performance status via the internet");

        DataCoordinatingCenter dcc = new DataCoordinatingCenter();
        dcc.setOrganization(OrganizationTestHelper.getDUKE());
        study.setDataCoordinatingCenter(dcc);

        StudySponsor ss = new StudySponsor();
        ss.setOrganization(OrganizationTestHelper.getCALGB());
        study.setStudySponsor(ss);

        FundingSponsor fs = new FundingSponsor();
        fs.setOrganization(OrganizationTestHelper.getNCI());
        study.setFundingSponsor(fs);

        LeadStudySite ls = new LeadStudySite();
        ls.setOrganization(OrganizationTestHelper.getMSKCC());
        study.setLeadStudySite(ls);
        sSiteMskcc = ls;

    }

    private static void secondTab_StudySites(Study study) {
        sSiteDuke = new StudySite();
        sSiteDuke.setOrganization(OrganizationTestHelper.getDUKE());
        study.addStudySite(sSiteDuke);
    }

    private static void thirdTab_OverallStudyStaff(Study study) {
        StudyOrganizationClinicalStaff odc = new StudyOrganizationClinicalStaff();
        odc.setRole(Role.ODC);
        odc.setOrganizationClinicalStaff(ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("MOLSEN"));
        study.getDataCoordinatingCenter().addOrUpdateStudyOrganizationClinicalStaff(odc);

        StudyOrganizationClinicalStaff lCra = new StudyOrganizationClinicalStaff();
        lCra.setRole(Role.LEAD_CRA);
        lCra.setOrganizationClinicalStaff(ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("LSIT"));
        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(lCra);

        StudyOrganizationClinicalStaff pi = new StudyOrganizationClinicalStaff();
        pi.setRole(Role.PI);
        pi.setOrganizationClinicalStaff(ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("EBASCH"));
        study.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(pi);

    }

    private static void fourthTab_SiteClinicalStaff() {

        //DUKE
        StudyOrganizationClinicalStaff sitePiDuke = new StudyOrganizationClinicalStaff();
        sitePiDuke.setRole(Role.SITE_PI);
        OrganizationClinicalStaff ocs1 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("AABERNETHY");
        sitePiDuke.setOrganizationClinicalStaff(ocs1);
        sitePiDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(sitePiDuke);

        StudyOrganizationClinicalStaff siteCraDuke = new StudyOrganizationClinicalStaff();
        siteCraDuke.setRole(Role.SITE_CRA);
        OrganizationClinicalStaff ocs2 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("CDAVIS");
        siteCraDuke.setOrganizationClinicalStaff(ocs2);
        siteCraDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(siteCraDuke);

        StudyOrganizationClinicalStaff physicianDuke = new StudyOrganizationClinicalStaff();
        physicianDuke.setRole(Role.TREATING_PHYSICIAN);
        OrganizationClinicalStaff ocs3 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("KBUECKERS");
        physicianDuke.setOrganizationClinicalStaff(ocs3);
        physicianDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(physicianDuke);

        StudyOrganizationClinicalStaff nurseDuke = new StudyOrganizationClinicalStaff();
        nurseDuke.setRole(Role.NURSE);
        OrganizationClinicalStaff ocs4 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("AWILLIAMS");
        nurseDuke.setOrganizationClinicalStaff(ocs4);
        nurseDuke.setStudyOrganization(sSiteDuke);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(nurseDuke);

        //MSKCC
        StudyOrganizationClinicalStaff sitePiMskcc = new StudyOrganizationClinicalStaff();
        sitePiMskcc.setRole(Role.SITE_PI);
        OrganizationClinicalStaff ocs5 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("LARCHER");
        sitePiMskcc.setOrganizationClinicalStaff(ocs5);
        sitePiMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(sitePiMskcc);

        StudyOrganizationClinicalStaff siteCraMskcc = new StudyOrganizationClinicalStaff();
        siteCraMskcc.setRole(Role.SITE_CRA);
        OrganizationClinicalStaff ocs6 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("HTODD");
        siteCraMskcc.setOrganizationClinicalStaff(ocs6);
        siteCraMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(siteCraMskcc);

        StudyOrganizationClinicalStaff physicianMskcc = new StudyOrganizationClinicalStaff();
        physicianMskcc.setRole(Role.TREATING_PHYSICIAN);
        OrganizationClinicalStaff ocs7 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("JHENNAGIR");
        physicianMskcc.setOrganizationClinicalStaff(ocs7);
        physicianMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(physicianMskcc);

        StudyOrganizationClinicalStaff nurseMskcc = new StudyOrganizationClinicalStaff();
        nurseMskcc.setRole(Role.NURSE);
        OrganizationClinicalStaff ocs8 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("DOPLAND");
        nurseMskcc.setOrganizationClinicalStaff(ocs8);
        nurseMskcc.setStudyOrganization(sSiteMskcc);
        sSiteMskcc.addOrUpdateStudyOrganizationClinicalStaff(nurseMskcc);
    }

    private static void addCCA() {
        StudyOrganizationClinicalStaff cca = new StudyOrganizationClinicalStaff();
        cca.setRole(Role.CCA);
        OrganizationClinicalStaff ocs5 = ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier("CCCA");
        cca.setOrganizationClinicalStaff(ocs5);
        cca.setStudyOrganization(sSiteMskcc);
        sSiteDuke.addOrUpdateStudyOrganizationClinicalStaff(cca);

    }

    private static void deleteDefaultStudy() {
        List<Study> studies = findDefaultStudy();
        for (Study study : studies) {
            studyRepository.delete(study);
        }
    }

    private static List<Study> findDefaultStudy() {
        StudyQuery query = new StudyQuery();
        query.filterByAssignedIdentifierExactMatch(STANDARD_STUDY_ASSIGNED_ID);
        return (List<Study>) studyRepository.find(query);
    }

    public static Study getDefaultStudy() {
        List<Study> studies = findDefaultStudy();
        if (studies == null || studies.size() == 0) {
            createDefaultStudy();
        } else {
            study = studies.get(0);
        }
        return study;
    }

    public static ClinicalStaff getLeadSite_SitePI(){
        study = getDefaultStudy();
        for(StudyOrganizationClinicalStaff socs : study.getLeadStudySite().getStudyOrganizationClinicalStaffs()){
            if(socs.getRole().equals(Role.SITE_PI)){
                return socs.getOrganizationClinicalStaff().getClinicalStaff();
            }
        }
        return null;
    }
}
