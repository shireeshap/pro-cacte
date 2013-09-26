package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.constants.ProctcTermTypeBasedCategoryEnum;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


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
public class StudyParticipantCrfSchedule extends BaseVersionable implements Comparable<StudyParticipantCrfSchedule> {

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

    /**
     * The completion date.
     */
    @Column(name = "form_completion_date")
    private Date completionDate;

    /**
     * The file_path .
     */
    @Column(name = "file_path")
    private String filePath;

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

    @Column(name = "verbatim", nullable = true)
    private String verbatim;

    @Column(name = "mark_delete", nullable = false)
    private boolean markDelete = false;

    @Column(name = "health_amount", nullable = true)
    private Integer healthAmount;    

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
     * The study participant crf grades.
     */
    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfGrades> studyParticipantCrfGrades = new ArrayList<StudyParticipantCrfGrades>();

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

    @OneToMany(mappedBy = "studyParticipantCrfSchedule", fetch = FetchType.EAGER)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<IvrsSchedule> ivrsSchedules = new ArrayList<IvrsSchedule>();

    @Transient
    private String language;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVerbatim() {
        return verbatim;
    }

    public void setVerbatim(String verbatim) {
        this.verbatim = verbatim;
    }

    public boolean isMarkDelete() {
        return markDelete;
    }

    public void setMarkDelete(boolean markDelete) {
        this.markDelete = markDelete;
    }

