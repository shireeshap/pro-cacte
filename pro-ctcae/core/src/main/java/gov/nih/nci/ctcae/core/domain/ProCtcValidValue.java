package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc_valid_values")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_valid_values_id")})
public class ProCtcValidValue extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "value", nullable = false)
	private Integer value;

	@Column(name = "display_name", nullable = false)
	private String displayName;

	@JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcQuestion proCtcQuestion;

	public ProCtcValidValue() {
	}

	public ProCtcValidValue(Integer id) {
		this.id = id;
	}

	public ProCtcValidValue(final String displayName) {
		this.displayName = displayName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public ProCtcQuestion getProCtcTerm() {
		return proCtcQuestion;
	}

	public void setProCtcTerm(ProCtcQuestion proCtcQuestion) {
		this.proCtcQuestion = proCtcQuestion;
	}


	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final ProCtcValidValue that = (ProCtcValidValue) o;

		if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
		if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
			return false;
		if (value != null ? !value.equals(that.value) : that.value != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = value != null ? value.hashCode() : 0;
		result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
		result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
		return result;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}


	@Override
	public String toString() {
		return value + "";
	}

}
