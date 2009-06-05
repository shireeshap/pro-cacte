package gov.nih.nci.ctcae.core.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Mehul Gulati
 *         Date: Jun 1, 2009
 */
@Entity
@Table(name = "MEDDRA_QUESTIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
    @Parameter(name = "sequence", value = "seq_meddra_questions_id")})
public class MeddraQuestion extends BasePersistable {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "meddra_llt_id", referencedColumnName = "id")
    @ManyToOne
    private LowLevelTerm lowLevelTerm;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProCtcQuestionType proCtcQuestionType;

    @Column(name = "display_order", nullable = true)
    private Integer displayOrder;

    @OneToMany(mappedBy = "meddraQuestion", fetch = FetchType.LAZY)
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<MeddraValidValue> validValues = new ArrayList<MeddraValidValue>();


    public MeddraQuestion() {
        super();
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

    public ProCtcQuestionType getProCtcQuestionType() {
        return proCtcQuestionType;
    }

    public void setProCtcQuestionType(ProCtcQuestionType proCtcQuestionType) {
        this.proCtcQuestionType = proCtcQuestionType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LowLevelTerm getLowLevelTerm() {
        return lowLevelTerm;
    }

    public void setLowLevelTerm(LowLevelTerm lowLevelTerm) {
        this.lowLevelTerm = lowLevelTerm;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeddraQuestion)) return false;

        MeddraQuestion that = (MeddraQuestion) o;

        if (displayOrder != null ? !displayOrder.equals(that.displayOrder) : that.displayOrder != null) return false;
        if (lowLevelTerm != null ? !lowLevelTerm.equals(that.lowLevelTerm) : that.lowLevelTerm != null) return false;
        if (proCtcQuestionType != that.proCtcQuestionType) return false;
        if (questionText != null ? !questionText.equals(that.questionText) : that.questionText != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (lowLevelTerm != null ? lowLevelTerm.hashCode() : 0);
        result = 31 * result + (questionText != null ? questionText.hashCode() : 0);
        result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        return result;
    }

    public List<MeddraValidValue> getValidValues() {
        return validValues;
    }

    public void setValidValues(List<MeddraValidValue> validValues) {
        this.validValues = validValues;
    }

    public void addValidValue(MeddraValidValue validValue) {
        if (validValue != null) {
            validValue.setMeddraQuestion(this);
            validValues.add(validValue);
        }
    }
}
