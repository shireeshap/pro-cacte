package gov.nih.nci.ctcae.core.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_TERMS")
public class ProCtcTerm extends BasePersistable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "question_text", nullable = false)
	private String questionText;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcTerm")
	private List<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcTerm")
	private List<CrfItem> crfItems = new ArrayList<CrfItem>();

	@JoinColumn(name = "ctc_term_id", referencedColumnName = "id")
	@ManyToOne
	private CtcTerm ctcTerm;

	@JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtc proCtc;

	public ProCtcTerm() {
	}

	public ProCtcTerm(Integer id) {
		this.id = id;
	}

	public ProCtcTerm(Integer id, String questionText) {
		this.id = id;
		this.questionText = questionText;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Collection<ProCtcValidValue> getValidValues() {
		return validValues;
	}

	public void addValidValue(ProCtcValidValue validValue) {
		if (validValue != null) {
			validValue.setProCtcTerm(this);
			validValues.add(validValue);
		}
	}

	public void removeValidValue(ProCtcValidValue validValue) {
		validValues.remove(validValue);
	}

	public void addValidValues(ArrayList<ProCtcValidValue> validValues) {
		for (ProCtcValidValue validValue : validValues) {
			addValidValue(validValue);
		}
	}

	public void removeValidValues(ArrayList<ProCtcValidValue> validValues) {
		for (ProCtcValidValue validValue : validValues) {
			removeValidValue(validValue);
		}
	}

	public List<CrfItem> getCrfItems() {
		return crfItems;
	}

	public CtcTerm getCtcTerm() {
		return ctcTerm;
	}

	public void setCtcTerm(CtcTerm ctcTerm) {
		this.ctcTerm = ctcTerm;
	}

	public ProCtc getProCtc() {
		return proCtc;
	}

	public void setProCtc(ProCtc proCtc) {
		this.proCtc = proCtc;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProCtcTerm that = (ProCtcTerm) o;

		if (crfItems != null ? !crfItems.equals(that.crfItems)
				: that.crfItems != null)
			return false;
		if (ctcTerm != null ? !ctcTerm.equals(that.ctcTerm)
				: that.ctcTerm != null)
			return false;
		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;
		if (proCtc != null ? !proCtc.equals(that.proCtc) : that.proCtc != null)
			return false;
		if (questionText != null ? !questionText.equals(that.questionText)
				: that.questionText != null)
			return false;
		if (validValues != null ? !validValues.equals(that.validValues)
				: that.validValues != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result
				+ (questionText != null ? questionText.hashCode() : 0);
		result = 31 * result
				+ (validValues != null ? validValues.hashCode() : 0);
		result = 31 * result + (crfItems != null ? crfItems.hashCode() : 0);
		result = 31 * result + (ctcTerm != null ? ctcTerm.hashCode() : 0);
		result = 31 * result + (proCtc != null ? proCtc.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return questionText;
	}
}
