package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

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

    @Column(name = "week_in_study", nullable = false)
    private Integer weekInStudy;

    @Column(name = "month_in_study", nullable = false)
    private Integer monthInStudy;

    @Column(name = "baseline", nullable = false)
    private boolean baseline = false;


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
        Collections.sort(studyParticipantCrfItems, new DisplayOrderComparator());
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

    public HashMap<String, ArrayList<String[]>> getSymptomItems() {
        HashMap<String, ArrayList<String[]>> symptomMap = new HashMap();
        for (StudyParticipantCrfItem studyParticipantCrfItem : getStudyParticipantCrfItems()) {
            String symptom = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getTerm();
            String question = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getQuestionText();
            String answer = studyParticipantCrfItem.getProCtcValidValue().getValue();
            addQuestionAnswerToMap(symptomMap, symptom, question, answer);

        }
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : studyParticipantCrfScheduleAddedQuestions) {
            Question q = studyParticipantCrfScheduleAddedQuestion.proCtcOrMeddraQuestion();
            String symptom = "";
            String question = "";
            String answer = "";
            if (q instanceof ProCtcQuestion) {
                ProCtcQuestion pq = (ProCtcQuestion) q;
                symptom = pq.getProCtcTerm().getTerm();
                question = pq.getQuestionText();
                answer = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue().getValue();
            }
            if (q instanceof MeddraQuestion) {
                MeddraQuestion mq = (MeddraQuestion) q;
                symptom = mq.getLowLevelTerm().getFullName();
                question = mq.getQuestionText();
                answer = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue().getValue();
            }

            addQuestionAnswerToMap(symptomMap, symptom, question, answer);
        }
        return symptomMap;
    }

    private void addQuestionAnswerToMap(HashMap<String, ArrayList<String[]>> symptomMap, String symptom, String question, String answer) {
        ArrayList<String[]> questionsAndAnswers;
        String[] questionAnswer = new String[2];
        questionAnswer[0] = question;
        questionAnswer[1] = answer;
        if (symptomMap.containsKey(symptom)) {
            questionsAndAnswers = symptomMap.get(symptom);
        } else {
            questionsAndAnswers = new ArrayList<String[]>();
            symptomMap.put(symptom, questionsAndAnswers);
        }
        questionsAndAnswers.add(questionAnswer);
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
        Date baselineDate = new Date();
        StudyParticipantCrfSchedule crfSchedule = studyParticipantCrf.getBaseLineSchedule();
        if (crfSchedule != null) {
            baselineDate = crfSchedule.getStartDate();
        }
        setWeekInStudy(DateUtils.weeksBetweenDates(startDate, baselineDate));
        setMonthInStudy(DateUtils.monthsBetweenDates(startDate, baselineDate));
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
        Collections.sort(studyParticipantCrfScheduleAddedQuestions, new DisplayOrderComparator());
        return studyParticipantCrfScheduleAddedQuestions;
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

    public Integer getWeekInStudy() {
        return weekInStudy;
    }

    public void setWeekInStudy(Integer weekInStudy) {
        this.weekInStudy = weekInStudy;
    }

    public Integer getMonthInStudy() {
        return monthInStudy;
    }

    public void setMonthInStudy(Integer monthInStudy) {
        this.monthInStudy = monthInStudy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrfSchedule that = (StudyParticipantCrfSchedule) o;

        if (baseline != that.baseline) return false;
        if (holiday != that.holiday) return false;
        if (cycleDay != null ? !cycleDay.equals(that.cycleDay) : that.cycleDay != null) return false;
        if (cycleNumber != null ? !cycleNumber.equals(that.cycleNumber) : that.cycleNumber != null) return false;
        if (dueDate != null ? !dueDate.equals(that.dueDate) : that.dueDate != null) return false;
        if (monthInStudy != null ? !monthInStudy.equals(that.monthInStudy) : that.monthInStudy != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (status != that.status) return false;
        if (studyParticipantCrf != null ? !studyParticipantCrf.equals(that.studyParticipantCrf) : that.studyParticipantCrf != null)
            return false;
        if (weekInStudy != null ? !weekInStudy.equals(that.weekInStudy) : that.weekInStudy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
        result = 31 * result + (holiday ? 1 : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (cycleNumber != null ? cycleNumber.hashCode() : 0);
        result = 31 * result + (cycleDay != null ? cycleDay.hashCode() : 0);
        result = 31 * result + (weekInStudy != null ? weekInStudy.hashCode() : 0);
        result = 31 * result + (monthInStudy != null ? monthInStudy.hashCode() : 0);
        result = 31 * result + (baseline ? 1 : 0);
        result = 31 * result + (studyParticipantCrf != null ? studyParticipantCrf.hashCode() : 0);
        return result;
    }

    public boolean isBaseline() {
        return baseline;
    }

    public void setBaseline(boolean baseline) {
        this.baseline = baseline;
    }

    public StudyParticipantCrfScheduleAddedQuestion addStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
        studyParticipantCrfScheduleAddedQuestion.setProCtcQuestion(studyParticipantCrfAddedQuestion.getProCtcQuestion());
        studyParticipantCrfScheduleAddedQuestion.setMeddraQuestion(studyParticipantCrfAddedQuestion.getMeddraQuestion());
        studyParticipantCrfScheduleAddedQuestion.setPageNumber(studyParticipantCrfAddedQuestion.getPageNumber());
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfAddedQuestion(studyParticipantCrfAddedQuestion);
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(this);
        studyParticipantCrfScheduleAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
        return studyParticipantCrfScheduleAddedQuestion;
    }
}