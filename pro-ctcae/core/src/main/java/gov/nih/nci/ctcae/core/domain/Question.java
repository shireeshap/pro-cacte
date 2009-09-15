package gov.nih.nci.ctcae.core.domain;

import javax.persistence.Column;

/**
 * Created by IntelliJ IDEA.
 * User: Harsh
 * Date: Aug 13, 2009
 * Time: 10:52:25 AM
 * To change this template use File | Settings | File Templates.
 */
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
