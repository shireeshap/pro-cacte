package gov.nih.nci.ctcae.web.clinicalStaff.notifications;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.MimeMessageHelper;


/**
 * @author AmeyS
 * ClinicalStaffNotificationHandler class.
 * Handles events published by ClinicalStaffNotificationPublisher.
 * Sends account notification e-mails to clinical staffs newly added into the system and to first Admin staff created 
 * on system's initial setup.
 */
public class ClinicalStaffNotificationHandler implements ApplicationListener{
	protected UserRepository userRepository;
	Log logger;
	
	ClinicalStaffNotificationHandler(){
		logger = LogFactory.getLog(getClass());
	}
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof NewStaffAccountNotificationEvent){
			try {

				//Reset Password link generation
				NewStaffAccountNotificationEvent newStaffAccountNotification = (NewStaffAccountNotificationEvent) event;
				ClinicalStaff clinicalStaff = newStaffAccountNotification.getClinicalStaff();
				String link = newStaffAccountNotification.getLink();
				User user = clinicalStaff.getUser();
				user.setToken(UUID.randomUUID().toString());
				user.setTokenTime(new Timestamp(new Date().getTime()));
				String token = user.getToken();
				userRepository.saveWithoutCheck(user, false);
				
				String content = "Dear " + clinicalStaff.getFirstName() + " " + clinicalStaff.getLastName();
				content += getEmailMessage(user.getUsername(), null);
				content += "<br/>You can reset your password by clicking on this link ";
				content += "<br/>" + link + "?token=" + token;
				content += getEmailFooter();
				
				sendEmail(content, clinicalStaff.getEmailAddress());
			} catch (IOException | MessagingException e) {
				logger.error("Error in sending account creation email for newly clinical staff. " + e.getMessage(), e);
			}
		} else if(event instanceof NewStaffAccountNotificationOnSetupEvent){
			try {
					NewStaffAccountNotificationOnSetupEvent newStaffAccountNotificationOnSetup = (NewStaffAccountNotificationOnSetupEvent) event;
			        
					String content = getEmailMessage(newStaffAccountNotificationOnSetup.getUserName(), newStaffAccountNotificationOnSetup.getClearCasePassword());
			        content += getEmailFooter();
					
			        sendEmail(content, newStaffAccountNotificationOnSetup.getEmailAddress());
				} catch (IOException | MessagingException e) {
					logger.error("Error in sending email to added staff on system setup. " + e.getMessage(), e);
				}
		} else if (event instanceof NewStudyParticipantNotification) {
			try {
				NewStudyParticipantNotification newParticipantNotification = (NewStudyParticipantNotification) event;
				String content =  getNewParticipantEmailMessage(newParticipantNotification.getParticipantUsername(),
						newParticipantNotification.getStudySite());
				content += getEmailFooter();
				sendEmail(content, newParticipantNotification.getCraEmail());

			} catch (IOException | MessagingException e) {
				logger.error("Error in sending email to added staff on system setup. " + e.getMessage(), e);
			}
		}
	}

	private String getNewParticipantEmailMessage(String username, String studySite) {
		StringBuffer sb = new StringBuffer();
		sb.append("A new participant has been added to a study. See below information for details.");
		sb.append("<br><br/>");
		sb.append("Participant Username: ");
		sb.append(username);
		sb.append("<br><br/>");
		sb.append("Study Site: ");
		sb.append(studySite);

		return sb.toString();
	}
	
	private String getEmailMessage(String userName, String password){
		String content = "<br/><br>You have been successfully registered as a clinical staff on PRO-CTCAE system.<br> Below are your login details:<br> Username: " + userName;
		if(!StringUtils.isEmpty(password)){
			content += "<br> Password: " + password;
		}
		content += " <br/><br>";
		return content;
	}
	
	private String getEmailFooter(){
		return "<br/>-- <br>This email was generated by the PRO-CTCAE system. Please do not reply to it.<br>";
	}
	
	private void sendEmail(String content, String emailAddress) throws IOException, MessagingException {
		JavaMailSender javaMailSender = new JavaMailSender();
		MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject("PRO-CTCAE Registration");
        message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailAddress);
        helper.setText(content, javaMailSender.isHtml());
        javaMailSender.send(message);
	}
	
	@Required
	public void setUserRepository(UserRepository userRepository){
		this.userRepository = userRepository;
	}
}
