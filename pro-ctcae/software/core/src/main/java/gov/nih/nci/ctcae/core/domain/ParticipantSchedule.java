package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;


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
                if (proCtcAECalendar.isDateAfterMonth(startDate)) {
                    break;
                }
                if (proCtcAECalendar.isDateWithinMonth(startDate) && !studyParticipantCrf.getCrf().isHidden()) {
                    currentMonthSchedules.add(studyParticipantCrfSchedule);
                } else if (proCtcAECalendar.isDateWithinMonth(startDate) && studyParticipantCrf.getCrf().isHidden() && (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED))) {
                    currentMonthSchedules.add(studyParticipantCrfSchedule);
                }
            }
        }
        return currentMonthSchedules;

    }

    public List<StudyParticipantCrfSchedule> getPreviousSchedules(StudyParticipantAssignment spa, StudyParticipantCrf spc) {
        List<StudyParticipantCrfSchedule> schedules = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrf studyParticipantCrf : spa.getStudyParticipantCrfs()) {
            if (!spc.equals(studyParticipantCrf) && studyParticipantCrf.getStudyParticipantCrfSchedules() != null && studyParticipantCrf.getStudyParticipantCrfSchedules().size() > 0) {
                for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                    Date startDate = studyParticipantCrfSchedule.getStartDate();
                    if (proCtcAECalendar.isDateAfterMonth(startDate)) {
                        break;
                    }
                    if (proCtcAECalendar.isDateWithinMonth(startDate) && !studyParticipantCrf.getCrf().isHidden()) {
                        schedules.add(studyParticipantCrfSchedule);
                    } else if (proCtcAECalendar.isDateWithinMonth(startDate) && studyParticipantCrf.getCrf().isHidden() && (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED))) {
                        schedules.add(studyParticipantCrfSchedule);
                    }
                }
            }
        }
        return schedules;

    }


    /**
     * Creates the schedules.
     *
     * @param scheduleType
     * @throws ParseException the parse exception
     */
    public void createSchedules(ScheduleType scheduleType, boolean armChange) throws ParseException {
        proCtcAECalendar.prepareSchedules(scheduleType);
        //TODO:Need to remove dueAfterPeriodInMill code totally after testing of new code for due date
        //long dueAfterPeriodInMill = proCtcAECalendar.getDueAfterPeriodInMill();

        while (proCtcAECalendar.hasMoreSchedules()) {
            if (scheduleType.equals(ScheduleType.GENERAL)) {
                Calendar nextSchedule = proCtcAECalendar.getNextGeneralScehdule();
                Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(nextSchedule, proCtcAECalendar.getDueDateUnit(), proCtcAECalendar.getDueDateAmount());
                createSchedule(nextSchedule, dueDate, -1, -1, null, false, armChange);
            }
            if (scheduleType.equals(ScheduleType.CYCLE)) {
                Calendar nextSchedule = proCtcAECalendar.getNextCycleScehdule();
                Date dueDate = proCtcAECalendar.getDueDateForCalendarDate(nextSchedule, proCtcAECalendar.getDueDateUnit(), proCtcAECalendar.getDueDateAmount());
                createSchedule(nextSchedule, dueDate, proCtcAECalendar.getCycleNumber(), proCtcAECalendar.getCycleDay(), null, false, armChange);
            }
        }
    }

    /**
     * Creates the schedule.
     *
     * @param c           the c
     * @param dueDate     due Date
     * @param cycleNumber
     * @param cycleDay
     * @param baseline
     */
    public void createSchedule(Calendar c, Date dueDate, int cycleNumber, int cycleDay, List<String> formIds, boolean baseline, boolean armChange) {
        if (c != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                boolean alreadyExists = false;
                if (formIds == null || (formIds != null && formIds.contains(studyParticipantCrf.getCrf().getId().toString()))) {
                    StudyParticipantAssignment spa = studyParticipantCrf.getStudyParticipantAssignment();
                    List<StudyParticipantCrfSchedule> spcsList = getPreviousSchedules(spa, studyParticipantCrf);
                    if (c.getTime().equals(studyParticipantCrf.getCrf().getEffectiveStartDate()) || c.getTime().after(studyParticipantCrf.getCrf().getEffectiveStartDate())) {
                        if (studyParticipantCrf.getCrf().getParentCrf() != null && studyParticipantCrf.getCrf().getParentCrf().getStudyParticipantCrfs() != null) {
                            if (spcsList != null) {
                                for (StudyParticipantCrfSchedule spcs : spcsList) {
                                    if (sdf.format(spcs.getStartDate()).equals(sdf.format(c.getTime())) && (spcs.getStatus().equals(CrfStatus.INPROGRESS) || spcs.getStatus().equals(CrfStatus.COMPLETED) || spcs.getStatus().equals(CrfStatus.PASTDUE))) {
                                        if (spcs.getStudyParticipantCrf().getCrf().equals(studyParticipantCrf.getCrf()) || spcs.getStudyParticipantCrf().getCrf().checkForParent(studyParticipantCrf.getCrf())) {
                                            alreadyExists = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (armChange) {
                            for (StudyParticipantCrf oldStudyParticipantCrf : spa.getStudyParticipantCrfs()) {
                                if (oldStudyParticipantCrf.getId() != null && studyParticipantCrf.equals(oldStudyParticipantCrf)) {
                                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : oldStudyParticipantCrf.getStudyParticipantCrfSchedules()) {
                                        if (sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(c.getTime())) && (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE))) {
                                            alreadyExists = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!alreadyExists) {
                            StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
                            studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                            if (baseline) {
                                studyParticipantCrfSchedule.setStartDate(studyParticipantCrf.getStartDate());
                            } else {
                                studyParticipantCrfSchedule.setStartDate(c.getTime());
                            }
                            //check if due date is not fixed, then check the values from arm schedules
                            Date dueDateNew = dueDate;
                            if (dueDate == null) {
                                dueDateNew = getDueDateForFormSchedule(c, studyParticipantCrf);
                            }
                            studyParticipantCrfSchedule.setDueDate(dueDateNew);

                            if (today.after(dueDateNew)) {
                                studyParticipantCrfSchedule.setStatus(CrfStatus.NOTAPPLICABLE);
                            }
                            if (cycleNumber != -1) {
                                studyParticipantCrfSchedule.setCycleNumber(cycleNumber);
                                studyParticipantCrfSchedule.setCycleDay(cycleDay);
                            }
                            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                                studyParticipantCrfSchedule.setHoliday(true);
                            }
                            if (studyParticipantCrf.getCrf().getCreateBaseline() && cycleDay == 1 && cycleNumber == 1) {
                                studyParticipantCrfSchedule.setBaseline(true);
                            }
                            if (isSpCrfScheduleAvailable(studyParticipantCrfSchedule)) {
                                //call getter on schedule for available forms
                                studyParticipantCrfSchedule.getStudyParticipantCrfItems();
                            }
                            addIvrsSchedules(studyParticipantCrfSchedule, studyParticipantCrf);
                        }

                    }
                }
            }
        }
    }

    public static boolean isSpCrfScheduleAvailable(
            StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        if (DateUtils.compareDate(studyParticipantCrfSchedule.getStartDate(), DateUtils.getCurrentDate()) <= 0 &&
                DateUtils.compareDate(studyParticipantCrfSchedule.getDueDate(), DateUtils.getCurrentDate()) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * Adds the ivrs schedules.
     *
     * @param studyParticipantCrfSchedule the study participant crf schedule
     */
    public void addIvrsSchedules(
            StudyParticipantCrfSchedule studyParticipantCrfSchedule, StudyParticipantCrf studyParticipantCrf) {
        //update ivrsSchedule for IVRS app Modes
        boolean isIvrs = false;
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrf.getStudyParticipantAssignment();
        IvrsSchedule ivrsSchedule;
        int offSetDiff = 0;
        for (StudyParticipantMode spm : studyParticipantAssignment.getStudyParticipantModes()) {
            if (spm.getMode().equals(AppMode.IVRS)) {
                isIvrs = true;
            }
        }
        if (isIvrs) {
            //for every sp_crf_schedule, create a new ivrsSchedule for every day from startDate to endDate
            Date startDate = studyParticipantCrfSchedule.getStartDate();
            offSetDiff = DateUtils.daysBetweenDates(studyParticipantCrfSchedule.getDueDate(), studyParticipantCrfSchedule.getStartDate());
            for (int i = 0; i <= offSetDiff; i++) {
                ivrsSchedule = new IvrsSchedule(studyParticipantAssignment, startDate);
                ivrsSchedule.setStudyParticipantCrfSchedule(studyParticipantCrfSchedule);
                studyParticipantCrfSchedule.getIvrsSchedules().add(ivrsSchedule);
                startDate = DateUtils.getNextDay(startDate);
            }
        }
    }

    /**
     * get the due date for a given Calendar date with calendar/cycle based definitions for the form.
     *
     * @param c                   Calendar, for which due date needs to calculate
     * @param studyParticipantCrf StudyParticipantCrf
     * @return Date, dueDate for the calendar
     */
    public Date getDueDateForFormSchedule(Calendar c, StudyParticipantCrf studyParticipantCrf) {
        Date dueDate = null;
        for (FormArmSchedule formArmSchedule : studyParticipantCrf.getCrf().getFormArmSchedules()) {
            if (formArmSchedule.getArm().equals(studyParticipantCrf.getStudyParticipantAssignment().getArm())) {
                //Calendar Based
                if (formArmSchedule.getCrfCalendars().size() > 0 && formArmSchedule.getCrfCalendars().get(0).isValid()) {
                    CRFCalendar crfCalendar = formArmSchedule.getCrfCalendars().get(0);
                    String dueDateValue = crfCalendar.getDueDateValue();
                    String dueDateUnit = crfCalendar.getDueDateUnit();
                    dueDate = proCtcAECalendar.getDueDateForCalendarDate(c, dueDateUnit, Integer.parseInt(dueDateValue));
                } else if (formArmSchedule.getCrfCycleDefinitions().size() > 0 && formArmSchedule.getCrfCycleDefinitions().get(0) != null) {
                    CRFCycleDefinition cycleDefinition = formArmSchedule.getCrfCycleDefinitions().get(0);
                    String dueDateValue = cycleDefinition.getDueDateValue();
                    String dueDateUnit = cycleDefinition.getDueDateUnit();
                    if (StringUtils.isBlank(cycleDefinition.getDueDateUnit()) || StringUtils.isBlank(cycleDefinition.getDueDateValue())) {
                        dueDateValue = "1";
                        dueDateUnit = "Days";
                    }

                    dueDate = proCtcAECalendar.getDueDateForCalendarDate(c, dueDateUnit, Integer.parseInt(dueDateValue));

                } else {
                    dueDate = proCtcAECalendar.getDueDateForCalendarDate(c, "Days", 2);
                }
                break;
            }
        }
        return dueDate;
    }

    /**
     * updates the schedule.
     *
     * @param oldCalendar Calendar, of current date
     * @param newCalendar Calendar, of reschedule date
     * @param formIds     List<String>, crfs for which schedules needs to be modified
     * @param resultMap   LinkedHashMap<String, List<String>>, stores the failure and success forms list map
     */
    public void updateSchedule(Calendar oldCalendar, Calendar newCalendar, List<String> formIds, LinkedHashMap<String, List<String>> resultMap) {
        List<String> updatedForms = new ArrayList<String>();
        List<String> completedForms = new ArrayList<String>();
        if (newCalendar != null) {
            Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();

            StudyParticipantCrfSchedule schToUpdate = null;
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                int alreadyExistsCount = 0;
                boolean alreadyExists = false;
                boolean alreadyPresentNewDate = false;
                if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                        String scheduleDate = DateUtils.format(studyParticipantCrfSchedule.getStartDate());
                        String calendarDate = DateUtils.format(oldCalendar.getTime());
                        String calendarNewDate = DateUtils.format(newCalendar.getTime());
                        if (calendarDate.equals(scheduleDate) && !studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
                            schToUpdate = studyParticipantCrfSchedule;
                            alreadyExists = true;
                        }
                        if (calendarNewDate.equals(scheduleDate)) {
                            schToUpdate = studyParticipantCrfSchedule;
                            alreadyPresentNewDate = true;
                        }
                        if (alreadyExists && alreadyPresentNewDate) {
                            break;
                        }

                    }
                    //checking for if same form is present in current and moving date or not
                    //if moving date has same form then do not process that record.
                    if (alreadyExists && !alreadyPresentNewDate) {
                        int dateOffsetBetweenStartAndDueDate = DateUtils.daysBetweenDates(schToUpdate.getDueDate(), schToUpdate.getStartDate());
                        int dateOffsetBetweenOldAndNewStartDates = DateUtils.daysBetweenDates(newCalendar.getTime(), schToUpdate.getStartDate());
                        schToUpdate.setStartDate(newCalendar.getTime());
                        //update ivrsSchedules
                        schToUpdate.updateIvrsSchedules(studyParticipantCrf, dateOffsetBetweenOldAndNewStartDates);

                        Calendar dueCalendar = (Calendar) newCalendar.clone();
                        dueCalendar.add(Calendar.DATE, dateOffsetBetweenStartAndDueDate);
                        schToUpdate.setDueDate(dueCalendar.getTime());
                        if (today.after(schToUpdate.getDueDate())) {
                            schToUpdate.setStatus(CrfStatus.PASTDUE);
                        } else {
                            if (schToUpdate.getStatus().equals(CrfStatus.PASTDUE)) {
                                schToUpdate.setStatus(CrfStatus.SCHEDULED);
                            }
                        }

                        updatedForms.add(studyParticipantCrf.getCrf().getTitle());
                    } else {
                        completedForms.add(studyParticipantCrf.getCrf().getTitle());
                    }
                }
            }
        }
        resultMap.put("successForms", updatedForms);
        resultMap.put("failedForms", completedForms);
    }

    /**
     * validation method to get the forms which are going to be past-due(due date<current system date).
     *
     * @param oldCalendar the Calendar, current date
     * @param newCalendar the Calendar, reschedule date
     * @param formIds     List<String>, form ids for which reschedule
     */
    public List<String> getReschedulePastDueForms(Calendar oldCalendar, Calendar newCalendar, List<String> formIds) {
        ArrayList<String> listPastDueForms = new ArrayList<String>();
        if (newCalendar != null) {
            Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();

            StudyParticipantCrfSchedule schToUpdate = null;
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantCrfs) {
                boolean alreadyExists = false;
                boolean alreadyPresentNewDate = false;
                if (formIds.contains(studyParticipantCrf.getCrf().getId().toString())) {
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                        String scheduleDate = DateUtils.format(studyParticipantCrfSchedule.getStartDate());
                        String calendarDate = DateUtils.format(oldCalendar.getTime());
                        String calendarNewDate = DateUtils.format(newCalendar.getTime());
                        if (calendarDate.equals(scheduleDate) && !studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
                            schToUpdate = studyParticipantCrfSchedule;
                            alreadyExists = true;
                        }
                        if (calendarNewDate.equals(scheduleDate)) {
                            schToUpdate = studyParticipantCrfSchedule;
                            alreadyPresentNewDate = true;
                        }
                        if (alreadyExists && alreadyPresentNewDate) {
                            break;
                        }

                    }
                    //checking for if same form is present in current and moving date or not
                    //if moving date has same form then do not process that record.
                    if (alreadyExists && !alreadyPresentNewDate) {
                        int dateOffset = DateUtils.daysBetweenDates(schToUpdate.getDueDate(), schToUpdate.getStartDate());
                        Calendar dueCalendar = (Calendar) newCalendar.clone();
                        dueCalendar.add(Calendar.DATE, dateOffset);
                        if (today.after(dueCalendar.getTime())) {
                            listPastDueForms.add(studyParticipantCrf.getCrf().getTitle());
                        }

                    }
                }
            }
        }
        return listPastDueForms;
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
                    if (studyParticipantCrfSchedule.isDeletable()) {
                        schedulesToRemove.add(studyParticipantCrfSchedule);
                    }
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
                        studyParticipantCrfSchedule.updateIvrsSchedules(studyParticipantCrf, offset);
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
            Date today = ProCtcAECalendar.getCalendarForDate(new Date()).getTime();
            if (today.after(studyParticipantCrfSchedule.getDueDate())) {
                studyParticipantCrfSchedule.setStatus(CrfStatus.PASTDUE);
            } else {
                if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.PASTDUE)) {
                    studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
                }
            }

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
