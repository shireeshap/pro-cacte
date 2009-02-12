package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

//
/**
 *
 * @author Vinay Kumar
 */

@Entity
@Table(name = "CS_ASSIGNMENT_ROLES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_cs_assignment_roles_id")})
public class ClinicalStaffAssignmentRole extends BasePersistable {

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
    private Date statusDate = DateUtils.getCurrentDate();

    /**
     * The clinical staff.
     */
    @JoinColumn(name = "clinical_staff_assignment_id", referencedColumnName = "id",nullable = false)
    @ManyToOne
    private ClinicalStaffAssignment clinicalStaffAssignment;


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

    public ClinicalStaffAssignment getClinicalStaffAssignment() {
        return clinicalStaffAssignment;
    }

    public void setClinicalStaffAssignment(ClinicalStaffAssignment clinicalStaffAssignment) {
        this.clinicalStaffAssignment = clinicalStaffAssignment;
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

        ClinicalStaffAssignmentRole that = (ClinicalStaffAssignmentRole) o;

        if (role != that.role) return false;
        if (clinicalStaffAssignment != null ? !clinicalStaffAssignment.equals(that.clinicalStaffAssignment) : that.clinicalStaffAssignment != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (clinicalStaffAssignment != null ? clinicalStaffAssignment.hashCode() : 0);
        return result;
    }
}