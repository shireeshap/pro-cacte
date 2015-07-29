package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;
import gov.nih.nci.ctcae.core.domain.MeddraValidValue;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestion;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.ProCtcValidValue;
import gov.nih.nci.ctcae.core.domain.Question;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfItem;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfScheduleAddedQuestion;
import gov.nih.nci.ctcae.core.domain.ValidValue;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by IntelliJ IDEA.
 * User: Gaurav Gupta
 * Date: 11/21/11
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantLevelAccruTDMOneReportResultsController extends AbstractController {

    GenericRepository genericRepository;
    ProCtcTermRepository proCtcTermRepository;


    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantLevelAccruTDMOneReportResults");
        StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request, modelAndView);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);

        List<StudyParticipantCrfSchedule> filteredSchedules = getFilteredSchedules(list, request);
        setReportStartDateEndDate(request, filteredSchedules);
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> results = getCareResults(null, filteredSchedules, request);
        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> dResults = new TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>(new MyDisplaySorter());
        dResults = results;
        modelAndView.addObject("resultsMap", dResults);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypesForSharedAEReport());

        request.getSession().setAttribute("sessionResultsMap", dResults);
        request.getSession().setAttribute("questionTypes", ProCtcQuestionType.getAllDisplayTypesForSharedAEReport());
        return modelAndView;
    }

    private void setReportStartDateEndDate(final HttpServletRequest request,  List<StudyParticipantCrfSchedule> schedules){
        String visitRange = request.getParameter("visitRange");
        String startDate = "";
        String endDate = "";
        if ("dateRange".equals(visitRange)) {
            startDate = request.getParameter("startDate");
            endDate = request.getParameter("endDate");
        }else {
            List<Date> dates = new ArrayList<Date>();
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
                dates.add(studyParticipantCrfSchedule.getStartDate());
            }
            Collections.sort(dates);
            if(dates.size()>0){
                startDate = DateUtils.format(dates.get(0));
            }
        }
        request.getSession().setAttribute("reportStartDate",startDate);
        request.getSession().setAttribute("reportEndDate", endDate);
    }


    private TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> getCareResults(List<String> dates, List<StudyParticipantCrfSchedule> schedules, HttpServletRequest request) {

        TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> symptomMap = new TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>>(new MyArraySorter());
        populateSymptomMap(symptomMap);
        HashMap<Question, ArrayList<ValidValue>> careResults = new HashMap<Question, ArrayList<ValidValue>>();

        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedules) {
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();

                String symptomId = proCtcQuestion.getProCtcTerm().getId().toString();
                String symptom = proCtcQuestion.getProCtcTerm().getCtcTerm().getCtcTermVocab().getTermEnglish();
                ValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                if (value != null) {
                    buildMap(proCtcQuestion,
                            new String[]{"P_" + symptomId, symptom,""},
                            value,
                            symptomMap,
                            careResults,
                            false,
                            null, null);
                }
            }
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                Question question = studyParticipantCrfScheduleAddedQuestion.getQuestion();
                String symptomId = question.getStringId();
                String symptom;
                if(question instanceof MeddraQuestion){
                	symptom = ((MeddraQuestion)question).getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH);
                } else if(question instanceof ProCtcQuestion){
                	//ctcTerm same as meddraTerm
                	symptom = ((ProCtcQuestion)question).getProCtcTerm().getCtcTerm().getCtcTermVocab().getTermEnglish();
                	//proTerm
                	//symptom = ((ProCtcQuestion)question).getProCtcTerm().getTerm();
                } else {
                	symptom = question.getQuestionSymptom();
                }
                
                ValidValue value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                if(value == null){
                	value = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue();
                }
                if (value != null) {
                    buildMap(question,
                            new String[]{"A_" + symptomId, symptom,""},
                            value,
                            symptomMap,
                            careResults,
                            true,
                            null,
                            null);
                }
            }
        }

        return symptomMap;
    }


    private void populateSymptomMap(
			TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> symptomMap) {

    	HashMap<Question, ArrayList<ValidValue>> careResults;
    	String[] keyArray = new String[3];
    	for(int i = 0;i< ParticipantLevelAccruTDMOneReportPdfView.aeNameArray.length;i++){
    		keyArray = new String[3];
    		careResults = new HashMap<Question, ArrayList<ValidValue>>();
    		
    		keyArray[0] = "P_"+i;
    		keyArray[1] = ParticipantLevelAccruTDMOneReportPdfView.aeNameArray[i];
    		keyArray[2] = ParticipantLevelAccruTDMOneReportPdfView.aeMeddraCode[i];
    		
        	symptomMap.put(keyArray, careResults);
    	}
	}

	private void buildMap(Question question,
                          String[] symptomArr,
                          ValidValue value,
                          TreeMap<String[], HashMap<Question, ArrayList<ValidValue>>> symptomMap,
                          HashMap<Question, ArrayList<ValidValue>> careResults,
                          boolean participantAddedQuestion,
                          Integer arraySize,
                          StudyParticipantCrfItem studyParticipantCrfItem) {
        String meddraCode = " ";
        if (question instanceof ProCtcQuestion) {
            ProCtcQuestion proQuestion = (ProCtcQuestion) question;
            meddraCode = proQuestion.getProCtcTerm().getCtcTerm().getCtepCode();
        }
        if (question instanceof MeddraQuestion) {
            MeddraQuestion meddraQuestion = (MeddraQuestion) question;
            if(meddraQuestion.getLowLevelTerm().getId() > 10000000){
            	meddraCode = "N/A";
            } else {
            	meddraCode = meddraQuestion.getLowLevelTerm().getMeddraCode();
            }
        }
        symptomArr[2] = meddraCode;
        ArrayList<ValidValue> validValue;
        
        if (symptomMap.containsKey(symptomArr)) {
            careResults = symptomMap.get(symptomArr);
            
            if (careResults.containsKey(question)) {
                validValue = careResults.get(question);
            } else {
                validValue = new ArrayList<ValidValue>();
                careResults.put(question, validValue);
            }

            ProCtcValidValue pValue;
            MeddraValidValue mValue;
            if(value instanceof ProCtcValidValue){
            	pValue = (ProCtcValidValue)value;
            	if (validValue.size() == 0 && pValue.getResponseCode() != null)
                    validValue.add(value);
                else if (validValue.size() > 0 && (((ProCtcValidValue)validValue.get(0)).getResponseCode() < pValue.getResponseCode())) {
                    validValue.set(0, value);
                }
            } else if(value instanceof MeddraValidValue){
            	mValue = (MeddraValidValue) value;
            	if (validValue.size() == 0 && mValue.getDisplayOrder() != null)
                    validValue.add(value);
                else if (validValue.size() > 0 && (((MeddraValidValue)validValue.get(0)).getDisplayOrder() < mValue.getDisplayOrder())) {
                    validValue.set(0, value);
                }
            }
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
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        String visitRange = request.getParameter("visitRange");

        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
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
    
    //using a seperate sorter for display...but need to update the elements at [0] for desired display
    private class MyDisplaySorter implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 != null & o2 != null) {
                String[] o1Arr = (String[]) o1;
                String[] o2Arr = (String[]) o2;
                return o1Arr[0].compareTo(o2Arr[0]);
            }
            return 0;
        }
    }
}
