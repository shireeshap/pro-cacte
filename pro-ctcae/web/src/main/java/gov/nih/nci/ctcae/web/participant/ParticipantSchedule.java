package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

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
}
