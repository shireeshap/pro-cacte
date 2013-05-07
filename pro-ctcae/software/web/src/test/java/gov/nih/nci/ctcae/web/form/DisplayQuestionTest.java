package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.apache.commons.collections.CollectionUtils;


/**
 * @author AmeyS
 * Testcases for DisplayQuestion.java
 */
public class DisplayQuestionTest extends AbstractWebTestCase{
	
	private Study study;
	private CRF crf;
	private DisplayQuestion displayQuestion;
	private SubmitFormCommand command;
	private Question question;
	private StudyParticipantCrfAddedQuestion  studyParticipantCrfAddedQuestion;
	private StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion;
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		study = StudyTestHelper.getDefaultStudy();
		crf = study.getCrfs().get(0);
		String scheduleId = crf.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0).getId().toString();
		System.out.println("scheduleId: " + scheduleId);
		System.out.println("genericRepository: " + genericRepository != null);
		command = new SubmitFormCommand(scheduleId, genericRepository, studyParticipantCrfScheduleRepository, null, null);
		displayQuestion = new DisplayQuestion(genericRepository, command);
		question = (ProCtcQuestion) getProCtcQuestion();
		studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
		studyParticipantCrfScheduleAddedQuestion.setProCtcQuestion(getProCtcQuestion());
		studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
		studyParticipantCrfAddedQuestion.setProCtcQuestion(getProCtcQuestion());
	}
	
	public void testGetterAndSetter_Question(){
		displayQuestion.setQuestion(question);
		
		assertEquals(question.getQuestionText(), displayQuestion.getQuestionText());
		assertEquals(question.getQuestionSymptom(), displayQuestion.getQuestionSymptom());
		assertEquals(question.getSymptomGender(), displayQuestion.getSymptomGender());
		assertEquals(question.getQuestionText(SupportedLanguageEnum.SPANISH), displayQuestion.getQuestionTextSpanish());
	}
	
	public void testGetValidValues(){
		displayQuestion.setQuestion(question);
		assertEquals(((ProCtcQuestion) question).getValidValues().size(), displayQuestion.getValidValues().size());
	}
	
	public void testSetSelectedValidValueId(){
		displayQuestion.setQuestion(question);
		displayQuestion.setParticipantAdded(true);
		studyParticipantCrfScheduleAddedQuestion.setId(question.getId());
		displayQuestion.setStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfScheduleAddedQuestion);
		ValidValue  v = (ValidValue) CollectionUtils.get(((ProCtcQuestion) question).getValidValues(), 0);
		command.getSchedule().addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfAddedQuestion, true);
		command.getSchedule().getStudyParticipantCrfScheduleAddedQuestions().get(0).setId(question.getId());
		displayQuestion.setSelectedValidValueId(v.getId().toString());
		
		assertEquals(v, displayQuestion.getSelectedValidValue());
	}
	
	 private ProCtcQuestion getProCtcQuestion(){
		return crf.getAllCrfPageItems().get(0).getProCtcQuestion();
	 }
	
}
