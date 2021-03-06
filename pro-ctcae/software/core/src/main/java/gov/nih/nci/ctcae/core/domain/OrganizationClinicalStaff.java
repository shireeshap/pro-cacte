package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

//
/**
 * @author mehul
 */

@Entity
@Table(name = "ORGANIZATION_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "SEQ_ORGANIZATION_CLINICAL_S_ID")})
public class OrganizationClinicalStaff extends BasePersistable {

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


    @OneToMany(mappedBy = "organizationClinicalStaff", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<StudyOrganizationClinicalStaff> studyOrganizationClinicalStaff = new ArrayList<StudyOrganizationClinicalStaff>();

    @Transient
    private String displayName;

    /**
     * The organization.
     */
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    @ManyToOne
    private Organization organization;


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
     * /**
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationClinicalStaff that = (OrganizationClinicalStaff) o;

        if (clinicalStaff != null ? !clinicalStaff.equals(that.clinicalStaff) : that.clinicalStaff != null)
            return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalStaff != null ? clinicalStaff.hashCode() : 0;
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }

    public String getDisplayName() {
        if (displayName == null) {
            displayName = clinicalStaff != null ? getClinicalStaff().getDisplayName() : "";
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<StudyOrganizationClinicalStaff> getStudyOrganizationClinicalStaff() {
        return studyOrganizationClinicalStaff;
    }
}

