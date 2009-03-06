package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

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
        studyOrganizationClinicalStaff.setRole(PI);
        studyOrganizationClinicalStaff.setOrganizationClinicalStaff(defaultOrganizationClinicalStaff);


    }

    public void testFind() {
        List<Role> rolesList = new ArrayList<Role>();
        rolesList.add(Role.NURSE);
        List<StudyOrganizationClinicalStaff> organizationClinicalStaffList = clinicalStaffRepository.findByStudyOrganizationIdAndRole("%", defaultStudySite.getId(), rolesList);

        assertFalse(organizationClinicalStaffList.isEmpty());
    }

    public void testAddStudyOrganizationClinicalStaff() {

        studyOrganizationClinicalStaff = addStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);
    }


    public void testDeleteStudyOrganizationClinicalStaff() {

        studyOrganizationClinicalStaff = addStudyOrganizationClinicalStaff(studyOrganizationClinicalStaff);

        assertNotNull("must find study clinical staff", finderRepository.findById(StudyOrganizationClinicalStaff.class, studyOrganizationClinicalStaff.getId()));

        //now remove it
        defaultStudySite.getStudyOrganizationClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudyOrganizationClinicalStaff expectedStudyOrganizationClinicalStaff = finderRepository.findById(StudyOrganizationClinicalStaff.class, studyOrganizationClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudyOrganizationClinicalStaff);

    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        super.onTearDownInTransaction();

    }
}