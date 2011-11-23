package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mehul Gulati
 *         Date: Jun 1, 2009
 */
@Entity
@Table(name = "MEDDRA_QUESTIONS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_meddra_questions_id")})
public class MeddraQuestion extends Question {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "meddraQuestion")
    @JoinColumn(name="meddra_questions_id")
    private MeddraQuestionVocab meddraQuestionVocab;

    @JoinColumn(name = "meddra_llt_id", referencedColumnName = "id")
    @ManyToOne
    private LowLevelTerm lowLevelTerm;


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
        if (meddraQuestionVocab != null ? !meddraQuestionVocab.equals(that.meddraQuestionVocab) : that.meddraQuestionVocab != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (lowLevelTerm != null ? lowLevelTerm.hashCode() : 0);
        result = 31 * result + (meddraQuestionVocab != null ? meddraQuestionVocab.hashCode() : 0);
        result = 31 * result + (proCtcQuestionType != null ? proCtcQuestionType.hashCode() : 0);
        result = 31 * result + (displayOrder != null ? displayOrder.hashCode() : 0);
        return result;
    }

    public List<MeddraValidValue> getValidValues() {
        Collections.sort(validValues, new DisplayOrderComparator());
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


	public MeddraQuestionVocab getMeddraQuestionVocab() {
		return meddraQuestionVocab;
	}


	public void setMeddraQuestionVocab(MeddraQuestionVocab meddraQuestionVocab) {
		this.meddraQuestionVocab = meddraQuestionVocab;
	}
}
