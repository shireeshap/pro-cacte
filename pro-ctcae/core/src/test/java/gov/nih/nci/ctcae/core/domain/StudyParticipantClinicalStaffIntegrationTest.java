package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudyParticipantClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private StudySiteClinicalStaff studySiteClinicalStaff;

    private StudyParticipantClinicalStaff studyParticipantClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

    }

    public void testAddStudyParticipantClinicalStaff() {

        addStudyParticipantClinicalStaff();

    }


    public void testDeleteStudyParticipantClinicalStaffByDeletingFromStudyParticipantAssignment() {

        addStudyParticipantClinicalStaff();
        //now remove it
        assertFalse("must save study participant clinical staff", defaultStudyParticipantAssignment.getStudyParticipantClinicalStaffs().isEmpty());

        defaultStudyParticipantAssignment.getStudyParticipantClinicalStaffs().clear();
        studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        StudyParticipantClinicalStaff expectedStudyParticipantClinicalStaff = finderRepository.findById(StudyParticipantClinicalStaff.class, studyParticipantClinicalStaff.getId());
        assertNull("must remove study clinical staff", expectedStudyParticipantClinicalStaff);

    }

    private void addStudyParticipantClinicalStaff() {
        studySiteClinicalStaff = addStudySiteClinicalStaff();

        studyParticipantClinicalStaff = new StudyParticipantClinicalStaff();
        studyParticipantClinicalStaff.setStudySiteClinicalStaff(studySiteClinicalStaff);

        defaultStudyParticipantAssignment.addStudyParticipantClinicalStaff(studyParticipantClinicalStaff);

        studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();


        List<StudyParticipantClinicalStaff> studyParticipantClinicalStaffList = defaultStudyParticipantAssignment.getStudyParticipantClinicalStaffs();

        assertFalse("must save study participant clinical staff", studyParticipantClinicalStaffList.isEmpty());

        studyParticipantClinicalStaff = studyParticipantClinicalStaffList.get(0);

        assertNotNull("must save study participant clinical staff", studyParticipantClinicalStaff.getId());

        assertEquals("study site must be same", studyParticipantClinicalStaff.getStudyParticipantAssignment(), defaultStudyParticipantAssignment);

        assertEquals("study site clinical staff  must be same", studySiteClinicalStaff, studyParticipantClinicalStaff.getStudySiteClinicalStaff());
    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        super.onTearDownInTransaction();
        deleteFromTables(new String[]{"study_participant_clinical_staffs", "STUDY_SITE_CLINICAL_STAFFS"});

    }
}