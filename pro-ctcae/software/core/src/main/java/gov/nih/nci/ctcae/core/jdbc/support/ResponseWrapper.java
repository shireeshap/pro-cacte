package gov.nih.nci.ctcae.core.jdbc.support;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

import java.util.Date;
import java.util.List;

public class ResponseWrapper {
	
	public Integer scheduleId;
	public Integer crfPageItemId;
	public Integer proQuestionId;
	public ProCtcQuestionType questionType;
	public String termEnglish;
	public String valueEnglish;
	public List<Integer> questionsList;
	public Date responseDate;
	public String responseCode;
	public String questionPosition;
	public String gender;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Integer> getQuestionsList() {
		return questionsList;
	}
	public void setQuestionsList(List<Integer> questionsList) {
		this.questionsList = questionsList;
	}
	public String getQuestionPosition() {
		return questionPosition;
	}
	public void setQuestionPosition(String questionPosition) {
		this.questionPosition = questionPosition;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public Date getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getCrfPageItemId() {
		return crfPageItemId;
	}
	public void setCrfPageItemId(Integer crfPageItemId) {
		this.crfPageItemId = crfPageItemId;
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
