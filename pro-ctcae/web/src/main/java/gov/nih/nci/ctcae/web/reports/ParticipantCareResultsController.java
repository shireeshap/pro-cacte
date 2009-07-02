package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class ParticipantCareResultsController extends AbstractController {

    StudyRepository studyRepository;
    ParticipantRepository participantRepository;
    CRFRepository crfRepository;
    StudyOrganizationRepository studyOrganizationRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantCareResults");
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer studySiteId = Integer.parseInt(request.getParameter("studySiteId"));
        Integer crfId = Integer.parseInt(request.getParameter("crfId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        String forVisits = request.getParameter("forVisits");
        String visitRange = request.getParameter("visitRange");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        List<String> dates = new ArrayList<String>();
        List visitTitle = new ArrayList();

        if (visitRange.equals("currentPrev")) {
            visitTitle.add("Current");
            visitTitle.add("Previous");
        }
        if (visitRange.equals("currentLast")) {
            visitTitle.add("Current");
            visitTitle.add("First");
        }
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> results = getCareResults(visitRange, studyId, crfId, studySiteId, participantId, dates, Integer.valueOf(forVisits), startDate, endDate);
        modelAndView.addObject("resultsMap", results);
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("visitTitle", visitTitle);
        Participant participant = participantRepository.findById(participantId);
        modelAndView.addObject("participant", participant);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", dates);
        request.getSession().setAttribute("participant", participant);
        request.getSession().setAttribute("study", studyRepository.findById(studyId));
        request.getSession().setAttribute("crf", crfRepository.findById(crfId));
        request.getSession().setAttribute("studySite", studyOrganizationRepository.findById(studySiteId));

        return modelAndView;
    }

    private TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> getCareResults(String visitRange, Integer studyId, Integer crfId, Integer studySiteId, Integer participantId, List<String> dates, Integer forVisits, String startDate, String endDate) throws ParseException {

        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = new TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults = new HashMap();
        boolean participantAddedQuestion;

        Study study = studyRepository.findById(studyId);
        StudySite studySite = study.getStudySiteById(studySiteId);

        for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
            if (studyParticipantAssignment.getParticipant().getId().equals(participantId)) {

                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    if (studyParticipantCrf.getCrf().getId().equals(crfId)) {

                        List<StudyParticipantCrfSchedule> completedCrfs = new ArrayList<StudyParticipantCrfSchedule>();
                        if (visitRange.equals("currentLast")) {
                            completedCrfs.add(studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).get(0));
                            if (studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).size() > 1) {
                                completedCrfs.add(studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).get(studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).size() - 1));
                            }
                        } else {
                            if (forVisits == -1) {
                                forVisits = studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).size();
                            }
                            for (int i = 1; i <= forVisits; forVisits--)
                                if (studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).size() - forVisits >= 0) {
                                    completedCrfs.add(studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).get(studyParticipantCrf.getCrfsByStatus(CrfStatus.COMPLETED).size() - forVisits));
                                }
                        }

                        sortByStartDate(completedCrfs);
                        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : completedCrfs) {
                            if (visitRange.equals("dateRange")) {
                                if (!dateBetween(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate), studyParticipantCrfSchedule.getStartDate())) {
                                    continue;
                                }
                            }
                            if (studyParticipantCrfSchedule.getCycleNumber() != null) {
                            dates.add(DateUtils.format(studyParticipantCrfSchedule.getStartDate()) + " (C " + studyParticipantCrfSchedule.getCycleNumber() + ", D " + studyParticipantCrfSchedule.getCycleDay() + ")");
                            } else {
                                dates.add(DateUtils.format(studyParticipantCrfSchedule.getStartDate()) + " ");
                            }
                                Integer arraySize = dates.size();
                            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                                ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                                ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                                participantAddedQuestion = false;
                                buildMap(proCtcQuestion, symptom, value, symptomMap, careResults, participantAddedQuestion, arraySize);
                            }
                            for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions()) {
                                ProCtcQuestion proCtcQuestion = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue().getProCtcQuestion();
                                ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                                ProCtcValidValue value = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue();
                                participantAddedQuestion = true;
                                buildMap(proCtcQuestion, symptom, value, symptomMap, careResults, participantAddedQuestion, arraySize);
                            }
                        }
                    }
                }
            }
        }

        return symptomMap;
    }

    private void buildMap(ProCtcQuestion proCtcQuestion, ProCtcTerm symptom, ProCtcValidValue value, TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults, boolean participantAddedQuestion, Integer arraySize) {

        ArrayList<ProCtcValidValue> validValue;
        if (symptomMap.containsKey(symptom)) {
            careResults = symptomMap.get(symptom);
        } else {
            careResults = new HashMap();
            symptomMap.put(symptom, careResults);
        }

        if (careResults.containsKey(proCtcQuestion)) {
            validValue = careResults.get(proCtcQuestion);
        } else {
            if (participantAddedQuestion) {
                validValue = new ArrayList<ProCtcValidValue>();
                for (int i = 0; i < arraySize - 1; i++) {
                    ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
                    myProCtcValidValue.setProCtcQuestion(proCtcQuestion);
                    myProCtcValidValue.setDisplayOrder(0);
                    validValue.add(myProCtcValidValue);
                }
                careResults.put(proCtcQuestion, validValue);

            } else {
                validValue = new ArrayList<ProCtcValidValue>();
                careResults.put(proCtcQuestion, validValue);
            }

        }
        if (value == null) {
            ProCtcValidValue myProCtcValidValue = new ProCtcValidValue();
            myProCtcValidValue.setProCtcQuestion(proCtcQuestion);
            myProCtcValidValue.setDisplayOrder(0);
            validValue.add(myProCtcValidValue);
        } else {
            validValue.add(value);
        }
    }

    private void sortByStartDate(List<StudyParticipantCrfSchedule> completedCrfs) {
        TreeSet<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = new TreeSet<StudyParticipantCrfSchedule>(new StudyParticipantCrfScheduleStartDateComparator());
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : completedCrfs) {
            studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
        }

        completedCrfs.clear();
        Iterator<StudyParticipantCrfSchedule> i = studyParticipantCrfSchedules.iterator();
        while (i.hasNext()) {
            completedCrfs.add(i.next());
        }
    }

    private boolean dateBetween
            (Date
                    startDate, Date
                    endDate, Date
                    scheduleStartDate) {
        return (startDate.getTime() <= scheduleStartDate.getTime() && scheduleStartDate.getTime() <= endDate.getTime());
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}
