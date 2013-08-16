package gov.nih.nci.ctcae.core.jdbc.support;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

/**
 * @author AmeyS
 * Used as a value object for holding proTerm & questionType for every question on given CRF.
 * Used along with CrfQuestionsTemplateCallBackHandler.
 */
public class CrfQuestionsTemplateWrapper {
	
	public String crfTitle;
	public ProCtcQuestionType questionType;
	public String termEnglish;
	
	public String getCrfTitle() {
		return crfTitle;
	}
	public void setCrfTitle(String crfTitle) {
		this.crfTitle = crfTitle;
	}
	public ProCtcQuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(ProCtcQuestionType questionType) {
		this.questionType = questionType;
	}
	public String getTermEnglish() {
		return termEnglish;
	}
	public void setTermEnglish(String termEnglish) {
		this.termEnglish = termEnglish;
	}
}
