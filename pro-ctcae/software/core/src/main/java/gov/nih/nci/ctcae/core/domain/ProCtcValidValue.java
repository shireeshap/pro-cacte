package gov.nih.nci.ctcae.core.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The Class ProCtcValidValue.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_VALID_VALUES")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {@Parameter(name = "sequence", value = "seq_pro_ctc_valid_values_id")})
public class ProCtcValidValue extends ValidValue {

    /**
     * The pro ctc question.
     */
    @JoinColumn(name = "pro_ctc_question_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtcQuestion proCtcQuestion;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "proCtcValidValue")
    @JoinColumn(name="pro_ctc_valid_values_id")
    private ProCtcValidValueVocab proCtcValidValueVocab;

    @Column(name = "response_code", nullable = true)
    protected Integer responseCode;

    /**
     * Instantiates a new pro ctc valid value.
     */
    public ProCtcValidValue() {
    }

    /**
     * Gets the pro ctc question.
     *
     * @return the pro ctc question
     */
    public ProCtcQuestion getProCtcQuestion() {
        return proCtcQuestion;
    }


    /**
     * Sets the pro ctc question.
     *
     * @param proCtcQuestion the new pro ctc question
     */
    public void setProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        this.proCtcQuestion = proCtcQuestion;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcValidValue)) return false;

        ProCtcValidValue that = (ProCtcValidValue) o;
        if (proCtcQuestion != null ? !proCtcQuestion.equals(that.proCtcQuestion) : that.proCtcQuestion != null)
            return false;
        if (proCtcValidValueVocab != null ? !proCtcValidValueVocab.equals(that.proCtcValidValueVocab) : that.proCtcValidValueVocab != null)
            return false;
        
        return true;
    }

    public int hashCode() {
        int result;
        result = (proCtcValidValueVocab != null ? proCtcValidValueVocab.hashCode() : 0);
        result = 31 * result + (proCtcQuestion != null ? proCtcQuestion.hashCode() : 0);
        return result;
    }


	public ProCtcValidValueVocab getProCtcValidValueVocab() {
		return proCtcValidValueVocab;
	}


	public void setProCtcValidValueVocab(ProCtcValidValueVocab proCtcValidValueVocab) {
		this.proCtcValidValueVocab = proCtcValidValueVocab;
	}

     public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

}
