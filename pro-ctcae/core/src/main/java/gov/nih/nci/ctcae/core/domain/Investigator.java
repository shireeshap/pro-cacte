package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author mehul
 */

@Entity
@Table(name= "investigators")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "investogators_id_seq") })


public class Investigator extends Person {

    @Column(name = "email_address" ,nullable = true)
    private String emailAddress;

    @Column(name = "fax_number" ,nullable = true)
    private String faxNumber;

    @Column(name = "nci_identifier" ,nullable = false)
    private String nciIdentifier;

    @Column(name = "phone_number" , nullable = true)
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigator")
    private Collection<SiteInvestigator> siteInvestigators;

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

    public Collection<SiteInvestigator> getSiteInvestigators() {
        return siteInvestigators;
    }

    public void addSiteInvestigator(SiteInvestigator siteInvestigator) {
        if (siteInvestigator != null) {
            addSiteInvestigator(siteInvestigator);
        }
    }

    public void addSiteInvestigators(Collection<SiteInvestigator> siteInvestigators) {
        for (SiteInvestigator siteInvestigator : siteInvestigators) {
            addSiteInvestigator(siteInvestigator);
        }
    }

     public void removeSiteInvestigator(SiteInvestigator siteInvestigator) {
        if (siteInvestigator != null) {
            removeSiteInvestigator(siteInvestigator);
        }
    }

    public void removeSiteInvestigators(Collection<SiteInvestigator> siteInvestigators) {
        for (SiteInvestigator siteInvestigator : siteInvestigators) {
            removeSiteInvestigator(siteInvestigator);
        }
    }

    @Override
	public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Investigator)) return false;
        if (!super.equals(o)) return false;

        Investigator that = (Investigator) o;

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
