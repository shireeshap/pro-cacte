package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//
/**
 * The Class ProCtcQuestion.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_QUESTIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_questions_id")})
public class ProCtcQuestion extends Question {

    /**
     * The id.
     */
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;


    /**
     * The pro ctc question type.
     */
    @Column(name = "question_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProCtcQuestionType proCtcQuestionType;

    /**
     * The valid values.
     */
    @OneToMany(mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ProCtcValidValue> validValues = new ArrayList<ProCtcValidValue>();

    /**
     * The pro ctc question display rules.
     */
    @OneToMany(mappedBy = "proCtcQuestion", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ProCtcQuestionDisplayRule> proCtcQuestionDisplayRules = new ArrayList<ProCtcQuestionDisplayRule>();


    /**
     * The pro ctc term.
     */
    @JoinColumn(name = "pro_ctc_term_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcTerm proCtcTerm;


    /**
     * The display order.
     */
    @Column(name = "display_order", nullable = true)
    private Integer displayOrder;


    /**
     * Instantiates a new pro ctc question.
     */
    public ProCtcQuestion() {
        super();
    }


    /**
     * Instantiates a new pro ctc question.
     *
     * @param id           the id
     * @param questionText the question text
     */
    public ProCtcQuestion(Integer id, String questionText) {
        this.id = id;
        this.questionText = questionText;
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
     * Gets the display order.
     *
     * @return the display order
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Sets the display order.
     *
     * @param displayOrder the new display order
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }



    /**
     * Gets the valid values.
     *
     * @return the valid values
     */
    public Collection<ProCtcValidValue> getValidValues() {
        Collections.sort(validValues, new DisplayOrderComparator());
        return validValues;
    }

    /**
     * Adds the valid value.
     *
     * @param validValue the valid value
     */
    public void addValidValue(ProCtcValidValue validValue) {
        if (validValue != null) {
            validValue.setProCtcQuestion(this);
            validValues.add(validValue);
        }
    }

    /**
     * Adds the display rules.
     *
     * @param proCtcQuestionDisplayRule the pro ctc question display rule
     */
    public void addDisplayRules(ProCtcQuestionDisplayRule proCtcQuestionDisplayRule) {
        if (proCtcQuestionDisplayRule != null) {
            proCtcQuestionDisplayRule.setProCtcQuestion(this);
            proCtcQuestionDisplayRules.add(proCtcQuestionDisplayRule);
        }
    }


    /**
     * Gets the pro ctc term.
     *
     * @return the pro ctc term
     */
    public ProCtcTerm getProCtcTerm() {
        return proCtcTerm;
    }

    /**
     * Sets the pro ctc term.
     *
     * @param proCtcTerm the new pro ctc term
     */
    public void setProCtcTerm(ProCtcTerm proCtcTerm) {
        this.proCtcTerm = proCtcTerm;
    }

    /**
     * Gets the pro ctc question type.
     *
     * @return the pro ctc question type
     */
    public ProCtcQuestionType getProCtcQuestionType() {
        return proCtcQuestionType;
    }

    /**
     * Sets the pro ctc question type.
     *
     * @param proCtcQuestionType the new pro ctc question type
     */
    public void setProCtcQuestionType(final ProCtcQuestionType proCtcQuestionType) {
        this.proCtcQuestionType = proCtcQuestionType;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return questionText;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return questionText + " " + proCtcTerm.getCtcTerm().getTerm();
    }

    /**
     * Gets the short text.
     *
     * @return the short text
     */
    public String getShortText() {
        return proCtcTerm.getTerm() + "-" + questionText;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcQuestion)) return false;

        ProCtcQuestion that = (ProCtcQuestion) o;

        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;
        if (proCtcQuestionType != that.proCtcQuestionType) return false;
        if (proCtcTerm != null ? !proCtcTerm.equals(that.proCtcTerm) : that.proCtcTerm != null) return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int result;
        result = (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
        result = 31 * result + (proCtcTerm != null ? proCtcTerm.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        return result;
    }

    /**
     * Gets the pro ctc question display rules.
     *
     * @return the pro ctc question display rules
     */
    public List<ProCtcQuestionDisplayRule> getProCtcQuestionDisplayRules() {
        return proCtcQuestionDisplayRules;
    }
}
