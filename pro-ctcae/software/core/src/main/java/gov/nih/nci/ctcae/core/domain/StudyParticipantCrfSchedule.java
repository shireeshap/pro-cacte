package gov.nih.nci.ctcae.core.domain;


import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;


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
public class StudyParticipantCrfSchedule extends BasePersistable implements Comparable<StudyParticipantCrfSchedule>{

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

    /**
     * Gets the study participant crf items.
     *
     * @return the study participant crf items
     */
    public List<StudyParticipantCrfItem> getStudyParticipantCrfItems() {
    	if(studyParticipantCrfItems == null || studyParticipantCrfItems.size() == 0){
    		if(studyParticipantCrf != null && studyParticipantCrf.getCrf() != null &&
    			 studyParticipantCrf.getCrf().getCrfPagesSortedByPageNumber() != null){
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
                question = ((ProCtcQuestion) q).getProCtcQuestionVocab().getQuestionTextEnglish();
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
        String language = this.getStudyParticipantCrf().getStudyParticipantAssignment().getHomeWebLanguage();
        if (language == null || language == "") {
            language = SupportedLanguageEnum.ENGLISH.getName();
        }
        Map<String, List<List>> symptomMap = new LinkedHashMap();
        List<List> studyParticipantCrfScheduleAddedQuestions;
        Integer counter = 0;
        for (StudyParticipantCrfScheduleAddedQuestion studyParticipantCrfScheduleAddedQuestion : getStudyParticipantCrfScheduleAddedQuestions()) {
            ArrayList itemCounter = new ArrayList();
            if (studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion() != null) {
                String symptom;
                if (language.equals(SupportedLanguageEnum.ENGLISH.getName())) {
                    symptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getFullName(SupportedLanguageEnum.ENGLISH);
                } else {
                    symptom = studyParticipantCrfScheduleAddedQuestion.getMeddraQuestion().getLowLevelTerm().getLowLevelTermVocab().getMeddraTermSpanish();
                }
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

    /**
     * Will populate the CRF items
     
    public void scheduleStudyParticipantCRFItems() {
        for (CRFPage crfPage : getStudyParticipantCrf().getCrf().getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
                studyParticipantCrfItem.setCrfPageItem(crfPageItem);
                addStudyParticipantCrfItem(studyParticipantCrfItem);
            }
        }
    }*/


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
        if (DateUtils.compareDate(getStartDate(), offHoldEffectiveDate) > 0) {
            setStatus(CrfStatus.ONHOLD);
        }
    }

    public List<IvrsSchedule> getIvrsSchedules() {
        return ivrsSchedules;
    }

    public void setIvrsSchedules(List<IvrsSchedule> ivrsSchedules) {
        this.ivrsSchedules = ivrsSchedules;
    }
}