package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Mar 10, 2009
 */

public class MonitorFormStatusController extends AbstractController {

    StudyRepository studyRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/monitorFormStatus");
        String studyId = request.getParameter("studyId");
        String studySiteId = request.getParameter("studySiteId");
        String crfId = request.getParameter("crfId");
        String dateRange = request.getParameter("dateRange");
        String strStartDate = request.getParameter("stDate");
        String strEndDate = request.getParameter("endDate");
        String participantId = request.getParameter("participantId");
        String status = request.getParameter("status");
        String pgStartDateNext = request.getParameter("pgStartDateNext");
        String pgStartDatePrev = request.getParameter("pgStartDatePrev");
        String direction = request.getParameter("direction");
        String view = request.getParameter("view");
        Date startDate = new Date(), endDate = new Date();

        if (view.equals("initial")) {
            view = "weekly";
            if (dateRange.equals("custom")) {
                startDate = DateUtils.parseDate(strStartDate);
                endDate = DateUtils.parseDate(strEndDate);
            } else {
                Date[] date = MonitorFormUtils.getStartEndDate(dateRange, new Date());
                startDate = date[0];
                endDate = date[1];
            }
        } else {
            int increment = 0;
            Calendar stDate;

            if (direction.equals("next")) {
                stDate = ProCtcAECalendar.getCalendarForDate(DateUtils.parseDate(pgStartDateNext));
                stDate.add(Calendar.DATE, 1);
            } else {
                stDate = ProCtcAECalendar.getCalendarForDate(DateUtils.parseDate(pgStartDatePrev));
                stDate.add(Calendar.DATE, -1);
            }
            if (view.equals("monthly")) {
                increment = stDate.getActualMaximum(Calendar.DAY_OF_MONTH) - 1;
            } else {
                increment = 6;
            }

            if (direction.equals("next")) {
                startDate = stDate.getTime();
                stDate.add(Calendar.DATE, increment);
                endDate = stDate.getTime();
            } else {
                if (direction.equals("prev")) {
                    stDate.add(Calendar.DATE, -increment);
                    startDate = stDate.getTime();
                    stDate.add(Calendar.DATE, increment);
                    endDate = stDate.getTime();
                } else {
                    if (view.equals("weekly")) {
                        stDate.set(Calendar.DAY_OF_WEEK, 1);
                        startDate = stDate.getTime();
                        stDate.add(Calendar.DATE, increment);
                        endDate = stDate.getTime();
                    }
                    if (view.equals("monthly")) {
                        stDate.set(Calendar.DATE, 1);
                        startDate = stDate.getTime();
                        stDate.add(Calendar.DATE, increment);
                        endDate = stDate.getTime();
                    }
                }
            }
        }

        modelAndView.addObject("crfStatusMap", getFormStatus(Integer.valueOf(studyId), startDate, endDate, crfId, studySiteId, participantId, status));
        modelAndView.addObject("calendar", getCalendar(startDate, endDate));
        modelAndView.addObject("pgStartNext", endDate);
        modelAndView.addObject("pgStartPrev", startDate);
        modelAndView.addObject("tablePeriod", view);


        return modelAndView;
    }

    private HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>> getFormStatus(Integer studyId, Date startDate, Date endDate, String crfId, String studySiteId, String participantId, String status) {

        int diffInDays = getDifferenceOfDates(startDate, endDate) + 1;

        HashMap<Participant, StudyParticipantCrfSchedule[]> crfStatus = new HashMap();

        HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>> siteCrfStatus = new HashMap<StudySite, HashMap<Participant, StudyParticipantCrfSchedule[]>>();

        Study study = studyRepository.findById(studyId);
        for (StudySite studySite : study.getStudySites()) {
            if (StringUtils.isBlank(studySiteId) || studySite.getId().equals(Integer.parseInt(studySiteId))) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                        if (StringUtils.isBlank(crfId) || studyParticipantCrf.getCrf().getId().equals(Integer.parseInt(crfId))) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                if (dateBetween(startDate, endDate, studyParticipantCrfSchedule.getStartDate())) {
                                    Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();

                                    if (StringUtils.isBlank(participantId) || participant.getId().equals(Integer.parseInt(participantId))) {

                                        if (status.equals("all") || studyParticipantCrfSchedule.getStatus().getDisplayName().toUpperCase().equals(status)) {

                                            StudyParticipantCrfSchedule[] participantCrfStatus;

                                            if (crfStatus.containsKey(participant)) {
                                                participantCrfStatus = crfStatus.get(participant);
                                            } else {
                                                participantCrfStatus = new StudyParticipantCrfSchedule[diffInDays];
                                                crfStatus.put(participant, participantCrfStatus);
                                            }
                                            int statusLocation = getDifferenceOfDates(startDate, studyParticipantCrfSchedule.getStartDate());

                                            participantCrfStatus[statusLocation] = studyParticipantCrfSchedule;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                siteCrfStatus.put(studySite, crfStatus);
                crfStatus = new HashMap();
            }
        }


        return siteCrfStatus;
    }

    private boolean dateBetween(Date startDate, Date endDate, Date scheduleStartDate) {
        return (startDate.getTime() <= scheduleStartDate.getTime() && scheduleStartDate.getTime() <= endDate.getTime());
    }

    private int getDifferenceOfDates(Date startDate, Date endDate) {
        Calendar date = ProCtcAECalendar.getCalendarForDate(startDate);
        Calendar endDateC = ProCtcAECalendar.getCalendarForDate(endDate);
        int daysBetween = 0;
        while (date.before(endDateC)) {
            date.add(Calendar.DATE, 1);
            daysBetween++;
        }
        return daysBetween;

    }

    private List<Date> getCalendar(Date startDate, Date endDate) {

        List<Date> dates = new ArrayList<Date>();
        Calendar c = ProCtcAECalendar.getCalendarForDate(startDate);
        int diffOfDates = getDifferenceOfDates(startDate, endDate);
        int currentMonth = c.get(Calendar.MONTH);
        for (int i = 0; i <= diffOfDates; i++) {
            if (c.get(Calendar.MONTH) != currentMonth) {
                currentMonth = c.get(Calendar.MONTH);
            }
            dates.add(c.getTime());
            c.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}