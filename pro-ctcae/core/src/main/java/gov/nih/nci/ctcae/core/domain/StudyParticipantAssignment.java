package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class StudyParticipantAssignment.
 *
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "STUDY_PARTICIPANT_ASSIGNMENTS")

@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_participant_assignments_id")})
public class StudyParticipantAssignment extends BaseVersionable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The study participant identifier.
     */
    @Column(name = "study_participant_identifier", nullable = false)
    private String studyParticipantIdentifier;

    /**
     * The participant.
     */
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    @ManyToOne
    private Participant participant;

    /**
     * The study site.
     */
    @JoinColumn(name = "study_site_id", referencedColumnName = "id")
    @ManyToOne
    private StudyOrganization studySite;

    /**
     * The study participant crfs.
     */
    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantCrf> studyParticipantCrfs = new ArrayList<StudyParticipantCrf>();

    @OneToMany(mappedBy = "studyParticipantAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantClinicalStaff> studyParticipantClinicalStaffs = new ArrayList<StudyParticipantClinicalStaff>();

    /**
     * Instantiates a new study participant assignment.
     */
    public StudyParticipantAssignment() {
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
     * Gets the study participant identifier.
     *
     * @return the study participant identifier
     */
    public String getStudyParticipantIdentifier() {
        return studyParticipantIdentifier;
    }

    /**
     * Sets the study participant identifier.
     *
     * @param studyParticipantIdentifier the new study participant identifier
     */
    public void setStudyParticipantIdentifier(String studyParticipantIdentifier) {
        this.studyParticipantIdentifier = studyParticipantIdentifier;
    }

    /**
     * Gets the participant.
     *
     * @return the participant
     */
    public Participant getParticipant() {
        return participant;
    }

    /**
     * Sets the participant.
     *
     * @param participant the new participant
     */
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    /**
     * Gets the study site.
     *
     * @return the study site
     */
    public StudyOrganization getStudySite() {
        return studySite;
    }

    /**
     * Sets the study site.
     *
     * @param studySite the new study site
     */
    public void setStudySite(StudyOrganization studySite) {
        this.studySite = studySite;
    }

    /**
     * Gets the study participant crfs.
     *
     * @return the study participant crfs
     */
    public List<StudyParticipantCrf> getStudyParticipantCrfs() {
        return studyParticipantCrfs;
    }

    /**
     * Adds the study participant crf.
     *
     * @param studyParticipantCrf the study participant crf
     */
    public void addStudyParticipantCrf(StudyParticipantCrf studyParticipantCrf) {
        if (studyParticipantCrf != null) {
            studyParticipantCrf.setStudyParticipantAssignment(this);
            studyParticipantCrfs.add(studyParticipantCrf);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantAssignment that = (StudyParticipantAssignment) o;

        if (participant != null ? !participant.equals(that.participant) : that.participant != null) return false;
        if (studyParticipantIdentifier != null ? !studyParticipantIdentifier.equals(that.studyParticipantIdentifier) : that.studyParticipantIdentifier != null)
            return false;
        if (studySite != null ? !studySite.equals(that.studySite) : that.studySite != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyParticipantIdentifier != null ? studyParticipantIdentifier.hashCode() : 0;
        result = 31 * result + (participant != null ? participant.hashCode() : 0);
        result = 31 * result + (studySite != null ? studySite.hashCode() : 0);
        return result;
    }

    public List<StudyParticipantClinicalStaff> getStudyParticipantClinicalStaffs() {
        return studyParticipantClinicalStaffs;
    }

    public void addStudyParticipantClinicalStaff(StudyParticipantClinicalStaff studyParticipantClinicalStaff) {

        if (studyParticipantClinicalStaff != null) {


            StudyOrganization expectedStudyOrganization = studyParticipantClinicalStaff.getStudyOrganizationClinicalStaff().getStudyOrganization();

            if (!(expectedStudyOrganization instanceof StudySite)) {
                String errorMessage = String.format("study organization clinical staff deos not belong to a study site %s. it belongs to study coordinating center",
                        expectedStudyOrganization);
                logger.error(errorMessage);
                throw new CtcAeSystemException(errorMessage);

            } else if (!expectedStudyOrganization.equals(this.getStudySite())) {
                String errorMessage = String.format("study site clinical staff belongs to study site %s. It does not belongs to study site %s of study participant assignment. " +
                        "So this study site clincal staff can not be added.",
                        expectedStudyOrganization, this.getStudySite());
                logger.error(errorMessage);
                throw new CtcAeSystemException(errorMessage);

            }

            studyParticipantClinicalStaff.setStudyParticipantAssignment(this);
            getStudyParticipantClinicalStaffs().add(studyParticipantClinicalStaff);
            logger.debug(String.format("added study participant clinical staff %s to study participant assignment %s", studyParticipantClinicalStaff.toString(), toString()));

        }

    }
}

