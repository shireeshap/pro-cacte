package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProctcaeGradeMapping;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfGrades;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**ParticipantLevelCtcaeGradesReportResultsController class
 * @author AmeyS
 * This class handles requests for generating ACCRU AE Report.
 * It pulls up the ctcae grades stored in database and processes them further and builds final data map which includes
 * list of adverse events to be reported in ACCRU AE Report
 */
public class ParticipantLevelCtcaeGradesReportResultsController extends AbstractController {

	protected static final Log logger = LogFactory.getLog(ParticipantLevelCtcaeGradesReportResultsController.class);
    GenericRepository genericRepository;
    ProCtcTermRepository proCtcTermRepository;
    private static String GRADE_ZERO = "0";
    private static String START_DATE = "startDate";
    private static String END_DATE = "endDate";
    


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantLevelCtcaeGradesReportResults");
        StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request, modelAndView);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);

        // Load studyParticipantCrfGrades from db
        Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> participantGardeMap = loadStudyParticipantCrfGrades(list);
        // Generate a consolidated list of AE's, with each AE entry reflecting its evaluated ctcae grade along with its starting & ending date 
        Map<AeWrapper, List<ParticipantGradeWrapper>> consolidatedParticipantGardeMap = getConsolidatedParticipantGradeMap(participantGardeMap);
        // Filter the consolidated list and generate a new list consiting of AE's reported within the selected start and end date period
        Map<AeWrapper, List<ParticipantGradeWrapper>> filteredParticipantGardeMap = getFilteredGrades(request, consolidatedParticipantGardeMap);
        // Generate final list of AE to be reported in pdf report, sorted according to AE's start date (primary sort criteria) and ctcae term (secondary sort criteria) 
        List<AeReportEntryWrapper> result = getSortedAeEntriesForDisplay(filteredParticipantGardeMap);
        
        Map<String, String> dateRangeMap = new HashMap<String, String>();
        String visitRange = request.getParameter("visitRange");
        if ("dateRange".equals(visitRange)) {
        	String startDate = request.getParameter(START_DATE);
        	String endDate = request.getParameter(END_DATE);
        	dateRangeMap.put(START_DATE, startDate);
        	dateRangeMap.put(END_DATE, endDate);
        } else {
        	dateRangeMap = getReportDateRange(participantGardeMap);
        }
      
        modelAndView.addObject("adverseEventsWithGradesList", result);
        request.getSession().setAttribute("dateRangeMap", dateRangeMap);
        request.getSession().setAttribute("adverseEventsWithGradesList", result);
        return modelAndView;
    }
    
    List<AeReportEntryWrapper> getSortedAeEntriesForDisplay(Map<AeWrapper, List<ParticipantGradeWrapper>> filteredParticipantGardeMap){
    	List<AeReportEntryWrapper> adverseEventListForDisplay = new ArrayList<AeReportEntryWrapper>();
    	
    	for(AeWrapper symptom : filteredParticipantGardeMap.keySet()){
    		for(ParticipantGradeWrapper participantGradeWrapper : filteredParticipantGardeMap.get(symptom)){
    			AeReportEntryWrapper aeReportEntryWrapper = new AeReportEntryWrapper();
    			aeReportEntryWrapper.setId(symptom.getId());
    			aeReportEntryWrapper.setCtcaeTerm(symptom.getCtcaeTerm());
    			aeReportEntryWrapper.setProCtcTerm(participantGradeWrapper.getProCtcTerms());
    			aeReportEntryWrapper.setMeddraCode(symptom.getMeddraCode());
    			aeReportEntryWrapper.setStartDate(participantGradeWrapper.getStartDate());
    			aeReportEntryWrapper.setEndDate(participantGradeWrapper.getEndDate());
    			aeReportEntryWrapper.setGrade(participantGradeWrapper.getGrade());
    			adverseEventListForDisplay.add(aeReportEntryWrapper);
    		}
    	}
    	
    	Collections.sort(adverseEventListForDisplay, new MyConsolidatedAeSorter());
    	return adverseEventListForDisplay;
    }
    
    private Map<String, String> getReportDateRange(Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> participantGardeMap){
    	Date minDate = null;
    	Date maxDate = null;
    	Map<String, String> dateRangeMap = new HashMap<String, String>();
    	
    	if(!participantGardeMap.isEmpty()){
    		AeWrapper term = participantGardeMap.keySet().iterator().next();
    		maxDate = Collections.min(participantGardeMap.get(term).keySet());
    		minDate = Collections.max(participantGardeMap.get(term).keySet());
    		
    		for(AeWrapper symptom : participantGardeMap.keySet()){
    			for(Date date : participantGardeMap.get(symptom).keySet()){
    				if(DateUtils.compareDate(minDate, date) > 0){
    					minDate = date;
    				}
    				if(DateUtils.compareDate(date, maxDate) > 0){
    					maxDate = date;
    				}
    			}
    		}
    	}
    	dateRangeMap.put("startDate", DateUtils.format(minDate));
    	dateRangeMap.put("endDate", DateUtils.format(maxDate));    	
    	return dateRangeMap;
    }
    
    private Map<AeWrapper, List<ParticipantGradeWrapper>> getFilteredGrades(final HttpServletRequest request, Map<AeWrapper, List<ParticipantGradeWrapper>> consolidatedParticipantGardeMap){
    	Map<AeWrapper, List<ParticipantGradeWrapper>> filteredParticipantGardeMap = new HashMap<AeWrapper, List<ParticipantGradeWrapper>>();
    	String startDate = "";
        String endDate = "";
        String visitRange = request.getParameter("visitRange");
    	if ("dateRange".equals(visitRange)) {
            startDate = request.getParameter("startDate");
            endDate = request.getParameter("endDate");
			try {
				Date sDate = DateUtils.parseDate(startDate);
				Date eDate = DateUtils.parseDate(endDate);
            
	            for(AeWrapper symptom : consolidatedParticipantGardeMap.keySet()){
	            	List<ParticipantGradeWrapper> filteredGrades = new ArrayList<ParticipantGradeWrapper>();
	            	filteredParticipantGardeMap.put(symptom, filteredGrades);
	
	            	for(ParticipantGradeWrapper grade : consolidatedParticipantGardeMap.get(symptom)){
	            		if(grade.getGrade() != null && !GRADE_ZERO.equals(grade.getGrade())){
	            			if((DateUtils.compareDate(sDate, grade.getStartDate()) >= 0)){
	            				if(grade.getEndDate() == null || (grade.getEndDate() != null && (DateUtils.compareDate(grade.getEndDate(), sDate) > 0))){
	            					filteredGrades.add(grade);
	            				}
	            			} else if((DateUtils.compareDate(eDate, grade.getStartDate()) >= 0)){
	            				if((grade.getEndDate() == null) || (grade.getEndDate() != null && (DateUtils.compareDate(grade.getEndDate(), eDate) > 0))){
	            					filteredGrades.add(grade);
	            				}
	            			} else if((DateUtils.compareDate(grade.getStartDate(), sDate) > 0)){
	            				if((grade.getEndDate() == null) || (grade.getEndDate() != null && (DateUtils.compareDate(eDate, grade.getEndDate()) > 0))){
	            					filteredGrades.add(grade);
	            				}
	            			}
	            		}
	            	}
	            }
			} catch (ParseException e) {
				logger.error("Error in filtering participant grades within the selected start and end date " + e.getStackTrace());
			}
    	} else {
    		   for(AeWrapper symptom : consolidatedParticipantGardeMap.keySet()){
	            	List<ParticipantGradeWrapper> filteredGrades = new ArrayList<ParticipantGradeWrapper>();
	            	filteredParticipantGardeMap.put(symptom, filteredGrades);
	
	            	for(ParticipantGradeWrapper grade : consolidatedParticipantGardeMap.get(symptom)){
	            		if(grade.getGrade() != null && !GRADE_ZERO.equals(grade.getGrade())){
	            			filteredGrades.add(grade);
	            		}
	            	}
	            }
    	}
    	return filteredParticipantGardeMap;
    }
    
    private Map<AeWrapper, List<ParticipantGradeWrapper>> getConsolidatedParticipantGradeMap(Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> participantGardeMap){
    	Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> intermidiateParticipantGradeMap = new HashMap<AeWrapper, Map<Date, List<GradeValueWrapper>>>();
    	Map<Date, List<GradeValueWrapper>> intermidiateDateMap;
    	List<GradeValueWrapper> intermidiateGradesList;
    	Map<Date, GradeValueWrapper> dateMap;
    	Map<Date, List<GradeValueWrapper>> pDateMap;
    	Map<AeWrapper, List<ParticipantGradeWrapper>> consolidatedParticipantGradeMap = new HashMap<AeWrapper, List<ParticipantGradeWrapper>>();
    	
    	for(AeWrapper symptom : participantGardeMap.keySet()){
    		pDateMap = participantGardeMap.get(symptom);

    		intermidiateDateMap = new LinkedHashMap<Date, List<GradeValueWrapper>>();
    		intermidiateParticipantGradeMap.put(symptom, intermidiateDateMap);
    		String currentGrade = "-1";
    		GradeValueWrapper gradeValueWrapper = null;
    		for(Date date : pDateMap.keySet()){
    			intermidiateGradesList = new ArrayList<GradeValueWrapper>();
    			intermidiateDateMap.put(date, intermidiateGradesList);
    			
    			for(GradeValueWrapper gradeValueItem : pDateMap.get(date)){
    				String evaluatedGrade = gradeValueItem.getGrade();
    				if(!currentGrade.equals(evaluatedGrade)){
    					gradeValueWrapper = new GradeValueWrapper();
    					gradeValueWrapper.setGrade(evaluatedGrade);
    					gradeValueWrapper.setProCtcTerms(gradeValueItem.getProCtcTerms());
    					intermidiateGradesList.add(gradeValueWrapper);
    					currentGrade = evaluatedGrade;
    				} else {
    					if(gradeValueWrapper != null){
    						HashSet<String> proCtcTerms = new HashSet<String>();
    						proCtcTerms = gradeValueWrapper.getProCtcTerms();
    						proCtcTerms.addAll(gradeValueItem.getProCtcTerms());
    						gradeValueWrapper.setProCtcTerms(proCtcTerms);
    					}
    				}
    				
    			}
    		}
    	}
    	
    	//Process the intermidiateParticipantGradeMap to have just one grade per date per ctcaeTerm (max grade from among grades available on same date for a ctcaeTerm)
    	Map<AeWrapper, Map<Date, GradeValueWrapper>> processedGradeMap = getProcessedGradeMap(intermidiateParticipantGradeMap);
    	
    	for(AeWrapper symptom : processedGradeMap.keySet()){
    		dateMap = processedGradeMap.get(symptom);
    		List<ParticipantGradeWrapper> consolidatedGradeList = new ArrayList<ParticipantGradeWrapper>();
    		consolidatedParticipantGradeMap.put(symptom, consolidatedGradeList);
    		
    		List<Map.Entry<Date, GradeValueWrapper>> processedGradeList = new ArrayList<Map.Entry<Date, GradeValueWrapper>>();
    		processedGradeList.addAll(dateMap.entrySet());
    		for(int i = 0; i< processedGradeList.size(); i++){
    			Date date = processedGradeList.get(i).getKey();
    			ParticipantGradeWrapper grade = new ParticipantGradeWrapper();
    			grade.setStartDate(date);
    			grade.setGrade(processedGradeMap.get(symptom).get(date).getGrade());
    			grade.setProCtcTerms(getProTermSetAsString(processedGradeMap.get(symptom).get(date).getProCtcTerms()));
    			if((i+1) < processedGradeList.size()){
    				grade.setEndDate(processedGradeList.get(i+1).getKey());
    			} else {
    				grade.setEndDate(null);
    			}
    			consolidatedGradeList.add(grade);
    		}
    	}
    	
    	return consolidatedParticipantGradeMap;
    }
    
    /*For symptoms mapping to same ctcae term, if both these symptom are present in a survey and 
	 * have been answered on the same date, then the one with max grade should be retained.
	*/
    private Map<AeWrapper, Map<Date, GradeValueWrapper>> getProcessedGradeMap(Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> intermidiateParticipantGradeMap){
    	Map<Date, List<GradeValueWrapper>> intermidiateDateMap;
    	Map<AeWrapper, Map<Date, GradeValueWrapper>> processedGradeMap = new HashMap<AeWrapper, Map<Date,GradeValueWrapper>>();
    	Map<Date, GradeValueWrapper> processedDateMap;
    	GradeValueWrapper maxGradeValueWrapper = null;
    	Integer maxGarde;
    	
    	
    	
    	for(AeWrapper aeWrapper : intermidiateParticipantGradeMap.keySet()){
    		intermidiateDateMap = intermidiateParticipantGradeMap.get(aeWrapper);
    		processedDateMap = new HashMap<Date, GradeValueWrapper>();
    		processedGradeMap.put(aeWrapper, processedDateMap);
    		
    		maxGarde = -1;
    		for(Date date : intermidiateDateMap.keySet()){
    			for(GradeValueWrapper gradeValueWrapper : intermidiateDateMap.get(date)){
    				if(gradeValueWrapper.getGrade().indexOf(ProctcaeGradeMapping.PRESENT_CLINICIAN_ASSESS) == -1 &&
    						intermidiateDateMap.get(date).size() > 1 ){
    					if(Integer.parseInt(gradeValueWrapper.getGrade()) > maxGarde){
    						maxGarde = Integer.parseInt(gradeValueWrapper.getGrade());
    						maxGradeValueWrapper = gradeValueWrapper;
    					}
    				} else {
    					maxGradeValueWrapper = gradeValueWrapper;
    					break;
    				}
    			}
    			processedDateMap.put(date, maxGradeValueWrapper);
    		}
    	}
    	return processedGradeMap;
    }
    
    private String getProTermSetAsString(HashSet<String> proCtcTerms){
    	String proTermString ="";
    	Iterator<String> itr = proCtcTerms.iterator();
    	while(itr.hasNext()){
    		proTermString += itr.next() + ";";
    	}
    	
    	return proTermString.substring(0, proTermString.length() - 1);
    }
    
    private Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> loadStudyParticipantCrfGrades(List<StudyParticipantCrfSchedule> schedules){
    	Map<AeWrapper, Map<Date, List<GradeValueWrapper>>> participantGradeMap = new HashMap<AeWrapper, Map<Date, List<GradeValueWrapper>>>();
    	TreeMap<Date, List<GradeValueWrapper>> gradeMap;
    	
    	for(StudyParticipantCrfSchedule schedule : schedules){
    		for(StudyParticipantCrfGrades studyParticipantCrfGrade : schedule.getStudyParticipantCrfGrades()){
    			if(studyParticipantCrfGrade.getGrade() != null){
    				AeWrapper symptom = new AeWrapper();
    				String proCtcTerm;
    				if(studyParticipantCrfGrade.getProCtcTerm() != null){
    					symptom.setId("P_"+studyParticipantCrfGrade.getProCtcTerm().getCtcTerm().getId());
    					symptom.setCtcaeTerm(studyParticipantCrfGrade.getProCtcTerm().getCtcTerm().getTerm());
    					proCtcTerm = studyParticipantCrfGrade.getProCtcTerm().getTerm();
    					symptom.setMeddraCode(studyParticipantCrfGrade.getProCtcTerm().getCtcTerm().getCtepCode());
    				} else {
    					symptom.setId("P_" + studyParticipantCrfGrade.getLowLevelTerm().getId());
    					if(!studyParticipantCrfGrade.getLowLevelTerm().isParticipantAdded()){
    						symptom.setCtcaeTerm(studyParticipantCrfGrade.getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH));
    					}
    					proCtcTerm = studyParticipantCrfGrade.getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH);
    					symptom.setMeddraCode(studyParticipantCrfGrade.getLowLevelTerm().getMeddraCode());
    				}
    				
    				if(participantGradeMap.get(symptom) != null){
    					gradeMap = (TreeMap<Date, List<GradeValueWrapper>>) participantGradeMap.get(symptom);
    				} else {
    					gradeMap = new TreeMap<Date, List<GradeValueWrapper>>(new MyDisplaySorter());
    					participantGradeMap.put(symptom, gradeMap);
    				}
    				
    				List<GradeValueWrapper> gradeValueList;
    				if(gradeMap.get(studyParticipantCrfGrade.getGradeEvaluationDate()) == null){
    					gradeValueList = new ArrayList<GradeValueWrapper>();
    					gradeMap.put(studyParticipantCrfGrade.getGradeEvaluationDate(), gradeValueList);
    				} else {
    					gradeValueList = gradeMap.get(studyParticipantCrfGrade.getGradeEvaluationDate());
    				}
					GradeValueWrapper gradeValueWrapper = new GradeValueWrapper();
					gradeValueWrapper.setGrade(studyParticipantCrfGrade.getGrade());
					HashSet<String> proCtcTerms = new HashSet<String>();
					proCtcTerms.add(proCtcTerm);
					gradeValueWrapper.setProCtcTerms(proCtcTerms);
					gradeValueList.add(gradeValueWrapper);
    			}
    			
    		}
    	}
    	
    	return participantGradeMap;
    }

	private StudyParticipantCrfScheduleQuery parseRequestParametersAndFormQuery(HttpServletRequest request, ModelAndView modelAndView) throws ParseException {
        StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));

        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
        query.filterByStatus(CrfStatus.COMPLETED);

        Participant participant = genericRepository.findById(Participant.class, participantId);
        modelAndView.addObject("participant", participant);
        request.getSession().setAttribute("participant", participant);
        request.getSession().setAttribute("study", genericRepository.findById(Study.class, studyId));
        return query;
    }
    
    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    private class MyDisplaySorter implements Comparator {
		@Override
		public int compare(Object obj1, Object obj2) {
			Date d1 = (Date) obj1;
			Date d2 = (Date) obj2;
			return d1.compareTo(d2);
		}
    }
    
    private class MyConsolidatedAeSorter implements Comparator<AeReportEntryWrapper>{
		@Override
		public int compare(AeReportEntryWrapper adverseEvent1, AeReportEntryWrapper adverseEvent2) {
			// primary sort by Adverse Event's start date.
			int result = adverseEvent1.getStartDate().compareTo(adverseEvent2.getStartDate());
			// secondary sort by Adverse Event's Ctcae term, in the event of AE's start dates being equal.
			if(result == 0 & !StringUtils.isEmpty(adverseEvent1.getCtcaeTerm()) && !StringUtils.isEmpty(adverseEvent2.getCtcaeTerm())){
				result = adverseEvent1.getCtcaeTerm().compareToIgnoreCase(adverseEvent2.getCtcaeTerm());
			}
			return result;
		}
    }
}
