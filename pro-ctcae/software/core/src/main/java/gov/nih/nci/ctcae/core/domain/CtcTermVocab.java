package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


/**
 * The Class CtcTermVocab.
 *
 * @author Vinay Gangoli
 */

@Entity
@Table(name = "ctc_terms_vocab")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_ctc_terms_vocab_id")})
public class CtcTermVocab extends BasePersistable {

	public CtcTermVocab(){
		super();
	}
	
	public CtcTermVocab(CtcTerm ctcTerm){
		super();
		this.ctcTerm = ctcTerm;
	}
	
	
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    /**
     * The term in English.
     */
    @Column(name = "term_english", nullable = false)
    private String termEnglish;

    @Column(name = "term_spanish", nullable = true)
    private String termSpanish;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "ctc_terms_id", nullable = false)
    @Cascade({CascadeType.SAVE_UPDATE})
    private CtcTerm ctcTerm; 
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return termEnglish;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CtcTermVocab)) return false;

        CtcTermVocab that = (CtcTermVocab) o;
        if (termEnglish != null ? !termEnglish.equals(that.termEnglish) : that.termEnglish != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (termEnglish != null ? termEnglish.hashCode() : 0);
        result = 31 * result;
        result = 31 * result;
        return result;
    }

	public String getTermEnglish() {
		return termEnglish;
	}

	public void setTermEnglish(String termEnglish) {
		this.termEnglish = termEnglish;
	}

	public String getTermSpanish() {
		return termSpanish;
	}

	public void setTermSpanish(String termSpanish) {
		this.termSpanish = termSpanish;
	}

	public CtcTerm getCtcTerm() {
		return ctcTerm;
	}

	public void setCtcTerm(CtcTerm ctcTerm) {
		this.ctcTerm = ctcTerm;
	}

}