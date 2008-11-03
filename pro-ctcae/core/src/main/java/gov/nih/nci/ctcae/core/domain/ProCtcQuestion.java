package gov.nih.nci.ctcae.core.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_QUESTIONS")
public class ProCtcQuestion extends BasePersistable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "question_text", nullable = false)
	private String questionText;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion",fetch = FetchType.EAGER)
	private List<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion")
	private List<CrfItem> crfItems = new ArrayList<CrfItem>();

	@JoinColumn(name = "pro_ctc_term_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcTerm proCtcTerm;



	public ProCtcQuestion() {
	}

	public ProCtcQuestion(Integer id) {
		this.id = id;
	}

	public ProCtcQuestion(Integer id, String questionText) {
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

	public ProCtcTerm getProCtcTerm() {
		return proCtcTerm;
	}

	public void setProCtcTerm(ProCtcTerm proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProCtcQuestion that = (ProCtcQuestion) o;

		if (crfItems != null ? !crfItems.equals(that.crfItems)
				: that.crfItems != null)
			return false;
		if (proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm)
				: that.proCtcTerm != null)
			return false;
		if (id != null ? !id.equals(that.id) : that.id != null)
			return false;

		if (questionText != null ? !questionText.equals(that.questionText)
				: that.questionText != null)
			return false;
		if (validValues != null ? !validValues.equals(that.validValues)
				: that.validValues != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		result = (id != null ? id.hashCode() : 0);
		result = 31 * result
				+ (questionText != null ? questionText.hashCode() : 0);
		result = 31 * result
				+ (validValues != null ? validValues.hashCode() : 0);
		result = 31 * result + (crfItems != null ? crfItems.hashCode() : 0);
		result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
		
		return result;
	}

	@Override
	public String toString() {
		return questionText;
	}
}
