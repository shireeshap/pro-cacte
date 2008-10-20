package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_VALID_VALUES")
public class ProCtcValidValue extends BasePersistable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "value", nullable = false)
	private String value;

	@JoinColumn(name = "pro_ctc_term_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcTerm proCtcTerm;

	public ProCtcValidValue() {
	}

	public ProCtcValidValue(Integer id) {
		this.id = id;
	}

	public ProCtcValidValue(String value) {
		this.value = value;
	}

	public ProCtcValidValue(Integer id, String value) {
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ProCtcTerm getProCtcTerm() {
		return proCtcTerm;
	}

	public void setProCtcTerm(ProCtcTerm proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProCtcValidValue that = (ProCtcValidValue) o;

		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;
		if (proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm)
				: that.proCtcTerm != null)
			return false;
		if (value != null ? !value.equals(that.value) : that.value != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return value;
	}

}
