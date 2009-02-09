package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudySiteClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudySiteClinicalStaff studySiteClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        studySiteClinicalStaff = new StudySiteClinicalStaff();
        studySiteClinicalStaff.setSiteClinicalStaff(defaultSiteClinicalStaff);
        defaultStudySite.addStudySiteClinicalStaff(studySiteClinicalStaff);


    }

    public void testAddStudySiteClinicalStaff() {

        addStudySiteClinicalStaff();
    }


    public void testDeleteStudySiteClinicalStaff() {

        addStudySiteClinicalStaff();
        commitAndStartNewTransaction();

        assertNotNull("must find study clinical staff", finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId()));

        //now remove it
        defaultStudySite.getStudySiteClinicalStaffs().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudySiteClinicalStaff expectedStudySiteClinicalStaff = finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudySiteClinicalStaff);

    }

    private void addStudySiteClinicalStaff() {
        defaultStudy = studyRepository.save(defaultStudy);

        defaultStudySite = defaultStudy.getStudySites().get(0);
        List<StudySiteClinicalStaff> studySiteClinicalStaffs = defaultStudySite.getStudySiteClinicalStaffs();


        assertFalse("must save study clinical staff", studySiteClinicalStaffs.isEmpty());
        studySiteClinicalStaff = studySiteClinicalStaffs.get(0);
        assertNotNull("must save study clinical staff", studySiteClinicalStaff.getId());
        assertEquals("study site must be same", studySiteClinicalStaff.getStudySite(), defaultStudySite);


        assertEquals("site clinical staff  must be same", defaultSiteClinicalStaff, studySiteClinicalStaff.getSiteClinicalStaff());
    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        super.onTearDownInTransaction();
        deleteFromTables(new String[]{"STUDY_SITE_CLINICAL_STAFFS"});

    }
}