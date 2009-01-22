package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "pro_ctc_questions")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_questions_id")})
public class ProCtcQuestion extends BasePersistable {

	@Id
	@GeneratedValue(generator = "id-generator")
	@Column(name = "id")
	private Integer id;

	@Column(name = "question_text", nullable = false)
	private String questionText;

	@Column(name = "question_type", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private ProCtcQuestionType proCtcQuestionType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
	private List<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
	private List<CrfPageItem> crfPageItems = new ArrayList<CrfPageItem>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
    private List<ProCtcQuestionDisplayRule> displayRules = new ArrayList<ProCtcQuestionDisplayRule>();


    @JoinColumn(name = "pro_ctc_term_id", referencedColumnName = "id")
	@ManyToOne
	private ProCtcTerm proCtcTerm;



    @Column(name = "display_order", nullable = true)
    private Integer displayOrder;



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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }


    public String getFormattedQuestionText() {
		String questionTextNew = "";
		if (proCtcQuestionType != null && proCtcQuestionType.equals(ProCtcQuestionType.SEVERITY)) {
			questionTextNew = "Over the past week, what was the WORST SEVERITY of your " + proCtcTerm.getCtcTerm().getCtepTerm();
		}
		if (proCtcQuestionType != null && proCtcQuestionType.equals(ProCtcQuestionType.INTERFERENCE)) {
			questionTextNew = "Over the past week, how much has the " + proCtcTerm.getCtcTerm().getCtepTerm() + " INTERFERED with your daily activities";
		}
		if (proCtcQuestionType != null && proCtcQuestionType.equals(ProCtcQuestionType.FREQUENCY)) {
			questionTextNew = "Over the past week, how OFTEN did you have " + proCtcTerm.getCtcTerm().getCtepTerm();
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
			validValue.setProCtcQuestion(this);
			validValues.add(validValue);
		}
	}

    public void addDisplayRules(ProCtcQuestionDisplayRule proCtcQuestionDisplayRule){
        if(proCtcQuestionDisplayRule != null){
            proCtcQuestionDisplayRule.setProCtcQuestion(this);
            displayRules.add(proCtcQuestionDisplayRule);
        }
    }

    public List<CrfPageItem> getCrfItems() {
		return crfPageItems;
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
	public String toString() {
		return questionText;
	}

	public String getDisplayName() {
		return questionText + " " + proCtcTerm.getCtcTerm().getCtepTerm();
	}

	public String getShortText() {
		return proCtcTerm.getTerm() + "-" + questionText;

	}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcQuestion)) return false;

        ProCtcQuestion that = (ProCtcQuestion) o;

        if (crfPageItems != null ? !crfPageItems.equals(that.crfPageItems) : that.crfPageItems != null) return false;
        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;
        if (proCtcQuestionType != that.proCtcQuestionType) return false;
        if (proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm) : that.proCtcTerm != null) return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;
        if (validValues != null ? !validValues.equals(that.validValues) : that.validValues != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
        result = 31 * result + (validValues != null ? validValues.hashCode() : 0);
        result = 31 * result + (crfPageItems != null ? crfPageItems.hashCode() : 0);
        result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        return result;
    }

    public List<ProCtcQuestionDisplayRule> getDisplayRules() {
        return displayRules;
    }

    public void setDisplayRules(List<ProCtcQuestionDisplayRule> displayRules) {
        this.displayRules = displayRules;
    }
}
