package gov.nih.nci.ctcae.core.domain.meddra;


import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;
import gov.nih.nci.ctcae.core.domain.LowLevelTermVocab;
import gov.nih.nci.ctcae.core.domain.MeddraQuestion;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "meddra_llt")
public class LowLevelTerm extends AbstractMeddraDomainObject implements Comparable<LowLevelTerm> {

    private List<MeddraQuestion> meddraQuestions = new ArrayList<MeddraQuestion>();
    private Boolean participantAdded = false;
    private Integer meddraPtId;
    private String meddraCode;
    private String currency;

    @Transient
    public String getFullName(SupportedLanguageEnum supportedLanguageEnum) {
        return getMeddraTerm(supportedLanguageEnum);
    }
    
    public String getMeddraTerm(SupportedLanguageEnum supportedLanguageEnum) {
        if(getLowLevelTermVocab() != null){
        	if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
        		return getLowLevelTermVocab().getMeddraTermSpanish();
        	} else {
        		return getLowLevelTermVocab().getMeddraTermEnglish();
        	}
        }
        return "";
    }

    public void setMeddraTerm(String meddraTerm, SupportedLanguageEnum supportedLanguageEnum) {
    	if(getLowLevelTermVocab() == null){
    		setLowLevelTermVocab(new LowLevelTermVocab());
    	}
    	
    	if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    		 getLowLevelTermVocab().setMeddraTermSpanish(meddraTerm);
    	} else {
    		 getLowLevelTermVocab().setMeddraTermEnglish(meddraTerm);
    	}
    }

    private LowLevelTermVocab lowLevelTermVocab;
    

    @OneToMany(mappedBy = "lowLevelTerm")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    public List<MeddraQuestion> getMeddraQuestions() {
        return meddraQuestions;
    }

    public void setMeddraQuestions(List<MeddraQuestion> meddraQuestions) {
        this.meddraQuestions = meddraQuestions;
    }

    @Column(name = "participant_added")
    public Boolean isParticipantAdded() {
        if (participantAdded == null) {
            return false;
        }
        return participantAdded;
    }

    public void setParticipantAdded(Boolean participantAdded) {
        this.participantAdded = participantAdded;
    }

    @Column(name = "meddra_pt_id")
    public Integer getMeddraPtId() {
        return meddraPtId;
    }

    public void setMeddraPtId(Integer meddraPtId) {
        this.meddraPtId = meddraPtId;
    }

    @Column(name = "meddra_code")
    public String getMeddraCode() {
        return meddraCode;
    }

    public void setMeddraCode(String meddraCode) {
        this.meddraCode = meddraCode;
    }

    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "lowLevelTerm")
    @JoinColumn(name="meddra_llt_id")
	public LowLevelTermVocab getLowLevelTermVocab() {
		return lowLevelTermVocab;
	}

	public void setLowLevelTermVocab(LowLevelTermVocab lowLevelTermVocab) {
		this.lowLevelTermVocab = lowLevelTermVocab;
	}

	@Override
	public int compareTo(LowLevelTerm o) {
		return this.getLowLevelTermVocab().getMeddraTermEnglish().toLowerCase().compareTo(o.getLowLevelTermVocab().getMeddraTermEnglish().toLowerCase());
	}
}