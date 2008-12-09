package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mehul
 */

@Entity
@Table(name = "SITE_CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "site_clinical_staffs_id_seq")})
public class SiteClinicalStaff extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "status_code", nullable = true)
	private String statusCode;

	@Column(name = "status_date", nullable = true)
	private Date statusDate;

	@JoinColumn(name = "clinical_staff_id", referencedColumnName = "id")
	@ManyToOne
	private ClinicalStaff clinicalStaff;

	@JoinColumn(name = "organization_id", referencedColumnName = "id")
	@ManyToOne
	private Organization organization;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public ClinicalStaff getClinicalStaff() {
		return clinicalStaff;
	}

	public void setClinicalStaff(ClinicalStaff clinicalStaff) {
		this.clinicalStaff = clinicalStaff;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SiteClinicalStaff)) return false;

		SiteClinicalStaff that = (SiteClinicalStaff) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (clinicalStaff != null ? !clinicalStaff.equals(that.clinicalStaff) : that.clinicalStaff != null)
			return false;
		if (organization != null ? !organization.equals(that.organization) : that.organization != null) return false;
		if (statusCode != null ? !statusCode.equals(that.statusCode) : that.statusCode != null) return false;
		if (statusDate != null ? !statusDate.equals(that.statusDate) : that.statusDate != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (statusCode != null ? statusCode.hashCode() : 0);
		result = 31 * result + (statusDate != null ? statusDate.hashCode() : 0);
		result = 31 * result + (clinicalStaff != null ? clinicalStaff.hashCode() : 0);
		result = 31 * result + (organization != null ? organization.hashCode() : 0);
		return result;
	}
}
