package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.ProctcTermTypeBasedCategoryEnum;
import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The Class ProCtcTerm.
 *
 * @author Harsh Agarwal
 * @created Oct 13, 2008
 */

@Entity
@Table(name = "PRO_CTC_TERMS")
@GenericGenerator(name = "id-generator", strategy = "native", parameters = {
        @Parameter(name = "sequence", value = "seq_pro_ctc_terms_id")})
public class ProCtcTerm extends BasePersistable implements Comparable<ProCtcTerm> {

    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "proCtcTerm")
    @JoinColumn(name="pro_ctc_terms_id")
    private ProCtcTermVocab proCtcTermVocab;
    

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "core", nullable = true)
    private Boolean core;

	@OneToMany(mappedBy = "proCtcTerm")
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<ProctcaeGradeMapping> proCtcGradeMappings = new ArrayList<ProctcaeGradeMapping>();
    
    /**
     * The pro ctc questions.
     */
    @OneToMany(mappedBy = "proCtcTerm")
    @OrderBy("displayOrder asc")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<ProCtcQuestion> proCtcQuestions = new ArrayList<ProCtcQuestion>();

    /**
     * The pro ctc.
     */
    @JoinColumn(name = "pro_ctc_id", referencedColumnName = "id")
    @ManyToOne
    private ProCtc proCtc;

    /**
     * The ctc term.
     */
    @JoinColumn(name = "ctc_term_id", referencedColumnName = "id")
    @ManyToOne
    private CtcTerm ctcTerm;

    @Column(name = "currency", nullable = true)
    private String currency;
    
    @Column(name="pro_ctc_sys_id", nullable = false)
    private Integer proCtcSystemId; 

    /**
     * Instantiates a new pro ctc term.
     */
    public ProCtcTerm() {
        super();
    }
    
    public List<ProctcaeGradeMapping> getProCtcGradeMappings() {
        return proCtcGradeMappings;
    }
    
    /**
     * Adds the pro ctc question.
     *
     * @param proCtcQuestion the pro ctc question
     */
    public void addProCtcGradeMapping(ProctcaeGradeMapping proCtcGradeMapping) {
        if (proCtcGradeMapping != null) {
        	proCtcGradeMapping.setProCtcTerm(this);
            proCtcGradeMappings.add(proCtcGradeMapping);
        }
    }
    
    public void addAllProCtcGradeMappings(List<ProctcaeGradeMapping> proCtcGradeMappingList) {
        if (proCtcGradeMappingList != null && !proCtcGradeMappingList.isEmpty()) {
        	for(ProctcaeGradeMapping pcgMapping : proCtcGradeMappingList){
        		addProCtcGradeMapping(pcgMapping);
        	}
        }
    }
    
    /*
     * Default implementation assumes English language
     */
    @Transient
    public String getTerm(){
    	return getTermEnglish(SupportedLanguageEnum.ENGLISH);
    }
    
    public void setTerm(String value){
    	setTermEnglish(value, SupportedLanguageEnum.ENGLISH);
    }
    
    @Transient
    public String getTermEnglish(SupportedLanguageEnum supportedLanguageEnum) {
    	if (getProCtcTermVocab() != null) {
    		return getProCtcTermVocab().getTermEnglish();
        } 
        return "";
    }

    public void setTermEnglish(String term, SupportedLanguageEnum supportedLanguageEnum) {
    	if (getProCtcTermVocab() == null) {
    		setProCtcTermVocab(new ProCtcTermVocab());
    		getProCtcTermVocab().setProCtcTerm(this);
    	}
    	if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    		getProCtcTermVocab().setTermSpanish(term);
    	} else {
    		getProCtcTermVocab().setTermEnglish(term);
    	}
    }
    
    @Transient
    public ProctcTermTypeBasedCategoryEnum getTypeBasedCategory(){
    	
    	if(getProCtcQuestions().size() == 1){
    		ProCtcQuestion pQs = getProCtcQuestions().get(0);
    		if(pQs.getQuestionType().equals(ProCtcQuestionType.PRESENT)){
    			return ProctcTermTypeBasedCategoryEnum.CATEGORY_PA;
    		} else if(pQs.getQuestionType().equals(ProCtcQuestionType.AMOUNT)){
    			return ProctcTermTypeBasedCategoryEnum.CATEGORY_AMT;
    		}
    	}

    	boolean isFreq = false;
    	boolean isSev = false;
    	boolean isInt = false;
    	for(ProCtcQuestion pQs : getProCtcQuestions()){
    		if(pQs.getQuestionType().equals(ProCtcQuestionType.FREQUENCY)){
    			isFreq = true;
    		} else if(pQs.getQuestionType().equals(ProCtcQuestionType.SEVERITY)){
    			isSev = true;
    		}else if(pQs.getQuestionType().equals(ProCtcQuestionType.INTERFERENCE)){
    			isInt = true;
    		}
    	}
    	
    	if(isFreq && isSev && isInt){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_FSI;
    	}
    	if(isFreq && isSev){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_FS;
    	}
    	if(isSev && isInt){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_SI;
    	}
    	if(isFreq && isInt){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_FI;
    	}
    	if(isFreq){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_F;
    	}
    	if(isSev){
    		return ProctcTermTypeBasedCategoryEnum.CATEGORY_S;
    	}
    	return null;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the pro ctc questions.
     *
     * @return the pro ctc questions
     */
    public List<ProCtcQuestion> getProCtcQuestions() {
        return proCtcQuestions;
    }

    /**
     * Gets the pro ctc.
     *
     * @return the pro ctc
     */
    public ProCtc getProCtc() {
        return proCtc;
    }

    /**
     * Sets the pro ctc.
     *
     * @param proCtc the new pro ctc
     */
    public void setProCtc(ProCtc proCtc) {
        this.proCtc = proCtc;
    }


    /**
     * Adds the pro ctc question.
     *
     * @param proCtcQuestion the pro ctc question
     */
    public void addProCtcQuestion(ProCtcQuestion proCtcQuestion) {
        if (proCtcQuestion != null) {
            proCtcQuestion.setProCtcTerm(this);
            proCtcQuestions.add(proCtcQuestion);

        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return getProCtcTermVocab().getTermEnglish();
    }

    /**
     * Gets the ctc term.
     *
     * @return the ctc term
     */
    public CtcTerm getCtcTerm() {
        return ctcTerm;
    }

    /**
     * Sets the ctc term.
     *
     * @param ctcTerm the new ctc term
     */
    public void setCtcTerm(CtcTerm ctcTerm) {
        this.ctcTerm = ctcTerm;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProCtcTerm)) return false;

        ProCtcTerm that = (ProCtcTerm) o;

        if (ctcTerm != null ? !ctcTerm.equals(that.ctcTerm) : that.ctcTerm != null) return false;
        if (proCtc != null ? !proCtc.equals(that.proCtc) : that.proCtc != null) return false;
        if (proCtcTermVocab != null ? !proCtcTermVocab.equals(that.proCtcTermVocab) : that.proCtcTermVocab != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (getProCtcTermVocab() != null ? getProCtcTermVocab().getTermEnglish().hashCode() : 0);
        //result = 31 * result + (proCtcTermVocab != null ? proCtcTermVocab.hashCode() : 0);
        result = 31 * result + (proCtc != null ? proCtc.hashCode() : 0);
        result = 31 * result + (ctcTerm != null ? ctcTerm.hashCode() : 0);
        return result;
    }
    
    
    @Transient
    public Map<ProctcaeGradeMapping, String> getProctcaeGradeMappingMap(){
    	Map<ProctcaeGradeMapping, String> pgmMap = new HashMap<ProctcaeGradeMapping, String>();
    	
    	for(ProctcaeGradeMapping pgm : proCtcGradeMappings){
    		pgmMap.put(pgm, pgm.getProCtcGrade());
    	}
    	
    	return pgmMap;
    }

    public Boolean isCore() {
        return core;
    }

    public void setCore(Boolean core) {
        this.core = core;
    }


	public ProCtcTermVocab getProCtcTermVocab() {
		return proCtcTermVocab;
	}


	public void setProCtcTermVocab(ProCtcTermVocab proCtcTermVocab) {
		this.proCtcTermVocab = proCtcTermVocab;
	}

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getProCtcSystemId() {
		return proCtcSystemId;
	}

	public void setProCtcSystemId(Integer proCtcSystemId) {
		this.proCtcSystemId = proCtcSystemId;
	}

	@Override
	public int compareTo(ProCtcTerm o) {
		 return this.getProCtcTermVocab().getTermEnglish().toLowerCase().compareTo(o.getProCtcTermVocab().getTermEnglish().toLowerCase());
	}
}
