package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.query.StudyOrganizationClinicalStaffQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudyOrganizationClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        studyOrganizationClinicalStaff = new StudyOrganizationClinicalStaff();
        studyOrganizationClinicalStaff.setRole(Role.PI);
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(defaultOrganizationClinicalStaff);
        defaultStudy.getLeadStudySite().addOrUpdateStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        studyOrganizationClinicalStaff = addStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
        insertDefaultUsers();

    }

    public void testFind() {
        List<Role> rolesList = new ArrayList<Role>();
        rolesList.add(Role.PI);
        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = clinicalStaffRepository.findByStudyOrganizationIdAndRole("%",
                defaultStudy.getLeadStudySite().getId(), rolesList);

        assertFalse(organizationClinicalStaffList.isEmpty());
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaffList) {
            assertEquals(Role.PI, studyOrganizationClinicalStaff.getRole());
        }
    }

    public void testFindByClinicalStaff() {
        StudyOrganizationClinicalStaffQuery query = new StudyOrganizationClinicalStaffQuery();

        query.filterByClinicalStaffId(defaultClinicalStaff.getId());
        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = genericRepository.find(query);

        assertFalse(organizationClinicalStaffList.isEmpty());
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : organizationClinicalStaffList) {
            assertEquals(defaultClinicalStaff.getId(), studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getId());
        }
    }


    public void testDeleteStudyOrganizationClinicalStaff() {


        assertNotNull("must find study clinical staff", genericRepository.findById(StudyOrganizationClinicalStaff.class, studyOrganizationClinicalStaff.getId()));

        //now remove it
        defaultStudy.getLeadStudySite().getStudyOrganizationClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudyOrganizationClinicalStaff expectedStudyOrganizationClinicalStaff = genericRepository.findById(StudyOrganizationClinicalStaff.class, studyOrganizationClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudyOrganizationClinicalStaff);

    }

    public void testUpdateStudyOrganizationClinicalStaff() {


        StudyOrganizationClinicalStaff staff = defaultStudy.getLeadStudySite().getStudyOrganizationClinicalStaffs().get(0);
        OrganizationClinicalStaff organizationClinicalStaff = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.NURSE).getOrganizationClinicalStaff();
        staff.setOrganizationClinicalStaff(organizationClinicalStaff);
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudyOrganizationClinicalStaff expectedStudyOrganizationClinicalStaff = genericRepository.findById(StudyOrganizationClinicalStaff.class, studyOrganizationClinicalStaff.getId());
        assertNotNull("must not study clinical staff", expectedStudyOrganizationClinicalStaff);
        assertEquals("must not study clinical staff", organizationClinicalStaff, expectedStudyOrganizationClinicalStaff.getOrganizationClinicalStaff());

    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        super.onTearDownInTransaction();

    }
}