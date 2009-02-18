package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

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

    @JoinColumn(name = "site_clinical_staff_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SiteClinicalStaff siteClinicalStaff;

    @JoinColumn(name = "study_organization_id", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private StudyOrganization studyOrganization;

    @Column(name = "role_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus = RoleStatus.ACTIVE;

    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    @ManyToOne
    private Roles roles;

    @Column(name = "status_date", nullable = false)
    private Date statusDate = new Date();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SiteClinicalStaff getSiteClinicalStaff() {
        return siteClinicalStaff;
    }

    public void setSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        this.siteClinicalStaff = siteClinicalStaff;
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
        if (roles != null ? !roles.equals(that.roles) : that.roles != null) return false;
        if (siteClinicalStaff != null ? !siteClinicalStaff.equals(that.siteClinicalStaff) : that.siteClinicalStaff != null)
            return false;
        if (studyOrganization != null ? !studyOrganization.equals(that.studyOrganization) : that.studyOrganization != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = siteClinicalStaff != null ? siteClinicalStaff.hashCode() : 0;
        result = 31 * result + (studyOrganization != null ? studyOrganization.hashCode() : 0);
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

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

}