package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.List;
import java.util.ArrayList;

@MappedSuperclass
public abstract class Question extends BasePersistable {
    @Column(name = "question_text", nullable = false)
    protected String questionText;

    public String getQuestionText() {
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
}
