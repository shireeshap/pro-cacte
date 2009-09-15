package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

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
}
