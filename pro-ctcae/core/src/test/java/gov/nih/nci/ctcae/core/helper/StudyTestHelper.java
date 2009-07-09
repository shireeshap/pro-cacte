package gov.nih.nci.ctcae.core.helper;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.List;
import java.util.Collection;

/**
 * User: Harsh
 * Date: Jun 5, 2009
 * Time: 8:40:02 AM
 */
public class StudyTestHelper {

    private static StudySite sSiteDuke;
    private static StudySite sSiteMskcc;
    private static StudyRepository studyRepository;
    public static final String STANDARD_STUDY_ASSIGNED_ID = "1-11-DEFAULT";
    public static final String SECONDARY_STUDY_ASSIGNED_ID = "2-22-SECONDARY";
    private static Study study;

    private StudyTestHelper() {
    }

    public static void initialize() {
        studyRepository = TestDataManager.studyRepository;
    }

    public static void createDefaultStudy() {
        deleteStudies();
        study = new Study();
        firstTab_GeneralInfo(study, "Collection of patient-reported symptoms and performance status via the internet", STANDARD_STUDY_ASSIGNED_ID);
        secondTab_StudySites(study);
        thirdTab_OverallStudyStaff(study);
        fourthTab_SiteClinicalStaff();
        addCCA();

        study = studyRepository.save(study);
    }

    private static void firstTab_GeneralInfo(Study study, String title, String assignedId) {
        study.setShortTitle(title);
        study.setLongTitle(title);
        study.setAssignedIdentifier(assignedId);
        study.setDescription(title);

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
        addOrUpdateStudyOrganizationClinicalStaff("MOLSEN", Role.ODC, study.getDataCoordinatingCenter());
        addOrUpdateStudyOrganizationClinicalStaff("LSIT", Role.LEAD_CRA, study.getLeadStudySite());
        addOrUpdateStudyOrganizationClinicalStaff("EBASCH", Role.PI, study.getLeadStudySite());
    }

    private static void addOrUpdateStudyOrganizationClinicalStaff(String nciId, Role role, StudyOrganization so) {
        StudyOrganizationClinicalStaff socs = new StudyOrganizationClinicalStaff();
        socs.setRole(role);
        socs.setOrganizationClinicalStaff(ClinicalStaffTestHelper.findOrganizationClinicalStaffByNciIdentifier(nciId));
        so.addOrUpdateStudyOrganizationClinicalStaff(socs);

    }

    private static void fourthTab_SiteClinicalStaff() {
        //DUKE
        addOrUpdateStudyOrganizationClinicalStaff("AABERNETHY", Role.SITE_PI, sSiteDuke);
        addOrUpdateStudyOrganizationClinicalStaff("CDAVIS", Role.SITE_CRA, sSiteDuke);
        addOrUpdateStudyOrganizationClinicalStaff("KBUECKERS", Role.TREATING_PHYSICIAN, sSiteDuke);
        addOrUpdateStudyOrganizationClinicalStaff("KBUECKERS", Role.NURSE, sSiteDuke);

        //MSKCC
        addOrUpdateStudyOrganizationClinicalStaff("LARCHER", Role.SITE_PI, sSiteMskcc);
        addOrUpdateStudyOrganizationClinicalStaff("HTODD", Role.SITE_CRA, sSiteMskcc);
        addOrUpdateStudyOrganizationClinicalStaff("JHENNAGIR", Role.TREATING_PHYSICIAN, sSiteMskcc);
        addOrUpdateStudyOrganizationClinicalStaff("JHENNAGIR", Role.NURSE, sSiteMskcc);
    }

    private static void addCCA() {
        addOrUpdateStudyOrganizationClinicalStaff("CCCA", Role.CCA, sSiteDuke);
    }

    private static void deleteStudies() {
        StudyQuery query = new StudyQuery();
        query.filterByAssignedIdentifierExactMatch(STANDARD_STUDY_ASSIGNED_ID);
        Collection<Study> studies = studyRepository.find(query);
        for (Study study : studies) {
            studyRepository.delete(study);
        }
    }

    private static List<Study> findDefaultStudy() {
        return findStudyByAssignedId(STANDARD_STUDY_ASSIGNED_ID);
    }

    public static Study getSecondaryStudy() {
        List<Study> studies = findSecondaryStudy();
        if (studies == null || studies.size() == 0) {
            createDefaultStudy();
        } else {
            study = studies.get(0);
        }
        return study;
    }

    private static List<Study> findSecondaryStudy() {

        return findStudyByAssignedId(SECONDARY_STUDY_ASSIGNED_ID);
    }

    private static List<Study> findStudyByAssignedId(String aId) {
        StudyQuery query = new StudyQuery();
        query.filterByAssignedIdentifierExactMatch(aId);
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

    public static ClinicalStaff getLeadSite_SitePI() {
        study = getDefaultStudy();
        for (StudyOrganizationClinicalStaff socs : study.getLeadStudySite().getStudyOrganizationClinicalStaffs()) {
            if (socs.getRole().equals(Role.SITE_PI)) {
                return socs.getOrganizationClinicalStaff().getClinicalStaff();
            }
        }
        return null;
    }

    public static ClinicalStaff getLeadSiteStaffByRole(Role role) {
        study = getDefaultStudy();
        for (StudyOrganizationClinicalStaff socs : study.getLeadStudySite().getStudyOrganizationClinicalStaffs()) {
            if (socs.getRole().equals(role)) {
                return socs.getOrganizationClinicalStaff().getClinicalStaff();
            }
        }
        return null;
    }

    public static ClinicalStaff getNonLeadSiteStaffByRole(Role role) {
        study = getDefaultStudy();
        StudySite ss = null;
        for (StudySite temp : study.getStudySites()) {
            if (temp != study.getLeadStudySite()) {
                ss = temp;
            }
        }
        if (ss != null) {
            for (StudyOrganizationClinicalStaff socs : ss.getStudyOrganizationClinicalStaffs()) {
                if (socs.getRole().equals(role)) {
                    return socs.getOrganizationClinicalStaff().getClinicalStaff();
                }
            }
        }
        return null;
    }

    public static void createSecondaryStudy() {
        Study study2 = new Study();
        firstTab_GeneralInfo(study2, "A Phase 2 Study of Suberoylanilide Hydroxamic Acid (SAHA) in Acute Myeloid Leukemia (AML)", SECONDARY_STUDY_ASSIGNED_ID);
        secondTab_StudySites(study2);
        thirdTab_OverallStudyStaff(study2);
        fourthTab_SiteClinicalStaff();
        addOrUpdateStudyOrganizationClinicalStaff("DOPLAND", Role.PI, study2.getLeadStudySite());
        studyRepository.save(study2);
    }
}
