package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
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

    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<UserNotification> userNotifications = new ArrayList<UserNotification>();

    @Column(name = "form_submission_mode", nullable = true)
    @Enumerated(value = EnumType.STRING)
    private AppMode formSubmissionMode;

    @OneToOne(mappedBy = "studyParticipantCrfSchedule")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification;
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

    private void mapQuestionAndAnswer(Map<String, List<List>> symptomMap, String symptom, String question, String answer) {
        List<List> questionsAndAnswers;
        List questionAnswer = new ArrayList();
        questionAnswer.add(question);
        questionAnswer.add(answer);
        if (symptomMap.containsKey(symptom)) {
            questionsAndAnswers = symptomMap.get(symptom);
        } else {
            questionsAndAnswers = new ArrayList<List>();
            symptomMap.put(symptom, questionsAndAnswers);
        }
        questionsAndAnswers.add(questionAnswer);
    }

    public Map getSymptomItems() {
        Map<String, List<List>> symptomMap = new LinkedHashMap<String, List<List>>();
        for (StudyParticipantCrfItem studyParticipantCrfItem : getStudyParticipantCrfItems()) {
            String symptom = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getTerm();
            String question = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getQuestionText();
            String answer = studyParticipantCrfItem.getProCtcValidValue() == null ? "" : studyParticipantCrfItem.getProCtcValidValue().getValue();
            mapQuestionAndAnswer(symptomMap, symptom, question, answer);
        }
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            Question q = studyParticipantCrfScheduleAddedQuestion.getQuestion();
            String symptom = "";
            String question = q.getQuestionText();
            String answer = "";
            if (q instanceof ProCtcQuestion) {
                symptom = ((ProCtcQuestion) q).getProCtcTerm().getTerm();
                answer = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue() == null ? "" : studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue().getValue();
            }
            if (q instanceof MeddraQuestion) {
                symptom = ((MeddraQuestion) q).getLowLevelTerm().getFullName();
                answer = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue() == null ? "" : studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue().getValue();
            }
            mapQuestionAndAnswer(symptomMap, symptom, question, answer);
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
        Date baselineDate = studyParticipantCrf.getStartDate();
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

    public AppMode getFormSubmissionMode() {
        return formSubmissionMode;
    }

    public void setFormSubmissionMode(AppMode formSubmissionMode) {
        this.formSubmissionMode = formSubmissionMode;
    }

    public StudyParticipantCrfScheduleNotification getStudyParticipantCrfScheduleNotification() {
        return studyParticipantCrfScheduleNotification;
    }

    public void setStudyParticipantCrfScheduleNotification(StudyParticipantCrfScheduleNotification studyParticipantCrfScheduleNotification) {
        this.studyParticipantCrfScheduleNotification = studyParticipantCrfScheduleNotification;
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

        if (formSubmissionMode != null ? !formSubmissionMode.equals(that.formSubmissionMode) : that.formSubmissionMode != null) return false;

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
        result = 31 * result + (formSubmissionMode != null ? formSubmissionMode.hashCode() : 0);
        return result;
    }

    public boolean isBaseline() {
        return baseline;
    }

    public void setBaseline(boolean baseline) {
        this.baseline = baseline;
    }

    public StudyParticipantCrfScheduleAddedQuestion addStudyParticipantCrfScheduleAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion, boolean firstTime) {
        if (studyParticipantCrfAddedQuestion.getProCtcQuestion() == null && studyParticipantCrfAddedQuestion.getMeddraQuestion() == null) {
            return null;
        }
        StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion = new StudyParticipantCrfScheduleAddedQuestion();
        if (studyParticipantCrfAddedQuestion.getProCtcQuestion() != null) {
            studyParticipantCrfScheduleAddedQuestion.setProCtcQuestion(studyParticipantCrfAddedQuestion.getProCtcQuestion());
        }
        if (studyParticipantCrfAddedQuestion.getMeddraQuestion() != null) {
            studyParticipantCrfScheduleAddedQuestion.setMeddraQuestion(studyParticipantCrfAddedQuestion.getMeddraQuestion());
            if (!firstTime) {
                if (studyParticipantCrfAddedQuestion.getMeddraQuestion().getLowLevelTerm().isParticipantAdded()) {
                    String meddraTerm = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getMeddraTerm();
                    String recallPeriod = getStudyParticipantCrf().getCrf().getRecallPeriod();
                    String recallPeriodFirstChar = recallPeriod.substring(0, 1).toUpperCase();
                    String recallPeriodEnd = recallPeriod.substring(1);
                    recallPeriod = recallPeriodFirstChar + recallPeriodEnd;
                    studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().setQuestionText("The last time you used this system, you reported " + meddraTerm + ". " + recallPeriod + ", have you still had this?");
                }
            }
        }
        studyParticipantCrfScheduleAddedQuestion.setPageNumber(studyParticipantCrfAddedQuestion.getPageNumber());
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfAddedQuestionId(studyParticipantCrfAddedQuestion.getId());
        studyParticipantCrfScheduleAddedQuestion.setStudyParticipantCrfSchedule(this);
        studyParticipantCrfScheduleAddedQuestions.add(studyParticipantCrfScheduleAddedQuestion);
        return studyParticipantCrfScheduleAddedQuestion;
    }

    public Hashtable<Integer, String> getDisplayRules() {
        Hashtable<Integer, String> displayRules = new Hashtable<Integer, String>();
        for (StudyParticipantCrfItem studyParticipantCrfItem : getStudyParticipantCrfItems()) {
            CrfPageItem crfPageItem = studyParticipantCrfItem.getCrfPageItem();
            String displayRule = "";
            for (CrfPageItemDisplayRule crfPageItemDisplayRule : crfPageItem.getCrfPageItemDisplayRules()) {
                displayRule = displayRule + "~" + crfPageItemDisplayRule.getProCtcValidValue().getId();
            }
            displayRules.put(crfPageItem.getId(), displayRule);
        }
        return displayRules;
    }

    public Set getParticipantAddedSymptoms() {
        HashSet symptoms = new HashSet();
        for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : getStudyParticipantCrf().getStudyParticipantCrfAddedQuestions()) {
            if (studyParticipantCrfAddedQuestion.getProCtcQuestion() != null) {
                symptoms.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getTerm());
            }
            if (studyParticipantCrfAddedQuestion.getMeddraQuestion() != null) {
                symptoms.add(studyParticipantCrfAddedQuestion.getMeddraQuestion().getLowLevelTerm().getMeddraTerm());
            }
        }
        return symptoms;
    }

    public Map<ProCtcTerm, ArrayList<ArrayList>> getCrfItemsBySymptom() {
        Map<ProCtcTerm, ArrayList<ArrayList>> symptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ArrayList>>();
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

    public void addParticipantAddedQuestions() {
        if (getStudyParticipantCrfScheduleAddedQuestions().size() == 0) {
            for (StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion : studyParticipantCrf.getStudyParticipantCrfAddedQuestions()) {
                addStudyParticipantCrfScheduleAddedQuestion(studyParticipantCrfAddedQuestion, false);
            }
        }
    }

    public Map getParticipantAddedProCtcQuestionsBySymptom() {
        Map<ProCtcTerm, List<List>> symptomMap = new LinkedHashMap();
        addParticipantAddedQuestions();
        List<List> studyParticipantCrfScheduleAddedQuestions;
        Integer counter = 0;
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            ArrayList itemCounter = new ArrayList();
            if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                ProCtcTerm symptom = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion().getProCtcTerm();
                if (symptomMap.containsKey(symptom)) {
                    studyParticipantCrfScheduleAddedQuestions = symptomMap.get(symptom);
                } else {
                    studyParticipantCrfScheduleAddedQuestions = new ArrayList();
                    symptomMap.put(symptom, studyParticipantCrfScheduleAddedQuestions);
                }
                itemCounter.add(studyParticipantCrfScheduleAddedQuestion);
                itemCounter.add(counter);
                studyParticipantCrfScheduleAddedQuestions.add(itemCounter);
                counter++;
            }
        }
        return symptomMap;
    }

    public Map getParticipantAddedMeddraQuestionsBySymptom() {
        addParticipantAddedQuestions();
        Map<String, List<List>> symptomMap = new LinkedHashMap();
        List<List> studyParticipantCrfScheduleAddedQuestions;
        Integer counter = 0;
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            ArrayList itemCounter = new ArrayList();
            if (studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion() != null) {
                String symptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getFullName();
                if (symptomMap.containsKey(symptom)) {
                    studyParticipantCrfScheduleAddedQuestions = symptomMap.get(symptom);
                } else {
                    studyParticipantCrfScheduleAddedQuestions = new ArrayList();
                    symptomMap.put(symptom, studyParticipantCrfScheduleAddedQuestions);
                }
                itemCounter.add(studyParticipantCrfScheduleAddedQuestion);
                itemCounter.add(counter);
                studyParticipantCrfScheduleAddedQuestions.add(itemCounter);
                counter++;
            }
        }
        return symptomMap;
    }

    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }
}