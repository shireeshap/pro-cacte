package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueNciIdentifierForOrganization;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "organizations")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_organizations_id")})
public class Organization extends BaseVersionable {

	public static final String DEFAULT_SITE_NAME = "default";

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "nci_institute_code", nullable = false)
	private String nciInstituteCode;


	public String getDisplayName() {
		return getName()
			+ (getNciInstituteCode() == null ? "" : " ( "
			+ getNciInstituteCode() + " )");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@UniqueNciIdentifierForOrganization
	public String getNciInstituteCode() {
		return nciInstituteCode;
	}

	public void setNciInstituteCode(String nciInstituteCode) {
		this.nciInstituteCode = nciInstituteCode;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Organization that = (Organization) o;

		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		if (nciInstituteCode != null ? !nciInstituteCode
			.equals(that.nciInstituteCode) : that.nciInstituteCode != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result
			+ (nciInstituteCode != null ? nciInstituteCode.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return getDisplayName();
	}
}
