package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class ParticipantLevelReportResultsController extends AbstractController {

    GenericRepository genericRepository;
    ProCtcTermRepository proCtcTermRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantLevelReportResults");
        StudyParticipantCrfScheduleQuery query = parseRequestParametersAndFormQuery(request, modelAndView);
        List<StudyParticipantCrfSchedule> list = genericRepository.find(query);
        List<String> dates = new ArrayList<String>();

        List<StudyParticipantCrfSchedule> filteredSchedules = getFilteredSchedules(list, request);
        TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> results = getCareResults(dates, filteredSchedules, request);


        modelAndView.addObject("resultsMap", results);
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", dates);

        return modelAndView;
    }


    private TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> getCareResults(List<String> dates, List<StudyParticipantCrfSchedule> schedules, HttpServletRequest request) {

        TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> symptomMap = new TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>>(new MyArraySorter());
        HashMap<Question, ArrayList<ProCtcValidValue>> careResults = new HashMap<Question, ArrayList<ProCtcValidValue>>();
        boolean participantAddedQuestion;

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
            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                String symptomId = proCtcQuestion.getProCtcTerm().getId().toString();
                String symptom = proCtcQuestion.getProCtcTerm().getTerm();
                ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                participantAddedQuestion = false;
                if (value != null){
                buildMap(proCtcQuestion, new String[]{"P_" + symptomId, symptom}, value, symptomMap, careResults, participantAddedQuestion, arraySize);
                }
            }
            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                Question question = studyParticipantCrfScheduleAddedQuestion.getQuestion();
                String symptomId = question.getStringId();
                String symptom = question.getQuestionSymptom();
                ProCtcValidValue value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                participantAddedQuestion = true;
                buildMap(question, new String[]{symptomId, symptom}, value, symptomMap, careResults, participantAddedQuestion, arraySize);
            }
        }

        return symptomMap;
    }

    private void buildMap(Question question, String[] symptomArr, ProCtcValidValue value, TreeMap<String[], HashMap<Question, ArrayList<ProCtcValidValue>>> symptomMap, HashMap<Question, ArrayList<ProCtcValidValue>> careResults, boolean participantAddedQuestion, Integer arraySize) {

        ArrayList<ProCtcValidValue> validValue;
        if (symptomMap.containsKey(symptomArr)) {
            careResults = symptomMap.get(symptomArr);
        } else {
            careResults = new HashMap();
            symptomMap.put(symptomArr, careResults);
        }

        if (careResults.containsKey(question)) {
            validValue = careResults.get(question);
        } else {
            if (participantAddedQuestion) {
                validValue = new ArrayList<ProCtcValidValue>();
                for (int i = 0; i < arraySize - 1; i++) {
                    ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
                    myProCtcValidValue.setDisplayOrder(0);
                    validValue.add(myProCtcValidValue);
                }
                careResults.put(question, validValue);

            } else {
                validValue = new ArrayList<ProCtcValidValue>();
                careResults.put(question, validValue);
            }

        }
        if (value == null) {
            ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
            myProCtcValidValue.setDisplayOrder(0);
            validValue.add(myProCtcValidValue);
        } else {
            validValue.add(value);
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
        Integer studySiteId = Integer.parseInt(request.getParameter("studySiteId"));
        Integer crfId = Integer.parseInt(request.getParameter("crfId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        String visitRange = request.getParameter("visitRange");

        query.filterByCrf(crfId);
        query.filterByStudy(studyId);
        query.filterByParticipant(participantId);
        query.filterByStudySite(studySiteId);
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
        request.getSession().setAttribute("crf", genericRepository.findById(CRF.class, crfId));
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
