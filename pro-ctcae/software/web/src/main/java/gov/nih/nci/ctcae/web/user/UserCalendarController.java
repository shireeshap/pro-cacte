package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.participant.ParticipantAjaxFacade;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mehul
 * Date: 5/30/12
*/

public class UserCalendarController extends AbstractController {

    private ParticipantAjaxFacade participantAjaxFacade;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
//        UserCalendarCommand userCalendarCommand = new UserCalendarCommand();
        ModelAndView modelAndView = new ModelAndView("user/userCalendar");
        List<Participant> participants = participantAjaxFacade.getParticipantList();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");

//        List<ParticipantSchedule> participantSchedules = new ArrayList<ParticipantSchedule>();
        TreeMap<Integer, List<StudyParticipantCrfSchedule>> scheduleDates = new TreeMap();
        for (Participant participant : participants) {
           List<StudyParticipantCrf> studyParticipantCrfs = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs();
            if(studyParticipantCrfs!=null && studyParticipantCrfs.size()>0) {
                  ParticipantSchedule participantSchedule = new ParticipantSchedule();
                  for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                      participantSchedule.addStudyParticipantCrf(studyParticipantCrf);
                  }
                    for (StudyParticipantCrfSchedule spcs : participantSchedule.getCurrentMonthSchedules()) {
                          List<StudyParticipantCrfSchedule> schedulesOnDate;
                        int day = Integer.parseInt(sdf.format(spcs.getStartDate()));
                        if(scheduleDates.containsKey(day)) {
                          schedulesOnDate = scheduleDates.get(day);
                          }else{
                              schedulesOnDate = new ArrayList<StudyParticipantCrfSchedule>();
                              scheduleDates.put(day, schedulesOnDate);
                          }
                          schedulesOnDate.add(spcs);
                      }
            }
        }



        modelAndView.addObject("schedules", scheduleDates);
        modelAndView.addObject("proCtcAeCalendar", new ProCtcAECalendar());
//        request.getSession().setAttribute("userCalendarCommandObj", userCalendarCommand);
//        request.getSession().getAttribute("userCalendarCommandObj");

        return modelAndView;
    }

    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }
}
