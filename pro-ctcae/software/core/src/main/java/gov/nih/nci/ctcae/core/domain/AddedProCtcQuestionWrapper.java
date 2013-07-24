package gov.nih.nci.ctcae.core.domain;

public class AddedProCtcQuestionWrapper {
	
	public Integer scheduleId;
	public Integer proQuestionId;
	public ProCtcQuestionType questionType;
	public String termEnglish;
	public String valueEnglish;
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getProQuestionId() {
		return proQuestionId;
	}
	public void setProQuestionId(Integer proQuestionId) {
		this.proQuestionId = proQuestionId;
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
	public String getValueEnglish() {
		return valueEnglish;
	}
	public void setValueEnglish(String valueEnglish) {
		this.valueEnglish = valueEnglish;
	}
}
