package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.ListValues;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//
/**
 * @author Vinay Kumar
 */

@Entity
@Table(name = "CLINICAL_STAFF_ASSIGNMENTS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "cs_assignments_id_seq")})
public class ClinicalStaffAssignment extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The clinical staff.
     */
    @JoinColumn(name = "clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private ClinicalStaff clinicalStaff;


    @Column(name = "domain_object_class", nullable = false)
    private String domainObjectClass;

    @Column(name = "domain_object_id", nullable = false)
    private Integer domainObjectId;


    @Transient
    private String displayName;

    @OneToMany(mappedBy = "clinicalStaffAssignment", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ClinicalStaffAssignmentRole> clinicalStaffAssignmentRoles = new ArrayList<ClinicalStaffAssignmentRole>();


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalStaffAssignment that = (ClinicalStaffAssignment) o;

        if (clinicalStaff != null ? !clinicalStaff.equals(that.clinicalStaff) : that.clinicalStaff != null)
            return false;
        if (domainObjectClass != null ? !domainObjectClass.equals(that.domainObjectClass) : that.domainObjectClass != null)
            return false;
        if (domainObjectId != null ? !domainObjectId.equals(that.domainObjectId) : that.domainObjectId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalStaff != null ? clinicalStaff.hashCode() : 0;
        result = 31 * result + (domainObjectClass != null ? domainObjectClass.hashCode() : 0);
        result = 31 * result + (domainObjectId != null ? domainObjectId.hashCode() : 0);
        return result;
    }

    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }

    public String getDomainObjectClass() {
        return domainObjectClass;
    }

    public void setDomainObjectClass(String domainObjectClass) {
        this.domainObjectClass = domainObjectClass;
    }

    public Integer getDomainObjectId() {
        return domainObjectId;
    }

    public void setDomainObjectId(Integer domainObjectId) {
        this.domainObjectId = domainObjectId;
    }

    public List<ClinicalStaffAssignmentRole> getClinicalStaffAssignmentRoles() {
        return clinicalStaffAssignmentRoles;
    }


    public void addClinicalStaffAssignmentRole(ClinicalStaffAssignmentRole clinicalStaffAssignmentRole) {

        if (clinicalStaffAssignmentRole != null) {
            clinicalStaffAssignmentRole.setClinicalStaffAssignment(this);
            getClinicalStaffAssignmentRoles().add(clinicalStaffAssignmentRole);
            logger.debug(String.format("added   %s to %s", clinicalStaffAssignmentRole.toString(), toString()));
        }

    }


    public void removeClinicalStaffAssignmentRoleRole(Integer clinicalStaffAssignmentRoleIndex) {
        ClinicalStaffAssignmentRole clinicalStaffAssignmentRole = getClinicalStaffAssignmentRoles().get(clinicalStaffAssignmentRoleIndex);
        getClinicalStaffAssignmentRoles().remove(clinicalStaffAssignmentRole);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ListValues> getStudySpeceficRoles() {
        Set<ListValues> studySpeceficRolesForAssignment = new HashSet<ListValues>();
        for (ClinicalStaffAssignmentRole existingClinicalStaffAssignmentRole : clinicalStaffAssignmentRoles) {

            Role role = existingClinicalStaffAssignmentRole.getRole();

            List<Role> studySpeceficRoles = Role.getStudySpeceficRoles(role);
            for (Role studySpeceficRole : studySpeceficRoles) {
                ListValues listValues = new ListValues(studySpeceficRole.getDisplayName(), studySpeceficRole.getDisplayName());
                studySpeceficRolesForAssignment.add(listValues);
            }
        }
        return new ArrayList(studySpeceficRolesForAssignment);
    }


}

