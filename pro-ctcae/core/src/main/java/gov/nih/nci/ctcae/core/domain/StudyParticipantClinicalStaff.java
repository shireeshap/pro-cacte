package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyParticipantClinicalStaff.
 *
 * @author Vinay Kumar
 */

@Entity
@Table(name = "STUDY_PARTICIPANT_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "sp_clinical_staffs_id_seq")})
public class StudyParticipantClinicalStaff extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "study_site_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private StudySiteClinicalStaff studySiteClinicalStaff;

    @JoinColumn(name = "sp_assignment_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public StudySiteClinicalStaff getStudySiteClinicalStaff() {
        return studySiteClinicalStaff;
    }

    public void setStudySiteClinicalStaff(StudySiteClinicalStaff studySiteClinicalStaff) {
        this.studySiteClinicalStaff = studySiteClinicalStaff;
    }

    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantClinicalStaff that = (StudyParticipantClinicalStaff) o;

        if (studyParticipantAssignment != null ? !studyParticipantAssignment.equals(that.studyParticipantAssignment) : that.studyParticipantAssignment != null)
            return false;
        if (studySiteClinicalStaff != null ? !studySiteClinicalStaff.equals(that.studySiteClinicalStaff) : that.studySiteClinicalStaff != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studySiteClinicalStaff != null ? studySiteClinicalStaff.hashCode() : 0;
        result = 31 * result + (studyParticipantAssignment != null ? studyParticipantAssignment.hashCode() : 0);
        return result;
    }
}