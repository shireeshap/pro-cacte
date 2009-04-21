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
        modelAndView.addObject("resultsMap", getCareResults(visitRange, Integer.valueOf(studyId), crfId, Integer.valueOf(studySiteId), participantId, dates, Integer.valueOf(forVisits)));
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("visitTitle", visitTitle);
        modelAndView.addObject("participant", participantRepository.findById(participantId));

        return modelAndView;
    }

    private HashMap getCareResults(String visitRange, Integer studyId, String crfId, Integer studySiteId, Integer participantId, List<Date> dates, Integer forVisits) {

        HashMap<CtcCategory, TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList>>> categoryMap = new HashMap();
        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList>> symptomMap;
        HashMap<ProCtcQuestion, ArrayList> careResults;

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

                                if (studyParticipantCrfItem.getProCtcValidValue() != null) {

                                    CtcCategory category = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion().getProCtcTerm().getCtcTerm().getCategory();
                                    ProCtcTerm symptom = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion().getProCtcTerm();
                                    ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion();
                                    ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
                                    ArrayList validValue;

                                    if (categoryMap.containsKey(category)) {
                                        symptomMap = categoryMap.get(category);
                                    } else {
                                        symptomMap = new TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList>>(new ProCtcTermComparator());
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
                                        validValue = new ArrayList();
                                        careResults.put(proCtcQuestion, validValue);
                                    }
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

//    private boolean dateBetween(Date startDate, Date endDate, Date scheduleDueDate) {
//        return (startDate.getTime() <= scheduleDueDate.getTime() && scheduleDueDate.getTime() <= endDate.getTime());
//    }


    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}
