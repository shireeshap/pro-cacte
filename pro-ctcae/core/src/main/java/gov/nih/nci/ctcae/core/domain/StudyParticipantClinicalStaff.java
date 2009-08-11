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
@Table(name = "STUDY_PAR_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_STUDY_PAR_CLINICAL_STAF_ID")})
public class StudyParticipantClinicalStaff extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "so_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;

    @JoinColumn(name = "sp_assignment_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

    @Column(name = "is_primary")
    private boolean isPrimary = false;

    @Column(name = "notify")
    private boolean notify = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyParticipantClinicalStaff)) return false;

        StudyParticipantClinicalStaff that = (StudyParticipantClinicalStaff) o;

        if (isPrimary != that.isPrimary) return false;
        if (notify != that.notify) return false;
        if (studyOrganizationClinicalStaff != null ? !studyOrganizationClinicalStaff.equals(that.studyOrganizationClinicalStaff) : that.studyOrganizationClinicalStaff != null)
            return false;
        if (studyParticipantAssignment != null ? !studyParticipantAssignment.equals(that.studyParticipantAssignment) : that.studyParticipantAssignment != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = studyOrganizationClinicalStaff != null ? studyOrganizationClinicalStaff.hashCode() : 0;
        result = 31 * result + (studyParticipantAssignment != null ? studyParticipantAssignment.hashCode() : 0);
        result = 31 * result + (isPrimary ? 1 : 0);
        result = 31 * result + (notify ? 1 : 0);
        return result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudyParticipantAssignment getStudyParticipantAssignment() {
        return studyParticipantAssignment;
    }

    public void setStudyParticipantAssignment(StudyParticipantAssignment studyParticipantAssignment) {
        this.studyParticipantAssignment = studyParticipantAssignment;
    }

    public StudyOrganizationClinicalStaff getStudyOrganizationClinicalStaff() {
        return studyOrganizationClinicalStaff;
    }

    public void setStudyOrganizationClinicalStaff(StudyOrganizationClinicalStaff studyOrganizationClinicalStaff) {
        this.studyOrganizationClinicalStaff = studyOrganizationClinicalStaff;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}