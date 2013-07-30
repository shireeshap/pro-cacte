package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author AmeyS
 * 
 */
public class StudyLevelFullReportIntegrationTest extends AbstractWebTestCase {

	StudyLevelFullReportResultsController controller;
    List<StudyParticipantCrfSchedule> schedules;
    StudySite studySite;
    Organization organization;
    CRF crf;
    Participant participant;
    StudyParticipantCrfSchedule studyParticipantCrfSchedule;
    StudyWideFormatReportData studyWideFormatReportData;
    Study study;
    String symptom;
    List<ProCtcValidValue> proCtcValidValues;
    

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login(StudyTestHelper.getDefaultStudy().getLeadCRAs().get(0).getOrganizationClinicalStaff().getClinicalStaff().getUser().getUsername());
        study = StudyTestHelper.getDefaultStudy();
        controller = new StudyLevelFullReportResultsController();
        proCtcValidValues = new ArrayList<ProCtcValidValue>();
        request.setMethod("GET");
        request.setParameter("study", study.getId().toString());
        controller.setGenericRepository(genericRepository);
        studyWideFormatReportData = new StudyWideFormatReportData();
        studyWideFormatReportData.setJdbcTemplate((JdbcTemplate) applicationContext.getBean("jdbcTemplate"));
        controller.setStudyWideFormatReportData(studyWideFormatReportData);
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
    }

    @SuppressWarnings("unchecked")
	public void testGetCareResultsForStudy() throws Exception {
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/fullStudyReport", modelAndView.getViewName());
        
       
        ArrayList<String> proCtcTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionProCtcTermHeaders");
        assertTrue(!proCtcTermHeaders.isEmpty());
       
        ArrayList<String> meddraTermHeaders = (ArrayList<String>) request.getSession().getAttribute("sessionMeddraTermHeaders");
        assertEquals(0, meddraTermHeaders.size()); // as meddra questions are not yet loaded into DB.
        
        TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping  = (TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionProCtcQuestionMapping");
        assertTrue(!proCtcQuestionMapping.isEmpty());
        
        TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping = (TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>>) request.getSession().getAttribute("sessionMeddraQuestionMapping");
        assertTrue(meddraQuestionMapping.isEmpty());
        
        int totalQuestions = 0;
        TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, LinkedHashMap<String, ArrayList<String>>>>>> overAllResults = (TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, LinkedHashMap<String, ArrayList<String>>>>>>) request.getSession().getAttribute("sessionResultsMap");
        assertEquals(2, overAllResults.size());
        for(String organization : overAllResults.keySet()){
        	for(String crf : overAllResults.get(organization).keySet()){
        		for(String participant : overAllResults.get(organization).get(crf).keySet()){
        			for(String symptom : overAllResults.get(organization).get(crf).get(participant).keySet()){
        				for(String questionType : overAllResults.get(organization).get(crf).get(participant).get(symptom).keySet()){
        					totalQuestions += overAllResults.get(organization).get(crf).get(participant).get(symptom).get(questionType).size();
        				}
        			}
        			
        		}
        	}
        	
        }
        assertTrue(!overAllResults.isEmpty());
        assertTrue(totalQuestions > 0);
        
        int noOfSchedules = 0;
        TreeMap<String, LinkedHashMap<String, ArrayList<Date>>> crfDateMap = (TreeMap<String, LinkedHashMap<String, ArrayList<Date>>>) request.getSession().getAttribute("sessionCRFDatesMap");
        assertEquals(1, crfDateMap.size());
        for(String crf : crfDateMap.keySet()){
        	for(String participant : crfDateMap.get(crf).keySet()){
        		noOfSchedules += crfDateMap.get(crf).get(participant).size();
        	}
        }
        assertTrue(!crfDateMap.isEmpty());
        assertTrue(noOfSchedules > 0);
        
        noOfSchedules = 0;
        TreeMap<String, LinkedHashMap<String, ArrayList<String>>> crfModeMap = (TreeMap<String, LinkedHashMap<String, ArrayList<String>>>) request.getSession().getAttribute("sessionCRFModeMap");
        assertEquals(1, crfModeMap.size());
        for(String crf : crfModeMap.keySet()){
        	for(String participant : crfModeMap.get(crf).keySet()){
        		noOfSchedules += crfModeMap.get(crf).get(participant).size();
        	}
        }
        assertTrue(!crfModeMap.isEmpty());
        assertTrue(noOfSchedules > 0);
    }
}