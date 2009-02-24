package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

/**
 * @author Vinay Kumar
 * @created Feb 20, 2009
 */
public class StudyClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudyClinicalStaff studyClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        studyClinicalStaff = new StudyClinicalStaff();
        studyClinicalStaff.setRole(PI);
        studyClinicalStaff.setOrganizationClinicalStaff(defaultOrganizationClinicalStaff);


    }

    public void testAddStudyClinicalStaff() {

        studyClinicalStaff = addStudyClinicalStaff(studyClinicalStaff);
    }


    public void testDeleteStudyClinicalStaff() {

        studyClinicalStaff = addStudyClinicalStaff(studyClinicalStaff);

        assertNotNull("must find study clinical staff", finderRepository.findById(StudyClinicalStaff.class, studyClinicalStaff.getId()));

        //now remove it
        defaultStudy.getStudyClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudyClinicalStaff expectedStudyClinicalStaff = finderRepository.findById(StudyClinicalStaff.class, studyClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudyClinicalStaff);

    }


}