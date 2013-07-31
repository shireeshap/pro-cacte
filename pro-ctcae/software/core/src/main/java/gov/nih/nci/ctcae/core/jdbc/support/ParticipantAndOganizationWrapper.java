package gov.nih.nci.ctcae.core.jdbc.support;

public class ParticipantAndOganizationWrapper {
	
	public Integer scheduleId;
	public String studyParticipantIdentifier;
	public String firstName;
	public String lastName;
	public String organizationName;
	public String participantId;
	public String gender;
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getStudyParticipantIdentifier() {
		return studyParticipantIdentifier;
	}
	public void setStudyParticipantIdentifier(String mrnIdentifier) {
		this.studyParticipantIdentifier = mrnIdentifier;
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