    /**
     * Gets the study participant crf items and creates them if not present.
     *
     * @return the study participant crf items
     */
    public List<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
        if (studyParticipantCrfItems == null || studyParticipantCrfItems.size() == 0) {
            if (studyParticipantCrf != null && studyParticipantCrf.getCrf() != null &&
                    studyParticipantCrf.getCrf().getCrfPagesSortedByPageNumber() != null) {
                for (CRFPage crfPage : studyParticipantCrf.getCrf().getCrfPagesSortedByPageNumber()) {
                    for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
                        studyParticipantCrfItem.setCrfPageItem(crfPageItem);
                        this.addStudyParticipantCrfItem(studyParticipantCrfItem);
                    }
                }
            }
        }
        Collections.sort(studyParticipantCrfItems, new DisplayOrderComparator());
        return studyParticipantCrfItems;
    }
    
    /**Gets study_participant_crf_grades for the schedule or creates them if not present.
     * @return the study participant crf grades
     */
    public List<StudyParticipantCrfGrades> getStudyParticipantCrfGrades(){
    	if(!studyParticipantCrfGrades.isEmpty()){
    		
    		return studyParticipantCrfGrades;
    	}
    	return new ArrayList<StudyParticipantCrfGrades>();
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
    
    public void addStudyParticipantCrfGrade(StudyParticipantCrfGrades studyParticipantCrfGrade) {
        if (studyParticipantCrfGrade != null) {
        	studyParticipantCrfGrade.setStudyParticipantCrfSchedule(this);
        	studyParticipantCrfGrades.add(studyParticipantCrfGrade);
        }
    }

    private void mapQuestionAndAnswer(Map<String, List<List>> symptomMap, String symptom, String question, String answer) {
        List<List> questionsAndAnswers;
        List<String> questionAnswer = new ArrayList<String>();
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
            String symptom = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcTerm().getProCtcTermVocab().getTermEnglish();
            String question = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getQuestionText(SupportedLanguageEnum.ENGLISH);
            String answer = studyParticipantCrfItem.getProCtcValidValue() == null ? "" : studyParticipantCrfItem.getProCtcValidValue().getValue(SupportedLanguageEnum.ENGLISH);
            mapQuestionAndAnswer(symptomMap, symptom, question, answer);
        }
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            Question q = studyParticipantCrfScheduleAddedQuestion.getQuestion();
            String symptom = "";
            String question = "";
            String answer = "";
            if (q instanceof ProCtcQuestion) {
                question = ((ProCtcQuestion) q).getProCtcQuestionVocab().getQuestionTextEnglish();
                symptom = ((ProCtcQuestion) q).getProCtcTerm().getProCtcTermVocab().getTermEnglish();
                answer = studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue() == null ? "" : studyParticipantCrfScheduleAddedQuestion.getProCtcValidValue().getValue(SupportedLanguageEnum.ENGLISH);
            }
            if (q instanceof MeddraQuestion) {
                question = ((MeddraQuestion) q).getMeddraQuestionVocab().getQuestionTextEnglish();
                symptom = ((MeddraQuestion) q).getLowLevelTerm().getFullName(SupportedLanguageEnum.ENGLISH);
                answer = studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue() == null ? "" : studyParticipantCrfScheduleAddedQuestion.getMeddraValidValue().getValue(SupportedLanguageEnum.ENGLISH);
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
     * Gets the file path
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param filePath the new file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Gets the completion Date
     *
     * @return the completion date
     */
    public Date getCompletionDate() {
        return completionDate;
    }

    /**
     * Sets the completion Date.
     *
     * @param completionDate the new completion date
     */
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
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

        if (formSubmissionMode != null ? !formSubmissionMode.equals(that.formSubmissionMode) : that.formSubmissionMode != null)
            return false;
        if (completionDate != null ? !completionDate.equals(that.completionDate) : that.completionDate != null)
            return false;
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null)
            return false;
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
        result = 31 * result + (completionDate != null ? completionDate.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        return result;
    }

    public int compareTo(StudyParticipantCrfSchedule crfSchedule) {
        return dueDate.compareTo(crfSchedule.dueDate);
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
                    String meddraTerm = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH);
                    String recallPeriod = getStudyParticipantCrf().getCrf().getRecallPeriod();
                    String recallPeriodFirstChar = recallPeriod.substring(0, 1).toUpperCase();
                    String recallPeriodEnd = recallPeriod.substring(1);
                    recallPeriod = recallPeriodFirstChar + recallPeriodEnd;
                    studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().setQuestionText("The last time you used this system, you reported " + meddraTerm.toUpperCase() + ". Do you still have this?", SupportedLanguageEnum.ENGLISH);
                    studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().setQuestionText("La última vez que utilizó este sistema, se informó " + meddraTerm.toUpperCase() + ".  Todavía tiene este?", SupportedLanguageEnum.SPANISH);
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
                symptoms.add(studyParticipantCrfAddedQuestion.getProCtcQuestion().getProCtcTerm().getProCtcTermVocab().getTermEnglish());
            }
            if (studyParticipantCrfAddedQuestion.getMeddraQuestion() != null) {
                symptoms.add(studyParticipantCrfAddedQuestion.getMeddraQuestion().getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH));
            }
        }
        return symptoms;
    }

    public Map<ProCtcTerm, ArrayList<ArrayList>> getCrfItemsBySymptom() {
        Map<ProCtcTerm, ArrayList<ArrayList>> symptomMap = new LinkedHashMap<ProCtcTerm, ArrayList<ArrayList>>();
        ArrayList<ArrayList> spCrfItems;
        Integer counter = 0;
        String lang = this.getStudyParticipantCrf().getStudyParticipantAssignment().getHomeWebLanguage();
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
        Map<String, List<List>> symptomMap = new LinkedHashMap();
        addParticipantAddedQuestions();
        List<List> studyParticipantCrfScheduleAddedQuestions;
        Integer counter = 0;
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            ArrayList itemCounter = new ArrayList();
            boolean isProTerm = false;
            if (studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion() != null) {
                String proSymptom;
                isProTerm = true;
                if (language.equals("en")) {
                    proSymptom = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion().getProCtcTerm().getProCtcTermVocab().getTermEnglish();
                } else {
                    proSymptom = studyParticipantCrfScheduleAddedQuestion.getProCtcQuestion().getProCtcTerm().getProCtcTermVocab().getTermSpanish();
                }
                if (symptomMap.containsKey(proSymptom)) {
                    studyParticipantCrfScheduleAddedQuestions = symptomMap.get(proSymptom);
                } else {
                    studyParticipantCrfScheduleAddedQuestions = new ArrayList();
                    symptomMap.put(proSymptom, studyParticipantCrfScheduleAddedQuestions);
                }
                itemCounter.add(studyParticipantCrfScheduleAddedQuestion);
                itemCounter.add(counter);
                itemCounter.add(isProTerm);
                studyParticipantCrfScheduleAddedQuestions.add(itemCounter);
                counter++;
            }
            if (studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion() != null) {
                String meddraSymptom;
                if (language.equals("en")) {
                    meddraSymptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getFullName(SupportedLanguageEnum.ENGLISH);
                } else {
                    meddraSymptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getLowLevelTermVocab().getMeddraTermSpanish();
                }
                if (symptomMap.containsKey(meddraSymptom)) {
                    studyParticipantCrfScheduleAddedQuestions = symptomMap.get(meddraSymptom);
                } else {
                    studyParticipantCrfScheduleAddedQuestions = new ArrayList();
                    symptomMap.put(meddraSymptom, studyParticipantCrfScheduleAddedQuestions);
                }
                itemCounter.add(studyParticipantCrfScheduleAddedQuestion);
                itemCounter.add(counter);
                itemCounter.add(isProTerm);
                studyParticipantCrfScheduleAddedQuestions.add(itemCounter);
                counter++;
            }
        }
        return symptomMap;
    }


    /**
     * Updates the ivrs schedules date basewd on the offset provided.
     *
     * @param studyParticipantCrf the study participant crf
     */
    public void updateIvrsSchedules(StudyParticipantCrf studyParticipantCrf, int offset) {
        //update ivrsSchedule for IVRS app Modes
        boolean isIvrs = false;
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrf.getStudyParticipantAssignment();
        for (StudyParticipantMode spm : studyParticipantAssignment.getStudyParticipantModes()) {
            if (spm.getMode().equals(AppMode.IVRS)) {
                isIvrs = true;
            }
        }
        if (isIvrs) {
            for (IvrsSchedule ivrSchedule : getIvrsSchedules()) {
                if (ivrSchedule.getCallStatus().equals(IvrsCallStatus.PENDING)) {
                    Calendar c1 = Calendar.getInstance();
                    c1.setTimeInMillis(ivrSchedule.getPreferredCallTime().getTime());
                    Calendar c2 = Calendar.getInstance();
                    c2.setTimeInMillis(ivrSchedule.getNextCallTime().getTime());
                    c1.add(Calendar.DATE, offset);
                    c2.add(Calendar.DATE, offset);

                    ivrSchedule.setNextCallTime(c1.getTime());
                    ivrSchedule.setPreferredCallTime(c2.getTime());
                }
            }
        }
    }

    /**
     * Update ivrs schedules status.
     *
     * @param ivrsCallStatus the ivrs call status
     */
    public void updateIvrsSchedulesStatus(IvrsCallStatus ivrsCallStatus) {
        boolean isIvrs = false;
        StudyParticipantAssignment studyParticipantAssignment = studyParticipantCrf.getStudyParticipantAssignment();
        for (StudyParticipantMode spm : studyParticipantAssignment.getStudyParticipantModes()) {
            if (spm.getMode().equals(AppMode.IVRS)) {
                isIvrs = true;
            }
        }
        if (isIvrs) {
            for (IvrsSchedule ivrsSchedule : getIvrsSchedules()) {
                ivrsSchedule.setCallStatus(ivrsCallStatus);
            }
        }
    }


    public List<UserNotification> getUserNotifications() {
        return userNotifications;
    }

    /**
     * Will on hold the if it is not started yet.
     *
     * @param offHoldEffectiveDate - The date on which OffHold starts
     */
    public void putOnHold(Date offHoldEffectiveDate) {
        if (DateUtils.compareDate(getStartDate(), offHoldEffectiveDate) >= 0) {
            setStatus(CrfStatus.ONHOLD);
            for (IvrsSchedule ivrsSchedule : getIvrsSchedules()) {
                if (ivrsSchedule.getCallStatus().equals(IvrsCallStatus.PENDING)) {
                    ivrsSchedule.setCallStatus(IvrsCallStatus.ON_HOLD);
                }
            }
        }
    }
    
    /**
     * Checks if is deletable.
     * Use this to determine if the schedule can be deleted from the patients calendar.
     * @return true, if is deletable
     */
    @Transient
    public boolean isDeletable(){
    	if (this.status.equals(CrfStatus.SCHEDULED))
    		return true;
    	return false;
    }

    public List<IvrsSchedule> getIvrsSchedules() {
        return ivrsSchedules;
    }

    public void setIvrsSchedules(List<IvrsSchedule> ivrsSchedules) {
        this.ivrsSchedules = ivrsSchedules;
    }
    
    
    public Integer getHealthAmount() {
		return healthAmount;
	}

	public void setHealthAmount(Integer healthAmount) {
		this.healthAmount = healthAmount;
	}
	
	
	/**generateStudyParticipantCrfGrades method.
	 * @param proctcaeGradeMappingVersion
	 * Generates ctcae grades for all proCtc and lowLevel terms in a survey (*only for non Eq5D surveys)
	 * This method should only be called upon a completed schedule.
	 */
	public void generateStudyParticipantCrfGrades(ProctcaeGradeMappingVersion proctcaeGradeMappingVersion){
		if(this.getStudyParticipantCrfGrades().isEmpty() && !getStudyParticipantCrf().getCrf().isEq5d()){
			Map<ProCtcTerm, Map<ProCtcQuestionType, String>> proResponseMap = new HashMap<ProCtcTerm, Map<ProCtcQuestionType, String>>();
			Map<LowLevelTerm, Map<ProCtcQuestionType, String>> meddraResponseMap = new HashMap<LowLevelTerm, Map<ProCtcQuestionType, String>>();
			
			for (StudyParticipantCrfItem spcCrfItem : getStudyParticipantCrfItems()) {
				StudyParticipantCrfGrades studyParticipantCrfGrade = createStudyParticipantCrfGrade(spcCrfItem, proctcaeGradeMappingVersion);
				if(studyParticipantCrfGrade != null){
					addToResponseMap(proResponseMap, spcCrfItem);
				}
			}
			for (StudyParticipantCrfScheduleAddedQuestion spcsaq : getStudyParticipantCrfScheduleAddedQuestions()) {
				StudyParticipantCrfGrades studyParticipantCrfGrade = createStudyParticipantCrfGrade(spcsaq, proctcaeGradeMappingVersion);
				if(studyParticipantCrfGrade != null){
					addToResponseMap(proResponseMap, meddraResponseMap, spcsaq);
				}
			}
			
			generateFinalGradeFromResponses(proResponseMap, meddraResponseMap, proctcaeGradeMappingVersion);
		}
	}
	
    private void generateFinalGradeFromResponses(Map<ProCtcTerm, Map<ProCtcQuestionType, String>> proResponseMap, Map<LowLevelTerm, Map<ProCtcQuestionType, String>> meddraResponseMap,
    		ProctcaeGradeMappingVersion proctcaeGradeMappingVersion){
    	StudyParticipantCrfGrades studyParticipantCrfGrade;
    	try {
	    	for(ProCtcTerm symptom : proResponseMap.keySet()){
	    		studyParticipantCrfGrade = getStudyParticipantCrfGrade(symptom);
	    		if(studyParticipantCrfGrade != null){
	    			Map<ProCtcQuestionType, String> questionTypeMap = proResponseMap.get(symptom);
	    			ProctcaeGradeMapping gradeKey = generateGradeKey(questionTypeMap, symptom, proctcaeGradeMappingVersion);
	    			String evaluatedGrade = symptom.getProctcaeGradeMappingMap().get(gradeKey);
	    			if(studyParticipantCrfGrade.getGradeEvaluationDate() == null){
	    				studyParticipantCrfGrade.setGradeEvaluationDate(getStartDate());
	    			}
	    			studyParticipantCrfGrade.setGrade(evaluatedGrade);
	    		}
	    	}
	    	
	    	/**CtcaeGrade for all lowlevel terms should always be defaulted to "Present, Clinician Assess"
	    	 * (In future, if we have grade mapping available for lowLevel terms, 
	    	 *  we can then run the logic for evaluating it's grade, which will be similar to the ProCtcTerms above)
	    	 *  Note: LowLevelTerms includes a mix of some ctcae terms and also the participant added free text (added when reporting additional symptom).
	    	 */
	    	for(LowLevelTerm symptom : meddraResponseMap.keySet()){
	    		studyParticipantCrfGrade = getStudyParticipantCrfGrade(symptom);
	    		if(studyParticipantCrfGrade != null){
	    			if(studyParticipantCrfGrade.getGradeEvaluationDate() == null){
						studyParticipantCrfGrade.setGradeEvaluationDate(getStartDate());
					}
	    			studyParticipantCrfGrade.setGrade(ProctcaeGradeMapping.PRESENT_CLINICIAN_ASSESS);
	    		}
	    	}
    	
    	}catch (Exception e) {
			logger.error("Error in generating ctcae grade in schedule id:" + getId() + " ,error message is: " + e.getMessage());
		}
    }
    
    /**generateGradeKey method
     * @param questionTypeMap
     * @param proCtcTerm
     * @param proctcaeGradeMappingVersion
     * Creates an instance of proctcaeGradeMapping with appropriate values set for each of ProCtcQuestionType responses.
     * This instance of proctcaeGradeMapping is used for querying the proctcaeGradeMappingMap to get corresponding ctcae grade.
     */
    public ProctcaeGradeMapping generateGradeKey(Map<ProCtcQuestionType, String> questionTypeMap, ProCtcTerm proCtcTerm, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion){
    	ProctcTermTypeBasedCategoryEnum category = proCtcTerm.getTypeBasedCategory();
    	ProctcaeGradeMapping key = null;
    	
    	if(ProctcTermTypeBasedCategoryEnum.CATEGORY_FSI.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryFSI(questionTypeMap.get(ProCtcQuestionType.FREQUENCY), questionTypeMap.get(ProCtcQuestionType.SEVERITY), 
    				questionTypeMap.get(ProCtcQuestionType.INTERFERENCE), proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_FS.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryFS(questionTypeMap.get(ProCtcQuestionType.FREQUENCY), questionTypeMap.get(ProCtcQuestionType.SEVERITY),
    				proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_SI.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategorySI(questionTypeMap.get(ProCtcQuestionType.SEVERITY), questionTypeMap.get(ProCtcQuestionType.INTERFERENCE),
    				proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_FI.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryFI(questionTypeMap.get(ProCtcQuestionType.FREQUENCY), questionTypeMap.get(ProCtcQuestionType.INTERFERENCE), 
    				proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_F.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryF(questionTypeMap.get(ProCtcQuestionType.FREQUENCY), proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_S.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryS(questionTypeMap.get(ProCtcQuestionType.SEVERITY), proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else if(ProctcTermTypeBasedCategoryEnum.CATEGORY_PA.equals(category)){
    		key = ProctcaeGradeMappingCreator.createCategoryPA(questionTypeMap.get(ProCtcQuestionType.PRESENT), proctcaeGradeMappingVersion, proCtcTerm);
    		
    	} else {
    		key = ProctcaeGradeMappingCreator.createCategoryAMT(questionTypeMap.get(ProCtcQuestionType.AMOUNT), proctcaeGradeMappingVersion, proCtcTerm);
    	}
    	
    	return key;
    }
	
    private void addToResponseMap(Map<ProCtcTerm, Map<ProCtcQuestionType, String>> proResponseMap, StudyParticipantCrfItem studyParticipantCrfItem){
    	Map<ProCtcQuestionType, String> responseMap;
    	ProCtcTerm proCtcTerm = studyParticipantCrfItem.getCrfPageItem().getCrfPage().getProCtcTerm();
    	ProCtcQuestionType questionType = studyParticipantCrfItem.getCrfPageItem().getProCtcQuestion().getProCtcQuestionType();
    	ProCtcValidValue proCtcValidValue = studyParticipantCrfItem.getProCtcValidValue();
    	if(proResponseMap.get(proCtcTerm) != null){
    		responseMap = proResponseMap.get(proCtcTerm);
    	} else {
    		responseMap = new HashMap<ProCtcQuestionType, String>();
    		proResponseMap.put(proCtcTerm, responseMap);
    	}
    	if(proCtcValidValue != null){
    		String responseCode = null;
    		if(proCtcValidValue.getResponseCode() != null){
    			Integer responseCodeIntVal = proCtcValidValue.getResponseCode();
    			// Responses like NotApplicable, Prefer not to answer, Not sexually active should be mapped to responseCode of Zero
    			responseCode = ((responseCodeIntVal > 4 | responseCodeIntVal < 0)  ? "0" : responseCodeIntVal.toString());
    		}
    		responseMap.put(questionType, responseCode);
    	} else {
    		responseMap.put(questionType, null);
    	}
    }
    
    private void addToResponseMap(Map<ProCtcTerm, Map<ProCtcQuestionType, String>> proResponseMap, Map<LowLevelTerm, Map<ProCtcQuestionType, String>> meddraResponseMap, StudyParticipantCrfScheduleAddedQuestion spcsAddedQuestion){
    	Map<ProCtcQuestionType, String> responseMap;
    	
    	if(spcsAddedQuestion.getProCtcQuestion() != null){
    		ProCtcQuestionType questionType = spcsAddedQuestion.getProCtcQuestion().getProCtcQuestionType();
    		ProCtcValidValue proCtcValidValue = spcsAddedQuestion.getProCtcValidValue();
    		ProCtcTerm proCtcTerm = spcsAddedQuestion.getProCtcQuestion().getProCtcTerm();
        	if(proResponseMap.get(proCtcTerm) != null){
        		responseMap = proResponseMap.get(proCtcTerm);
        	} else {
        		responseMap = new HashMap<ProCtcQuestionType, String>();
        		proResponseMap.put(proCtcTerm, responseMap);
        	}
    		if(proCtcValidValue != null){
        		String responseCode = null;
        		if(proCtcValidValue.getResponseCode() != null){
        			Integer responseCodeIntVal = proCtcValidValue.getResponseCode();
        			// Responses like NotApplicable, Prefer not to answer, Not sexually active should be mapped to responseCode of Zero
        			responseCode = ((responseCodeIntVal > 4 | responseCodeIntVal < 0) ? "0" : responseCodeIntVal.toString());
        		}
        		responseMap.put(questionType, responseCode);
        	} else {
        		responseMap.put(questionType, null);
        	}
    	} else {
    		ProCtcQuestionType questionType = spcsAddedQuestion.getMeddraQuestion().getProCtcQuestionType();
    		MeddraValidValue meddraValidValue = spcsAddedQuestion.getMeddraValidValue();
    		LowLevelTerm lowLevelTerm = spcsAddedQuestion.getMeddraQuestion().getLowLevelTerm();
    		if(meddraResponseMap.get(lowLevelTerm) != null){
        		responseMap = meddraResponseMap.get(lowLevelTerm);
        	} else {
        		responseMap = new HashMap<ProCtcQuestionType, String>();
        		meddraResponseMap.put(lowLevelTerm, responseMap);
        	}
    		if(meddraValidValue != null){
    			String displayOrder = null;
        		if(meddraValidValue.getDisplayOrder() != null){
        			Integer displayOrderIntVal = meddraValidValue.getDisplayOrder();
        			displayOrder = (displayOrderIntVal > 4 ? "0" : displayOrderIntVal.toString());
        		}
    			responseMap.put(questionType, displayOrder);
    		} else {
    			responseMap.put(questionType, null);
    		}
    	}
    }
	
	/**Creates a studyParticipantCrfGrade instance for a proCtcTerm in a survey, 
	 * if at least one of the questions for that term is answered to by the participant.
     */
    private StudyParticipantCrfGrades createStudyParticipantCrfGrade(StudyParticipantCrfItem studyParticipantCrfItem, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion){
    	if(studyParticipantCrfItem.getProCtcValidValue() != null){
    		ProCtcTerm proCtcTerm = studyParticipantCrfItem.getCrfPageItem().getCrfPage().getProCtcTerm();
    		StudyParticipantCrfGrades studyParticipantCrfGrade = getStudyParticipantCrfGrade(proCtcTerm);
    		if(studyParticipantCrfGrade == null){
    			studyParticipantCrfGrade = new StudyParticipantCrfGrades();
    			studyParticipantCrfGrade.setGradeMappingVersion(proctcaeGradeMappingVersion);
    			studyParticipantCrfGrade.setProCtcTerm(studyParticipantCrfItem.getCrfPageItem().getCrfPage().getProCtcTerm());
    			studyParticipantCrfGrade.setLowLevelTerm(null);
    			if(studyParticipantCrfItem.getReponseDate() != null && studyParticipantCrfGrade.getGradeEvaluationDate() == null){
    				studyParticipantCrfGrade.setGradeEvaluationDate(studyParticipantCrfItem.getReponseDate());
    			}
    			addStudyParticipantCrfGrade(studyParticipantCrfGrade);
    		}
    		return studyParticipantCrfGrade;
    		
    	} else {
    		return null;
    	}
    }
    
    /**Overloaded createStudyParticipantCrfGrade method
     * Creates a studyParticipantCrfGrade instance for a lowlevelTerm in a survey, 
     * if at least one of the questions for that term is answered to by the participant.
     */
    private StudyParticipantCrfGrades createStudyParticipantCrfGrade(StudyParticipantCrfScheduleAddedQuestion spcsAddedQuestion, ProctcaeGradeMappingVersion proctcaeGradeMappingVersion){
    	StudyParticipantCrfGrades studyParticipantCrfGrade;
    	if((spcsAddedQuestion.getProCtcValidValue() != null && spcsAddedQuestion.getMeddraValidValue() == null) || 
    			(spcsAddedQuestion.getMeddraValidValue() != null && spcsAddedQuestion.getProCtcValidValue() == null)){
    		
    		if(spcsAddedQuestion.getProCtcQuestion() != null){
    			studyParticipantCrfGrade = getStudyParticipantCrfGrade(spcsAddedQuestion.getProCtcQuestion().getProCtcTerm());
    		} else {
    			studyParticipantCrfGrade = getStudyParticipantCrfGrade(spcsAddedQuestion.getMeddraQuestion().getLowLevelTerm());
    		}
    		if(studyParticipantCrfGrade == null){
    			studyParticipantCrfGrade = new StudyParticipantCrfGrades();
    			studyParticipantCrfGrade.setGradeMappingVersion(proctcaeGradeMappingVersion);
    			if(spcsAddedQuestion.getReponseDate() != null && studyParticipantCrfGrade.getGradeEvaluationDate() == null){
    				studyParticipantCrfGrade.setGradeEvaluationDate(spcsAddedQuestion.getReponseDate());
    			}
    			if(spcsAddedQuestion.getProCtcQuestion() != null){
    				studyParticipantCrfGrade.setProCtcTerm(spcsAddedQuestion.getProCtcQuestion().getProCtcTerm());
    				studyParticipantCrfGrade.setLowLevelTerm(null);
    			} else {
    				studyParticipantCrfGrade.setLowLevelTerm(spcsAddedQuestion.getMeddraQuestion().getLowLevelTerm());
    				studyParticipantCrfGrade.setProCtcTerm(null);
    			}
    			addStudyParticipantCrfGrade(studyParticipantCrfGrade);
    		}
    		return studyParticipantCrfGrade;
    		
    	} else {
    		return null;
    	}
    }
    
    public StudyParticipantCrfGrades getStudyParticipantCrfGrade(ProCtcTerm proCtcTerm){
    	if(proCtcTerm != null){
    		for(StudyParticipantCrfGrades studyParticipantCrfGrade : getStudyParticipantCrfGrades()){
    			if((studyParticipantCrfGrade.getProCtcTerm() != null) && (studyParticipantCrfGrade.getProCtcTerm().equals(proCtcTerm))){
    				return studyParticipantCrfGrade;
    			}
    		}
    	}
    	return null;
    }
    
    public StudyParticipantCrfGrades getStudyParticipantCrfGrade(LowLevelTerm lowLevelTerm){
    	if(lowLevelTerm != null){
    		for(StudyParticipantCrfGrades studyParticipantCrfGrade : getStudyParticipantCrfGrades()){
    			if((studyParticipantCrfGrade.getLowLevelTerm() != null) && (studyParticipantCrfGrade.getLowLevelTerm().equals(lowLevelTerm))){
    				return studyParticipantCrfGrade;
    			}
    		}
    	}
    	return null;
    }
}