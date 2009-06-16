package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.ClinicalStaffTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudyOrganizationClinicalStaffIntegrationTest extends TestDataManager {

    public void testFind() {
        List<Role> rolesList = new ArrayList<Role>();
        rolesList.add(Role.PI);
        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = clinicalStaffRepository.findByStudyOrganizationIdAndRole("%",
                StudyTestHelper.getDefaultStudy().getLeadStudySite().getId(), rolesList);

        assertFalse(organizationClinicalStaffList.isEmpty());
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaffList) {
            assertEquals(Role.PI, studyOrganizationClinicalStaff.getRole());
        }
    }

    public void testFindByClinicalStaff() {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();
        ClinicalStaff defaultClinicalStaff = ClinicalStaffTestHelper.getDefaultClinicalStaff();
        query.filterByClinicalStaffId(defaultClinicalStaff.getId());
        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = (List<StudyOrganizationClinicalStaff>) studyOrganizationClinicalStaffRepository.find(query);

        assertFalse(organizationClinicalStaffList.isEmpty());
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaffList) {
            assertEquals(defaultClinicalStaff.getId(), studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getId());
        }
    }


    public void testDeleteStudyOrganizationClinicalStaff() {
        StudyOrganizationClinicalStaff staff = defaultStudy.getLeadStudySite().getStudyOrganizationClinicalStaffs().get(0);
        assertNotNull("must find study clinical staff", studyOrganizationClinicalStaffRepository.findById(staff.getId()));
        //now remove it
        defaultStudy.getLeadStudySite().getStudyOrganizationClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

        StudyOrganizationClinicalStaff expectedStudyOrganizationClinicalStaff = studyOrganizationClinicalStaffRepository.findById(staff.getId());
        assertNull("must remove study clinical staff", expectedStudyOrganizationClinicalStaff);
        deleteAndCreateTestData();
    }

    public void testUpdateStudyOrganizationClinicalStaff() {
        StudyOrganizationClinicalStaff staff = defaultStudy.getLeadStudySite().getStudyOrganizationClinicalStaffs().get(0);
        OrganizationClinicalStaff organizationClinicalStaff = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.NURSE).getOrganizationClinicalStaff();
        staff.setOrganizationClinicalStaff(organizationClinicalStaff);
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();
        StudyOrganizationClinicalStaff expectedStudyOrganizationClinicalStaff = studyOrganizationClinicalStaffRepository.findById(staff.getId());
        assertNotNull("must not study clinical staff", expectedStudyOrganizationClinicalStaff);
        assertEquals("must not study clinical staff", organizationClinicalStaff, expectedStudyOrganizationClinicalStaff.getOrganizationClinicalStaff());
        deleteAndCreateTestData();
    }
}