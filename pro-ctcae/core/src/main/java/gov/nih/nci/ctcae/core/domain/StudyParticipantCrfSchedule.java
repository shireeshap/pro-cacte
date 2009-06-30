package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

//
/**
 * The Class StudyParticipantCrfSchedule.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "SP_CRF_SCHEDULES")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
    @Parameter(name = "sequence", value = "seq_sp_crf_schedules_id")})
public class StudyParticipantCrfSchedule extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The start date.
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * The due date.
     */
    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "is_holiday")
    private boolean holiday;

    /**
     * The status.
     */
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrfStatus status = CrfStatus.SCHEDULED;

    @Column(name = "cycle_number", nullable = true)
    private Integer cycleNumber;

    @Column(name = "cycle_day", nullable = true)
    private Integer cycleDay;

    @Column(name = "week_in_year", nullable = false)
    private Integer weekInYear;

    @Column(name = "month_in_year", nullable = false)
    private Integer monthInYear;


    /**
     * The study participant crf.
     */
    @JoinColumn(name = "study_participant_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantCrf studyParticipantCrf;

    /**
     * The study participant crf items.
     */
    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})

    private List<StudyParticipantCrfItem> studyParticipantCrfItems = new ArrayList<StudyParticipantCrfItem>();

    /**
     * The study participant crf schedule added questions.
     */
    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfScheduleAddedQuestion> studyParticipantCrfScheduleAddedQuestions = new ArrayList<StudyParticipantCrfScheduleAddedQuestion>();

    /**
     * Instantiates a new study participant crf schedule.
     */
    public StudyParticipantCrfSchedule() {
        super();

    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#getId()
     */
    public Integer getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Persistable#setId(java.lang.Integer)
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the study participant crf items.
     *
     * @return the study participant crf items
     */
    public List<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
        Collections.sort(studyParticipantCrfItems, new ParticipantCrfDisplayOrderComparator());
        return studyParticipantCrfItems;
    }

    /**
     * Adds the study participant crf item.
     *
     * @param studyParticipantCrfItem the study participant crf item
     */
    public void addStudyParticipantCrfItem(
            StudyParticipantCrfItem studyParticipantCrfItem) {
        if (studyParticipantCrfItem != null) {
            studyParticipantCrfItem.setStudyParticipantCrfSchedule(this);
            studyParticipantCrfItems.add(studyParticipantCrfItem);
        }
    }

    public HashMap<ProCtcTerm, ArrayList<ArrayList>> getSymptomItems() {
        HashMap<ProCtcTerm, ArrayList<ArrayList>> symptomMap = new HashMap();
        ArrayList<ArrayList> spCrfItems;
        Integer counter = 0;
        for (StudyParticipantCrfItem studyParticipantCrfItem : getStudyParticipantCrfItems()) {
            ArrayList itemCounter = new ArrayList();
            ProCtcTerm symptom = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm();
            if (symptomMap.containsKey(symptom)) {
                spCrfItems = symptomMap.get(symptom);
            } else {
                spCrfItems = new ArrayList();
                symptomMap.put(symptom, spCrfItems);
            }
            itemCounter.add(studyParticipantCrfItem);
            itemCounter.add(counter);
            spCrfItems.add(itemCounter);
            counter++;
        }
        return symptomMap;
    }

    /**
     * Gets the start date.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     *
     * @param startDate the new start date
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        setWeekInYear(c.get(Calendar.WEEK_OF_YEAR));
        setMonthInYear(c.get(Calendar.MONTH));
    }


    /**
     * Gets the due date.
     *
     * @return the due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate the new due date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
     * Gets the status.
     *
     * @return the status
     */
    public CrfStatus getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(CrfStatus status) {
        this.status = status;
    }

    /**
     * Gets the study participant crf schedule added questions.
     *
     * @return the study participant crf schedule added questions
     */
    public List<StudyParticipantCrfScheduleAddedQuestion> getStudyParticipantCrfScheduleAddedQuestions() {
        Collections.sort(studyParticipantCrfScheduleAddedQuestions, new ParticipantAddedQuestionDisplayOrderComparator());
        return studyParticipantCrfScheduleAddedQuestions;
    }

    /**
     * Adds the study participant crf schedule added question.
     *
     * @param studyParticipantCrfScheduleAddedQuestion
     *         the study participant crf schedule added question
     */
    public void addStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion) {
        if (studyParticipantCrfScheduleAddedQuestion != null) {
            studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(this);
            studyParticipantCrfScheduleAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);

        }
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public Integer getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(Integer cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public Integer getCycleDay() {
        return cycleDay;
    }

    public void setCycleDay(Integer cycleDay) {
        this.cycleDay = cycleDay;
    }

    public Integer getWeekInYear() {
        return weekInYear;
    }

    public void setWeekInYear(Integer weekInYear) {
        this.weekInYear = weekInYear;
    }

    public Integer getMonthInYear() {
        return monthInYear;
    }

    public void setMonthInYear(Integer monthInYear) {
        this.monthInYear = monthInYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfSchedule that = (StudyParticipantCrfSchedule) o;

        if (holiday != that.holiday) return false;
        if (cycleDay != null ? !cycleDay.equals(that.cycleDay) : that.cycleDay != null) return false;
        if (cycleNumber != null ? !cycleNumber.equals(that.cycleNumber) : that.cycleNumber != null) return false;
        if (!dueDate.equals(that.dueDate)) return false;
        if (!startDate.equals(that.startDate)) return false;
        if (status != that.status) return false;
        if (!studyParticipantCrf.equals(that.studyParticipantCrf)) return false;
        if (!monthInYear.equals(that.monthInYear)) return false;
        if (!weekInYear.equals(that.weekInYear)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + dueDate.hashCode();
        result = 31 * result + (holiday ? 1 : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (cycleNumber != null ? cycleNumber.hashCode() : 0);
        result = 31 * result + (cycleDay != null ? cycleDay.hashCode() : 0);
        result = 31 * result + weekInYear.hashCode();
        result = 31 * result + monthInYear.hashCode();
        result = 31 * result + studyParticipantCrf.hashCode();
        return result;
    }
}