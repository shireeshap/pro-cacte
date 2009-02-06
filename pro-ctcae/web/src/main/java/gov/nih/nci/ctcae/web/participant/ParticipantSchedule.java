package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.lang.StringUtils;


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

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

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
     * Gets the study participant crf.
     *
     * @return the study participant crf
     */
    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
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
     * @throws ParseException the parse exception
     */
    public void createSchedules() throws ParseException {
        removeAllSchedules();
        calendar.prepareSchedules();
        int dueAfterPeriodInMill = calendar.getDueAfterPeriodInMill();
        while (calendar.hasMoreSchedules()) {
            createSchedule(calendar.getNextScehdule(), dueAfterPeriodInMill);
        }
    }

    /**
     * Creates the schedule.
     *
     * @param startDate the start date
     * @param dueDate   the due date
     * @throws ParseException the parse exception
     */
    public void createSchedule(String startDate, String dueDate) throws ParseException {
        if (!StringUtils.isBlank(dueDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Calendar cstart = new GregorianCalendar();
            cstart.setTime(sdf.parse(startDate));
            Calendar cdue = new GregorianCalendar();
            cdue.setTime(sdf.parse(dueDate));

            createSchedule(cstart, cdue.getTimeInMillis() - cstart.getTimeInMillis());
        }

    }

    /**
     * Creates the schedule.
     *
     * @param c                    the c
     * @param dueAfterPeriodInMill the due after period in mill
     */
    public void createSchedule(Calendar c, long dueAfterPeriodInMill) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
            if (sdf.format(studyParticipantCrfSchedule.getStartDate()).equals(sdf.format(c.getTime()))) {
                return;
            }
        }
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setStartDate(c.getTime());
        studyParticipantCrfSchedule.setDueDate(new Date(c.getTime().getTime() + dueAfterPeriodInMill));
        CRF crf = finderRepository.findById(CRF.class, studyParticipantCrf.getCrf().getId());
        studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule, crf);
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
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    /**
     * Checks if is repeat.
     *
     * @return true, if is repeat
     */
    public boolean isRepeat() {
        return studyParticipantCrf.getStudyParticipantCrfSchedules().size() > 1 ? true : false;
    }
}
