package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mehul Gulati
 *         Date: Apr 11, 2009
 */
public class ParticipantCareResultsController extends AbstractController {

    StudyRepository studyRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantCareResults");
        String studyId = request.getParameter("studyId");
        String studySiteId = request.getParameter("studySiteId");
        String crfId = request.getParameter("crfId");
        String dateRange = request.getParameter("dateRange");
        String strStartDate = request.getParameter("stDate");
        String strEndDate = request.getParameter("endDate");
        String participantId = request.getParameter("participantId");
        Date startDate = new Date(), endDate = new Date();
        List dates = new ArrayList<Date>();


        if (dateRange.equals("custom")) {
            startDate = DateUtils.parseDate(strStartDate);
            endDate = DateUtils.parseDate(strEndDate);
        }

        modelAndView.addObject("resultsMap", getCareResults(Integer.valueOf(studyId), startDate, endDate, crfId, Integer.valueOf(studySiteId), participantId, dates));
        modelAndView.addObject("dates", dates);

        return modelAndView;
    }

    private HashMap getCareResults(Integer studyId, Date startDate, Date endDate, String crfId, Integer studySiteId, String participantId, List<Date> dates) {

        HashMap<CtcCategory, HashMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList>>> categoryMap = new HashMap();
        HashMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList>> symptomMap;
        HashMap<ProCtcQuestion, ArrayList> careResults;

        Study study = studyRepository.findById(studyId);
        StudySite studySite = study.getStudySiteById(studySiteId);

        for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
            if (studyParticipantAssignment.getParticipant().getId().equals(Integer.parseInt(participantId))) {

                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    if (studyParticipantCrf.getCrf().getId().equals(Integer.parseInt(crfId))) {

                        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                            if (dateBetween(startDate, endDate, studyParticipantCrfSchedule.getDueDate()) && studyParticipantCrfSchedule.getStatus().getDisplayName().equals("Completed")) {
                                dates.add(studyParticipantCrfSchedule.getStartDate());

                                for (StudyParticipantCrfItem studyParticipantCrfItem : studyParticipantCrfSchedule.getStudyParticipantCrfItems()) {

                                    CtcCategory category = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion().getProCtcTerm().getCtcTerm().getCategory();
                                    ProCtcTerm symptom = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion().getProCtcTerm();
                                    ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getProCtcValidValue().getProCtcQuestion();
                                    String value = studyParticipantCrfItem.getProCtcValidValue().getValue();
                                    ArrayList validValue;

                                    if (categoryMap.containsKey(category)) {
                                        symptomMap = categoryMap.get(category);
                                    } else {
                                        symptomMap = new HashMap();
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

    private boolean dateBetween(Date startDate, Date endDate, Date scheduleDueDate) {
        return (startDate.getTime() <= scheduleDueDate.getTime() && scheduleDueDate.getTime() <= endDate.getTime());
    }


    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
