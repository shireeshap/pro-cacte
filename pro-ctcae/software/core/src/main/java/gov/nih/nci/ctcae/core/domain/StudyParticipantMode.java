package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.BaseVersionable;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;


/**
 * @author mehul gulati
 * Date: Nov 3, 2010
 */
@Entity
@Table(name = "STUDY_PARTICIPANT_MODES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_modes_id")})
public class StudyParticipantMode extends BaseVersionable {

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

    @Column(name = "is_email", nullable = false)
    private Boolean email;

    @Column(name = "is_call", nullable = false)
    private Boolean call;

    @Column(name = "is_text", nullable = false)
    private Boolean text;

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

    public Boolean isEmail() {
        return email;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean isCall() {
        return call;
    }

    public Boolean getCall() {
        return call;
    }

    public void setCall(Boolean call) {
        this.call = call;
    }

    public Boolean isText() {
        return text;
    }

    public Boolean getText() {
        return text;
    }

    public void setText(Boolean text) {
        this.text = text;
    }
}
