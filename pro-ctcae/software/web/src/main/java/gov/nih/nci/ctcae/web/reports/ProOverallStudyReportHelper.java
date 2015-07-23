package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.jdbc.support.AddedMeddraQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.AddedProCtcQuestionWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.CrfQuestionsTemplateWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ParticipantAndOganizationWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.ResponseWrapper;
import gov.nih.nci.ctcae.core.jdbc.support.SpcrfsWrapper;
import gov.nih.nci.ctcae.core.query.ProCtcTermQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author AmeyS
 * Helper for generating overall study report for ProCtcae surverys
 */
public class ProOverallStudyReportHelper{
	GenericRepository genericRepository;
	private Log logger;
	List<SpcrfsWrapper> schedules;
	Map<Integer, List<ResponseWrapper>> responses;
	Map<Integer, List<AddedProCtcQuestionWrapper>> addedProCtcQuestions;
	Map<Integer, List<AddedMeddraQuestionWrapper>> addedMeddraQuestions;
	Map<Integer,List<ParticipantAndOganizationWrapper>> participantsAndOrganizations;
	Map<Integer, Date> firstResponseDates;
	Map<Integer, HashSet<String>> responseModes;
	Map<String, List<CrfQuestionsTemplateWrapper>> crfQuestionsTemplate; 

	private static String FREQUENCY = "FRQ";
	private static String INTERFERENCE = "INT";
	private static String SEVERITY = "SEV";
	private static String PRESENT =	"PRES";
	private static String AMOUNT =	"AMT";
	private static Integer MARK_MANUAL_SKIP = -55;
	private static Integer MARK_FORCE_SKIP =-99;
	private static Integer MARK_NOT_ADMINISTERED = -2000;
	private static String GENDER_BOTH = "both";
	
	
	public ProOverallStudyReportHelper(){
		logger = LogFactory.getLog(getClass());
	}
	
