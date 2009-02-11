package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueObjectInCollection;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//
/**
 * The Class ClinicalStaff.
 *
 * @author mehul
 */

@Entity
@Table(name = "CLINICAL_STAFFS")
@GenericGenerator(name = "id-generator", strategy = "native",
        parameters = {@Parameter(name = "sequence", value = "seq_clinical_staffs_id")})


public class ClinicalStaff extends Person {

    /**
     * The email address.
     */
    @Column(name = "email_address", nullable = true)
    private String emailAddress;

    /**
     * The fax number.
     */
    @Column(name = "fax_number", nullable = true)
    private String faxNumber;

    /**
     * The nci identifier.
     */
    @Column(name = "nci_identifier", nullable = false)
    private String nciIdentifier;

    /**
     * The phone number.
     */
    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    /**
     * The site clinical staffs.
     */
    @OneToMany(mappedBy = "clinicalStaff", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<SiteClinicalStaff> siteClinicalStaffs = new ArrayList<SiteClinicalStaff>();

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address.
     *
     * @param emailAddress the new email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the fax number.
     *
     * @return the fax number
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the fax number.
     *
     * @param faxNumber the new fax number
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * Gets the nci identifier.
     *
     * @return the nci identifier
     */
    public String getNciIdentifier() {
        return nciIdentifier;
    }

    /**
     * Sets the nci identifier.
     *
     * @param nciIdentifier the new nci identifier
     */
    public void setNciIdentifier(String nciIdentifier) {
        this.nciIdentifier = nciIdentifier;
    }

    /**
     * Gets the phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber the new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the site clinical staffs.
     *
     * @return the site clinical staffs
     */
    @UniqueObjectInCollection(message = "Duplicate Site")
    public List<SiteClinicalStaff> getSiteClinicalStaffs() {
        return siteClinicalStaffs;
    }

    /**
     * Adds the site clinical staff.
     *
     * @param siteClinicalStaff the site clinical staff
     */
    public void addSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        if (siteClinicalStaff != null) {
            siteClinicalStaff.setClinicalStaff(this);
            this.getSiteClinicalStaffs().add(siteClinicalStaff);
        }
    }

    /**
     * Adds the site clinical staffs.
     *
     * @param siteClinicalStaffs the site clinical staffs
     */
    public void addSiteClinicalStaffs(Collection<SiteClinicalStaff> siteClinicalStaffs) {
        for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
            addSiteClinicalStaff(siteClinicalStaff);
        }
    }

    /**
     * Removes the site clinical staff.
     *
     * @param siteClinicalStaff the site clinical staff
     */
    public void removeSiteClinicalStaff(SiteClinicalStaff siteClinicalStaff) {
        if (siteClinicalStaff != null) {
            this.getSiteClinicalStaffs().remove(siteClinicalStaff);
        }
    }

    /**
     * Removes the site clinical staffs.
     *
     * @param siteClinicalStaffs the site clinical staffs
     */
    public void removeSiteClinicalStaffs(List<SiteClinicalStaff> siteClinicalStaffs) {
        for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
            removeSiteClinicalStaff(siteClinicalStaff);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Person#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.core.domain.Person#hashCode()
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (faxNumber != null ? faxNumber.hashCode() : 0);
        result = 31 * result + (nciIdentifier != null ? nciIdentifier.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }

    public void removeSiteClinicalStaff(Integer siteClinicalStaffIndex) {
        SiteClinicalStaff siteClinicalStaff = getSiteClinicalStaffs().get(siteClinicalStaffIndex);
        removeSiteClinicalStaff(siteClinicalStaff);
    }

    public void removeSiteClinicalStaffRole(Integer siteClinicalStaffIndex, Integer siteClinicalStaffRoleIndex) {
        SiteClinicalStaff siteClinicalStaff = getSiteClinicalStaffs().get(siteClinicalStaffIndex);
        siteClinicalStaff.removeSiteClinicalStaffRole(siteClinicalStaffRoleIndex);

    }
}
