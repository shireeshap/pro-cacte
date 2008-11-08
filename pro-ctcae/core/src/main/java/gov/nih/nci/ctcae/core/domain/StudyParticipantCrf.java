package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crfs")
public class StudyParticipantCrf extends BaseVersionable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    private List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();

    @JoinColumn(name = "study_crf_id", referencedColumnName = "id")
    @ManyToOne
    private StudyCrf studyCrf;

    @JoinColumn(name = "study_participant_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;


    public StudyParticipantCrf() {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public StudyCrf getStudyCrf() {
        return studyCrf;
    }


    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyCrf(StudyCrf studyCrf) {
        this.studyCrf = studyCrf;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipant) {
        this.studyParticipantAssignment = studyParticipant;
    }

    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedules() {
        return studyParticipantCrfSchedules;
    }

    public void addStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule){
            if(studyParticipantCrfSchedule != null){
                studyParticipantCrfSchedule.setStudyParticipantCrf(this);
                studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
            }
    }

}
