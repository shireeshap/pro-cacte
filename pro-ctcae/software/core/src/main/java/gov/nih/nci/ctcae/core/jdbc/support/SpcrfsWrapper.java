package gov.nih.nci.ctcae.core.jdbc.support;

import gov.nih.nci.ctcae.core.domain.AppMode;
import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Date;

public class SpcrfsWrapper {
	private Integer id;
    private Date startDate;
    private Date dueDate;
    private Date completionDate;
    private String filePath;
    private boolean holiday;
    private CrfStatus status = CrfStatus.SCHEDULED;
    private Integer cycleNumber;
    private Integer cycleDay;
    private Integer weekInStudy;
    private Integer monthInStudy;
    private boolean baseline = false;
    private String verbatim;
    private boolean markDelete = false;
    private Integer healthAmount;    
    private Integer studyParticipantCrf;
    private Integer studyParticipantCrfItems;
    private Integer studyParticipantCrfScheduleAddedQuestions;
    private Integer userNotifications;
    private AppMode formSubmissionMode;
    private Integer studyParticipantCrfScheduleNotification;
    private Integer ivrsSchedules; 
    private String language;
    private String crfTitle;
    
    public String getCrfTitle() {
		return crfTitle;
	}
	public void setCrfTitle(String crfTitle) {
		this.crfTitle = crfTitle;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public boolean isHoliday() {
		return holiday;
	}
	public void setHoliday(boolean holiday) {
		this.holiday = holiday;
	}
	public CrfStatus getStatus() {
		return status;
	}
	public void setStatus(CrfStatus status) {
		this.status = status;
	}
	public Integer getCycleNumber() {
		return cycleNumber;
	}
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
	public Integer getCycleDay() {
		return cycleDay;
	}
	public void setCycleDay(Integer cycleDay) {
		this.cycleDay = cycleDay;
	}
	public Integer getWeekInStudy() {
		return weekInStudy;
	}
	public void setWeekInStudy(Integer weekInStudy) {
		this.weekInStudy = weekInStudy;
	}
	public Integer getMonthInStudy() {
		return monthInStudy;
	}
	public void setMonthInStudy(Integer monthInStudy) {
		this.monthInStudy = monthInStudy;
	}
	public boolean isBaseline() {
		return baseline;
	}
	public void setBaseline(boolean baseline) {
		this.baseline = baseline;
	}
	public String getVerbatim() {
		return verbatim;
	}
	public void setVerbatim(String verbatim) {
		this.verbatim = verbatim;
	}
	public boolean isMarkDelete() {
		return markDelete;
	}
	public void setMarkDelete(boolean markDelete) {
		this.markDelete = markDelete;
	}
	public Integer getHealthAmount() {
		return healthAmount;
	}
	public void setHealthAmount(Integer healthAmount) {
		this.healthAmount = healthAmount;
	}
	public Integer getStudyParticipantCrf() {
		return studyParticipantCrf;
	}
	public void setStudyParticipantCrf(Integer studyParticipantCrf) {
		this.studyParticipantCrf = studyParticipantCrf;
	}
	public Integer getStudyParticipantCrfItems() {
		return studyParticipantCrfItems;
	}
	public void setStudyParticipantCrfItems(Integer studyParticipantCrfItems) {
		this.studyParticipantCrfItems = studyParticipantCrfItems;
	}
	public Integer getStudyParticipantCrfScheduleAddedQuestions() {
		return studyParticipantCrfScheduleAddedQuestions;
	}
	public void setStudyParticipantCrfScheduleAddedQuestions(
			Integer studyParticipantCrfScheduleAddedQuestions) {
		this.studyParticipantCrfScheduleAddedQuestions = studyParticipantCrfScheduleAddedQuestions;
	}
	public Integer getUserNotifications() {
		return userNotifications;
	}
	public void setUserNotifications(Integer userNotifications) {
		this.userNotifications = userNotifications;
	}
	public AppMode getFormSubmissionMode() {
		return formSubmissionMode;
	}
	public void setFormSubmissionMode(AppMode formSubmissionMode) {
		this.formSubmissionMode = formSubmissionMode;
	}
	public Integer getStudyParticipantCrfScheduleNotification() {
		return studyParticipantCrfScheduleNotification;
	}
	public void setStudyParticipantCrfScheduleNotification(
			Integer studyParticipantCrfScheduleNotification) {
		this.studyParticipantCrfScheduleNotification = studyParticipantCrfScheduleNotification;
	}
	public Integer getIvrsSchedules() {
		return ivrsSchedules;
	}
	public void setIvrsSchedules(Integer ivrsSchedules) {
		this.ivrsSchedules = ivrsSchedules;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
}
