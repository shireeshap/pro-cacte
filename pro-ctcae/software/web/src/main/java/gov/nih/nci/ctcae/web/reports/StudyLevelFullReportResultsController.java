package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import gov.nih.nci.ctcae.core.query.MeddraQuery;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class StudyLevelFullReportResultsController extends AbstractController {

	GenericRepository genericRepository;
	StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
	private static Integer MARK_MANUAL_SKIP = -55;
	private static Integer MARK_FORCE_SKIP =-99;
	private static Integer MARK_NOT_ADMINISTERED = -2000;
	private static String FREQUENCY = "FRQ";
	private static String INTERFERENCE = "INT";
	private static String SEVERITY = "SEV";
	private static String PRESENT =	"PRES";
	private static String AMOUNT =	"AMT";
	private static String GENDER_BOTH = "both";

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("reports/fullStudyReport");
		
		try{
			// List of all the answered surveys, by all the participants, for all the crf's associated with the selected Study at each of the studySite.
			StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request);
			List<StudyParticipantCrfSchedule> list = studyParticipantCrfScheduleRepository.find(query);
	
			// Mapping a ProCtcQuestion to a column in Report.
			TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping = new TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>>();
			// Mapping a MeddraQuestion to a column in Report.
			TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping = new TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>>();
			// ProCtcQuestion List to be displayed in Report's table header (ordered according to proCtcQuestionMapping)
			ArrayList<String> proCtcTermHeaders = new ArrayList<String>();
			
			// MeddraQuestion List to be displayed in Report's table header (ordered according to meddraQuestionMapping)
			ArrayList<String> meddraTermHeaders = new ArrayList<String>();
			// Save the start dates of the submitted surveys listed in 'list'
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap = new TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>>();
			// Save the survey_answering_mode of the submitted surveys listed in 'list'
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>> crfModeMap = new TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>>();
			// Save the survey status of the submitted surveys listed in 'list'
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>> crfStatusMap = new TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>>();
		  
			int col = generateQuestionMappingForTableHeader(proCtcQuestionMapping, proCtcTermHeaders);
			generateMeddraQuestionMappingForTableHeader(list, meddraQuestionMapping, meddraTermHeaders, col);
			
			TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> overAllResults = 
				getCareResults(crfDateMap, crfModeMap, crfStatusMap, list, meddraQuestionMapping, meddraTermHeaders, col);
	
			modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
			request.getSession().setAttribute("sessionResultsMap", overAllResults);
			request.getSession().setAttribute("sessionCRFDatesMap", crfDateMap);
			request.getSession().setAttribute("sessionCRFModeMap", crfModeMap);
			request.getSession().setAttribute("sessionCRFStatusMap", crfStatusMap);
			request.getSession().setAttribute("sessionProCtcQuestionMapping", proCtcQuestionMapping);
			request.getSession().setAttribute("sessionMeddraQuestionMapping", meddraQuestionMapping);
			request.getSession().setAttribute("sessionProCtcTermHeaders", proCtcTermHeaders);
			request.getSession().setAttribute("sessionMeddraTermHeaders", meddraTermHeaders);
		
		}catch (Exception e) {
			e.getStackTrace();
			logger.debug("Debugging on error:" + e.getStackTrace());
		}
		return modelAndView;
	}
	
