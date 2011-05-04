package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.constants.SupportedLanguageEnum;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class Question extends BasePersistable {
	
//    @Column(name = "question_text", nullable = false)
//    protected String questionText;
    @Transient
    public String getQuestionText(SupportedLanguageEnum supportedLanguageEnum) {
    	if (this instanceof ProCtcQuestion) {
    		if(((ProCtcQuestion) this).getProCtcQuestionVocab() == null){
    			return "";
    		}
    		if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    			return ((ProCtcQuestion) this).getProCtcQuestionVocab().getQuestionTextSpanish();
    		} else {
    			return ((ProCtcQuestion) this).getProCtcQuestionVocab().getQuestionTextEnglish();
    		}
        } else if (this instanceof MeddraQuestion) {
            	if(((MeddraQuestion) this).getMeddraQuestionVocab() == null){
            		return "";
        		}
            	if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
            		return ((MeddraQuestion) this).getMeddraQuestionVocab().getQuestionTextSpanish();
        		} else {
        			return ((MeddraQuestion) this).getMeddraQuestionVocab().getQuestionTextEnglish();
        		}
        }
        return "";
    }

    public void setQuestionText(String questionText, SupportedLanguageEnum supportedLanguageEnum) {
    	if (this instanceof ProCtcQuestion) {
    		if(((ProCtcQuestion) this).getProCtcQuestionVocab() == null){
    			((ProCtcQuestion) this).setProCtcQuestionVocab(new ProCtcQuestionVocab((ProCtcQuestion) this));
    		}
    		if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
    			 ((ProCtcQuestion) this).getProCtcQuestionVocab().setQuestionTextSpanish(questionText);
    		} else {
    			 ((ProCtcQuestion) this).getProCtcQuestionVocab().setQuestionTextEnglish(questionText);
    		}
        } else if (this instanceof MeddraQuestion) {
            	if(((MeddraQuestion) this).getMeddraQuestionVocab() == null){
            		((MeddraQuestion) this).setMeddraQuestionVocab(new MeddraQuestionVocab((MeddraQuestion) this));
        		}
            	if (this instanceof MeddraQuestion) {
                	if(supportedLanguageEnum.equals(SupportedLanguageEnum.SPANISH)){
                		 ((MeddraQuestion) this).getMeddraQuestionVocab().setQuestionTextSpanish(questionText);
            		} else {
            			 ((MeddraQuestion) this).getMeddraQuestionVocab().setQuestionTextEnglish(questionText);
            		}
                }
        }
    }

    @Transient
    public String getQuestionSymptom() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcTerm().getProCtcTermVocab().getTermEnglish();
        } else {
            if (this instanceof MeddraQuestion) {
                return ((MeddraQuestion) this).getLowLevelTerm().getMeddraTerm(SupportedLanguageEnum.ENGLISH);
            }
        }
        return "";

    }

    @Transient
    public String getSymptomGender() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcTerm().getGender();
        }
        return null;
    }

    @Transient
    public ProCtcQuestionType getQuestionType() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcQuestionType();
        }
        if (this instanceof MeddraQuestion) {
            return ((MeddraQuestion) this).getProCtcQuestionType();
        }
        return null;
    }

    @Transient
    public String getStringId() {
        if (this instanceof ProCtcQuestion) {
            return "P_" + ((ProCtcQuestion) this).getProCtcTerm().getId();
        }
        if (this instanceof MeddraQuestion) {
            return "M_" + ((MeddraQuestion) this).getLowLevelTerm().getId();
        }
        return null;
    }

}
