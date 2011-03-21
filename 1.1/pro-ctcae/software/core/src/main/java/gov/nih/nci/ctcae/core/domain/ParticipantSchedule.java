package gov.nih.nci.ctcae.core.domain;

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
     * The calendar.
     */
    private ProCtcAECalendar proCtcAECalendar;

    /**
     * The study participant crf.
     */
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    public enum ScheduleType {
        GENERAL,
        CYCLE
    }

    /**
     * Instantiates a new participant schedule.
     */
    public ParticipantSchedule() {
        super();
        proCtcAECalendar = new ProCtcAECalendar();
    }

    /**
     * Gets the calendar.
     *
     * @return the calendar
     */
    public ProCtcAECalendar getProCtcAECalendar() {
        return proCtcAECalendar;
    }

    /**
     * Sets the calendar.
     *
     * @param proCtcAECalendar the new calendar
     */
    public void setProCtcAECalendar(ProCtcAECalendar proCtcAECalendar) {
        this.proCtcAECalendar = proCtcAECalendar;
    }


    /**
     * Sets the study participant crf.
     *
     * @param studyParticipantCrf the new study participant crf
     */
    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        studyParticipantCrfs.add(studyParticipantCrf);
    }

    /**
     * Gets the current month schedules.
     *
     * @return the current month schedules
     */
    public List<StudyParticipantCrfSchedule> getCurrentMonthSchedules() {
        List<StudyParticipantCrfSchedule> currentMonthSchedules = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                Date startDate = studyParticipantCrfSchedule.getStartDate();
                if (proCtcAECalendar.isDateWithinMonth(startDate) && !studyParticipantCrf.getCrf().isHidden()) {
                    currentMonthSchedules.add(studyParticipantCrfSchedule);
                }
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
        proCtcAECalendar.prepareSchedules(scheduleType);
        //TODO:Need to remove dueAfterPeriodInMill code totally after testing of new code for due date
        //long dueAfterPeriodInMill = proCtcAECalendar.getDueAfterPeriodInMill();

        while (proCtcAECalendar.hasMoreSchedules()) {
            if (scheduleType.equals(ScheduleType.GENERAL)) {
                Calendar nextSchedule = proCtcAECalendar.getNextGeneralScehdule();
                Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(nextSchedule,proCtcAECalendar.getDueDateUnit(), proCtcAECalendar.getDueDateAmount());
                createSchedule(nextSchedule, dueDate, -1, -1, null, false);
            }
            if (scheduleType.equals(ScheduleType.CYCLE)) {
                Calendar nextSchedule = proCtcAECalendar.getNextCycleScehdule();
                Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(nextSchedule,proCtcAECalendar.getDueDateUnit(), proCtcAECalendar.getDueDateAmount());
                createSchedule(nextSchedule, dueDate, proCtcAECalendar.getCycleNumber(), proCtcAECalendar.getCycleDay(), null, false);
            }
        }
    }

    /**
     * Creates the schedule.
     *
     * @param c                    the c
     * @param dueDate due Date 
     * @param cycleNumber
     * @param cycleDay
     * @param baseline
     */
    public void createSchedule(Calendar c, Date dueDate, int cycleNumber, int cycleDay, List<String> formIds, boolean baseline) {
        if (c != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                boolean alreadyExists = false;
                if (formIds == null || (formIds != null && formIds.contains(studyParticipantCrf.getCrf().getId().toString()))) {
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                        if (sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(c.getTime()))) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
                        studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                        studyParticipantCrfSchedule.setStartDate(c.getTime());
                        studyParticipantCrfSchedule.setDueDate(dueDate);
                        if (cycleNumber != -1) {
                            studyParticipantCrfSchedule.setCycleNumber(cycleNumber);
                            studyParticipantCrfSchedule.setCycleDay(cycleDay);
                        }
                        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                            studyParticipantCrfSchedule.setHoliday(true);
                        }
                        studyParticipantCrfSchedule.setBaseline(baseline);
                    }
                }
            }
        }
    }

    /**
     * Removes the schedule.
     *
     * @param c the c
     */
    public void removeSchedule(Calendar c, List<String> formIds) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        StudyParticipantCrfSchedule schToRemove = null;
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    String scheduleDate = sdf.format(studyParticipantCrfSchedule.getStartDate());
                    String calendarDate = sdf.format(c.getTime());
                    if (calendarDate.equals(scheduleDate)) {
                        schToRemove = studyParticipantCrfSchedule;
                        break;
                    }
                }
                studyParticipantCrf.removeCrfSchedule(schToRemove);
            }
        }
    }


    /**
     * Removes all the schedules.
     */
    public void removeAllSchedules(List<String> formIds) {
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    schedulesToRemove.add(studyParticipantCrfSchedule);
                }
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
                    studyParticipantCrf.removeCrfSchedule(studyParticipantCrfSchedule);
                }
            }
        }
    }

    public void moveAllSchedules(int offset, List<String> formIds) {
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    moveSingleSchedule(studyParticipantCrfSchedule, offset);
                }
            }
        }
    }


    public void moveFutureSchedules(Calendar c, int offset, List<String> formIds) {
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DATE);
        Calendar a = new GregorianCalendar(year, month, date);
        Calendar b = new GregorianCalendar();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
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
    }

    public void deleteFutureSchedules(Calendar c, List<String> formIds) {
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int date = c.get(Calendar.DATE);
        Calendar a = new GregorianCalendar(year, month, date);
        Calendar b = new GregorianCalendar();
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
            if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
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
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
                    studyParticipantCrf.removeCrfSchedule(studyParticipantCrfSchedule);
                }
            }
        }
    }

    private void moveSingleSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule, int offset) {
        if (!studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
            Calendar c1 = ProCtcAECalendar.getCalendarForDate(studyParticipantCrfSchedule.getStartDate());
            Calendar c2 = ProCtcAECalendar.getCalendarForDate(studyParticipantCrfSchedule.getDueDate());
            c1.add(Calendar.DATE, offset);
            c2.add(Calendar.DATE, offset);

            studyParticipantCrfSchedule.setStartDate(c1.getTime());
            studyParticipantCrfSchedule.setDueDate(c2.getTime());
            studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
        }
    }

//    /**
//     * Checks if is repeat.
//     *
//     * @return true, if is repeat
//     */
//    public boolean isRepeat() {
//        return studyParticipantCrf.getStudyParticipantCrfSchedules().size() > 1;
//    }

    //

    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }
}