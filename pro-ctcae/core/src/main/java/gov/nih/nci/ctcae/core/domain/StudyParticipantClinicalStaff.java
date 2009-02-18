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

    @JoinColumn(name = "study_organization_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private StudyOrganizationClinicalStaff studyOrganizationClinicalStaff;

    @JoinColumn(name = "sp_assignment_id", referencedColumnName = "id")
    @ManyToOne
    private StudyParticipantAssignment studyParticipantAssignment;

    @Column(name = "role_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus = RoleStatus.ACTIVE;

    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    @ManyToOne
    private Roles roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyParticipantClinicalStaff that = (StudyParticipantClinicalStaff) o;

        if (roleStatus != that.roleStatus) return false;
        if (roles != null ? !roles.equals(that.roles) : that.roles != null) return false;
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
        result = 31 * result + (roleStatus != null ? roleStatus.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }

    public RoleStatus getRoleStatus() {

        return roleStatus;
    }

    public void setRoleStatus(RoleStatus roleStatus) {
        this.roleStatus = roleStatus;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
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

}