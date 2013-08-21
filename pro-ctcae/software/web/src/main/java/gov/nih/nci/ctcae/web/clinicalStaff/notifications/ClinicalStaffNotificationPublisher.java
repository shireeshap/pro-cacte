package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author AmeyS
 * ClinicalStaffNotificationPublisher class
 * Decouples the logic of sending notification e-mails to newly created clinical staffs
 * to setup their user account, from the controller.
 * Sending notification e-mails are managed here as spring events, which are published using this class.
 * These events are handled by ClinicalStaffNotificationHandler.
 */
public class ClinicalStaffNotificationPublisher implements ApplicationEventPublisherAware{
	ApplicationEventPublisher applicationEventPublisher;

	// send notifications to newly added clinical staff into the system.
	public void publishClinicalStaffNotification(String link, ClinicalStaff clinicalStaff){
		NewStaffAccountNotification event = new NewStaffAccountNotification(this, link, clinicalStaff);
		applicationEventPublisher.publishEvent(event);
	}
	
	// send notification to first Admin user created on Initial setup. 
	public void publishClinicalStaffNotificationOnSetup(String userName, String clearCasePassword, String emailAddress){
		NewStaffAccountNotificationOnSetup event = new NewStaffAccountNotificationOnSetup(this, userName, clearCasePassword, emailAddress);
		applicationEventPublisher.publishEvent(event);
	}
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}
}
