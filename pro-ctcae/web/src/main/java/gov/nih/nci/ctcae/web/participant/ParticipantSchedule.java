package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * User: Harsh
 * Date: Jan 19, 2009
 * Time: 12:13:05 PM
 */
public class ParticipantSchedule {

    private int repetitionPeriodAmount;
    private String repetitionPeriodUnit;
    private int dueDateAmount;
    private String dueDateUnit;
    private String repeatOn;
    private String repeatUntilUnit;
    private String repeatUntilValue;
    private List<StudyParticipantCrfSchedule> currentMonthSchedules = new ArrayList<StudyParticipantCrfSchedule>();
    private StudyParticipantCrfCalendar calendar;
    private StudyParticipantCrf studyParticipantCrf;
    private Date startDate;
    private FinderRepository finderRepository;

    public ParticipantSchedule() {
        calendar = new StudyParticipantCrfCalendar();
    }

    public int getRepetitionPeriodAmount() {
        return repetitionPeriodAmount;
    }

    public void setRepetitionPeriodAmount(int repetitionPeriodAmount) {
        this.repetitionPeriodAmount = repetitionPeriodAmount;
    }

    public String getRepetitionPeriodUnit() {
        return repetitionPeriodUnit;
    }

    public void setRepetitionPeriodUnit(String repetitionPeriodUnit) {
        this.repetitionPeriodUnit = repetitionPeriodUnit;
    }

    public int getDueDateAmount() {
        return dueDateAmount;
    }

    public void setDueDateAmount(int dueDateAmount) {
        this.dueDateAmount = dueDateAmount;
    }

    public String getDueDateUnit() {
        return dueDateUnit;
    }

    public void setDueDateUnit(String dueDateUnit) {
        this.dueDateUnit = dueDateUnit;
    }

    public String getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(String repeatOn) {
        this.repeatOn = repeatOn;
    }

    public String getRepeatUntilUnit() {
        return repeatUntilUnit;
    }

    public void setRepeatUntilUnit(String repeatUntilUnit) {
        this.repeatUntilUnit = repeatUntilUnit;
    }

    public String getRepeatUntilValue() {
        return repeatUntilValue;
    }

    public void setRepeatUntilValue(String repeatUntilValue) {
        this.repeatUntilValue = repeatUntilValue;
    }

    public StudyParticipantCrfCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(StudyParticipantCrfCalendar calendar) {
        this.calendar = calendar;
    }

    public StudyParticipantCrf getStudyParticipantCrf() {
        return studyParticipantCrf;
    }

    public void setStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        this.studyParticipantCrf = studyParticipantCrf;
    }

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


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void createSchedules() throws ParseException {

        int numberOfRepetitions = getNumberOfRepetitions(repeatUntilUnit, repeatUntilValue, startDate, repetitionPeriodUnit, repetitionPeriodAmount);
        Calendar c = getCalendarForDate(startDate);
        int dueAfterPeriodInMill = getDuePeriodInMillis(dueDateUnit, dueDateAmount);

        removeSchedules(startDate);


        for (int i = 0; i < numberOfRepetitions; i++) {
            createSchedule(c, dueAfterPeriodInMill);
            if ("Days".equals(repetitionPeriodUnit)) {
                c.add(Calendar.DATE, repetitionPeriodAmount);
            }
            if ("Weeks".equals(repetitionPeriodUnit)) {
                c.add(Calendar.WEEK_OF_MONTH, repetitionPeriodAmount);
            }
            if ("Months".equals(repetitionPeriodUnit)) {
                c.add(Calendar.MONTH, repetitionPeriodAmount);
            }
        }

    }

    public void createSchedule(Calendar c, int dueAfterPeriodInMill) {
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        studyParticipantCrfSchedule.setStartDate(c.getTime());
        studyParticipantCrfSchedule.setDueDate(new Date(c.getTime().getTime() + dueAfterPeriodInMill));
        CRF crf = finderRepository.findById(CRF.class, studyParticipantCrf.getCrf().getId());
        studyParticipantCrf.addStudyParticipantCrfSchedule(studyParticipantCrfSchedule, crf);
    }

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

    public Calendar getCalendarForDate(Date date) {
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

        int month = Integer.parseInt(sdfMonth.format(date));
        int day = Integer.parseInt(sdfDay.format(date));
        int year = Integer.parseInt(sdfYear.format(date));

        Calendar c1 = Calendar.getInstance();
        c1.set(year, month - 1, day);
        return c1;
    }


    private int getNumberOfRepetitions(String repeatUntil, String repeatUntilValue, Date startDate, String repeatEvery, int repeatEveryValue) throws ParseException {
        int numberOfRepetitions = 0;
        if ("Number".equals(repeatUntil)) {
            numberOfRepetitions = Integer.parseInt(repeatUntilValue);
        }
        if ("Indefinitely".equals(repeatUntil)) {
            numberOfRepetitions = 100;
        }
        if ("Date".equals(repeatUntil)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date endDate = sdf.parse(repeatUntilValue);
            int numberOfDays = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
            if ("Days".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / repeatEveryValue;
            }
            if ("Weeks".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 7;
            }
            if ("Months".equals(repeatEvery)) {
                numberOfRepetitions = numberOfDays / 30;
            }
        }
        return numberOfRepetitions;

    }

    private int getDuePeriodInMillis(String dueAfter, int dueAfterValue) {
        int dueAfterPeriodInMillis = 0;
        if ("Hours".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 60 * 60 * 1000;
        }
        if ("Days".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 24 * 60 * 60 * 1000;
        }
        if ("Weeks".equals(dueAfter)) {
            dueAfterPeriodInMillis = dueAfterValue * 7 * 24 * 60 * 60 * 1000;
        }
        return dueAfterPeriodInMillis;
    }

    private void removeSchedules(Date startDate) {
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

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}