	public int generateQuestionMappingForTableHeader(Map<String, Map<ProCtcQuestionType, String>> proCtcQuestionMapping,
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
	
	public Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> getCareResults(

			Map<String, Map<String, LinkedHashMap<String, List<Date>>>> crfDateMap,
			Map<String, LinkedHashMap<String, List<String>>> crfModeMap,
			Map<String, LinkedHashMap<String, List<CrfStatus>>> crfStatusMap,
			Map<String, String> participantInfoMap,
			Map<String, Map<ProCtcQuestionType, String>> meddraQuestionMapping,
			List<String> meddraTermHeaders,
			int col
	) throws ParseException {

		Map<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>> organizationMap = 
			new TreeMap<String, Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>>>(); 
		Map<String, Map<String, Map<String, LinkedHashMap<String, List<String>>>>> crfMap;
		Map<String, Map<String, LinkedHashMap<String, List<String>>>> participantMap;
		Map<String, LinkedHashMap<String, List<String>>> symptomMap;

		LinkedHashMap<String, List<Date>> datesToListByType;
		ArrayList<String> appModes;
		ArrayList<CrfStatus> statusList;
		boolean participantAddedQuestion;
		
		for(SpcrfsWrapper studyParticipantCrfSchedule : schedules){

			String organization = participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getOrganizationName();

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
			
			String participant = participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getParticipantId();
			if(participantInfoMap.get(participant) == null){
				participantInfoMap.put(participant, participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getStudyParticipantIdentifier());
			}
			if(participantMap.get(participant) != null){
				symptomMap = participantMap.get(participant);
			} else {
				symptomMap = new TreeMap<String, LinkedHashMap<String, List<String>>>();
				participantMap.put(participant, symptomMap);
			}
			
			Map<String, LinkedHashMap<String, List<Date>>> datesMap;
			LinkedHashMap<String,List<String>> modeMap;
			LinkedHashMap<String,List<CrfStatus>> statusMap;
			
			if(crfDateMap.get(crf) != null){
				datesMap = crfDateMap.get(crf);
			} else {
				datesMap = new LinkedHashMap<>();
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
				datesToListByType = datesMap.get(participant);
			} else {
				//init the lists
				datesToListByType = new LinkedHashMap<>();
				datesToListByType.put("firstResponseDates", new ArrayList<Date>());
				datesToListByType.put("scheduledStartDates", new ArrayList<Date>());
				datesToListByType.put("scheduledDueDates", new ArrayList<Date>());
				datesToListByType.put("scheduledCompletionDates", new ArrayList<Date>());
				datesMap.put(participant, datesToListByType);
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
			datesToListByType.get("firstResponseDates").add(firstResponseDates.get(studyParticipantCrfSchedule.getId()));
			datesToListByType.get("scheduledStartDates").add(studyParticipantCrfSchedule.getStartDate());
			datesToListByType.get("scheduledDueDates").add(studyParticipantCrfSchedule.getDueDate());
			datesToListByType.get("scheduledCompletionDates").add(studyParticipantCrfSchedule.getCompletionDate());


			String participantGender = participantsAndOrganizations.get(studyParticipantCrfSchedule.getId()).get(0).getGender();
			
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

							buildMap(questionType, symtom, value, symptomMap, responseForFirstQuestion, datesToListByType.get("firstResponseDates").size() - 1, participantAddedQuestion,
									studyParticipantCrfSchedule.getStatus(), participantGender, questionGender, isMeddraQuestion, isFirstQuestion);
						}catch(Exception e){
							logger.error("Error in populating responses: ", e);
						}
					}
				} else {
					defaultResponses(symptomMap, crfQuestionsTemplate.get(crf));
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
							
						buildMap(questionType, symtom, value, symptomMap, responseForFirstQuestion, datesToListByType.get("firstResponseDates").size() - 1, participantAddedQuestion,
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
						buildMap(questionType, symtom, value, symptomMap, null, datesToListByType.get("firstResponseDates").size() - 1, participantAddedQuestion,
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
	
	// Mark by default as MARK_MANUAL_SKIP (-55), for schedules for which studyParticipantCrfItems are not yet created in the db.
	private void defaultResponses(Map<String, LinkedHashMap<String, List<String>>> symptomMap, List<CrfQuestionsTemplateWrapper> studyParticipantCrfItems){
		for(CrfQuestionsTemplateWrapper studyParticipantCrfItem : studyParticipantCrfItems){
			String symptom = studyParticipantCrfItem.getTermEnglish();
			String questionType = studyParticipantCrfItem.getQuestionType().getDisplayName();
			String value = MARK_MANUAL_SKIP.toString();
			
			LinkedHashMap<String, List<String>> careResults;
			List<String> validValue;
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
				careResults.put(questionType, validValue);
			}
			validValue.add(value);
		}
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
	
	public void setSchedules(List<SpcrfsWrapper> proSchedules) {
		this.schedules = proSchedules;
	}

	public void setResponses(Map<Integer, List<ResponseWrapper>> proResoponses) {
		this.responses = proResoponses;
	}

	public void setAddedProCtcQuestions(
			Map<Integer, List<AddedProCtcQuestionWrapper>> proAddedProCtcQuestions) {
		this.addedProCtcQuestions = proAddedProCtcQuestions;
	}

	public void setAddedMeddraQuestions(
			Map<Integer, List<AddedMeddraQuestionWrapper>> proAddedMeddraQuestions) {
		this.addedMeddraQuestions = proAddedMeddraQuestions;
	}

	public void setParticipantsAndOrganizations(
			Map<Integer, List<ParticipantAndOganizationWrapper>> proParticipantsAndOrganizations) {
		this.participantsAndOrganizations = proParticipantsAndOrganizations;
	}

	public void setFirstResponseDates(Map<Integer, Date> proFirstResponseDates) {
		this.firstResponseDates = proFirstResponseDates;
	}

	public void setResponseModes(Map<Integer, HashSet<String>> proResponseModes) {
		this.responseModes = proResponseModes;
	}
	
	public void setCrfQuestionsTemplate(
			Map<String, List<CrfQuestionsTemplateWrapper>> crfQuestionsTemplate) {
		this.crfQuestionsTemplate = crfQuestionsTemplate;
	}
	
	@Required
	public void setGenericRepository(GenericRepository genericRepository) {
		this.genericRepository = genericRepository;
	}
}
