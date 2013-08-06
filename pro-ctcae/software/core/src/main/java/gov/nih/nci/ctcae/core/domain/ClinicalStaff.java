package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.commons.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
    @Column(name = "nci_identifier", nullable = true)
    private String nciIdentifier;

    /**
     * The phone number.
     */
    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "cs_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleStatus status = RoleStatus.ACTIVE;

    @Column(name = "effective_date", nullable = true)
    private Date effectiveDate = new Date();

    /**
     * The site clinical staffs.
     */
    @OneToMany(mappedBy = "clinicalStaff", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<OrganizationClinicalStaff> organizationClinicalStaffs = new ArrayList<OrganizationClinicalStaff>();


    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = true)
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public RoleStatus getStatus() {
        return status;
    }

    public void setStatus(RoleStatus status) {
        this.status = status;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public String getFormattedDate(){
        if(effectiveDate!=null){
            return DateUtils.format(effectiveDate);
        }
        return "";
    }
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
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


    /**
     * Gets the site clinical staffs.
     *
     * @return the site clinical staffs
     */
    public List<OrganizationClinicalStaff> getOrganizationClinicalStaffs() {
        return organizationClinicalStaffs;
    }

    public void removeOrganizationClinicalStaff(Integer organizationClinicalStaffIndex) {
        OrganizationClinicalStaff organizationClinicalStaff = getOrganizationClinicalStaffs().get(organizationClinicalStaffIndex);
        removeOrganizationClinicalStaff(organizationClinicalStaff);
    }


    /**
     * Adds the site clinical staff.
     *
     * @param organizationClinicalStaff the site clinical staff
     */
    public void addOrganizationClinicalStaff(OrganizationClinicalStaff organizationClinicalStaff) {
        if (organizationClinicalStaff != null) {
            organizationClinicalStaff.setClinicalStaff(this);
            this.getOrganizationClinicalStaffs().add(organizationClinicalStaff);
        }
    }

    /**
     * Adds the site clinical staffs.
     *
     * @param organizationClinicalStaffs the site clinical staffs
     */
    public void addOrganizationClinicalStaff(Collection<OrganizationClinicalStaff> organizationClinicalStaffs) {
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            addOrganizationClinicalStaff(organizationClinicalStaff);
        }
    }

    /**
     * Removes the site clinical staff.
     *
     * @param organizationClinicalStaff the site clinical staff
     */
    public void removeOrganizationClinicalStaff(OrganizationClinicalStaff organizationClinicalStaff) {
        if (organizationClinicalStaff != null) {
            this.getOrganizationClinicalStaffs().remove(organizationClinicalStaff);
        }
    }

    /**
     * Removes the site clinical staffs.
     *
     * @param organizationClinicalStaffs the site clinical staffs
     */
    public void removeOrganizationClinicalStaff(List<OrganizationClinicalStaff> organizationClinicalStaffs) {
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            removeOrganizationClinicalStaff(organizationClinicalStaff);
        }
    }

    public List<Organization> getOrganizationsWithCCARole() {
        Set<Organization> organizations = new HashSet<Organization>();
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            for (UserRole userRole : organizationClinicalStaff.getClinicalStaff().getUser().getUserRoles()) {
                if (userRole.getRole().equals(Role.CCA)) {
                    organizations.add(organizationClinicalStaff.getOrganization());
                }
            }
        }
        return new ArrayList(organizations);
    }


    public void deactivateClinicalStaff(){
    	setStatus(RoleStatus.IN_ACTIVE);
    	deactivateUser();
    	deactivateStudyOrganizationalClinicalStaff();
    	removeUserRoles();
    }
    
    public void removeUserRoles(){
    	if(getUser() != null){
    		getUser().getUserRoles().clear();
    	}
    }
    
    public void addUserRole(Role role){
    	if(role != null && getUser() != null){
    		getUser().addUserRole(new UserRole(role));
    	}
    }
    
    public void activateClinicalStaff(Role role){
    	setStatus(RoleStatus.ACTIVE);
    	activateUser();
    	addUserRole(role);
    }
    
    public void deactivateStudyOrganizationalClinicalStaff(){
    	for(OrganizationClinicalStaff ocs : organizationClinicalStaffs){
    		for(StudyOrganizationClinicalStaff socs : ocs.getStudyOrganizationClinicalStaff()){
    			socs.deactivateStudyOrganizationalClinicalStaff();
    		}
    	}
    }
    
    private void deactivateUser(){
    	if(getUser() != null){
    		getUser().deactivateUser();
    	}
    }
    
    private void activateUser(){
    	if(getUser() != null){
    		getUser().activateUser();
    	}
    }
}
