package gov.nih.nci.ctcae.core.domain;

public class AddedMeddraQuestionWrapper {
	
	public Integer scheduleId;
	public Integer meddraQuestionId;
	public ProCtcQuestionType questionType;
	public String meddraTermEnglish;
	public String valueEnglish;
	public String displayOrder;
	
	public String getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getMeddraQuestionId() {
		return meddraQuestionId;
	}
	public void setMeddraQuestionId(Integer proQuestionId) {
		this.meddraQuestionId = proQuestionId;
	}
	public ProCtcQuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(ProCtcQuestionType questionType) {
		this.questionType = questionType;
	}
	public String getTermEnglish() {
		return meddraTermEnglish;
	}
	public void setTermEnglish(String termEnglish) {
		this.meddraTermEnglish = termEnglish;
	}
	public String getValueEnglish() {
		return valueEnglish;
	}
	public void setValueEnglish(String valueEnglish) {
		this.valueEnglish = valueEnglish;
	}
}
