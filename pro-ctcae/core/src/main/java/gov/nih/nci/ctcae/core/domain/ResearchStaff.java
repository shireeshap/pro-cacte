package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Mehul Gulati
 * Date: Oct 24, 2008
 */
@Entity
@Table(name = "RESEARCH_STAFF")
public class ResearchStaff extends Person {

     @Column(name = "email_address" ,nullable = true)
      private String emailAddress;

      @Column(name = "fax_number" ,nullable = true)
      private String faxNumber;

      @Column(name = "researcher_id" ,nullable = false)
      private String researcherID;

      @Column(name = "phone_number" , nullable = true)
      private String phoneNumber;

     @JoinColumn(name = "organization_id", referencedColumnName = "id")
	 @ManyToOne
      private Organization organization;


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

    public String getResearcherID() {
        return researcherID;
    }

    public void setResearcherID(String researcherID) {
        this.researcherID = researcherID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchStaff)) return false;
        if (!super.equals(o)) return false;

        ResearchStaff that = (ResearchStaff) o;

        if (emailAddress != null ? !emailAddress.equals(that.emailAddress) : that.emailAddress != null) return false;
        if (faxNumber != null ? !faxNumber.equals(that.faxNumber) : that.faxNumber != null) return false;
        if (organization != null ? !organization.equals(that.organization) : that.organization != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (researcherID != null ? !researcherID.equals(that.researcherID) : that.researcherID != null) return false;

        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (faxNumber != null ? faxNumber.hashCode() : 0);
        result = 31 * result + (researcherID != null ? researcherID.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }
}
