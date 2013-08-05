package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyOrganization;
import gov.nih.nci.ctcae.core.jdbc.StudyWideFormatReportDao;
import gov.nih.nci.ctcae.core.jdbc.support.AddedMeddraQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ParticipantAndOganizationWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ResponseWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.SpcrfsWrapper;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	private StudyWideFormatReportDao studyWideFormatReportData;
	private static Integer MARK_MANUAL_SKIP = -55;
	private static Integer MARK_FORCE_SKIP =-99;
	private static Integer MARK_NOT_ADMINISTERED = -2000;
	private static String FREQUENCY = "FRQ";
	private static String INTERFERENCE = "INT";
	private static String SEVERITY = "SEV";
	private static String PRESENT =	"PRES";
	private static String AMOUNT =	"AMT";
	private static String GENDER_BOTH = "both";
	List<SpcrfsWrapper> listSchedules;
	Map<Integer, List<ResponseWrapper>> listResoponses;
	Map<Integer, List<AddedProCtcQuestionWrapper>> listAddedProCtcQuestions;
	Map<Integer, List<AddedMeddraQuestionWrapper>> listAddedMeddraQuestions;
	Map<Integer,List<ParticipantAndOganizationWrapper>> listParticipantsAndOrganizations;
	Map<Integer, Date> listFirstResponseDates;
	Map<Integer, HashSet<String>> listResponseModes;
	
	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView("reports/fullStudyReport");
		
		try{
			parseRequestParametersAndFetchData(request);
			Map<String, String> participantInfoMap = new HashMap<String, String>();
			// Mapping a ProCtcQuestion to a column in Report.
			Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping = new TreeMap<String, Map<ProCtcQuestionType, String>>();
			// Mapping a MeddraQuestion to a column in Report.
			Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping = new TreeMap<String, Map<ProCtcQuestionType, String>>();
			// ProCtcQuestion List to be displayed in Report's table header (ordered according to proCtcQuestionMapping)
			List<String> proCtcTermHeaders = new ArrayList<String>();
			// MeddraQuestion List to be displayed in Report's table header (ordered according to meddraQuestionMapping)
			List<String> meddraTermHeaders = new ArrayList<String>();
			// Save the start dates of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<Date>>> crfDateMap = new TreeMap<String, LinkedHashMap<String, List<Date>>>();
			// Save the survey_answering_mode of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<String>>> crfModeMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
			// Save the survey status of the submitted surveys listed in 'list'
			Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap = new TreeMap<String, LinkedHashMap<String, List<CrfStatus>>>();
		  
			int col = generateQuestionMappingForTableHeader(proCtcQuestionMapping, proCtcTermHeaders);
			
			Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> overAllResults =
				getCareResults(listSchedules, listParticipantsAndOrganizations, crfDateMap, crfModeMap, crfStatusMap, listResoponses, 
						listAddedProCtcQuestions, listAddedMeddraQuestions, participantInfoMap, listFirstResponseDates, listResponseModes, meddraQuestionMapping, meddraTermHeaders, col);
	
			modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
			request.getSession().setAttribute("sessionResultsMap", overAllResults);
			request.getSession().setAttribute("sessionCRFDatesMap", crfDateMap);
			request.getSession().setAttribute("sessionCRFModeMap", crfModeMap);
			request.getSession().setAttribute("sessionCRFStatusMap", crfStatusMap);
			request.getSession().setAttribute("sessionProCtcQuestionMapping", proCtcQuestionMapping);
			request.getSession().setAttribute("sessionMeddraQuestionMapping", meddraQuestionMapping);
			request.getSession().setAttribute("sessionProCtcTermHeaders", proCtcTermHeaders);
			request.getSession().setAttribute("sessionMeddraTermHeaders", meddraTermHeaders);
			request.getSession().setAttribute("participantInfoMap", participantInfoMap);
		
		}catch (Exception e) {
			e.getStackTrace();
			logger.info("Debugging on error:" + e.getStackTrace());
		}
		return modelAndView;
	}
	
	private int generateQuestionMappingForTableHeader(Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping,
			List<String> proCtcTermHeaders) {
		
		int col = 0;
		String prefix = "";
		String proCtcTermEnglishTermText;
		Map<ProCtcQuestionType, String> typeMap;
		ProCtcTermQuery proCtcTermQuery = new ProCtcTermQuery();
		List<ProCtcTerm> proCtcTerms = genericRepository.find(proCtcTermQuery);
		for (ProCtcTerm proCtcTerm : proCtcTerms) {
			if (proCtcQuestionMapping.containsKey(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH))) {
				typeMap = proCtcQuestionMapping.get(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH));
			} else {
				typeMap = new TreeMap<ProCtcQuestionType, String>();
				proCtcQuestionMapping.put(proCtcTerm.getTermEnglish(SupportedLanguageEnum.ENGLISH), typeMap);
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

	private Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> getCareResults(
			List<SpcrfsWrapper> schedules, Map<Integer, List<ParticipantAndOganizationWrapper>> participants,
			Map<String, LinkedHashMap<String, List<Date>>> crfDateMap, Map<String, LinkedHashMap<String, List<String>>> crfModeMap,
			Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap, Map<Integer, List<ResponseWrapper>> responses,
			Map<Integer, List<AddedProCtcQuestionWrapper>> addedProCtcQuestions, Map<Integer, List<AddedMeddraQuestionWrapper>> addedMeddraQuestions,
			Map<String, String> participantInfoMap, Map<Integer, Date> firstResponseDates, Map<Integer, HashSet<String>> responseModes, Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping,
			List<String> meddraTermHeaders, int col) throws ParseException {

		Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> organizationMap = 
			new TreeMap<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>(); 
		Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap;
		Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap;
		Map<String, LinkedHashMap<String, List<String>>> symptomMap;
		
		ArrayList<Date> dates;
		ArrayList<String> appModes;
		ArrayList<CrfStatus> statusList;
		boolean participantAddedQuestion;
		
		for(SpcrfsWrapper studyParticipantCrfSchedule : schedules){
			String organization = participants.get(studyParticipantCrfSchedule.getId()).get(0).getOrganizationName();
			
			if(organizationMap.get(organization) != null){
				crfMap = organizationMap.get(organization);
			} else {
				crfMap = new TreeMap<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>();
				organizationMap.put(organization, crfMap);
			}
			
			String crf = studyParticipantCrfSchedule.getCrfTitle();
			if(crfMap.get(crf) != null){
				participantMap = crfMap.get(crf);
			} else {
				participantMap = new TreeMap<String, Map<String, LinkedHashMap<String, List<String>>>>();;
				crfMap.put(crf, participantMap);
			}
			
			String participant = participants.get(studyParticipantCrfSchedule.getId()).get(0).getParticipantId();
			if(participantInfoMap.get(participant) == null){
				participantInfoMap.put(participant, participants.get(studyParticipantCrfSchedule.getId()).get(0).getStudyParticipantIdentifier());
			}
			if(participantMap.get(participant) != null){
				symptomMap = participantMap.get(participant);
			} else {
				symptomMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
				participantMap.put(participant, symptomMap);
			}
			
			LinkedHashMap<String, List<Date>> datesMap;
			LinkedHashMap<String,List<String>> modeMap;
			LinkedHashMap<String,List<CrfStatus>> statusMap;
			
			if(crfDateMap.get(crf) != null){
				datesMap = crfDateMap.get(crf);
			} else {
				datesMap = new LinkedHashMap<String, List<Date>>();
				crfDateMap.put(crf, datesMap);
			}
			
			if (crfModeMap.containsKey(crf)) {
				modeMap = crfModeMap.get(crf);
			} else {
				modeMap = new LinkedHashMap<String, List<String>>();
				crfModeMap.put(crf, modeMap);
			}
			
			if(crfStatusMap.containsKey(crf)){
				statusMap = crfStatusMap.get(crf);
			} else {
				statusMap = new LinkedHashMap<String, List<CrfStatus>>();
				crfStatusMap.put(crf, statusMap);
			}
			
			if (datesMap.containsKey(participant)) {
				dates = (ArrayList<Date>) datesMap.get(participant);
			} else {
				dates = new ArrayList<Date>();
				datesMap.put(participant, dates);
			}

			if (modeMap.containsKey(participant)) {
				appModes = (ArrayList<String>) modeMap.get(participant);
			} else {
				appModes = new ArrayList<String>();
				modeMap.put(participant, appModes);
			}
			
			if(statusMap.containsKey(participant)){
				statusList = (ArrayList<CrfStatus>) statusMap.get(participant);
			} else {
				statusList = new ArrayList<CrfStatus>();
				statusMap.put(participant, statusList);
			}
			
			statusList.add(studyParticipantCrfSchedule.getStatus());
			String mode = null;
			if(responseModes.get(studyParticipantCrfSchedule.getId()) != null){
				HashSet<String> responseModeSet = (HashSet<String>) responseModes.get(studyParticipantCrfSchedule.getId());
				Iterator<String> itr = responseModeSet.iterator();
				mode = (itr.hasNext()? itr.next() : null);
				while(itr.hasNext()){
					mode += ", " + itr.next();
				}

			}
			appModes.add(mode);
			dates.add(firstResponseDates.get(studyParticipantCrfSchedule.getId()));
			
			
			String participantGender = participants.get(studyParticipantCrfSchedule.getId()).get(0).getGender();
			
			String responseForFirstQuestion = null;
				if(responses.get(studyParticipantCrfSchedule.getId()) != null){
					for(ResponseWrapper studyParticipantCrfItem : responses.get(studyParticipantCrfSchedule.getId())){
						try{
							boolean isFirstQuestion = false;
							if(!StringUtils.isEmpty(studyParticipantCrfItem.getQuestionPosition()) && Integer.parseInt(studyParticipantCrfItem.getQuestionPosition()) == 1){
								if(studyParticipantCrfItem.getResponseCode() != null){
									responseForFirstQuestion = studyParticipantCrfItem.getResponseCode();
								}
								isFirstQuestion = true;
							}
							
							String symtom = studyParticipantCrfItem.getTermEnglish();
							String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
							String value = studyParticipantCrfItem.getResponseCode();
							String questionGender = studyParticipantCrfItem.getGender();
							participantAddedQuestion = false;
							boolean isMeddraQuestion = false;
							
							buildMap(questionType, symtom, value, symptomMap, responseForFirstQuestion, datesMap.get(participant).size() - 1, participantAddedQuestion, 
									studyParticipantCrfSchedule.getStatus(), participantGender, questionGender , isMeddraQuestion, isFirstQuestion);
						}catch(Exception e){
							logger.debug(new String("Error in populating responses: " + e.getMessage()));
							e.printStackTrace();
						}
					}
				}
			
			responseForFirstQuestion = null;
			if(addedProCtcQuestions.get(studyParticipantCrfSchedule.getId()) != null){
				for(AddedProCtcQuestionWrapper studyParticipantCrfItem : addedProCtcQuestions.get(studyParticipantCrfSchedule.getId())){
					try{
						boolean isFirstQuestion = false;
						if(!StringUtils.isEmpty(studyParticipantCrfItem.getQuestionPosition()) && Integer.parseInt(studyParticipantCrfItem.getQuestionPosition()) == 1){
							if(studyParticipantCrfItem.getResponseCode() != null){
								responseForFirstQuestion = studyParticipantCrfItem.getResponseCode();
							}
							isFirstQuestion = true;
						}
						String symtom = studyParticipantCrfItem.getTermEnglish();
						String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
						String value = studyParticipantCrfItem.getResponseCode();
						String questionGender = studyParticipantCrfItem.getGender();
						participantAddedQuestion = true;
						boolean isMeddraQuestion = false;
							
						buildMap(questionType, symtom, value, symptomMap, responseForFirstQuestion, datesMap.get(participant).size() - 1, participantAddedQuestion, 
							studyParticipantCrfSchedule.getStatus(), participantGender, questionGender, isMeddraQuestion, isFirstQuestion);
					}catch(Exception e){
						e.getCause();
						logger.debug(new String("Error in populating added ProCtcae question: " + e.getMessage()));
						e.printStackTrace();
					}
				}
			}
			
			responseForFirstQuestion = null;
			if(addedMeddraQuestions.get(studyParticipantCrfSchedule.getId()) != null){
				for(AddedMeddraQuestionWrapper studyParticipantCrfItem : addedMeddraQuestions.get(studyParticipantCrfSchedule.getId())){
					try{
						String symtom = studyParticipantCrfItem.getTermEnglish();
						String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
						String value = studyParticipantCrfItem.getDisplayOrder();
						String questionGender = null;
						boolean isFirstQuestion = false;
						participantAddedQuestion = true;
						boolean isMeddraQuestion = false;
						
						String prefix = "";
						Map<ProCtcQuestionType, String> typeMap;
						String llt = studyParticipantCrfItem.getTermEnglish();
						if (meddraQuestionMapping.containsKey(llt)) {
							typeMap = meddraQuestionMapping.get(llt);
						} else {
							typeMap = new TreeMap<ProCtcQuestionType, String>();
							meddraQuestionMapping.put(llt, typeMap);
						}
						
						if(!typeMap.containsKey(studyParticipantCrfItem.getQuestionType())){
							typeMap.put(studyParticipantCrfItem.getQuestionType(), String.valueOf(col++));
							
							switch (studyParticipantCrfItem.getQuestionType()) {
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
							String ctcTermEnglishTermText = prefix +"_"+ studyParticipantCrfItem.getTermEnglish();
							if(ctcTermEnglishTermText.length() > 32)
								ctcTermEnglishTermText = ctcTermEnglishTermText.substring(0, 32);
							
							meddraTermHeaders.add(ctcTermEnglishTermText);
						}
						
							
						
						buildMap(questionType, symtom, value, symptomMap, null, datesMap.get(participant).size() - 1, participantAddedQuestion, 
								studyParticipantCrfSchedule.getStatus(), participantGender, questionGender, isMeddraQuestion, isFirstQuestion);
					}catch(Exception e){
						e.getCause();
						logger.debug(new String("Error in populating added meddra question: " + e.getMessage()));
						e.printStackTrace();
					}
				}
			}
			
			
		} 
		return organizationMap;
	}
	
	
	private void buildMap(String questionType, String symptom, String value, Map<String, LinkedHashMap<String, List<String>>> symptomMap,
			String responseForFirstQuestion, Integer dateIndex, boolean participantAddedQuestion, CrfStatus crfStatus, String participantGender, String questionGender,
			boolean isMeddraQuestion, boolean isFirstQuestion) {
		
		LinkedHashMap<String, List<String>> careResults;
		ArrayList<String> validValue;
		if (symptomMap.get(symptom) != null) {
			careResults = symptomMap.get(symptom);
		} else {
			careResults = new LinkedHashMap<String, List<String>>();
			symptomMap.put(symptom, careResults);
		}

		if (careResults.get(questionType) != null) {
			validValue = (ArrayList<String>) careResults.get(questionType);
		} else {
			validValue = new ArrayList<String>();
			for (int j = 0; j <= dateIndex; j++) {
				if (validValue.size() < j) {
					validValue.add(MARK_NOT_ADMINISTERED.toString());
				}
			}
			careResults.put(questionType, validValue);
		}
		
		/* survey status=SCHEDULED indicates survey not yet started (i.e for the current survey there are no responses captured yet)
		 * 1. Fill in the previous empty validvalues if any, for the current question, as NotAsked (-2000)
		 * 2. Set the current response as MANUALSKIP (-55)
		 */
		if(crfStatus.equals(CrfStatus.SCHEDULED)){
			if (dateIndex > validValue.size()) {
				for (int j = validValue.size(); j < dateIndex; j++) {
					validValue.add(MARK_NOT_ADMINISTERED.toString());
				}
			}
			if(isMeddraQuestion){
				validValue.add(MARK_MANUAL_SKIP.toString());
			} else {
				Integer responseCode = MARK_MANUAL_SKIP;
				if(participantGender != null && questionGender != null){
            		 if(!participantGender.equalsIgnoreCase(questionGender) && !GENDER_BOTH.equalsIgnoreCase(questionGender)){
            			 	responseCode = MARK_NOT_ADMINISTERED;
            		 }
            	 }
				validValue.add(dateIndex, responseCode.toString());
			}
			
		} 
		/* survey status=INPROGRESS or PASTDUE indicates a survey that can have a mix of captured validValues (participant responded with a validValue) and not answered empty validValues
		 * Hence for the questions for which responses are not captured do the following:
		 * 1. Fill in the previous empty validValues if any, for the current question as NotAsked (-2000)
		 * 2. Set the current response as MANUALSKIP (-55)
		*/ 
		else if(value == null && (crfStatus.equals(CrfStatus.INPROGRESS) || crfStatus.equals(CrfStatus.PASTDUE)) || crfStatus.equals(CrfStatus.ONHOLD)){
			if (dateIndex > validValue.size()) {
				for (int j = validValue.size(); j < dateIndex; j++) {
					validValue.add(MARK_NOT_ADMINISTERED.toString());
				}
			}
			if(isMeddraQuestion){
				validValue.add(dateIndex, MARK_MANUAL_SKIP.toString());
			}else{
				Integer responseCode = MARK_MANUAL_SKIP;
				if(participantGender != null && questionGender != null){
					if(!participantGender.equalsIgnoreCase(questionGender) && !GENDER_BOTH.equalsIgnoreCase(questionGender)){
						responseCode = MARK_NOT_ADMINISTERED;
					}
				}
				validValue.add(dateIndex, responseCode.toString());
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
				String responseCode = ReportResultsHelper.getValidValueResponseCode(value, responseForFirstQuestion, isFirstQuestion);
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						validValue.add(MARK_NOT_ADMINISTERED.toString());
					}
				}

				if(participantGender != null && questionGender != null){
            		 if(!participantGender.equalsIgnoreCase(questionGender) && !GENDER_BOTH.equalsIgnoreCase(questionGender)){
            			 responseCode = MARK_NOT_ADMINISTERED.toString();
            		 }
            	 }
				validValue.add(dateIndex, responseCode);

			} else if(value == null && isMeddraQuestion){
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						validValue.add(MARK_NOT_ADMINISTERED.toString());
					}
				}
				validValue.add(dateIndex, MARK_MANUAL_SKIP.toString());
			} else {
				if (dateIndex > validValue.size()) {
					for (int j = validValue.size(); j < dateIndex; j++) {
						validValue.add(MARK_NOT_ADMINISTERED.toString());
					}
				}
				validValue.add(dateIndex, value);
					
			}
		}
	}
	
	private void parseRequestParametersAndFetchData(HttpServletRequest request) throws ParseException {
		String studyParam = request.getParameter("study");
		String crfIdParam = request.getParameter("crf");
		String studySiteParam = request.getParameter("studySite");
		Integer studyId = null;
		List<Integer> crfIds = null;
		Integer studySiteId = null;
		
		if(!StringUtils.isBlank(studyParam)){
			studyId = Integer.parseInt(studyParam);
		}

		if(!StringUtils.isBlank(crfIdParam)){
			Integer crfId = Integer.parseInt(crfIdParam);
			CRF crf = genericRepository.findById(CRF.class, crfId);
			crfIds = new ArrayList<Integer>();
			crfIds.add(crfId);
			while (crf.getParentCrf() != null) {
				crfIds.add(crf.getParentCrf().getId());
				crf = crf.getParentCrf();
			}
			request.getSession().setAttribute("crf", crf);
			request.getSession().setAttribute("crfTitle", crf.getTitle());
		}
		
		
		if (!StringUtils.isBlank(studySiteParam)) {
			studySiteId = Integer.parseInt(studySiteParam);
			StudyOrganization studyOrganization = genericRepository.findById(StudyOrganization.class, studySiteId);
			request.getSession().setAttribute("organizationName", studyOrganization.getOrganization().getName());
		}
		
		listSchedules = studyWideFormatReportData.getSchedulesOnly(studyId, crfIds, studySiteId);
		listResoponses = studyWideFormatReportData.getResponsesOnly(studyId, crfIds, studySiteId);
		listAddedProCtcQuestions = studyWideFormatReportData.getAddedProQuestions(studyId, crfIds, studySiteId);
		listAddedMeddraQuestions = studyWideFormatReportData.getAddedMeddraQuestions(studyId, crfIds, studySiteId);
		listParticipantsAndOrganizations = studyWideFormatReportData.getParticipantsAndOrg(studyId, crfIds, studySiteId);
		listFirstResponseDates =  studyWideFormatReportData.getFirstResponseDate(studyId, crfIds, studySiteId);
		listResponseModes =  studyWideFormatReportData.getResponseModes(studyId, crfIds, studySiteId);
		
		request.getSession().setAttribute("study",
				genericRepository.findById(Study.class, Integer.parseInt(studyParam)));
	}

	@Required
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}
	@Required
	public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
		this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
	}
	
	 public StudyWideFormatReportDao getStudyWideFormatReportData() {
	        return studyWideFormatReportData;
	 }

	 public void setStudyWideFormatReportData(StudyWideFormatReportDao studyWideFormatReportData) {
	        this.studyWideFormatReportData = studyWideFormatReportData;
	 }
}
