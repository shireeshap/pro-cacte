package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


//
/**
 * User: Harsh
 * Date: Jan 19, 2009
 * Time: 12:13:05 PM.
 */
public class ParticipantSchedule {

    /**
     * The current month schedules.
     */
    private List<StudyParticipantCrfSchedule> currentMonthSchedules = new ArrayList<StudyParticipantCrfSchedule>();

    /**
     * The calendar.
     */
    private ProCtcAECalendar calendar;

    /**
     * The study participant crf.
     */
    private StudyParticipantCrf studyParticipantCrf;

    public enum ScheduleType {
        GENERAL,
        CYCLE;
    }

    /**
     * Instantiates a new participant schedule.
     */
    public ParticipantSchedule() {
        super();
        calendar = new ProCtcAECalendar();
    }

    /**
     * Gets the calendar.
     *
     * @return the calendar
     */
    public ProCtcAECalendar getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar.
     *
     * @param calendar the new calendar
     */
    public void setCalendar(ProCtcAECalendar calendar) {
        this.calendar = calendar;
    }


    /**
     * Sets the study participant crf.
     *
     * @param studyParticipantCrf the new study participant crf
     */
    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

    /**
     * Gets the current month schedules.
     *
     * @return the current month schedules
     */
    public List<StudyParticipantCrfSchedule> getCurrentMonthSchedules() {
        currentMonthSchedules = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            Date startDate = studyParticipantCrfSchedule.getStartDate();
            if (calendar.isDateWithinMonth(startDate)) {
                currentMonthSchedules.add(studyParticipantCrfSchedule);
            }
        }
        return currentMonthSchedules;

    }


    /**
     * Creates the schedules.
     *
     * @param scheduleType
     * @throws ParseException the parse exception
     */
    public void createSchedules(ScheduleType scheduleType) throws ParseException {
        //removeAllSchedules();
        calendar.prepareSchedules(scheduleType);
        int dueAfterPeriodInMill = calendar.getDueAfterPeriodInMill();
        while (calendar.hasMoreSchedules()) {
            if (scheduleType.equals(ScheduleType.GENERAL)) {
                createSchedule(calendar.getNextGeneralScehdule(), dueAfterPeriodInMill, -1, -1);
            }
            if (scheduleType.equals(ScheduleType.CYCLE)) {
                createSchedule(calendar.getNextCycleScehdule(), dueAfterPeriodInMill, calendar.getCycleNumber(), calendar.getCycleDay());
            }
        }
    }

    /**
     * Creates the schedule.
     *
     * @param c                    the c
     * @param dueAfterPeriodInMill the due after period in mill
     * @param cycleNumber
     * @param cycleDay
     */
    public void createSchedule(Calendar c, long dueAfterPeriodInMill, int cycleNumber, int cycleDay) {
        if (c != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                if (sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(c.getTime()))) {
                    return;
                }
            }
            StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
            studyParticipantCrfSchedule.setStartDate(c.getTime());
            studyParticipantCrfSchedule.setDueDate(new Date(c.getTime().getTime() + dueAfterPeriodInMill));
            if (cycleNumber != -1) {
                studyParticipantCrfSchedule.setCycleNumber(cycleNumber);
                studyParticipantCrfSchedule.setCycleDay(cycleDay);
            }

            CRF crf = studyParticipantCrf.getCrf();
            studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule, crf);
        }
    }

    /**
     * Removes the schedule.
     *
     * @param c the c
     */
    public void removeSchedule(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        StudyParticipantCrfSchedule schToRemove = null;
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {

            String scheduleDate = sdf.format(studyParticipantCrfSchedule.getStartDate());
            String calendarDate = sdf.format(c.getTime());
            if (calendarDate.equals(scheduleDate)) {
                schToRemove = studyParticipantCrfSchedule;
            }
        }
        if (schToRemove != null) {
            if (schToRemove.getStatus().equals(CrfStatus.SCHEDULED)) {
                studyParticipantCrf.removeCrfSchedule(schToRemove);
            }
        }
    }


    /**
     * Removes all the schedules.
     */
    public void removeAllSchedules() {
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (//(sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(startDate)) || studyParticipantCrfSchedule.getStartDate().after(startDate)) &&
                    studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                schedulesToRemove.add(studyParticipantCrfSchedule);
            }
        }
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
            studyParticipantCrf.removeCrfSchedule(studyParticipantCrfSchedule);
        }
    }

    public void moveAllSchedules(int offset) {
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                moveSingleSchedule(studyParticipantCrfSchedule, offset);
            }
        }
    }


    public void moveFutureSchedules(Calendar c, int offset) {
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DATE);
        Calendar a = new GregorianCalendar(year, month, date);
        Calendar b = new GregorianCalendar();

        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                b.setTime(studyParticipantCrfSchedule.getStartDate());
                int mon = b.get(Calendar.MONTH);
                int dt = b.get(Calendar.DATE);
                int yr = b.get(Calendar.YEAR);
                b.clear();
                b.set(yr, mon, dt);
                if (b.getTimeInMillis() >= a.getTimeInMillis()) {
                    moveSingleSchedule(studyParticipantCrfSchedule, offset);
                }
            }
        }
    }

    public void deleteFutureSchedules(Calendar c) {
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DATE);
        Calendar a = new GregorianCalendar(year, month, date);
        Calendar b = new GregorianCalendar();
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED)) {
                b.setTime(studyParticipantCrfSchedule.getStartDate());
                int mon = b.get(Calendar.MONTH);
                int dt = b.get(Calendar.DATE);
                int yr = b.get(Calendar.YEAR);
                b.clear();
                b.set(yr, mon, dt);
                if (b.getTimeInMillis() >= a.getTimeInMillis()) {
                    schedulesToRemove.add(studyParticipantCrfSchedule);
                }
            }
        }
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
            studyParticipantCrf.removeCrfSchedule(studyParticipantCrfSchedule);
        }
    }

    private void moveSingleSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule, int offset) {
        Calendar c1 = calendar.getCalendarForDate(studyParticipantCrfSchedule.getStartDate());
        Calendar c2 = calendar.getCalendarForDate(studyParticipantCrfSchedule.getDueDate());
        c1.add(Calendar.DATE, offset);
        c2.add(Calendar.DATE, offset);

        studyParticipantCrfSchedule.setStartDate(c1.getTime());
        studyParticipantCrfSchedule.setDueDate(c2.getTime());

    }

    /**
     * Checks if is repeat.
     *
     * @return true, if is repeat
     */
    public boolean isRepeat() {
        return studyParticipantCrf.getStudyParticipantCrfSchedules().size() > 1 ? true : false;
    }

    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }
}
