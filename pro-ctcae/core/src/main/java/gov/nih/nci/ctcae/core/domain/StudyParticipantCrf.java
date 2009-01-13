package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_crfs")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_crfs_id")})
public class StudyParticipantCrf extends BaseVersionable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = new ArrayList<StudyParticipantCrfSchedule>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantCrf", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrfAddedQuestion> studyParticipantCrfAddedQuestions = new ArrayList<StudyParticipantCrfAddedQuestion>();

    @JoinColumn(name = "crf_id", referencedColumnName = "id")
    @ManyToOne
    private CRF crf;

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


    public CRF getCrf() {
        return crf;
    }


    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setCrf(CRF crf) {
        this.crf = crf;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipant) {
        this.studyParticipantAssignment = studyParticipant;
    }

    public List<StudyParticipantCrfSchedule> getStudyParticipantCrfSchedules() {
        return studyParticipantCrfSchedules;
    }

    public void addStudyParticipantCrfSchedule(StudyParticipantCrfSchedule studyParticipantCrfSchedule, CRF crf) {
        if (studyParticipantCrfSchedule != null) {
            for (CRFPage crfPage : crf.getCrfPages()) {
                for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
                    StudyParticipantCrfItem studyParticipantCrfItem = new StudyParticipantCrfItem();
                    studyParticipantCrfItem.setCrfPageItem(crfPageItem);
                    studyParticipantCrfSchedule.addStudyParticipantCrfItem(studyParticipantCrfItem);
                }
            }
            studyParticipantCrfSchedule.setStudyParticipantCrf(this);
            studyParticipantCrfSchedules.add(studyParticipantCrfSchedule);
        }
    }

    public void removeCrfSchedule(StudyParticipantCrfSchedule crfSchedule) {
        studyParticipantCrfSchedules.remove(crfSchedule);
    }

    public List<StudyParticipantCrfAddedQuestion> getStudyParticipantCrfAddedQuestions() {
        return studyParticipantCrfAddedQuestions;
    }

    public void addStudyParticipantCrfAddedQuestion(StudyParticipantCrfAddedQuestion studyParticipantCrfAddedQuestion) {
        if (studyParticipantCrfAddedQuestion != null) {
            studyParticipantCrfAddedQuestion.setStudyParticipantCrf(this);
            studyParticipantCrfAddedQuestions.add(studyParticipantCrfAddedQuestion);
        }
    }
}
