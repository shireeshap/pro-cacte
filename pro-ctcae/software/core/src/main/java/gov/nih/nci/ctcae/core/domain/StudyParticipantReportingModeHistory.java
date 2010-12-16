package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;


/**
 * @author Suneel Allareddy
 * Date: Dec 13, 2010
 */
@Entity
@Table(name = "STUDY_PARTICIPANT_REPORTING_MODE_HISTORY")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_reporting_mode_history_id")})
public class StudyParticipantReportingModeHistory extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "mode", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AppMode mode;

    @JoinColumn(name = "spa_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

   /**
     * The effective start date.
     */
    @Column(name = "effective_start_date", nullable = false)
    private Date effectiveStartDate;

    /**
     * The effective start date.
     */
    @Column(name = "effective_end_date", nullable = true)
    private Date effectiveEndDate;

    public StudyParticipantReportingModeHistory() {
        this.effectiveStartDate = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AppMode getMode() {
        return mode;
    }

    public void setMode(AppMode mode) {
        this.mode = mode;
    }

    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }

    /**
     * Gets the effective start date.
     *
     * @return the effective start date
     */
    public Date getEffectiveStartDate() {
        return effectiveStartDate;
    }

    /**
     * Sets the effective start date.
     *
     * @param effectiveStartDate the new effective start date
     */
    public void setEffectiveStartDate(Date effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    /**
     * Gets the effective end date.
     *
     * @return the effective end date
     */
    public Date getEffectiveEndDate() {
        return effectiveEndDate;
    }

    /**
     * Sets the effective end date.
     *
     * @param effectiveEndDate the new effective end date
     */
    public void setEffectiveEndDate(Date effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    
    
}