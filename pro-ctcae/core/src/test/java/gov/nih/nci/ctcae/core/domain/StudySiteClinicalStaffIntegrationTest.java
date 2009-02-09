package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudySiteClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudySiteClinicalStaff studySiteClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();



    }

    public void testAddStudySiteClinicalStaff() {

        studySiteClinicalStaff = addStudySiteClinicalStaff();
    }


    public void testDeleteStudySiteClinicalStaff() {

        studySiteClinicalStaff = addStudySiteClinicalStaff();

        assertNotNull("must find study clinical staff", finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId()));

        //now remove it
        defaultStudySite.getStudySiteClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudySiteClinicalStaff expectedStudySiteClinicalStaff = finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudySiteClinicalStaff);

    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        super.onTearDownInTransaction();
        deleteFromTables(new String[]{"STUDY_SITE_CLINICAL_STAFFS"});

    }
}