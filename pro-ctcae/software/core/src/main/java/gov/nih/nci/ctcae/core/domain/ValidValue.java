package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ValidValue extends BasePersistable {
    @Id
    @GeneratedValue(generator = "id-generator")
    @Column(name = "id")
    protected Integer id;

//    @Column(name = "value", nullable = false)
//    protected String value;

    @Column(name = "display_order", nullable = true)
    protected Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    /*
     * default implementation assumes English as the preferred language
     */
    public String getValue(){
    	return getValue(SupportedLanguageEnum.ENGLISH);
    }
    
    public void setValue(String value){
    	setValue(value, SupportedLanguageEnum.ENGLISH);
    }
    

    public String getValue(SupportedLanguageEnum supportedLanguageEnum) {
    	if(this instanceof ProCtcValidValue){
    		if(((ProCtcValidValue)this).getProCtcValidValueVocab() != null){
    			if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    				return ((ProCtcValidValue)this).getProCtcValidValueVocab().getValueSpanish();
    			} else {
    				return ((ProCtcValidValue)this).getProCtcValidValueVocab().getValueEnglish();
    			}
    		}
    	}
    	if(this instanceof MeddraValidValue){
    		if(((MeddraValidValue)this).getMeddraValidValueVocab() != null){
    			if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    				return ((MeddraValidValue)this).getMeddraValidValueVocab().getValueSpanish();
    			} else {
    				return ((MeddraValidValue)this).getMeddraValidValueVocab().getValueEnglish();
    			}
    			
    		}
    	}
        return "";
    }

    public String getValueSpanish() {
         return getValue(SupportedLanguageEnum.SPANISH);
    }

    public void setValue(String value, SupportedLanguageEnum supportedLanguageEnum) {
    	if(this instanceof ProCtcValidValue){
    		if(((ProCtcValidValue)this).getProCtcValidValueVocab() == null){
    			((ProCtcValidValue)this).setProCtcValidValueVocab(new ProCtcValidValueVocab((ProCtcValidValue)this));
    		}
			if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
				 ((ProCtcValidValue)this).getProCtcValidValueVocab().setValueSpanish(value);
			} else {
				 ((ProCtcValidValue)this).getProCtcValidValueVocab().setValueEnglish(value);
			}
    			
    	}
    	if(this instanceof MeddraValidValue){
    		if(((MeddraValidValue)this).getMeddraValidValueVocab() == null){
    			((MeddraValidValue)this).setMeddraValidValueVocab(new MeddraValidValueVocab((MeddraValidValue)this));
    		}
			if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
				 ((MeddraValidValue)this).getMeddraValidValueVocab().setValueSpanish(value);
			} else {
				 ((MeddraValidValue)this).getMeddraValidValueVocab().setValueEnglish(value);
			}
    	}
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getCode() {
        return displayOrder;
    }

    public String getDesc() {
    	return getValue(SupportedLanguageEnum.ENGLISH);
    }
    
    public String getDesc(SupportedLanguageEnum supportedLanguageEnum) {
    	return getValue(supportedLanguageEnum);
    }
    
}
