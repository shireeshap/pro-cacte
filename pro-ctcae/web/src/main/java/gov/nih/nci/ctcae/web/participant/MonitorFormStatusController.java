package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.*;
import java.text.SimpleDateFormat;

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
        String startDate = request.getParameter("stDate");
        String endDate = request.getParameter("endDate");

        modelAndView.addObject("crfStatusMap", getFormStatus(Integer.valueOf(studyId), DateUtils.parseDate(startDate), DateUtils.parseDate(endDate), crfId, studySiteId, dateRange));
        modelAndView.addObject("calendar", getCalendar(DateUtils.parseDate(startDate), DateUtils.parseDate(endDate)));

        return modelAndView;
    }

    private Date getStartDate(String dateRange, Date startDate, Date endDate) {
        Date today = new Date();
        Calendar c = ProCtcAECalendar.getCalendarForDate(today);

        if ("thisWeek".equals(dateRange)) {
            c.set(Calendar.DAY_OF_WEEK, 2);
        }
        if ("lastWeek".equals(dateRange)) {
        }
        if ("thisMonth".equals(dateRange)) {
        }
        if ("lastMonth".equals(dateRange)) {
        }
        if ("custom".equals(dateRange)) {
            return startDate;
        }
        return startDate;
    }

    private Date getEndDate(String dateRange, Date startDate, Date endDate) {

        if ("thisWeek".equals(dateRange)) {
            return new Date();
        }
        if ("lastWeek".equals(dateRange)) {
        }
        if ("thisMonth".equals(dateRange)) {
            return new Date();
        }
        if ("lastMonth".equals(dateRange)) {
        }
        if ("custom".equals(dateRange)) {
            return endDate;
        }
        return endDate;
    }

    private HashMap getFormStatus(Integer studyId, Date startDate, Date endDate, String crfId, String studySiteId, String dateRange) {

        int diffInDays = getDifferenceOfDates(startDate, endDate) + 1;
        HashMap<Participant, String[]> crfStatus = new HashMap();

        Study study = studyRepository.findById(studyId);
        for (StudySite studySite : study.getStudySites()) {
            if (StringUtils.isBlank(studySiteId) || studySite.getId().equals(Integer.parseInt(studySiteId))) {
                for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
                    for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                        if (StringUtils.isBlank(crfId) || studyParticipantCrf.getCrf().getId().equals(Integer.parseInt(crfId))) {
                            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                if (dateBetween(startDate, endDate, studyParticipantCrfSchedule.getStartDate())) {
                                    Participant participant = studyParticipantCrfSchedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();

                                    String[] participantCrfStatus;

                                    if (crfStatus.containsKey(participant)) {
                                        participantCrfStatus = crfStatus.get(participant);
                                    } else {
                                        participantCrfStatus = new String[diffInDays];
                                        crfStatus.put(participant, participantCrfStatus);
                                    }
                                    int statusLocation = getDifferenceOfDates(startDate, studyParticipantCrfSchedule.getStartDate());

                                    participantCrfStatus[statusLocation] = studyParticipantCrfSchedule.getStatus().getDisplayName();
                                }
                            }
                        }
                    }
                }
            }

        }

        return crfStatus;
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

    private List<String> getCalendar(Date startDate, Date endDate) {

        List<String> dates = new ArrayList<String>();
        Calendar c = ProCtcAECalendar.getCalendarForDate(startDate);
        int diffOfDates = getDifferenceOfDates(startDate, endDate);
        int currentMonth = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        for (int i = 0; i <= diffOfDates; i++) {
            if (c.get(Calendar.MONTH) != currentMonth) {
                date = 1;
                currentMonth = c.get(Calendar.MONTH);
            }
            dates.add(new SimpleDateFormat("MM/dd").format(c.getTime()));
            date++;
            c.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }

}