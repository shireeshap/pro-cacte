package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.helper.OrganizationTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Jun 17, 2009
 * Time: 3:25:35 PM
 */
public class SiteCRASecurityTest extends TestDataManager {

    public void testInstance_StudySitesForSiteCRANotOnLeadSite() {
        ClinicalStaff clinicalStaff = StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA);
        login(clinicalStaff.getUser().getUsername());

        List<StudyOrganization> organizations = studyOrganizationRepository.findByStudyId("%", StudyTestHelper.getDefaultStudy().getId());
        assertEquals(2, organizations.size());
        assertEquals(OrganizationTestHelper.getDUKE(), organizations.get(0).getOrganization());

    }

    public void testInstance_StudySitesForSiteCRAOnLeadSite() {
        ClinicalStaff clinicalStaff = StudyTestHelper.getLeadSiteStaffByRole(Role.SITE_CRA);
        login(clinicalStaff.getUser().getUsername());

        List<StudyOrganization> organizations = studyOrganizationRepository.findByStudyId("%", StudyTestHelper.getDefaultStudy().getId());
        assertEquals(clinicalStaff.getOrganizationClinicalStaffs().size(), organizations.size());
        assertEquals(OrganizationTestHelper.getMSKCC(), organizations.get(1).getOrganization());

    }

    public void testInstance_StudySitesForLeadCRA() {
        ClinicalStaff clinicalStaff = StudyTestHelper.getLeadSiteStaffByRole(Role.LEAD_CRA);
        login(clinicalStaff.getUser().getUsername());

        List<StudyOrganization> organizations = studyOrganizationRepository.findByStudyId("%", StudyTestHelper.getDefaultStudy().getId());
        assertEquals(2, organizations.size());

    }
    public void testInstance_StudySitesForPI() {
        ClinicalStaff clinicalStaff = StudyTestHelper.getLeadSiteStaffByRole(Role.PI);
        login(clinicalStaff.getUser().getUsername());

        List<StudyOrganization> organizations = studyOrganizationRepository.findByStudyId("%", StudyTestHelper.getDefaultStudy().getId());
        assertEquals(2, organizations.size());

    }

}
