package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.util.*;

/**
 * The Class StudyParticipantCrf.
 *
 * @author
 * @since Oct 7, 2008
 */

@Entity
@Table(name = "STUDY_PARTICIPANT_CRFS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_study_participant_crfs_id")})
public class StudyParticipantCrf extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The study participant crf schedules.
     */
    @OneToMany(mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    @OrderBy("startDate asc ")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();

    /**
     * The study participant crf added questions.
     */
    @OneToMany(mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
    private List<StudyParticipantCrfAddedQuestion> studyParticipantCrfAddedQuestions = new ArrayList<StudyParticipantCrfAddedQuestion>();

    /**
     * The crf.
     */
    @JoinColumn(name = "crf_id", referencedColumnName = "id")
    @ManyToOne
    private CRF crf;

    /**
     * The study participant assignment.
     */
    @JoinColumn(name = "study_participant_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

    @Column(name = "start_date", nullable = true)
    private Date startDate;

    @Column(name = "is_schedule_initialized", nullable = true)
    private Boolean scheduleInitialized = false;

    @ManyToOne
    @JoinColumn(name = "arm_id", referencedColumnName = "id")
    private Arm arm;

    /**
     * Instantiates a new study participant crf.
     */
    public StudyParticipantCrf() {
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
     * Gets the crf.
     *
     * @return the crf
     */
    public CRF getCrf() {
        return crf;
    }


    /**
     * Gets the study participant assignment.
     *
     * @return the study participant assignment
     */
    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }

    /**
     * Sets the crf.
     *
     * @param crf the new crf
     */
    public void setCrf(CRF crf) {
        this.crf = crf;
    }

    /**
     * Sets the study participant assignment.
     *
     * @param studyParticipant the new study participant assignment
     */
    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipant) {
        this.studyParticipantAssignment = studyParticipant;
    }

    /**
     * Gets the study participant crf schedules.
     *
     * @return the study participant crf schedules
     */
    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedules() {
        try {
            if ((studyParticipantCrfSchedules == null || studyParticipantCrfSchedules.size() == 0) && getCrf().getChildCrf() == null && !getScheduleInitialized()) {
                
            	if(getStudyParticipantAssignment().getStatus()!=null){     					/* changed if..else structure to address added to handle nullpointer exception.. */
            		//creating schedules dynamically, except for OFFSTUDY participants																			
	            	if (getStudyParticipantAssignment().getStatus().equals(RoleStatus.OFFSTUDY)){
	            		return studyParticipantCrfSchedules;
	            	}else{
	            		createSchedules(false, false);
	                    setScheduleInitialized(true);	
	            	}
            	}
            }
        } catch (ParseException pe) {
            logger.error(pe.getStackTrace());
        }
        Collections.sort(studyParticipantCrfSchedules, new StudyParticipantCrfScheduleDueDateComparator());
        return studyParticipantCrfSchedules;
    }

    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedules(boolean dynamicallyCreate) {
        if (dynamicallyCreate) {
            getStudyParticipantCrfSchedules();
        }
        return studyParticipantCrfSchedules;
    }

    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedulesByStatus(List<CrfStatus> crfStatusList) {
        try {
            if ((studyParticipantCrfSchedules == null || studyParticipantCrfSchedules.size() == 0) && getCrf().getChildCrf() == null  && !getScheduleInitialized()) {
                //creating schedules dynamically
                createSchedules(false, false);
                setScheduleInitialized(true);
            }
        } catch (ParseException pe) {
            logger.error(pe.getStackTrace());
        }
        List<StudyParticipantCrfSchedule> studyParticipantCrfSchedulesWithSpecifiedStatuses = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule spcrfSchd : studyParticipantCrfSchedules) {
            if (crfStatusList.contains(spcrfSchd.getStatus())) {
                studyParticipantCrfSchedulesWithSpecifiedStatuses.add(spcrfSchd);
            }
        }
        return studyParticipantCrfSchedulesWithSpecifiedStatuses;
    }


    /**
     * Adds the study participant crf schedule.
     *
     * @param studyParticipantCrfSchedule the study participant crf schedule
     */
    public void addStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        studyParticipantCrfSchedule.setStudyParticipantCrf(this);
        studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
    }

    /**
     * Removes the crf schedule.
     *
     * @param crfSchedule the crf schedule
     */
    public void removeCrfSchedule(StudyParticipantCrfSchedule crfSchedule) {
        if (crfSchedule != null) {
            if (!crfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
                studyParticipantCrfSchedules.remove(crfSchedule);
            }
        }
    }

    public void removeScheduledSpCrfSchedules() throws Exception {
        List<StudyParticipantCrfSchedule> schedulesToRemove = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : getStudyParticipantCrfSchedules()) {
            if (!studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED) && !studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS) && !studyParticipantCrfSchedule.getStatus().equals((CrfStatus.PASTDUE))) {
                schedulesToRemove.add(studyParticipantCrfSchedule);
            }
        }
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : schedulesToRemove) {
            studyParticipantCrfSchedules.remove(studyParticipantCrfSchedule);
        }
    }


    /**
     * Gets the study participant crf added questions.
     *
     * @return the study participant crf added questions
     */
    public List<StudyParticipantCrfAddedQuestion> getStudyParticipantCrfAddedQuestions() {
        return studyParticipantCrfAddedQuestions;
    }

    /**
     * Removes the study participant crf added question.
     *
     * @param studyParticipantCrfAddedQuestion
     *         the study participant crf added question
     */
    public void removeStudyParticipantCrfAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        studyParticipantCrfAddedQuestions.remove(studyParticipantCrfAddedQuestion);
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    @Transient
    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedulesByStatus(CrfStatus crfStatus) {
        List l = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            if (studyParticipantCrfSchedule.getStatus().equals(crfStatus)) {
                l.add(studyParticipantCrfSchedule);
            }
        }
        return l;
    }

    public void createSchedules(boolean armChange, boolean isStartDateOrArmChanged) throws ParseException {

        Date calendarStartDate = getStartDate();
        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();

        for (FormArmSchedule formArmSchedule : crf.getFormArmSchedules()) {
            if (formArmSchedule.getArm().equals(studyParticipantAssignment.getArm())) {
                if (calendarStartDate != null && (calendarStartDate.equals(crf.getEffectiveStartDate()) || calendarStartDate.after(crf.getEffectiveStartDate()))) {
//                    createBaseLineSchedule();
                }
                for (CRFCalendar crfCalendar : formArmSchedule.getCrfCalendars()) {
                    if (crfCalendar.isValid()) {
                        proCtcAECalendar.setGeneralScheduleParameters(crfCalendar, calendarStartDate);
                        createSchedules(proCtcAECalendar, ParticipantSchedule.ScheduleType.GENERAL, armChange, isStartDateOrArmChanged);
                    }
                }
                int cycleNumber = 1;
                for (CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()) {
                    for (CRFCycle crfCycle : crfCycleDefinition.getCrfCycles()) {
                        if (crfCycle.isValid()) {
                            proCtcAECalendar.setCycleParameters(crfCycle, calendarStartDate, cycleNumber);
                            createSchedules(proCtcAECalendar, ParticipantSchedule.ScheduleType.CYCLE, armChange, isStartDateOrArmChanged);
                            calendarStartDate = proCtcAECalendar.incrementCalendar().getTime();
                            cycleNumber++;
                        } else {
                        	 proCtcAECalendar.setCycleParameters(crfCycle, calendarStartDate, cycleNumber);
                             calendarStartDate = proCtcAECalendar.incrementCalendar().getTime();
                             cycleNumber++;
                        }
                    }
                }
            }
        }
    }

    public void moveSingleSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule, int offset) {
        if (!studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED) && !studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS)) {
            Calendar c1 = ProCtcAECalendar.getCalendarForDate(studyParticipantCrfSchedule.getStartDate());
            Calendar c2 = ProCtcAECalendar.getCalendarForDate(studyParticipantCrfSchedule.getDueDate());
            c1.add(Calendar.DATE, offset);
            c2.add(Calendar.DATE, offset);

            studyParticipantCrfSchedule.setStartDate(c1.getTime());
            studyParticipantCrfSchedule.setDueDate(c2.getTime());
            studyParticipantCrfSchedule.setStatus(CrfStatus.SCHEDULED);
        }
    }

    private void createSchedules(ProCtcAECalendar proCtcAECalendar, ParticipantSchedule.ScheduleType scheduleType, 
    		boolean armChange, boolean isStartDateOrArmChanged) throws ParseException {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.addStudyParticipantCrf(this);
        participantSchedule.setProCtcAECalendar(proCtcAECalendar);
        participantSchedule.createSchedules(scheduleType, armChange, isStartDateOrArmChanged);
    }

    public void createIvrsSchedules(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.addStudyParticipantCrf(this);
        participantSchedule.addIvrsSchedules(studyParticipantCrfSchedule, this);
    }

    public StudyParticipantCrfSchedule getBaseLineSchedule() throws Exception {
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.isBaseline()) {
                return studyParticipantCrfSchedule;
            }
        }
        return null;
    }

    @Transient
    public List<CRFCycleDefinition> getCrfCycleDefinitions() {
        FormArmSchedule armScheduleForArm = crf.getFormArmScheduleForArm(studyParticipantAssignment.getArm());
        if (armScheduleForArm == null) {
            return new ArrayList();
        } else {
            return armScheduleForArm.getCrfCycleDefinitions();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrf that = (StudyParticipantCrf) o;

        if (arm != null ? !arm.equals(that.arm) : that.arm != null) return false;
        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (studyParticipantAssignment != null ? !studyParticipantAssignment.equals(that.studyParticipantAssignment) : that.studyParticipantAssignment != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = crf != null ? crf.hashCode() : 0;
        result = 31 * result + (studyParticipantAssignment != null ? studyParticipantAssignment.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        return result;
    }

    public void removeCrfSchedules(CrfStatus status, Date effectiveStartDate) {
        List<StudyParticipantCrfSchedule> l = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            if (status.equals(studyParticipantCrfSchedule.getStatus()) && (effectiveStartDate.equals(studyParticipantCrfSchedule.getStartDate()) || effectiveStartDate.before(studyParticipantCrfSchedule.getStartDate()))) {
                l.add(studyParticipantCrfSchedule);
            }
        }
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : l) {
            removeCrfSchedule(studyParticipantCrfSchedule);
        }
    }

    public StudyParticipantCrfAddedQuestion addStudyParticipantCrfAddedQuestion(Question question, int pageNumber) {
        StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion = new StudyParticipantCrfAddedQuestion();
        if (question instanceof ProCtcQuestion) {
            studyParticipantCrfAddedQuestion.setProCtcQuestion((ProCtcQuestion) question);
        }
        if (question instanceof MeddraQuestion) {
            studyParticipantCrfAddedQuestion.setMeddraQuestion((MeddraQuestion) question);
        }
        studyParticipantCrfAddedQuestion.setPageNumber(pageNumber);
        studyParticipantCrfAddedQuestion.setStudyParticipantCrf(this);
        studyParticipantCrfAddedQuestions.add(studyParticipantCrfAddedQuestion);
        return studyParticipantCrfAddedQuestion;
    }
    
    /**
     * Utility method used before creating/updating IVRS schedules by the ParticipantSchedule and StudyParticipantCrfSchedule.
     * @return
     */
    public boolean isIvrsCompliant(){
    	if(this.getCrf().isEq5d()){
    		return false;
    	}
    	boolean isIvrsCompliant = false;
        StudyParticipantAssignment studyParticipantAssignment = this.getStudyParticipantAssignment();
        for (StudyParticipantMode spm : studyParticipantAssignment.getStudyParticipantModes()) {
            if (spm.getMode().equals(AppMode.IVRS)) {
            	isIvrsCompliant = true;
            	break;
            }
        }
        return isIvrsCompliant;
    }


    /**
     * The date from which off hold is effective
     *
     * @param effectiveDate
     */
    public void putOnHold(Date effectiveDate) throws Exception {
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.SCHEDULED) || studyParticipantCrfSchedule.getStatus().equals(CrfStatus.INPROGRESS)) {
                studyParticipantCrfSchedule.putOnHold(effectiveDate);
            }
        }
    }


	public Boolean getScheduleInitialized() {
		return scheduleInitialized;
	}

	public void setScheduleInitialized(Boolean scheduleInitialized) {
		this.scheduleInitialized = scheduleInitialized;
	}
}
