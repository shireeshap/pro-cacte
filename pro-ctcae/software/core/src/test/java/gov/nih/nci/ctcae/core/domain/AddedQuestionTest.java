package gov.nih.nci.ctcae.core.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.helper.TestDataManager;

public class AddedQuestionTest extends TestDataManager{
	private static Study study;
	private static StudyParticipantAssignment studyParticipantAssignment;
	private static String DUMMY_USER = "dummy.user";

	
	protected void onSetUp() throws Exception {
		super.onSetUp();
		study = StudyTestHelper.getDefaultStudy();
		studyParticipantAssignment = getStudyParticipantAssignment();
	}	
	
	public void testEqualsAndHashCodeForStudyParticipantCrfAddedQuestions(){
		StudyParticipantCrfAddedQuestion spcaq = new StudyParticipantCrfAddedQuestion();
		setValuesForSpcaq(spcaq, getProCtcQuestionFromRepository().get(1), null, 11, studyParticipantAssignment.getStudyParticipantCrfs().get(0));
		
		StudyParticipantCrfAddedQuestion otherSpcaq = new StudyParticipantCrfAddedQuestion();
		setValuesForSpcaq(otherSpcaq, getProCtcQuestionFromRepository().get(1), null, 12, studyParticipantAssignment.getStudyParticipantCrfs().get(0));
		
		//testing pageNumber field used in equals and hashCode method
		assertFalse(spcaq.equals(otherSpcaq));
		assertNotSame(spcaq.hashCode(),otherSpcaq.hashCode());
		otherSpcaq.setPageNumber(11);
		assertTrue(spcaq.equals(otherSpcaq));
		assertEquals(spcaq.hashCode(),otherSpcaq.hashCode());
		
		//testing StudyParticipantCrf field used in equals and hashCode method
		otherSpcaq.setStudyParticipantCrf(null);
		assertFalse(spcaq.equals(otherSpcaq));
		assertNotSame(spcaq.hashCode(),otherSpcaq.hashCode());
	}
	
	
	public void testEqualsAndHashCodeForStudyParticipantCrfScheduleAddedQuestion(){
		
		StudyParticipantCrfScheduleAddedQuestion spcsaq = new StudyParticipantCrfScheduleAddedQuestion();
		ProCtcValidValue proCtcValidValue = (ProCtcValidValue)CollectionUtils.get(getProCtcQuestionFromRepository().get(1).getValidValues(), 0);
		setValuesForSpcsaq(spcsaq, getProCtcQuestionFromRepository().get(1), null, 11, proCtcValidValue, null, 22);
	
		StudyParticipantCrfScheduleAddedQuestion otherSpcsaq = new StudyParticipantCrfScheduleAddedQuestion();
		setValuesForSpcsaq(otherSpcsaq, getProCtcQuestionFromRepository().get(1), null, 12, proCtcValidValue, null, 22);
		
		// testing pageNumber field used in equals and hashCode method
		assertFalse(spcsaq.equals(otherSpcsaq));
		assertNotSame(otherSpcsaq.hashCode(), spcsaq.hashCode());
		otherSpcsaq.setPageNumber(11);
		assertTrue(spcsaq.equals(otherSpcsaq));
		assertEquals(otherSpcsaq.hashCode(), spcsaq.hashCode());
		
		// testing proctcValidValue field used in equals and hascode method
		otherSpcsaq.setProCtcValidValue(null);
		assertFalse(spcsaq.equals(otherSpcsaq));
		assertNotSame(otherSpcsaq.hashCode(), spcsaq.hashCode());
		
		// testing StudyParticipantCrfAddedQuestion field in equals and hashcode method
		otherSpcsaq.setProCtcValidValue(proCtcValidValue);
		otherSpcsaq.setStudyParticipantCrfAddedQuestionId(null);
		assertFalse(spcsaq.equals(otherSpcsaq));
		assertNotSame(otherSpcsaq.hashCode(), spcsaq.hashCode());
		
		// test that reponseDate, responseMode and UpdatedBy filed not considered in equals and hashCode method
		otherSpcsaq.setStudyParticipantCrfAddedQuestionId(22);
		otherSpcsaq.setReponseDate(new Date());
		otherSpcsaq.setResponseMode(AppMode.HOMEWEB);
		otherSpcsaq.setUpdatedBy(DUMMY_USER);
		assertTrue(spcsaq.equals(otherSpcsaq));
		assertEquals(otherSpcsaq.hashCode(), spcsaq.hashCode());
	}
	
	private void setValuesForSpcaq(StudyParticipantCrfAddedQuestion spcaq, ProCtcQuestion proCtcQuestion, MeddraQuestion meddraQuestion, int pageNumber, StudyParticipantCrf studyParticipantCrf){
		spcaq.setProCtcQuestion(proCtcQuestion);
		spcaq.setMeddraQuestion(meddraQuestion);
		spcaq.setPageNumber(pageNumber);
		spcaq.setStudyParticipantCrf(studyParticipantCrf);
	}
	
	private void setValuesForSpcsaq(StudyParticipantCrfScheduleAddedQuestion spcsaq, ProCtcQuestion proCtcQuestion, MeddraQuestion meddraQuestion, int pageNumber, ProCtcValidValue proCtcValidValue, MeddraValidValue meddraValidValue, int spaqId){
		spcsaq.setProCtcQuestion(proCtcQuestion);
		spcsaq.setMeddraQuestion(meddraQuestion);
		spcsaq.setPageNumber(pageNumber);
		spcsaq.setProCtcValidValue(proCtcValidValue);
		spcsaq.setMeddraValidValue(meddraValidValue);
		spcsaq.setStudyParticipantCrfAddedQuestionId(spaqId);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ProCtcQuestion> getProCtcQuestionFromRepository(){
		return hibernateTemplate.find("from ProCtcQuestion");
	}
	
	public StudyParticipantAssignment getStudyParticipantAssignment(){
		return study.getStudySites().get(0).getStudyParticipantAssignments().get(0);
	}
}
