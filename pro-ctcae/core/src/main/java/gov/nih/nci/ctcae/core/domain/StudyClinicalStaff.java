package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

//
/**
 * The Class StudyClinicalStaff.
 *
 * @author mehul
 */

@Entity
@Table(name = "STUDY_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_study_clinical_staffs_id")})

public class StudyClinicalStaff extends BasePersistable {


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
     * The role code.
     */
    @Column(name = "role_code", nullable = true)
    private String roleCode;

    /**
     * The signature text.
     */
    @Column(name = "signature_text", nullable = true)
    private String signatureText;

    /**
     * The site clinical staff.
     */
    @JoinColumn(name = "site_clinical_staff_id", referencedColumnName = "id")
    @ManyToOne
    private SiteClinicalStaff siteClinicalStaff;

    /**
     * The study organization.
     */
    @JoinColumn(name = "study_organization_id", referencedColumnName = "id")
    @ManyToOne
    private StudyOrganization studyOrganization;


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
     * Gets the role code.
     *
     * @return the role code
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * Sets the role code.
     *
     * @param roleCode the new role code
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * Gets the signature text.
     *
     * @return the signature text
     */
    public String getSignatureText() {
        return signatureText;
    }

    /**
     * Sets the signature text.
     *
     * @param signatureText the new signature text
     */
    public void setSignatureText(String signatureText) {
        this.signatureText = signatureText;
    }

    /**
     * Gets the site clinical staff.
     *
     * @return the site clinical staff
     */
    public SiteClinicalStaff getSiteClinicalStaff() {
        return siteClinicalStaff;
    }

    /**
     * Sets the site clinical staff.
     *
     * @param siteClinicalStaff the new site clinical staff
     */
    public void setSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        this.siteClinicalStaff = siteClinicalStaff;
    }

    /**
     * Gets the study organization.
     *
     * @return the study organization
     */
    public StudyOrganization getStudyOrganization() {
        return studyOrganization;
    }

    /**
     * Sets the study organization.
     *
     * @param studyOrganization the new study organization
     */
    public void setStudyOrganization(StudyOrganization studyOrganization) {
        this.studyOrganization = studyOrganization;
    }


    /* (non-Javadoc)
      * @see java.lang.Object#equals(java.lang.Object)
      */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudyClinicalStaff)) return false;

        StudyClinicalStaff that = (StudyClinicalStaff) o;

        if (roleCode != null ? !roleCode.equals(that.roleCode) : that.roleCode != null) return false;
        if (signatureText != null ? !signatureText.equals(that.signatureText) : that.signatureText != null)
            return false;
        if (siteClinicalStaff != null ? !siteClinicalStaff.equals(that.siteClinicalStaff) : that.siteClinicalStaff != null)
            return false;
        if (statusCode != null ? !statusCode.equals(that.statusCode) : that.statusCode != null) return false;
        if (studyOrganization != null ? !studyOrganization.equals(that.studyOrganization) : that.studyOrganization != null)
            return false;

        return true;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
    public int hashCode() {
        int result;
        result = (statusCode != null ? statusCode.hashCode() : 0);
        result = 31 * result + (roleCode != null ? roleCode.hashCode() : 0);
        result = 31 * result + (signatureText != null ? signatureText.hashCode() : 0);
        result = 31 * result + (siteClinicalStaff != null ? siteClinicalStaff.hashCode() : 0);
		result = 31 * result + (studyOrganization != null ? studyOrganization.hashCode() : 0);
		return result;
	}


}
