package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.web.reports.graphical.ReportResultsHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class ParticipantLevelReportResultsController extends AbstractController {

    GenericRepository genericRepository;
    ProCtcTermRepository proCtcTermRepository;
    private static Integer RECORDS_PER_PAGE = 8;
    private static String PAGE = "page";


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantLevelReportResults");
        StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request, modelAndView);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);
        List<String> dates = new ArrayList<String>();


        List<StudyParticipantCrfSchedule> filteredSchedules = getFilteredSchedules(list, request);
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = getCareResults(dates, filteredSchedules, request);
        Map<String, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>> bundledResultMap = formatPageDataForDisplay(results, dates);
        Map<String, List<String>> bundledDates = formatSurvryDatesForDisplay(dates);

        modelAndView.addObject("resultsMap", results);
        modelAndView.addObject("bundledResultMap", bundledResultMap);
        modelAndView.addObject("bundledDates", bundledDates);
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());
        modelAndView.addObject("totalPageCount", dates.size());
        modelAndView.addObject("recordsPerPage", RECORDS_PER_PAGE);

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", dates);
        

        return modelAndView;
    }
    
    public Map<String, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>> formatPageDataForDisplay(TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results, List<String> dates){
    	Map<String, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>> bundledResultMap = new TreeMap<String, TreeMap<String[],HashMap<Question,ArrayList<ValidValue>>>>();
    	TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> pageResults = null;
    	HashMap<Question,ArrayList<ValidValue>> pageQuestions;
    	ArrayList<ValidValue> validValues = null;
    	
    	
    	int totalPageCount = getTotalPages(dates.size());
    	for(int i = 0; i<totalPageCount; i++){
    		pageResults = new TreeMap<String[], HashMap<Question,ArrayList<ValidValue>>>(new MyArraySorter());
    		bundledResultMap.put(getPage(i), pageResults);
    	}
    	
    	int page;
    	for(String[] symptom : results.keySet()){
    		for(Question question : results.get(symptom).keySet()){
    			page = -1;
    			int recordNumber = 0;
    			for(ValidValue validValue : results.get(symptom).get(question)){
    				if(isNewPage(recordNumber)){
    					page++;
    					pageResults = getPageResults(bundledResultMap, page);
    				}
    					if(pageResults != null){
    						
    						if(pageResults.get(symptom) == null){
    							pageQuestions = new HashMap<Question, ArrayList<ValidValue>>();
    							pageResults.put(symptom, pageQuestions);
    							
    						} else {
    							pageQuestions = pageResults.get(symptom);
    						}
    						
    						if(pageQuestions.get(question) == null){
								validValues= new ArrayList<ValidValue>();
    							pageQuestions.put(question, validValues);
							} else {
								validValues = pageQuestions.get(question); 
							}
    						
    						validValues.add(validValue);
    						recordNumber++;
    					}
    				
    			}
    		}
    	}
    	
    	
    	return bundledResultMap;
    	
    }
    
    public Map<String, List<String>> formatSurvryDatesForDisplay(List<String> dates){
    	Map<String, List<String>> bundledDates = new HashMap<String, List<String>>();
    	List<String> pageDates = null;
    	
    	int recordNumber = 0;
    	int page = -1;
    	for(String date : dates){
    		if(isNewPage(recordNumber)){
    			page++;
    			pageDates = new ArrayList<String>();
    			bundledDates.put(getPage(page), pageDates);
    		}
    		pageDates.add(date);
    		recordNumber++;
    	}
    	
    	return bundledDates;
    }
    
    private String getPage(int pageNumber){
    	return PAGE + (pageNumber + 1);
    }
    
    private boolean isNewPage(int recordNumber){
    	if(recordNumber % RECORDS_PER_PAGE == 0){
    		return true;
		}
    	return false;
    }
    
    private TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> getPageResults(Map<String, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>> bundledResultMap, int page){
    	return bundledResultMap.get(PAGE + (page + 1));
    }
    
    private int getTotalPages(int totalSurveyCount){
    	int totalPages = totalSurveyCount / RECORDS_PER_PAGE;
    	totalPages += (totalSurveyCount % RECORDS_PER_PAGE > 0 ? 1 : 0 );
    	return totalPages;
    }
    
    
    
    private TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> getCareResults(List<String> dates, List<StudyParticipantCrfSchedule> schedules, HttpServletRequest request) {

        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> symptomMap = new TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>(new MyArraySorter());
        HashMap<Question, ArrayList<ValidValue>> careResults = new HashMap<Question, ArrayList<ValidValue>>();
        boolean participantAddedQuestion;
        Integer dateIndex = 0;
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
            String displayDate = DateUtils.format(studyParticipantCrfSchedule.getStartDate());
            if (studyParticipantCrfSchedule.getCycleNumber() != null) {
                displayDate += " (C" + studyParticipantCrfSchedule.getCycleNumber() + ", D" + studyParticipantCrfSchedule.getCycleDay() + ")";
            }
            if (studyParticipantCrfSchedule.isBaseline()) {
                displayDate += "<br/>(Baseline)";
                request.getSession().setAttribute("baselineDate", DateUtils.format(studyParticipantCrfSchedule.getStartDate()));
            }
            dates.add(displayDate);

            Integer arraySize = dates.size();

            StudyParticipantCrfItem firstQuestion = new StudyParticipantCrfItem();
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                if (proCtcQuestion.getDisplayOrder() == 1) {
                    firstQuestion = studyParticipantCrfItem;
                }
                String symptomId = proCtcQuestion.getProCtcTerm().getId().toString();
                String symptom = proCtcQuestion.getProCtcTerm().getProCtcTermVocab().getTermEnglish();
                ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                participantAddedQuestion = false;
                buildMap(proCtcQuestion, new String[]{"P_" + symptomId, symptom}, value, symptomMap, careResults, participantAddedQuestion, arraySize, firstQuestion, dateIndex);
            }
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                Question question = studyParticipantCrfScheduleAddedQuestion.getQuestion();
                String symptomId = question.getStringId();
                String symptom = question.getQuestionSymptom();
                ValidValue value;
                if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                    value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                } else {
                    value = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue();
                }
                participantAddedQuestion = true;
                buildMap(question, new String[]{symptomId, symptom}, value, symptomMap, careResults, participantAddedQuestion, arraySize, null, dateIndex);
            }
            dateIndex++;
        }

        return symptomMap;
    }

    private void buildMap(Question question, String[] symptomArr, ValidValue value, TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> symptomMap, HashMap<Question, ArrayList<ValidValue>> careResults, boolean participantAddedQuestion, Integer arraySize, StudyParticipantCrfItem studyParticipantCrfItem, Integer dateIndex) {
        ProCtcQuestion proQuestion = new ProCtcQuestion();
        if (question instanceof ProCtcQuestion) {
            proQuestion = (ProCtcQuestion) question;
        }
        boolean isMeddraQuestion = false;
        if (question instanceof MeddraQuestion) {
            isMeddraQuestion = true;
        }
        ArrayList<ValidValue> validValue;
        if (symptomMap.containsKey(symptomArr)) {
            careResults = symptomMap.get(symptomArr);
        } else {
            careResults = new HashMap();
            symptomMap.put(symptomArr, careResults);
        }

        if (careResults.containsKey(question)) {
            validValue = careResults.get(question);
        } else {
            validValue = new ArrayList<ValidValue>();
            for (int j = 0; j <= dateIndex; j++) {
                if (validValue.size() < j) {
                    validValue.add(null);
                }
            }
            if (participantAddedQuestion) {
                for (int i = 0; i < arraySize - 1; i++) {
                    ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
                    myProCtcValidValue.setDisplayOrder(0);

                    validValue.add(dateIndex, myProCtcValidValue);
                }
                careResults.put(question, validValue);
            } else {
                careResults.put(question, validValue);
            }
        }
        if (value == null && !isMeddraQuestion) {
            ProCtcValidValue myProCtcValidValue = ReportResultsHelper.getValidValueResponseCode(proQuestion, studyParticipantCrfItem);
            myProCtcValidValue.setDisplayOrder(0);
            if (dateIndex > validValue.size()) {
                for (int j = validValue.size(); j < dateIndex; j++) {
                    validValue.add(null);
                }
            }
            validValue.add(dateIndex, myProCtcValidValue);
        } else {
            if (dateIndex > validValue.size()) {
                for (int j = validValue.size(); j < dateIndex; j++) {
                    validValue.add(null);
                }
            }
            validValue.add(dateIndex, value);
        }
    }


    private List<StudyParticipantCrfSchedule> getFilteredSchedules(List<StudyParticipantCrfSchedule> list, HttpServletRequest request) {
        ArrayList<StudyParticipantCrfSchedule> filtered = new ArrayList<StudyParticipantCrfSchedule>();
        String visitRange = request.getParameter("visitRange");
        if ("dateRange".equals(visitRange) || "all".equals(visitRange)) {
            filtered.addAll(list);
        } else {
            if ("currentPrev".equals(visitRange)) {
                if (list.size() > 0) {
                    filtered.add(list.get(list.size() - 1));
                    if (list.size() > 1) {
                        filtered.add(list.get(list.size() - 2));
                    }
                }
            } else {
                if ("currentLast".equals(visitRange)) {
                    if (list.size() > 0) {
                        filtered.add(list.get(0));
                        if (list.size() > 1) {
                            filtered.add(list.get(list.size() - 1));
                        }
                    }
                } else {
                    int numberOfRecentVisits;
                    if ("lastFour".equals(visitRange)) {
                        numberOfRecentVisits = 4;
                    } else {
                        numberOfRecentVisits = Integer.parseInt(request.getParameter("forVisits"));
                    }
                    for (int i = list.size() - numberOfRecentVisits; i < list.size(); i++)
                        if (i >= 0) {
                            filtered.add(list.get(i));
                        }
                }
            }
        }
        return filtered;
    }

    private StudyParticipantCrfScheduleQuery parseRequestParametersAndFormQuery(HttpServletRequest request, ModelAndView modelAndView) throws ParseException {
        StudyParticipantCrfScheduleQuery query = new StudyParticipantCrfScheduleQuery();
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        String studyParam = request.getParameter("studySiteId");
        Integer studySiteId = StringUtils.isNotBlank(studyParam) && StringUtils.isNumeric(studyParam) ? Integer.parseInt(studyParam): null;
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        String visitRange = request.getParameter("visitRange");

        if(!StringUtils.isEmpty(request.getParameter("crfId"))){
        	Integer crfId = Integer.parseInt(request.getParameter("crfId"));
        	CRF crf = genericRepository.findById(CRF.class, crfId);
        	List<Integer> crfIds = new ArrayList();
        	crfIds.add(crfId);
        	while (crf.getParentCrf() != null) {
        		crfIds.add(crf.getParentCrf().getId());
        		crf = crf.getParentCrf();
        	}
        	query.filterByCRFIds(crfIds);
        	request.getSession().setAttribute("crf", crf);
        } else if(!StringUtils.isEmpty(request.getParameter("studyId"))){
        	List<CRF> crfList = ReportResultsHelper.getReducedCrfs(studyId);
        	List<Integer> crfIds = new ArrayList<Integer>(); 
        	for(CRF crf : crfList){
        		crfIds.add(crf.getId());
                while (crf.getParentCrf() != null) {
                    crfIds.add(crf.getParentCrf().getId());
                    crf = crf.getParentCrf();
                }
        	}
        	query.filterByCRFIds(crfIds);
        }
        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
        if(studySiteId != null) {
            query.filterByStudySite(studySiteId);
        }
        if ("dateRange".equals(visitRange)) {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            query.filterByDate(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate));
        }
        query.filterByStatus(CrfStatus.COMPLETED);

        Participant participant = genericRepository.findById(Participant.class, participantId);
        modelAndView.addObject("participant", participant);
        request.getSession().setAttribute("participant", participant);
        request.getSession().setAttribute("study", genericRepository.findById(Study.class, studyId));

        request.getSession().setAttribute("studySite", genericRepository.findById(StudySite.class, studySiteId));

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

    private class MyArraySorter implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 != null & o2 != null) {
                String[] o1Arr = (String[]) o1;
                String[] o2Arr = (String[]) o2;
                return o1Arr[1].compareTo(o2Arr[1]);
            }
            return 0;
        }
    }
}
