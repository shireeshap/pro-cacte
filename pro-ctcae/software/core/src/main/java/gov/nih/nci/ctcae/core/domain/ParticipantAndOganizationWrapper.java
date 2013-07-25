package gov.nih.nci.ctcae.core.domain;

public class ParticipantAndOganizationWrapper {
	
	public Integer scheduleId;
	public String mrnIdentifier;
	public String firstName;
	public String lastName;
	public String organizationName;
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getMrnIdentifier() {
		return mrnIdentifier;
	}
	public void setMrnIdentifier(String mrnIdentifier) {
		this.mrnIdentifier = mrnIdentifier;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	
	
}
