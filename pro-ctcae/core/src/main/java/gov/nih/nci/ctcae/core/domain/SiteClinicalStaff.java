package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//
/**
 * The Class SiteClinicalStaff.
 *
 * @author mehul
 */

@Entity
@Table(name = "SITE_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "site_clinical_staffs_id_seq")})
public class SiteClinicalStaff extends BasePersistable {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The status code.
     */
    @Column(name = "status_code", nullable = true)
    private String statusCode;

    /**
     * The status date.
     */
    @Column(name = "status_date", nullable = true)
    private Date statusDate;

    /**
     * The clinical staff.
     */
    @JoinColumn(name = "clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private ClinicalStaff clinicalStaff;

    /**
     * The organization.
     */
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "siteClinicalStaff", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<SiteClinicalStaffRole> siteClinicalStaffRoles = new ArrayList<SiteClinicalStaffRole>();


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
     * Gets the status code.
     *
     * @return the status code
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the new status code
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    /**
     * Gets the clinical staff.
     *
     * @return the clinical staff
     */
    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    /**
     * Sets the clinical staff.
     *
     * @param clinicalStaff the new clinical staff
     */
    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }

    /**
     * Gets the organization.
     *
     * @return the organization
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the organization.
     *
     * @param organization the new organization
     */
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#equals(java.lang.Object)
      */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteClinicalStaff)) return false;

        SiteClinicalStaff that = (SiteClinicalStaff) o;

        if (clinicalStaff != null ? !clinicalStaff.equals(that.clinicalStaff) : that.clinicalStaff != null)
            return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;
        if (statusCode != null ? !statusCode.equals(that.statusCode) : that.statusCode != null) return false;
        if (statusDate != null ? !statusDate.equals(that.statusDate) : that.statusDate != null) return false;

        return true;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
    @Override
    public int hashCode() {
        int result;
        result = (statusCode != null ? statusCode.hashCode() : 0);
        result = 31 * result + (statusDate != null ? statusDate.hashCode() : 0);
        result = 31 * result + (clinicalStaff != null ? clinicalStaff.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }

    public List<SiteClinicalStaffRole> getSiteClinicalStaffRoles() {
        return siteClinicalStaffRoles;
    }

    public void addSiteClinicalStaffRole(SiteClinicalStaffRole siteClinicalStaffRole) {

        if (siteClinicalStaffRole != null) {
            siteClinicalStaffRole.setSiteClinicalStaff(this);
            getSiteClinicalStaffRoles().add(siteClinicalStaffRole);
            logger.debug(String.format("added   %s to %s", siteClinicalStaffRole.toString(), toString()));
        }

    }


    public void removeSiteClinicalStaffRole(Integer siteClinicalStaffRoleIndex) {
        SiteClinicalStaffRole siteClinicalStaffRole = getSiteClinicalStaffRoles().get(siteClinicalStaffRoleIndex);
        getSiteClinicalStaffRoles().remove(siteClinicalStaffRole);
    }

}

