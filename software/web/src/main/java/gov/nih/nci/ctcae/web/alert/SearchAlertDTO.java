package gov.nih.nci.ctcae.web.alert;

public class SearchAlertDTO {
    private String startDate;
    private String endDate;
	private String alertStatus;
	private String alertMessage;
	private String actions;
	
	public String getActions() {
		return actions;
	}
	public String getAlertMessage() {
		return alertMessage;
	}
	public String getAlertStatus() {
		return alertStatus;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setActions(String actions) {
		this.actions = actions;
	}
	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}
	public void setAlertStatus(String alertStatus) {
		this.alertStatus = alertStatus;
	}
    public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
}