package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.domain.meddra.LowLevelTerm;

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
 * The Class LowLevelTermVocab.
 *
 * @author Vinay Gangoli
 */

@Entity
@Table(name = "meddra_llt_vocab")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_meddra_llt_vocab_id")})
public class LowLevelTermVocab extends BasePersistable {

	public LowLevelTermVocab(){
		super();
	}
	
	public LowLevelTermVocab(LowLevelTerm lowLevelTerm){
		super();
		this.lowLevelTerm = lowLevelTerm;
	}
	
	
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "meddra_term_english", nullable = false)
    private String meddraTermEnglish;

    @Column(name = "meddra_term_spanish", nullable = true)
    private String meddraTermSpanish;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "meddra_llt_id", nullable = false)
    @Cascade({CascadeType.SAVE_UPDATE})
    private LowLevelTerm lowLevelTerm; 
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return meddraTermEnglish;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LowLevelTermVocab)) return false;

        LowLevelTermVocab that = (LowLevelTermVocab) o;
        if (meddraTermEnglish != null ? !meddraTermEnglish.equals(that.meddraTermEnglish) : that.meddraTermEnglish != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (meddraTermEnglish != null ? meddraTermEnglish.hashCode() : 0);
        result = 31 * result;
        result = 31 * result;
        return result;
    }

	public String getMeddraTermEnglish() {
		return meddraTermEnglish;
	}

	public void setMeddraTermEnglish(String meddraTermEnglish) {
		this.meddraTermEnglish = meddraTermEnglish;
	}

	public String getMeddraTermSpanish() {
		return meddraTermSpanish;
	}

	public void setMeddraTermSpanish(String meddraTermSpanish) {
		this.meddraTermSpanish = meddraTermSpanish;
	}

	public LowLevelTerm getLowLevelTerm() {
		return lowLevelTerm;
	}

	public void setLowLevelTerm(LowLevelTerm lowLevelTerm) {
		this.lowLevelTerm = lowLevelTerm;
	}

}
