package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//
/**
 * The Class StudyParticipantCrf.
 *
 * @author
 * @crated Oct 7, 2008
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
     * @param crf                         the crf
     */
    public void addStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule, CRF crf) {
        if (studyParticipantCrfSchedule != null) {
            if (crf != null) {
                for (CRFPage crfPage : crf.getCrfPagesSortedByPageNumber()) {
                    for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                        StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
                        studyParticipantCrfItem.setCrfPageItem(crfPageItem);
                        studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);
                    }
                }
            }
            studyParticipantCrfSchedule.setStudyParticipantCrf(this);
            studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
        }
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
     * Adds the study participant crf added question.
     *
     * @param studyParticipantCrfAddedQuestion
     *         the study participant crf added question
     */
    public void addStudyParticipantCrfAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        if (studyParticipantCrfAddedQuestion != null) {
            studyParticipantCrfAddedQuestion.setStudyParticipantCrf(this);
            studyParticipantCrfAddedQuestions.add(studyParticipantCrfAddedQuestion);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantCrf that = (StudyParticipantCrf) o;

        if (crf != null ? !crf.equals(that.crf) : that.crf != null) return false;
        if (studyParticipantAssignment != null ? !studyParticipantAssignment.equals(that.studyParticipantAssignment) : that.studyParticipantAssignment != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = crf != null ? crf.hashCode() : 0;
        result = 31 * result + (studyParticipantAssignment != null ? studyParticipantAssignment.hashCode() : 0);
        return result;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Transient
    public List<StudyParticipantCrfSchedule> getCompletedCrfs() {
        List completedCrfs = new ArrayList<StudyParticipantCrfSchedule>();
        for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrfSchedules) {
            if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
                completedCrfs.add(studyParticipantCrfSchedule);
            }
        }
        return completedCrfs;
    }
}
