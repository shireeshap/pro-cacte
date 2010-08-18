package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public abstract class Question extends BasePersistable {
    @Column(name = "question_text", nullable = false)
    protected String questionText;

    public String getQuestionText() {
        if (questionText.indexOf(":") != -1) {
            questionText = questionText.substring(0, questionText.indexOf(":"));
        }
        questionText = questionText.replaceAll("\\?", "");
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Transient
    public String getQuestionSymptom() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcTerm().getTerm();
        } else {
            if (this instanceof MeddraQuestion) {
                return ((MeddraQuestion) this).getLowLevelTerm().getMeddraTerm();
            }
        }
        return "";

    }

    public String getSymptomGender() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcTerm().getGender();
        }
        return null;
    }

    public ProCtcQuestionType getQuestionType() {
        if (this instanceof ProCtcQuestion) {
            return ((ProCtcQuestion) this).getProCtcQuestionType();
        }
        if (this instanceof MeddraQuestion) {
            return ((MeddraQuestion) this).getProCtcQuestionType();
        }
        return null;
    }

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
