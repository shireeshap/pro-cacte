package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

//
/**
 * The Class SiteClinicalStaffRole .
 *
 * @author Vinay Kumar
 */

@Entity
@Table(name = "SITE_CLINICAL_STAFF_ROLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_scs_staff_roles_id")})
public class SiteClinicalStaffRole extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "role_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus = RoleStatus.ACTIVE;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.CRA;

    /**
     * The status date.
     */
    @Column(name = "status_date", nullable = false)
    private Date statusDate = new Date();

    /**
     * The clinical staff.
     */
    @JoinColumn(name = "site_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private SiteClinicalStaff siteClinicalStaff;


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
     * Gets the status date.
     *
     * @return the status date
     */
    public Date getStatusDate() {
        return statusDate;
    }

    /**
     * Sets the status date.
     *
     * @param statusDate the new status date
     */
    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public RoleStatus getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(RoleStatus roleStatus) {
        this.roleStatus = roleStatus;
    }

    public SiteClinicalStaff getSiteClinicalStaff() {
        return siteClinicalStaff;
    }

    public void setSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        this.siteClinicalStaff = siteClinicalStaff;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SiteClinicalStaffRole that = (SiteClinicalStaffRole) o;

        if (role != that.role) return false;
        if (siteClinicalStaff != null ? !siteClinicalStaff.equals(that.siteClinicalStaff) : that.siteClinicalStaff != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (siteClinicalStaff != null ? siteClinicalStaff.hashCode() : 0);
        return result;
    }
}