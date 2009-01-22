package gov.nih.nci.ctcae.core.csv.loader;

/**
 * @author Mehul Gulati
 *         Date: Jan 15, 2009
 */
public class CsvLine {

    String proctcTerm;
    String ctcTerm;
    String displayOrder;
    String questionType;
    String questionText;
    String proctcValidValues;

    public String getProctcTerm() {
        return proctcTerm;
    }

    public void setProctcTerm(String proctcTerm) {
        this.proctcTerm = proctcTerm;
    }

    public String getCtcTerm() {
        return ctcTerm;
    }

    public void setCtcTerm(String ctcTerm) {
        this.ctcTerm = ctcTerm;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getProctcValidValues() {
        return proctcValidValues;
    }

    public void setProctcValidValues(String proctcValidValues) {
        this.proctcValidValues = proctcValidValues;
    }

    public String toString() {
        return proctcTerm + "," + ctcTerm + "," + displayOrder + "," + questionType + "," + questionText + "," + proctcValidValues;

    }
}
