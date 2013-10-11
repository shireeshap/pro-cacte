package gov.nih.nci.ctcae.web.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

/**
 * @author AmeyS
 * Testcases for ParticipantAddedQuestionsReportResultsController and ParticipantAddedQuestionsReportDetailsController
 */
public class ParticipantAddedQuestionsReportDetailsControllerIntegrationTest extends AbstractWebTestCase {
	private static ParticipantAddedQuestionsReportResultsController controller;
	private static ParticipantAddedQuestionsReportDetailsController detailsController;
	private static Participant participant;
	private static StudyParticipantCrf studyParticipantCrf;
	private static String BLANK = "";
	String symptom;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		controller = new ParticipantAddedQuestionsReportResultsController();
		detailsController = new ParticipantAddedQuestionsReportDetailsController();
		
		controller.setGenericRepository(genericRepository);
		detailsController.setGenericRepository(genericRepository);
		participant = ParticipantTestHelper.getDefaultParticipant();
		studyParticipantCrf = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0);
		
		StudyParticipantCrfAddedQuestion spcrf_addedQuestion = studyParticipantCrf.addStudyParticipantCrfAddedQuestion(getProCtcQuestionFromRepository().get(11), studyParticipantCrf.getCrf().getCrfPages().size());
    	genericRepository.save(spcrf_addedQuestion);
    	StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrf.getStudyParticipantCrfSchedules().get(0);
    	studyParticipantCrfSchedule.addParticipantAddedQuestions();
    	genericRepository.save(studyParticipantCrfSchedule);
    	request.setParameter("crf", getCrfIdForDefaultParticipant().toString());
		request.setParameter("studySite", getStudyStiteForDefaultParticipant().toString());
		request.setParameter("symptom", BLANK);
	}
	
	public void testHandleRequestInternal() throws Exception{
		ModelAndView modelAndView = controller.handleRequestInternal(request, response);
		assertEquals("reports/participantAddedQuestionsResults", modelAndView.getViewName());
		assertNotNull(modelAndView.getModelMap().get("results"));
		
		symptom = getProCtcQuestionFromRepository().get(11).getQuestionText(SupportedLanguageEnum.ENGLISH);
		request.setParameter("symptom", symptom);
		modelAndView = detailsController.handleRequestInternal(request, response);
		assertEquals("reports/participantAddedQuestionsDetails", modelAndView.getViewName());
		assertNotNull(modelAndView.getModelMap().get("results"));
		assertEquals(symptom, modelAndView.getModelMap().get("symptom"));
	}
	
	private Integer getCrfIdForDefaultParticipant(){
		return participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getCrf().getId();
	}
	
	private Integer getStudyStiteForDefaultParticipant(){
		return participant.getStudyParticipantAssignments().get(0).getStudySite().getId();
	}

	public List<ProCtcQuestion> getProCtcQuestionFromRepository(){
    	return hibernateTemplate.find("from ProCtcQuestion");
    }
}
