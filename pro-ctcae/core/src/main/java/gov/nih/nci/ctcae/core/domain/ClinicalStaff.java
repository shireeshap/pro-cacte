package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollection;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mehul
 */

@Entity
@Table(name = "clinical_staffs")
@GenericGenerator(name = "id-generator", strategy = "native",
	parameters = {@Parameter(name = "sequence", value = "seq_clinical_staffs_id")})


public class ClinicalStaff extends Person {

	@Column(name = "email_address", nullable = true)
	private String emailAddress;

	@Column(name = "fax_number", nullable = true)
	private String faxNumber;

	@Column(name = "nci_identifier", nullable = false)
	private String nciIdentifier;

	@Column(name = "phone_number", nullable = true)
	private String phoneNumber;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clinicalStaff", fetch = FetchType.LAZY)
	private List<SiteClinicalStaff> siteClinicalStaffs = new ArrayList<SiteClinicalStaff>();

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getNciIdentifier() {
		return nciIdentifier;
	}

	public void setNciIdentifier(String nciIdentifier) {
		this.nciIdentifier = nciIdentifier;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@UniqueObjectInCollection(message = "Duplicate Site")
	public List<SiteClinicalStaff> getSiteClinicalStaffs() {
		return siteClinicalStaffs;
	}

	public void addSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
		if (siteClinicalStaff != null) {
			siteClinicalStaff.setClinicalStaff(this);
			this.getSiteClinicalStaffs().add(siteClinicalStaff);
		}
	}

	public void addSiteClinicalStaffs(Collection<SiteClinicalStaff> siteClinicalStaffs) {
		for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
			addSiteClinicalStaff(siteClinicalStaff);
		}
	}

	public void removeSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
		if (siteClinicalStaff != null) {
			this.getSiteClinicalStaffs().remove(siteClinicalStaff);
		}
	}

	public void removeSiteClinicalStaffs(List<SiteClinicalStaff> siteClinicalStaffs) {
		for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
			removeSiteClinicalStaff(siteClinicalStaff);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClinicalStaff)) return false;
		if (!super.equals(o)) return false;

		ClinicalStaff that = (ClinicalStaff) o;

		if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
		if (faxNumber != null ? !faxNumber.equals(that.faxNumber) : that.faxNumber != null) return false;
		if (nciIdentifier != null ? !nciIdentifier.equals(that.nciIdentifier) : that.nciIdentifier != null)
			return false;
		if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
		result = 31 * result + (faxNumber != null ? faxNumber.hashCode() : 0);
		result = 31 * result + (nciIdentifier != null ? nciIdentifier.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		return result;
	}
}
