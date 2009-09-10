package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Mar 10, 2009
 */

public class ParticipantResponseReportController extends AbstractController {

    UserRepository userRepository;
    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participants = (List<Participant>) userRepository.findParticipantForUser(user);
        if (participants == null || participants.size() != 1) {
            throw new CtcAeSystemException("Can not find participant for username " + user.getUsername());
        }
        ModelAndView modelAndView = new ModelAndView("participant/responseReport");
        Participant participant = participants.get(0);

        Integer scheduleId = Integer.parseInt(request.getParameter("id"));
        StudyParticipantCrfSchedule spcs = studyParticipantCrfScheduleRepository.findById(scheduleId);

        if (!spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().equals(participant)) {
            throw (new AccessDeniedException("Participant does not have access to this form"));
        }

        TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>> symptomMap = new TreeMap<ProCtcTerm, HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>>>(new ProCtcTermComparator());
        HashMap<ProCtcQuestion, ArrayList<ProCtcValidValue>> careResults;
        List dates = new ArrayList<Date>();

        for (StudyParticipantCrfSchedule mySch : spcs.getStudyParticipantCrf().getStudyParticipantCrfSchedules()) {
            if (mySch.getStatus().equals(CrfStatus.COMPLETED)) {
                dates.add(mySch.getStartDate());
                for (StudyParticipantCrfItem studyParticipantCrfItem : mySch.getStudyParticipantCrfItems()) {
                    ProCtcQuestion proCtcQuestion = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion();
                    ProCtcTerm symptom = proCtcQuestion.getProCtcTerm();
                    ProCtcValidValue value = studyParticipantCrfItem.getProCtcValidValue();
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
        modelAndView.addObject("resultsMap", symptomMap);
        modelAndView.addObject("dates", dates);
        modelAndView.addObject("schedule", spcs);
        modelAndView.addObject("questionTypes", ProCtcQuestionType.getAllDisplayTypes());

        return modelAndView;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}