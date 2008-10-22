package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC")
public class ProCtc extends BasePersistable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "pro_ctc_version", nullable = false, unique = true)
	private String proCtcVersion;

	@Column(name = "release_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date releaseDate;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtc")
	private Collection<ProCtcTerm> proCtcTerms = new ArrayList<ProCtcTerm>();

	public ProCtc() {
	}

	public ProCtc(Integer id) {
		this.id = id;
	}

	public ProCtc(Integer id, String proCtcVersion, Date releaseDate) {
		this.id = id;
		this.proCtcVersion = proCtcVersion;
		this.releaseDate = releaseDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProCtcVersion() {
		return proCtcVersion;
	}

	public void setProCtcVersion(String proCtcVersion) {
		this.proCtcVersion = proCtcVersion;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Collection<ProCtcTerm> getProCtcTerms() {
		return proCtcTerms;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProCtc proCtc = (ProCtc) o;

		if (id != null ? !id.equals(proCtc.id) : proCtc.id != null)
			return false;
		if (proCtcTerms != null ? !proCtcTerms.equals(proCtc.proCtcTerms)
				: proCtc.proCtcTerms != null)
			return false;
		if (proCtcVersion != null ? !proCtcVersion.equals(proCtc.proCtcVersion)
				: proCtc.proCtcVersion != null)
			return false;
		if (releaseDate != null ? !releaseDate.equals(proCtc.releaseDate)
				: proCtc.releaseDate != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result
				+ (proCtcVersion != null ? proCtcVersion.hashCode() : 0);
		result = 31 * result
				+ (releaseDate != null ? releaseDate.hashCode() : 0);
		result = 31 * result
				+ (proCtcTerms != null ? proCtcTerms.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Version: " + proCtcVersion + ", Released: " + releaseDate;
	}

}
