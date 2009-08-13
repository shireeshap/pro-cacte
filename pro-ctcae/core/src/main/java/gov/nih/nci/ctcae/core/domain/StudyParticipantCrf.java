package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.text.ParseException;

//
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
        return studyParticipantCrfSchedules;
    }

    /**
     * Adds the study participant crf schedule.
     *
     * @param studyParticipantCrfSchedule the study participant crf schedule
     */
    public void addStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule) {
        for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
                studyParticipantCrfItem.setCrfPageItem(crfPageItem);
                studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);
            }
        }
        studyParticipantCrfSchedule.setStudyParticipantCrf(this);
        studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
    }

    /**
     * Removes the crf schedule.
     *
     * @param crfSchedule the crf schedule
     */
    public void removeCrfSchedule(StudyParticipantCrfSchedule crfSchedule) {
        studyParticipantCrfSchedules.remove(crfSchedule);
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

    public void createSchedules() throws ParseException {
        createBaseLineSchedule();
        Date calendarStartDate = getStartDate();
        ProCtcAECalendar proCtcAECalendar = new ProCtcAECalendar();

        for (FormArmSchedule formArmSchedule : crf.getFormArmSchedules()) {
            if (formArmSchedule.getArm().equals(studyParticipantAssignment.getArm())) {
                for (CRFCalendar crfCalendar : formArmSchedule.getCrfCalendars()) {
                    if (crfCalendar.isValid()) {
                        proCtcAECalendar.setGeneralScheduleParameters(crfCalendar, calendarStartDate);
                        createSchedules(proCtcAECalendar, ParticipantSchedule.ScheduleType.GENERAL);
                    }
                }
                int cycleNumber = 1;
                for (CRFCycleDefinition crfCycleDefinition : formArmSchedule.getCrfCycleDefinitions()) {
                    if (crfCycleDefinition.getCrfCycles() != null) {
                        for (CRFCycle crfCycle : crfCycleDefinition.getCrfCycles()) {
                            Integer cycleLength = crfCycleDefinition.getCycleLength();
                            String cycleDays = crfCycle.getCycleDays();
                            if (!validCycleDays(cycleDays)) {
                                continue;
                            }
                            String cycleLengthUnit = crfCycleDefinition.getCycleLengthUnit();
                            proCtcAECalendar.setCycleParameters(cycleLength, cycleDays, 1, cycleLengthUnit, calendarStartDate, cycleNumber);
                            createSchedules(proCtcAECalendar, ParticipantSchedule.ScheduleType.CYCLE);
                            Calendar c = ProCtcAECalendar.getCalendarForDate(calendarStartDate);
                            ProCtcAECalendar.incrementCalendar(c, cycleLength, cycleLengthUnit);
                            calendarStartDate = c.getTime();
                            cycleNumber++;
                        }
                    }
                }
            }

        }
    }

    private boolean validCycleDays(String cycleDays) {
        boolean validCycleDays = true;
        if (StringUtils.isBlank(cycleDays)) {
            return false;
        }
        String[] cycleDaysArr = cycleDays.split(",");
        if (cycleDaysArr.length == 0) {
            return false;
        }
        for (int i = 1; i < cycleDaysArr.length; i++) {
            String cycleDay = cycleDaysArr[i];
            if (StringUtils.isBlank(cycleDay) || !StringUtils.isNumeric(cycleDay)) {
                validCycleDays = false;
                break;
            }
        }
        return validCycleDays;
    }

    private void createBaseLineSchedule() {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.setStudyParticipantCrf(this);
        Calendar c = Calendar.getInstance();
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = participantSchedule.createSchedule(Calendar.getInstance(), c.getTimeInMillis(), -1, -1);
        studyParticipantCrfSchedule.setBaseline(true);
    }

    private void createSchedules(ProCtcAECalendar proCtcAECalendar, ParticipantSchedule.ScheduleType scheduleType) throws ParseException {
        ParticipantSchedule participantSchedule = new ParticipantSchedule();
        participantSchedule.setStudyParticipantCrf(this);
        participantSchedule.setCalendar(proCtcAECalendar);
        participantSchedule.createSchedules(scheduleType);
    }

    public StudyParticipantCrfSchedule getBaseLineSchedule() {
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : getStudyParticipantCrfSchedules()) {
            if (studyParticipantCrfSchedule.isBaseline()) {
                return studyParticipantCrfSchedule;
            }
        }
        return null;
    }

    @Transient
    public List<CRFCycleDefinition> getCrfCycleDefinitions() {
        return crf.getFormArmScheduleForArm(studyParticipantAssignment.getArm()).getCrfCycleDefinitions();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrf that = (StudyParticipantCrf) o;

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

    public void removeCrfSchedules(CrfStatus status) {
        List<StudyParticipantCrfSchedule> l = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            if (status.equals(studyParticipantCrfSchedule.getStatus())) {
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
}
