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

	@Column(name = "question_type", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private ProCtcQuestionType proCtcQuestionType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
	private List<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
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
        String questionTextNew = "";
        if(proCtcQuestionType.equals(ProCtcQuestionType.SEVERITY)){
            questionTextNew = "Over the past week, what was the WORST SEVERITY of your " + proCtcTerm.getCtepTerm();
        }
        if(proCtcQuestionType.equals(ProCtcQuestionType.INTERFERENCE)){
            questionTextNew = "Over the past week, how much has the " + proCtcTerm.getCtepTerm() + " INTERFERED with your daily activities";
        }
        if(proCtcQuestionType.equals(ProCtcQuestionType.FREQUENCY)){
            questionTextNew = "Over the past week, how OFTEN did you have " + proCtcTerm.getCtepTerm();
        }
        return questionTextNew;
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

	public ProCtcQuestionType getProCtcQuestionType() {
		return proCtcQuestionType;
	}

	public void setProCtcQuestionType(final ProCtcQuestionType proCtcQuestionType) {
		this.proCtcQuestionType = proCtcQuestionType;
	}


    @Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final ProCtcQuestion that = (ProCtcQuestion) o;

		if (proCtcQuestionType != that.proCtcQuestionType) return false;
		if (proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm) : that.proCtcTerm != null) return false;
		if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = questionText != null ? questionText.hashCode() : 0;
		result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
		result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return questionText;
	}

	public String getDisplayName(){
		return questionText+" "+proCtcTerm.getCtepTerm();
	}
}
