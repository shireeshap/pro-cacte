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
 * The Class MeddraValidValueVocab.
 *
 * @author Vinay Gangoli
 */

@Entity
@Table(name = "meddra_valid_values_vocab")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_meddra_valid_values_vocab_id")})
public class MeddraValidValueVocab extends BasePersistable {

	public MeddraValidValueVocab(){
		super();
	}
	
	public MeddraValidValueVocab(MeddraValidValue meddraValidValue){
		super();
		this.meddraValidValue = meddraValidValue;
	}
	
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @Column(name = "value_english", nullable = false)
    private String valueEnglish;

    @Column(name = "value_spanish", nullable = true)
    private String valueSpanish;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id", name = "meddra_valid_values_id", nullable = false)
    @Cascade({CascadeType.SAVE_UPDATE})
    private MeddraValidValue meddraValidValue;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return valueEnglish;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeddraValidValueVocab)) return false;

        MeddraValidValueVocab that = (MeddraValidValueVocab) o;
        if (valueEnglish != null ? !valueEnglish.equals(that.valueEnglish) : that.valueEnglish != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (valueEnglish != null ? valueEnglish.hashCode() : 0);
        result = 31 * result;
        result = 31 * result;
        return result;
    }
	
	public String getValueEnglish() {
		return valueEnglish;
	}

	public void setValueEnglish(String valueEnglish) {
		this.valueEnglish = valueEnglish;
	}

	public String getValueSpanish() {
        if(valueSpanish == null)
            return valueEnglish;
        else
            return valueSpanish;
	}

	public void setValueSpanish(String valueSpanish) {
		this.valueSpanish = valueSpanish;
	}

	public MeddraValidValue getMeddraValidValue() {
		return meddraValidValue;
	}

	public void setMeddraValidValue(MeddraValidValue meddraValidValue) {
		this.meddraValidValue = meddraValidValue;
	}


}
