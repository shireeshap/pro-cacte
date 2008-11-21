package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "study_participant_assignments")
public class StudyParticipantAssignment extends BaseVersionable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "study_participant_identifier", nullable = false)
    private String studyParticipantIdentifier;

    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    @ManyToOne
    private Participant participant;

    @JoinColumn(name = "study_site_id", referencedColumnName = "id")
    @ManyToOne
    private StudyOrganization studySite;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    public StudyParticipantAssignment() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudyParticipantIdentifier() {
        return studyParticipantIdentifier;
    }

    public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
        this.studyParticipantIdentifier = studyParticipantIdentifier;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public StudyOrganization getStudySite() {
        return studySite;
    }

    public void setStudySite(StudyOrganization studySite) {
        this.studySite = studySite;
    }

    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        if (studyParticipantCrf != null) {
            studyParticipantCrf.setStudyParticipantAssignment(this);
            studyParticipantCrfs.add(studyParticipantCrf);
        }
    }

}
