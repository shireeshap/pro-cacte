package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

//
/**
 * @author Vinay Kumar
 */

@Entity
@Table(name = "STUDY_ORGANIZATION_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "so_clinical_staffs_id_seq")})
public class StudyOrganizationClinicalStaff extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "organization_clinical_staff_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private OrganizationClinicalStaff organizationClinicalStaff;

    @JoinColumn(name = "study_organization_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudyOrganization studyOrganization;

    @Column(name = "role_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus = RoleStatus.ACTIVE;

    @Column(name = "role_name", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;


    @Column(name = "status_date", nullable = false)
    private Date statusDate = new Date();


    @OneToMany(mappedBy = "studyOrganizationClinicalStaff")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyParticipantClinicalStaff> studyParticipantClinicalStaffs = new ArrayList<StudyParticipantClinicalStaff>();

    @Transient
    private String displayName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrganizationClinicalStaff getOrganizationClinicalStaff() {
        return organizationClinicalStaff;
    }

    public void setOrganizationClinicalStaff(OrganizationClinicalStaff organizationClinicalStaff) {
        this.organizationClinicalStaff = organizationClinicalStaff;
    }


    public StudyOrganization getStudyOrganization() {
        return studyOrganization;
    }

    public void setStudyOrganization(StudyOrganization studyOrganization) {
        this.studyOrganization = studyOrganization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyOrganizationClinicalStaff that = (StudyOrganizationClinicalStaff) o;

        if (roleStatus != that.roleStatus) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (organizationClinicalStaff != null ? !organizationClinicalStaff.equals(that.organizationClinicalStaff) : that.organizationClinicalStaff != null)
            return false;
        if (studyOrganization != null ? !studyOrganization.equals(that.studyOrganization) : that.studyOrganization != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = organizationClinicalStaff != null ? organizationClinicalStaff.hashCode() : 0;
        result = 31 * result + (studyOrganization != null ? studyOrganization.hashCode() : 0);
        result = 31 * result + (roleStatus != null ? roleStatus.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    public RoleStatus getRoleStatus() {

        return roleStatus;
    }

    public void setRoleStatus(RoleStatus roleStatus) {
        this.roleStatus = roleStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = organizationClinicalStaff != null ? organizationClinicalStaff.getClinicalStaff().getDisplayName() : "";
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}