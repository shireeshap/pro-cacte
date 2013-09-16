package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import org.springframework.context.ApplicationEvent;

/**
 * @author AmeyS
 * NewStaffAccountNotificationOnSetup class.
 * Custom ApplicationEvent class, used to send account notification e-mail to first Admin created on
 * system's initial setup.
 */
public class NewStaffAccountNotificationOnSetupEvent extends ApplicationEvent{

	private String userName;
	private String clearCasePassword;
	private String emailAddress;
	

	public NewStaffAccountNotificationOnSetupEvent(Object source, String userName, String clearCasePassword, String emailAddress) {
		super(source);
		setUserName(userName);
		setClearCasePassword(clearCasePassword);
		setEmailAddress(emailAddress);
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getClearCasePassword() {
		return clearCasePassword;
	}
	
	public void setClearCasePassword(String clearCasePassword) {
		this.clearCasePassword = clearCasePassword;
	}
}
