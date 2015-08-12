package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfGrades;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.web.servlet.ModelAndView;

public class ParticipantLevelCtcaeGradesReportResultsControllerTest extends AbstractWebTestCase{
	private Study defaultStudy;
	private Participant defaultParticipant;
	private static String STUDY_ID = "studyId";
	private static String PARTICIPANT_ID = "participantId";
	private static String VISIT_RANGE = "visitRange";
	private static String AE_WITH_GRADES_LIST = "adverseEventsWithGradesList";
	ParticipantLevelCtcaeGradesReportResultsController controller;
	private List<StudyParticipantCrfSchedule> list;
	private Random random = new Random();
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();
		defaultStudy = StudyTestHelper.getDefaultStudy();
		defaultParticipant = getDefaultParticipant();
		request.setParameter(STUDY_ID, defaultStudy.getId().toString());
		request.setParameter(PARTICIPANT_ID, defaultParticipant.getId().toString());
		request.setAttribute(VISIT_RANGE, "dateRange");
		controller = new ParticipantLevelCtcaeGradesReportResultsController();
		controller.setGenericRepository(genericRepository);
		request.setMethod("GET");
		setUpStudyPariticipantCrfGrades();
	}
	
	public void testHandleRequestInternal(){
		ModelAndView modelAndView = new ModelAndView();
		try {
			modelAndView = controller.handleRequest(request, response);
			assertEquals("reports/participantLevelCtcaeGradesReportResults", modelAndView.getViewName());
			assertFalse(((List<AeReportEntryWrapper>)request.getSession().getAttribute(AE_WITH_GRADES_LIST)).isEmpty());
		} catch (Exception e) {
			System.out.println("Failure in testHandleRequestInternal() " +e.getStackTrace());
		}
		
	}
	
	@Override
	protected void onTearDownInTransaction() throws Exception {
		super.onTearDownInTransaction();
		deleteData();
	}
	
	private void deleteData() {
		for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : list){
			studyParticipantCrfSchedule.getStudyParticipantCrfGrades().clear();
			studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
		}
	}

	private void setUpStudyPariticipantCrfGrades() throws Exception{
		// Answer and submit surveys for defaultParticipant
		ParticipantTestHelper.completeParticipantSchedule(defaultParticipant, defaultStudy.getStudySites().get(0), false, AppMode.HOMEWEB);
		
		// Fetch the list of schedules which will be fetched by ACCRU report controller
		ProctcaeGradeMappingVersion proctcaeGradeMappingVersion = getDefaultProctcaeGradeMappingVersion();
		StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
        query.filterByStatus(CrfStatus.COMPLETED);
        list = genericRepository.find(query);
        
        // Generate dummy grades for the schedule list
        ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
        ProCtcTerm proCtcTerm = genericRepository.findSingle(proCtcTermQuery);
        for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : list){
        	for(int i=0; i<2; i++){
        		StudyParticipantCrfGrades studyParticipantCrfGrade = new StudyParticipantCrfGrades();
        		studyParticipantCrfGrade.setGrade(String.valueOf(random.nextInt(4)));
        		studyParticipantCrfGrade.setGradeMappingVersion(proctcaeGradeMappingVersion);
        		studyParticipantCrfGrade.setProCtcTerm(proCtcTerm);
        		studyParticipantCrfGrade.setGradeEvaluationDate(new Date());
        		studyParticipantCrfSchedule.addStudyParticipantCrfGrade(studyParticipantCrfGrade);
        	}
        	genericRepository.save(studyParticipantCrfSchedule);
        }
	}
	
	 public Participant getDefaultParticipant(){
	    	return StudyTestHelper.getDefaultStudy().getArms().get(0).getStudyParticipantAssignments().get(0).getParticipant();
	 }
}
