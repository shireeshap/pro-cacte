package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.repository.ParticipantRepository;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class ParticipantCareResultsController extends AbstractController {

    StudyRepository studyRepository;
    ParticipantRepository participantRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantCareResults");
        String studyId = request.getParameter("studyId");
        String studySiteId = request.getParameter("studySiteId");
        String crfId = request.getParameter("crfId");
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));
        String forVisits = request.getParameter("forVisits");
        String visitRange = request.getParameter("visitRange");
        List dates = new ArrayList<Date>();
        List visitTitle = new ArrayList();

        if (visitRange.equals("currentPrev")) {
            visitTitle.add("Current");
            visitTitle.add("Previous");
        }
        if (visitRange.equals("currentLast")) {
            visitTitle.add("Current");
            visitTitle.add("First");
        }
        HashMap<CtcCategory, TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> results = getCareResults(visitRange, Integer.valueOf(studyId), crfId, Integer.valueOf(studySiteId), participantId, dates, Integer.valueOf(forVisits));
        modelAndView.addObject("resultsMap", results);
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("visitTitle", visitTitle);
        modelAndView.addObject("participant", participantRepository.findById(participantId));
        request.getSession().setAttribute("sessionResultsMap", results);
        request.getSession().setAttribute("sessionDates", dates);
        return modelAndView;
    }

    private HashMap<CtcCategory, TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> getCareResults(String visitRange, Integer studyId, String crfId, Integer studySiteId, Integer participantId, List<Date> dates, Integer forVisits) {

        HashMap<CtcCategory, TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>> categoryMap = new HashMap();
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap;
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults;

        Study study = studyRepository.findById(studyId);
        StudySite studySite = study.getStudySiteById(studySiteId);

        for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
            if (studyParticipantAssignment.getParticipant().getId().equals(participantId)) {

                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    if (studyParticipantCrf.getCrf().getId().equals(Integer.parseInt(crfId))) {

                        List<StudyParticipantCrfSchedule> completedCrfs = new ArrayList<StudyParticipantCrfSchedule>();
                        if (visitRange.equals("currentLast")) {
                            completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(0));
                            if (studyParticipantCrf.getCompletedCrfs().size() > 1) {
                                completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(studyParticipantCrf.getCompletedCrfs().size() - 1));
                            }
                        } else {
                            if (forVisits == -1) {
                                forVisits = studyParticipantCrf.getCompletedCrfs().size();
                            }
                            for (int i = 1; i <= forVisits; forVisits--)
                                if (studyParticipantCrf.getCompletedCrfs().size() - forVisits >= 0) {
                                    completedCrfs.add(studyParticipantCrf.getCompletedCrfs().get(studyParticipantCrf.getCompletedCrfs().size() - forVisits));
                                }
                        }

                        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : completedCrfs) {
                            dates.add(studyParticipantCrfSchedule.getStartDate());
                            for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {
                                ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                                CtcCategory category = proCtcQuestion.getProCtcTerm().getCtcTerm().getCategory();
                                ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                                ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                                ArrayList<ProCtcValidValue> validValue;

                                if (categoryMap.containsKey(category)) {
                                    symptomMap = categoryMap.get(category);
                                } else {
                                    symptomMap = new TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
                                    categoryMap.put(category, symptomMap);
                                }

                                if (symptomMap.containsKey(symptom)) {
                                    careResults = symptomMap.get(symptom);
                                } else {
                                    careResults = new HashMap();
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
        }

        return categoryMap;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
