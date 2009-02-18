package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

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
        studyOrganizationClinicalStaff.setRoles(PI);
        studyOrganizationClinicalStaff.setSiteClinicalStaff(defaultSiteClinicalStaff);


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