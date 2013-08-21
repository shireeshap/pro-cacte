package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

import org.springframework.context.ApplicationEvent;

/**
 * @author AmeyS
 * NewStaffAccountNotification class.
 * Custom ApplicationEvent class, used to send account notification e-mails to all newly added clinical staff.
 */
public class NewStaffAccountNotification extends ApplicationEvent{

	String link;
	private ClinicalStaff clinicalStaff;
	

	public NewStaffAccountNotification(Object source, String link, ClinicalStaff clinicalStaff) {
		super(source);
		setLink(link);
		setClinicalStaff(clinicalStaff);
	}
	
	public void setLink(String link){
		this.link = link;
	}
	
	public String getLink(){
		return link;
	}
	
	
	public ClinicalStaff getClinicalStaff() {
		return clinicalStaff;
	}
	
	public void setClinicalStaff(ClinicalStaff clinicalStaff) {
		this.clinicalStaff = clinicalStaff;
	}
}
