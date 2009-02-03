package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.validation.annotation.UniqueNciIdentifierForOrganization;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Organization.
 * 
 * @author
 * @crated Oct 7, 2008
 */

@Entity
@Table(name = "ORGANIZATIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_organizations_id")})
public class Organization extends BaseVersionable {

	/** The Constant DEFAULT_SITE_NAME. */
	public static final String DEFAULT_SITE_NAME = "default";

	/** The id. */
	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	/** The name. */
	@Column(name = "name", nullable = false)
	private String name;

	/** The nci institute code. */
	@Column(name = "nci_institute_code", nullable = false)
	private String nciInstituteCode;


	/**
	 * Gets the display name.
	 * 
	 * @return the display name
	 */
	public String getDisplayName() {
		return getName()
			+ (getNciInstituteCode() == null ? "" : " ( "
			+ getNciInstituteCode() + " )");
	}

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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the nci institute code.
	 * 
	 * @return the nci institute code
	 */
	@UniqueNciIdentifierForOrganization
	public String getNciInstituteCode() {
		return nciInstituteCode;
	}

	/**
	 * Sets the nci institute code.
	 * 
	 * @param nciInstituteCode the new nci institute code
	 */
	public void setNciInstituteCode(String nciInstituteCode) {
		this.nciInstituteCode = nciInstituteCode;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result
			+ (nciInstituteCode != null ? nciInstituteCode.hashCode() : 0);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getDisplayName();
	}
}