private void generateMeddraQuestionMappingForTableHeader(List<StudyParticipantCrfSchedule> list, TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping, ArrayList<String> meddraTermHeaders, int col){
		
		for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : list) {
			for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
				if(studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion() != null){
					LowLevelTerm llt = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm();
					MeddraQuestion meddraQuestion = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion();
					TreeMap<ProCtcQuestionType, String> typeMap;
					if (meddraQuestionMapping.containsKey(llt)) {
						typeMap = meddraQuestionMapping.get(llt);
					} else {
						typeMap = new TreeMap<ProCtcQuestionType, String>();
						meddraQuestionMapping.put(llt, typeMap);
					}
					
					String prefix = "";
					if(!typeMap.containsKey(meddraQuestion.getProCtcQuestionType())){
						typeMap.put(meddraQuestion.getProCtcQuestionType(), String.valueOf(col++));
						switch (meddraQuestion.getProCtcQuestionType()) {
							case FREQUENCY:
								prefix = FREQUENCY;					
								break;
								
							case SEVERITY:
								prefix = SEVERITY;					
								break;
								
							case INTERFERENCE:
								prefix = INTERFERENCE;					
								break;
								
							case PRESENT:
								prefix = PRESENT;					
								break;
							case AMOUNT:
								prefix = AMOUNT;					
								break;
						}
						String ctcTermEnglishTermText = prefix +"_"+ llt.getLowLevelTermVocab().getMeddraTermEnglish();
						if(ctcTermEnglishTermText.length() > 32)
							ctcTermEnglishTermText = ctcTermEnglishTermText.substring(0, 32);
							
							meddraTermHeaders.add(ctcTermEnglishTermText);
					}
				}
			
			}
		}
		
	}

	private int generateQuestionMappingForTableHeader(TreeMap<ProCtcTerm, TreeMap<ProCtcQuestionType, String>> proCtcQuestionMapping,
			 		ArrayList<String> proCtcTermHeaders) {
		
		int col = 0;
		String prefix = "";
		String proCtcTermEnglishTermText;
		TreeMap<ProCtcQuestionType, String> typeMap;
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		List<ProCtcTerm> proCtcTerms = genericRepository.find(proCtcTermQuery);
		for (ProCtcTerm proCtcTerm : proCtcTerms) {
			if (proCtcQuestionMapping.containsKey(proCtcTerm)) {
				typeMap = proCtcQuestionMapping.get(proCtcTerm);
			} else {
				typeMap = new TreeMap<ProCtcQuestionType, String>();
				proCtcQuestionMapping.put(proCtcTerm, typeMap);
			}
			for (ProCtcQuestion proCtcQuestion : proCtcTerm.getProCtcQuestions()) {
				typeMap.put(proCtcQuestion.getProCtcQuestionType(), String.valueOf(col++));
				switch (proCtcQuestion.getProCtcQuestionType()) {
					case FREQUENCY:
						prefix = FREQUENCY;					
						break;
						
					case SEVERITY:
						prefix = SEVERITY;					
						break;
						
					case INTERFERENCE:
						prefix = INTERFERENCE;					
						break;
						
					case PRESENT:
						prefix = PRESENT;					
						break;
					case AMOUNT:
						prefix = AMOUNT;					
						break;
				}
				
				proCtcTermEnglishTermText = prefix +"_"+ proCtcTerm.getProCtcTermVocab().getTermEnglish();
				if(proCtcTermEnglishTermText.length() > 32)
					proCtcTermEnglishTermText = proCtcTermEnglishTermText.substring(0, 32);
				proCtcTermHeaders.add(proCtcTermEnglishTermText);
			}
		}
		return col;
	}

	private TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> getCareResults(
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<Date>>> crfDateMap, TreeMap<CRF, LinkedHashMap<Participant, ArrayList<String>>> crfModeMap,
			TreeMap<CRF, LinkedHashMap<Participant, ArrayList<CrfStatus>>> crfStatusMap,
			List<StudyParticipantCrfSchedule> schedules, TreeMap<LowLevelTerm, TreeMap<ProCtcQuestionType, String>> meddraQuestionMapping, 
			ArrayList<String> meddraTermHeaders, int col) throws ParseException {

		TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>> organizationMap = 
				new TreeMap<Organization, TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>>();
		TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>> crfMap;
		TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>> participantMap;
		TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap;
		ArrayList<Date> dates;
		ArrayList<String> appModes;
		ArrayList<CrfStatus> statusList;
		boolean participantAddedQuestion;
		for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
			Organization organization = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getOrganization();
			if (organizationMap.containsKey(organization)) {
				crfMap = organizationMap.get(organization);
			} else {
				crfMap = new TreeMap<CRF, TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>>();
				organizationMap.put(organization, crfMap);
			}

			CRF crf = studyParticipantCrfSchedule.getStudyParticipantCrf().getCrf();
			if (crfMap.containsKey(crf)) {
				participantMap = crfMap.get(crf);
			} else {
				participantMap = new TreeMap<Participant, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>>();
				crfMap.put(crf, participantMap);
			}
			
			Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();
			initializeParticipant(participant);
			if (participantMap.containsKey(participant)) {
				symptomMap = participantMap.get(participant);
			} else {
				symptomMap = new TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>>();
				participantMap.put(participant, symptomMap);
			}

			LinkedHashMap<Participant, ArrayList<Date>> datesMap;
			LinkedHashMap<Participant,ArrayList<String>> modeMap;
			LinkedHashMap<Participant,ArrayList<CrfStatus>> statusMap;
			if (crfDateMap.containsKey(crf)) {
				datesMap = crfDateMap.get(crf);
			} else {
				datesMap = new LinkedHashMap<Participant, ArrayList<Date>>();
				crfDateMap.put(crf, datesMap);
			}

			if (crfModeMap.containsKey(crf)) {
				modeMap = crfModeMap.get(crf);
			} else {
				modeMap = new LinkedHashMap<Participant, ArrayList<String>>();
				crfModeMap.put(crf, modeMap);
			}
			
			if(crfStatusMap.containsKey(crf)){
				statusMap = crfStatusMap.get(crf);
			} else {
				statusMap = new LinkedHashMap<Participant, ArrayList<CrfStatus>>();
				crfStatusMap.put(crf, statusMap);
			}

			if (datesMap.containsKey(participant)) {
				dates = datesMap.get(participant);
			} else {
				dates = new ArrayList<Date>();
				datesMap.put(participant, dates);
			}

			if (modeMap.containsKey(participant)) {
				appModes = modeMap.get(participant);
			} else {
				appModes = new ArrayList<String>();
				modeMap.put(participant, appModes);
			}
			
			if(statusMap.containsKey(participant)){
				statusList = statusMap.get(participant);
			} else {
				statusList = new ArrayList<CrfStatus>();
				statusMap.put(participant, statusList);
			}

			statusList.add(studyParticipantCrfSchedule.getStatus());
			AppMode appModeForSurvery;
			ArrayList<AppMode> tempModesList = new ArrayList<AppMode>();
			String allUsedModes = "";
			Date firstResponseDate = null;
			Date responseDate = null;
			if (studyParticipantCrfSchedule.getStudyParticipantCrfItems().size() > 0) {
				for(StudyParticipantCrfItem crfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()){
					if (crfItem.getResponseMode() != null) {
						if(!tempModesList.contains(crfItem.getResponseMode())){
							tempModesList.add(crfItem.getResponseMode());
						}
					}
					responseDate = crfItem.getReponseDate();
					if(responseDate != null){
						if(firstResponseDate != null){
							if(DateUtils.compareDate(firstResponseDate, responseDate) > 0){
								firstResponseDate = responseDate;
							}
						} else {
							firstResponseDate = responseDate;
						}
					}
				}
				if (tempModesList.size() != 0) {
					for(AppMode m : tempModesList){
						allUsedModes += ";" + m.toString();
					}
					appModes.add(allUsedModes.substring(1));
				} else {
					appModes.add(null);
				}
				
				if(firstResponseDate != null){
					dates.add(firstResponseDate);
				} else {
					dates.add(null);
				}
			} else {
				appModes.add(null);
				dates.add(null);
			}

			StudyParticipantCrfItem firstQuestion = new StudyParticipantCrfItem();
			ProCtcQuestion proCtcQuestion;

			ProCtcValidValue value;
			for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
				proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
				if (proCtcQuestion.getDisplayOrder() == 1) {
					firstQuestion = studyParticipantCrfItem;
				}
				String symptom = proCtcQuestion.getProCtcTerm().getProCtcTermVocab().getTermEnglish();
				value = studyParticipantCrfItem.getProCtcValidValue();
				participantAddedQuestion = false;
				buildMap(proCtcQuestion, symptom, value, symptomMap, firstQuestion, datesMap.get(participant).size() - 1, participantAddedQuestion, studyParticipantCrfSchedule.getStatus(), participant);
			}
			for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
				Question question = studyParticipantCrfScheduleAddedQuestion.getQuestion();
				String addedSymptom = question.getQuestionSymptom();
				ValidValue validValue;
				if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
					validValue = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
				} else {
					String prefix = "";
					validValue = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue();
					LowLevelTerm llt = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm();
					MeddraQuestion meddraQuestion = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion();
					TreeMap<ProCtcQuestionType, String> typeMap;
					if (meddraQuestionMapping.containsKey(llt)) {
						typeMap = meddraQuestionMapping.get(llt);
					} else {
						typeMap = new TreeMap<ProCtcQuestionType, String>();
						meddraQuestionMapping.put(llt, typeMap);
					}
					
					if(!typeMap.containsKey(meddraQuestion.getProCtcQuestionType())){
						typeMap.put(meddraQuestion.getProCtcQuestionType(), String.valueOf(col++));

						switch (meddraQuestion.getProCtcQuestionType()) {
						case FREQUENCY:
							prefix = FREQUENCY;					
							break;
							
						case SEVERITY:
							prefix = SEVERITY;					
							break;
							
						case INTERFERENCE:
							prefix = INTERFERENCE;					
							break;
							
						case PRESENT:
							prefix = PRESENT;					
							break;
						case AMOUNT:
							prefix = AMOUNT;					
							break;
					}
					String ctcTermEnglishTermText = prefix +"_"+ llt.getLowLevelTermVocab().getMeddraTermEnglish();
					if(ctcTermEnglishTermText.length() > 32)
						ctcTermEnglishTermText = ctcTermEnglishTermText.substring(0, 32);
						
						meddraTermHeaders.add(ctcTermEnglishTermText);
					}
				}

				value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
				String symptom = question.getQuestionSymptom();
				participantAddedQuestion = true;
				buildMap(question, symptom, validValue, symptomMap, null, datesMap.get(participant).size() - 1, participantAddedQuestion, studyParticipantCrfSchedule.getStatus(), participant);
			}
		}

		return organizationMap;
	}

	private void initializeParticipant(Participant participant) {
	        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
	            StudyOrganization studyOrganization = studyParticipantAssignment.getStudySite();
	            studyOrganization.getStudy();
	            studyOrganization.getOrganization();
	            studyParticipantAssignment.getParticipant();
	            studyParticipantAssignment.getStudyParticipantCrfs();
	            studyParticipantAssignment.getStudyParticipantClinicalStaffs();
	            for (StudyParticipantMode studyParticipantMode : studyParticipantAssignment.getStudyParticipantModes()) {
	                studyParticipantMode.getMode();
	            }
	            for (StudyMode studyMode : studyParticipantAssignment.getStudySite().getStudy().getStudyModes()) {
	                studyMode.getMode();
	            }
	            for (StudyParticipantReportingModeHistory studyParticipantReportingModeHistory : studyParticipantAssignment.getStudyParticipantReportingModeHistoryItems()) {
	                studyParticipantReportingModeHistory.getMode();
	            }
	        }
	    }
	
	private void initializeStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule){
		studyParticipantCrfSchedule.getStudyParticipantCrfItems().size();
		studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions().size();
		studyParticipantCrfSchedule.getStudyParticipantCrfScheduleNotification();
	}
	   
	private void buildMap(Question question, String symptom, ValidValue value, TreeMap<String, LinkedHashMap<Question, ArrayList<ValidValue>>> symptomMap,
			StudyParticipantCrfItem firstQuestion, Integer dateIndex, boolean participantAddedQuestion, CrfStatus crfStatus, Participant participant) {
		
		LinkedHashMap<Question, ArrayList<ValidValue>> careResults;
		ProCtcQuestion proQuestion = new ProCtcQuestion();
		MeddraQuestion meddraQuestion = new MeddraQuestion();
		if (question instanceof ProCtcQuestion) {
			proQuestion = (ProCtcQuestion) question;
		}
		boolean isMeddraQuestion = false;
		if (question instanceof MeddraQuestion) {
			isMeddraQuestion = true;
			meddraQuestion = (MeddraQuestion) question;
		}
		ArrayList<ValidValue> validValue;
		if (symptomMap.containsKey(symptom)) {
			careResults = symptomMap.get(symptom);
		} else {
			careResults = new LinkedHashMap<Question, ArrayList<ValidValue>>();
			symptomMap.put(symptom, careResults);
		}

		if (careResults.containsKey(question)) {
			validValue = careResults.get(question);
		} else {
			validValue = new ArrayList<ValidValue>();
			for (int j = 0; j <= dateIndex; j++) {
				if (validValue.size() < j) {
					if(isMeddraQuestion){
						MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.NotAsked.getDisplayName(), null, -MARK_NOT_ADMINISTERED);
						validValue.add(meddraValidValue);
					}else{
						ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, ResponseCode.NotAsked.getDisplayName(), MARK_NOT_ADMINISTERED, null);
						validValue.add(proCtcValidValue);
					}
				}
			}
			careResults.put(question, validValue);
		}
		
		/* survey status=SCHEDULED indicates survey not yet started (i.e for the current survey there are no responses captured yet)
		 * 1. Fill in the previous empty validvalues if any, for the current question as NotAsked (-2000)
		 * 2. Set the current response as FORCEDSKIP (-99)
		 */
		if(crfStatus.equals(CrfStatus.SCHEDULED)){
			if (dateIndex > validValue.size()) {
				for (int j = validValue.size(); j < dateIndex; j++) {
					if(isMeddraQuestion){
						MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.NotAsked.getDisplayName(), null, MARK_NOT_ADMINISTERED);
						validValue.add(meddraValidValue);
					}else{
						ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, ResponseCode.NotAsked.getDisplayName(), MARK_NOT_ADMINISTERED, null);
						validValue.add(proCtcValidValue);
					}
				}
			}
			if(isMeddraQuestion){
				MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.MANUALSKIP.getDisplayName(), null, MARK_MANUAL_SKIP);
				validValue.add(dateIndex, meddraValidValue);
			}else{
				Integer responseCode = MARK_MANUAL_SKIP;
				String validVal = ResponseCode.MANUALSKIP.getDisplayName();
				if(participant.getGender() != null && proQuestion.getProCtcTerm().getGender() != null){
            		 if(!participant.getGender().equalsIgnoreCase(proQuestion.getProCtcTerm().getGender()) && !GENDER_BOTH.equalsIgnoreCase(proQuestion.getProCtcTerm().getGender())){
            			 	responseCode = MARK_NOT_ADMINISTERED;
            			 	validVal = ResponseCode.NotAsked.getDisplayName();
            		 }
            	 }
				ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, validVal, responseCode, null);
				validValue.add(dateIndex, proCtcValidValue);
			}
			
		} 
		/* survey status=INPROGRESS or PASTDUE indicates a survey that can have a mix of captured validValues (participant responded with a validValue) and not answered empty validValues
		 * Hence for the questions for which responses are not captured do the following:
		 * 1. Fill in the previous empty validValues if any, for the current question as NotAsked (-2000)
		 * 2. Set the current response as FORCEDSKIP (-99)
		*/ 
		else if(value == null && (crfStatus.equals(CrfStatus.INPROGRESS) || crfStatus.equals(CrfStatus.PASTDUE)) || crfStatus.equals(CrfStatus.ONHOLD)){
			if (dateIndex > validValue.size()) {
				for (int j = validValue.size(); j < dateIndex; j++) {
					if(isMeddraQuestion){
						MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.NotAsked.getDisplayName(), null, MARK_NOT_ADMINISTERED);
						validValue.add(meddraValidValue);
					}else{
						ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, ResponseCode.NotAsked.getDisplayName(), MARK_NOT_ADMINISTERED, null);
						validValue.add(proCtcValidValue);
					}
				}
			}
			if(isMeddraQuestion){
				MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.MANUALSKIP.getDisplayName(), null, MARK_MANUAL_SKIP);
				validValue.add(dateIndex, meddraValidValue);
			}else{
				Integer responseCode = MARK_MANUAL_SKIP;
				String validVal = ResponseCode.MANUALSKIP.getDisplayName();
				if(participant.getGender() != null && proQuestion.getProCtcTerm().getGender() != null){
					if(!participant.getGender().equalsIgnoreCase(proQuestion.getProCtcTerm().getGender()) && !GENDER_BOTH.equalsIgnoreCase(proQuestion.getProCtcTerm().getGender())){
						responseCode = MARK_NOT_ADMINISTERED;
        			 	validVal = ResponseCode.NotAsked.getDisplayName();
					}
				}
				ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, validVal, responseCode, null);
				validValue.add(dateIndex, proCtcValidValue);
			}
		}
		/* survey status=COMPLETED or INPROGRESS or PASTDUE indicates a survey that can have a mix of captured validValues (participant responded with a validValue) and not answered empty validValues
		 * Hence, for the questions for which responses are captured and survey status is COMPLETED or INPROGRESS or PASTDUE do the following:
		 * 		1. Fill in the previous empty validValues if any, for the current question as NotAsked (-2000)
		 * 		2. Set the current response value with participant responded ValidValue
		 * The questions for which responses are not captured, necessarily correspond to the survey which is COMPLETED and it may be a participantAdded meddra or proCtc question
		 *  	1. Fill in the previous empty validValues if any, for the current question as NotAsked (-2000)
		 *  	2. Set the current response as FORCEDSKIP (-99) or MANUALSKIP (-55) (based on the logic to handle conditional question)
		 */
		
		else {
			if (value == null && !isMeddraQuestion) {
				ProCtcValidValue myProCtcValidValue = ReportResultsHelper.getValidValueResponseCode(proQuestion, firstQuestion);
				myProCtcValidValue.setDisplayOrder(0);
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, ResponseCode.NotAsked.getDisplayName(), MARK_NOT_ADMINISTERED, null);
						validValue.add(proCtcValidValue);
					}
				}

				if(participant.getGender() != null && proQuestion.getProCtcTerm().getGender() != null){
            		 if(!participant.getGender().equalsIgnoreCase(proQuestion.getProCtcTerm().getGender()) && !GENDER_BOTH.equalsIgnoreCase(proQuestion.getProCtcTerm().getGender())){
            			 	myProCtcValidValue.setResponseCode(MARK_NOT_ADMINISTERED);
            			 	myProCtcValidValue.setValue(ResponseCode.NotAsked.getDisplayName());
            		 }
            	 }
			
				validValue.add(dateIndex, myProCtcValidValue);
			} else if(value == null && isMeddraQuestion){
				MeddraValidValue meddraValidValue;
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						MeddraValidValue mValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.NotAsked.getDisplayName(), null, MARK_NOT_ADMINISTERED);
						validValue.add(mValidValue);
					}
				}
				meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.MANUALSKIP.getDisplayName(), null, MARK_MANUAL_SKIP);
				validValue.add(dateIndex, meddraValidValue);
			} else {
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						if(isMeddraQuestion){
							MeddraValidValue meddraValidValue = (MeddraValidValue) getAppropriateValidValue(true, ResponseCode.NotAsked.getDisplayName(), null, MARK_NOT_ADMINISTERED);
							validValue.add(meddraValidValue);
						}else{
							ProCtcValidValue proCtcValidValue = (ProCtcValidValue) getAppropriateValidValue(false, ResponseCode.NotAsked.getDisplayName(), MARK_NOT_ADMINISTERED, null);
							validValue.add(proCtcValidValue);
						}
					}
				}
				validValue.add(dateIndex, value);
			}
		}
	}
	
	private ValidValue getAppropriateValidValue(boolean isMeddraQuestion, String value, Integer responseCode, Integer displayOrder){
		ProCtcValidValue proCtcValidValue = null;
		MeddraValidValue meddraValidValue = null;
		
		if(isMeddraQuestion){
			meddraValidValue = new MeddraValidValue();
			meddraValidValue.setValue(value);
			meddraValidValue.setDisplayOrder(displayOrder);
			return meddraValidValue;
		} else {
			proCtcValidValue = new ProCtcValidValue();
			proCtcValidValue.setValue(value);
			proCtcValidValue.setResponseCode(responseCode);
			return proCtcValidValue;
		}
	}

	private StudyParticipantCrfScheduleQuery parseRequestParametersAndFormQuery(
			HttpServletRequest request) throws ParseException {
		
		StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
		String studyParam = request.getParameter("study");
		String crfIdParam = request.getParameter("crf");
		String studySite = request.getParameter("studySite");
		
		
		
		if(!StringUtils.isBlank(studyParam) && StringUtils.isBlank(crfIdParam)){
        	Integer studyId = Integer.parseInt(studyParam);
        	query.filterByStudy(studyId);
        	
        	/*List<CRF> crfList = ReportResultsHelper.getReducedCrfs(studyId);
        	List<Integer> crfIds = new ArrayList<Integer>(); 
        	for(CRF crf : crfList){
        		crfIds.add(crf.getId());
                while (crf.getParentCrf() != null) {
                    crfIds.add(crf.getParentCrf().getId());
                    crf = crf.getParentCrf();
                }
        	}
        	query.filterByCRFIds(crfIds);*/
        }
		
		if(!StringUtils.isBlank(crfIdParam)){
        	Integer crfId = Integer.parseInt(crfIdParam);
            CRF crf = genericRepository.findById(CRF.class, crfId);
            List<Integer> crfIds = new ArrayList();
            crfIds.add(crfId);
            while (crf.getParentCrf() != null) {
                crfIds.add(crf.getParentCrf().getId());
                crf = crf.getParentCrf();
            }
            query.filterByCRFIds(crfIds);
            request.getSession().setAttribute("crf", crf);
        }
		
		if (!StringUtils.isBlank(studySite)) {
	        Integer studySiteId = Integer.parseInt(studySite);
	        query.filterByStudySite(studySiteId);
		}

		/*List<CrfStatus> crfStatusList = new ArrayList<CrfStatus>();
		crfStatusList.add(CrfStatus.COMPLETED);
		crfStatusList.add(CrfStatus.INPROGRESS);
		crfStatusList.add(CrfStatus.SCHEDULED);
		crfStatusList.add(CrfStatus.PASTDUE);
		crfStatusList.add(CrfStatus.ONHOLD);
		query.filterByStatuses(crfStatusList);*/
		
		request.getSession().setAttribute("study",
				genericRepository.findById(Study.class, Integer.parseInt(studyParam)));
		return query;
	}

	private void startRow(StringBuilder table) {
		table.append("<tr>");
	}

	private void endRow(StringBuilder table) {
		table.append("</tr>");
	}

	private void addColumn(StringBuilder table, String text, int colSpan, String style) {
		if (text == null) {
			text = "";
		}
		String colSpanStr = "";
		if (colSpan > 1) {
			colSpanStr = "colspan=\"" + colSpan + "\"";
		}
		table.append("<td ").append(colSpanStr).append(" class=\"").append(style).append("\">").append(text).append("</td>");
	}

	@Required
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}
	@Required
	public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}
}
