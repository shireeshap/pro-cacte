package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mehul
 */

@Entity
@Table(name = "SITE_INVESTIGATORS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = { @Parameter(name = "sequence", value = "site_investigators_id_seq") })
public class SiteInvestigator extends BasePersistable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "status_code" ,nullable = true)
    private String statusCode;

    @Column(name = "status_date" ,nullable = true)
    private Date statusDate;

    @JoinColumn(name = "investigator_id", referencedColumnName = "id")
	@ManyToOne
    private Investigator investigator;

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

    public Investigator getInvestigator() {
        return investigator;
    }

    public void setInvestigator(Investigator investigator) {
        this.investigator = investigator;
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
        if (!(o instanceof SiteInvestigator)) return false;

        SiteInvestigator that = (SiteInvestigator) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (investigator != null ? !investigator.equals(that.investigator) : that.investigator != null) return false;
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
        result = 31 * result + (investigator != null ? investigator.hashCode() : 0);
        result = 31 * result + (organization != null ? organization.hashCode() : 0);
        return result;
    }
}
