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
 * The Class ProCtcTermVocab.
 *
 * @author Vinay Gangoli
 */

@Entity
@Table(name = "pro_ctc_terms_vocab")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_pro_ctc_terms_vocab_id")})
public class ProCtcTermVocab extends BasePersistable {

	public ProCtcTermVocab(){
		super();
	}
	
	public ProCtcTermVocab(ProCtcTerm proCtcTerm){
		super();
		this.proCtcTerm = proCtcTerm;
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
    @JoinColumn(referencedColumnName = "id", name = "pro_ctc_terms_id", nullable = false)
    @Cascade({CascadeType.SAVE_UPDATE})
    //@PrimaryKeyJoinColumn
    private ProCtcTerm proCtcTerm; 
    
    
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
        if (!(o instanceof ProCtcTermVocab)) return false;

        ProCtcTermVocab that = (ProCtcTermVocab) o;
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
        if(termSpanish == null)
            termSpanish = termEnglish;
		return termSpanish;
	}

	public void setTermSpanish(String termSpanish) {
		this.termSpanish = termSpanish;
	}

	public ProCtcTerm getProCtcTerm() {
		return proCtcTerm;
	}

	public void setProCtcTerm(ProCtcTerm proCtcTerm) {
		this.proCtcTerm = proCtcTerm;
	}

}
