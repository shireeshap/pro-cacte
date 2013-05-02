package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
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
        
        getTestResponseValues();
        request.setMethod("GET");
        request.setParameter("study", study.getId().toString());
        controller.setGenericRepository(genericRepository);
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
        TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> overAllResults = (TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>) request.getSession().getAttribute("sessionResultsMap");
        assertEquals(2, overAllResults.size());
        for(Organization organization : overAllResults.keySet()){
        	for(CRF crf : overAllResults.get(organization).keySet()){
        		for(Participant participant : overAllResults.get(organization).get(crf).keySet()){
        			for(String symptom : overAllResults.get(organization).get(crf).get(participant).keySet()){
        				for(Question question : overAllResults.get(organization).get(crf).get(participant).get(symptom).keySet()){
        					totalQuestions += overAllResults.get(organization).get(crf).get(participant).get(symptom).get(question).size();
        				}
        			}
        			
        		}
        	}
        	
        }
        assertTrue(!overAllResults.isEmpty());
        assertTrue(totalQuestions > 0);
        
        int noOfSchedules = 0;
        TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>) request.getSession().getAttribute("sessionCRFDatesMap");
        assertEquals(1, crfDateMap.size());
        for(CRF crf : crfDateMap.keySet()){
        	for(Participant participant : crfDateMap.get(crf).keySet()){
        		noOfSchedules += crfDateMap.get(crf).get(participant).size();
        	}
        }
        assertTrue(!crfDateMap.isEmpty());
        assertTrue(noOfSchedules > 0);
        
        noOfSchedules = 0;
        TreeMap<CRF, LinkedHashMap<Participant, ArrayList<AppMode>>> crfModeMap = (TreeMap<CRF, LinkedHashMap<Participant, ArrayList<AppMode>>>) request.getSession().getAttribute("sessionCRFModeMap");
        assertEquals(1, crfModeMap.size());
        for(CRF crf : crfModeMap.keySet()){
        	for(Participant participant : crfModeMap.get(crf).keySet()){
        		noOfSchedules += crfModeMap.get(crf).get(participant).size();
        	}
        }
        assertTrue(!crfModeMap.isEmpty());
        assertTrue(noOfSchedules > 0);
    }
    
    private void getTestResponseValues(){
    	studySite = study.getStudySites().get(0);
    	organization = studySite.getOrganization();
    	studyParticipantCrfSchedule = studySite.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);
    	crf = studySite.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getCrf();
    	participant = studySite.getStudyParticipantAssignments().get(0).getParticipant();
    	
    	symptom = studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(0).getProCtcValidValue().getProCtcQuestion().getProCtcTerm().getTermEnglish(SupportedLanguageEnum.ENGLISH);
    	proCtcValidValues.add(studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(0).getProCtcValidValue());
    	proCtcValidValues.add(studyParticipantCrfSchedule.getStudyParticipantCrfItems().get(1).getProCtcValidValue());
    }
}