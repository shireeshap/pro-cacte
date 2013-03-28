package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class StudyParticipantModeTest extends TestDataManager{
	private static Study study;
	private static StudyParticipantAssignment studyParticipantAssignment;
	private static StudyParticipantMode studyParticipantMode;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		study = StudyTestHelper.getDefaultStudy();
		studyParticipantAssignment = getStudyParticipantAssignment();
	}	
	public void testGetterAndSetters(){

		studyParticipantMode = new StudyParticipantMode();
		studyParticipantMode.setMode(AppMode.HOMEWEB);
		studyParticipantMode.setEmail(true);
		studyParticipantMode.setText(false);
		studyParticipantMode.setCall(false);
		studyParticipantMode.setStudyParticipantAssignment(studyParticipantAssignment);
		studyParticipantAssignment.addStudyParticipantMode(studyParticipantMode);
		
		StudyParticipantAssignment spa = getStudyParticipantAssignment();
		StudyParticipantMode spm = spa.getStudyParticipantModes().get(spa.getStudyParticipantModes().size() - 1);
		assertEquals(AppMode.HOMEWEB, spm.getMode());
		assertTrue(spm.getEmail());
		assertFalse(spm.getText());
		assertFalse(spm.getText());
	}
	
	public StudyParticipantAssignment getStudyParticipantAssignment(){
		return study.getStudySites().get(0).getStudyParticipantAssignments().get(0);
	}
}
