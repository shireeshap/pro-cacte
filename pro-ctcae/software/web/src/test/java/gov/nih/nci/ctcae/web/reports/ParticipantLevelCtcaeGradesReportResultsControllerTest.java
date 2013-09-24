package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMappingVersion;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

public class ParticipantLevelCtcaeGradesReportResultsControllerTest extends AbstractWebTestCase{
	private Study defaultStudy;
	private Participant defaultParticipant;
	private static String STUDY_ID = "studyId";
	private static String PARTICIPANT_ID = "participantId";
	private static String VISIT_RANGE = "visitRange";
	private static String AE_WITH_GRADES_LIST = "adverseEventsWithGradesList";
	ParticipantLevelCtcaeGradesReportResultsController controller;
	
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
	}
	
	public void testHandleRequestInternal(){
		ModelAndView modelAndView = new ModelAndView();
		try {
			StudyParticipantCrfSchedule spcrfSchedule = setUpData();
			modelAndView = controller.handleRequest(request, response);
			assertEquals("reports/participantLevelCtcaeGradesReportResults", modelAndView.getViewName());
			assertFalse(((List<AeReportEntryWrapper>)request.getSession().getAttribute(AE_WITH_GRADES_LIST)).isEmpty());
			deleteData(spcrfSchedule);
		} catch (Exception e) {
			System.out.println("Failure in testHandleRequestInternal() " +e.getStackTrace());
		}
		
	}
	
	private void deleteData(StudyParticipantCrfSchedule spcrfSchedule) {
		spcrfSchedule.getStudyParticipantCrfGrades().clear();
		studyParticipantCrfScheduleRepository.save(spcrfSchedule);
		
	}

	private StudyParticipantCrfSchedule setUpData() throws Exception{
		ParticipantTestHelper.completeParticipantSchedule(defaultParticipant, defaultStudy.getStudySites().get(0), false, AppMode.HOMEWEB);
		
		ProctcaeGradeMappingVersion proctcaeGradeMappingVersion = getDefaultProctcaeGradeMappingVersion();
		
		StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
        query.filterByStatus(CrfStatus.COMPLETED);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);
        
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = list.get(0);
        for(StudyParticipantCrfItem spCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()){
    		ProCtcValidValue proCtcValidValue = spCrfItem.getProCtcValidValue();
    		if(proCtcValidValue != null){
    			proCtcValidValue.setResponseCode(proCtcValidValue.getDisplayOrder());
    		}
    	}
    	studyParticipantCrfSchedule.generateStudyParticipantCrfGrades(proctcaeGradeMappingVersion);
    	studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
    	return studyParticipantCrfSchedule;
        	
//        for(StudyParticipantCrfSchedule studyParticipantCrfSchedule : list){
//        	/*  responseCode values in ProCtcValidValue table are updated through loaders.
//        	 *  Mocking up the same behavior below for this testCase. 
//        	 */
//        	for(StudyParticipantCrfItem spCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()){
//        		ProCtcValidValue proCtcValidValue = spCrfItem.getProCtcValidValue();
//        		if(proCtcValidValue != null){
//        			proCtcValidValue.setResponseCode(proCtcValidValue.getDisplayOrder());
//        		}
//        	}
//        	/*
//        	 *  generate grades for each studyParticipantCrfSchedule. 
//        	 */
//        	studyParticipantCrfSchedule.generateStudyParticipantCrfGrades(proctcaeGradeMappingVersion);
//        }
	}
	
	 public Participant getDefaultParticipant(){
	    	return StudyTestHelper.getDefaultStudy().getArms().get(0).getStudyParticipantAssignments().get(0).getParticipant();
	 }
}
