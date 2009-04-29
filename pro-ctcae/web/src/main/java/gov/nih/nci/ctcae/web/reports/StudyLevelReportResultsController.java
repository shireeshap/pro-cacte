package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyOrganizationRepository;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
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
public class StudyLevelReportResultsController extends AbstractController {

    StudyRepository studyRepository;
    CRFRepository crfRepository;
    StudyOrganizationRepository studyOrganizationRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/studyLevelReportResults");
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer studySiteId = Integer.parseInt(request.getParameter("studySiteId"));
        Integer crfId = Integer.parseInt(request.getParameter("crfId"));
        String forVisits = request.getParameter("forVisits");
        String visitRange = request.getParameter("visitRange");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        LinkedHashMap<Participant, ArrayList<Date>> datesMap = new LinkedHashMap<Participant, ArrayList<Date>>();
        List visitTitle = new ArrayList();

        if (visitRange.equals("currentPrev")) {
            visitTitle.add("Current");
            visitTitle.add("Previous");
        }
        if (visitRange.equals("currentLast")) {
            visitTitle.add("Current");
            visitTitle.add("First");
        }
        TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> results = getCareResults(visitRange, studyId, crfId, studySiteId, datesMap, Integer.valueOf(forVisits), startDate, endDate);
        modelAndView.addObject("resultsMap", results);
        modelAndView.addObject("datesMap", datesMap);
        modelAndView.addObject("visitTitle", visitTitle);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());

        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDatesMap", datesMap);
        request.getSession().setAttribute("study", studyRepository.findById(studyId));
        request.getSession().setAttribute("crf", crfRepository.findById(crfId));
        request.getSession().setAttribute("studySite", studyOrganizationRepository.findById(studySiteId));

        return modelAndView;
    }

    private TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> getCareResults(String visitRange, Integer studyId, Integer crfId, Integer studySiteId, LinkedHashMap<Participant, ArrayList<Date>> datesMap, Integer forVisits, String startDate, String endDate) throws ParseException {

        TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> participantMap = new TreeMap<Participant, TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>>(new ParticipantNameComparator());
        TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap;
        LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults;
        ArrayList<Date> dates;
        Study study = studyRepository.findById(studyId);
        StudySite studySite = study.getStudySiteById(studySiteId);

        for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
            int tempVisits = forVisits;
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                if (studyParticipantCrf.getCrf().getId().equals(crfId)) {
                    List<StudyParticipantCrfSchedule> completedCrfs = new ArrayList<StudyParticipantCrfSchedule>();
                    if (visitRange.equals("currentLast")) {
                        completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(0));
                        if (studyParticipantCrf.getCompletedCrfs().size() > 1) {
                            completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(studyParticipantCrf.getCompletedCrfs().size() - 1));
                        }
                    } else {
                        if (tempVisits == -1) {
                            tempVisits = studyParticipantCrf.getCompletedCrfs().size();
                        }
                        for (int i = 1; i <= tempVisits; tempVisits--)
                            if (studyParticipantCrf.getCompletedCrfs().size() - tempVisits >= 0) {
                                completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(studyParticipantCrf.getCompletedCrfs().size() - tempVisits));
                            }
                    }

                    sortByStartDate(completedCrfs);
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : completedCrfs) {
                        if (visitRange.equals("dateRange")) {
                            if (!dateBetween(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate), studyParticipantCrfSchedule.getStartDate())) {
                                continue;
                            }
                        }


                        Participant participant = studyParticipantAssignment.getParticipant();
                        if (participantMap.containsKey(participant)) {
                            symptomMap = participantMap.get(participant);
                        } else {
                            symptomMap = new TreeMap<ProCtcTerm, LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
                            participantMap.put(participant, symptomMap);
                        }
                        if (datesMap.containsKey(participant)) {
                            dates = datesMap.get(participant);
                        } else {
                            dates = new ArrayList<Date>();
                            datesMap.put(participant, dates);
                        }
                        dates.add(studyParticipantCrfSchedule.getStartDate());
                        for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                            ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                            ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                            ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                            ArrayList<ProCtcValidValue> validValue;

                            if (symptomMap.containsKey(symptom)) {
                                careResults = symptomMap.get(symptom);
                            } else {
                                careResults = new LinkedHashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>();
                                symptomMap.put(symptom, careResults);
                            }

                            if (careResults.containsKey(proCtcQuestion)) {
                                validValue = careResults.get(proCtcQuestion);
                            } else {
                                validValue = new ArrayList<ProCtcValidValue>();
                                careResults.put(proCtcQuestion, validValue);
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
                    }
                }
            }
        }

        return participantMap;
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

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setStudyOrganizationRepository(StudyOrganizationRepository studyOrganizationRepository) {
        this.studyOrganizationRepository = studyOrganizationRepository;
    }
}
