package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
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
@Table(name = "SS_CLINICAL_STAFF_ROLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_ss_clinical_staff_roles_id")})
public class StudySiteClinicalStaffRole extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    @Column(name = "role_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus roleStatus;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * The status date.
     */
    @Column(name = "status_date", nullable = false)
    private Date statusDate = DateUtils.getCurrentDate();

    /**
     * The clinical staff.
     */
    @JoinColumn(name = "study_site_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private StudySiteClinicalStaff studySiteClinicalStaff;


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

    public StudySiteClinicalStaff getStudySiteClinicalStaff() {
        return studySiteClinicalStaff;
    }

    public void setStudySiteClinicalStaff(StudySiteClinicalStaff studySiteClinicalStaff) {
        this.studySiteClinicalStaff = studySiteClinicalStaff;
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

        StudySiteClinicalStaffRole that = (StudySiteClinicalStaffRole) o;

        if (role != that.role) return false;
        if (studySiteClinicalStaff != null ? !studySiteClinicalStaff.equals(that.studySiteClinicalStaff) : that.studySiteClinicalStaff != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (studySiteClinicalStaff != null ? studySiteClinicalStaff.hashCode() : 0);
        return result;
    }
}