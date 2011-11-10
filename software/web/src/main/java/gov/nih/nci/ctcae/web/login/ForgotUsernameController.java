package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author mehul gulati
 *         Date: Jun 2, 2010
 */

public class ForgotUsernameController extends AbstractFormController {

    GenericRepository genericRepository;
    protected Properties proCtcAEProperties;
    private DelegatingMessageSource messageSource;

    public void setMessageSource(DelegatingMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ForgotUsernameController() {
        setCommandClass(ClinicalStaff.class);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mv=new ModelAndView("forgotUsername");
        String mode = proCtcAEProperties.getProperty("mode.nonidentifying");
        mv.addObject("mode", mode);
        return mv;
    }

    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {
        String email = request.getParameter("email");
        ModelAndView mv;
        if(email.length()>0){
            ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
            clinicalStaffQuery.filterByEmail(email);
            List<ClinicalStaff> clinicalStaffs = genericRepository.find(clinicalStaffQuery);
            if (clinicalStaffs == null || clinicalStaffs.size() == 0){
                ParticipantQuery participantQuery = new ParticipantQuery();
                participantQuery.filterByEmail(email);
                List<Participant> participants = genericRepository.find(participantQuery);
                if(participants == null || participants.size() ==0){
                    mv = new ModelAndView("forgotUsername");
                    String mode = proCtcAEProperties.getProperty("mode.nonidentifying");
                    mv.addObject("Message", "user.forgotusername.usernotfound");
                    mv.addObject("mode", mode);
                    return mv;
                }
                else{
                    mv = new ModelAndView("forgotUsername");
                    mv.addObject("showConfirmation", true);
                    sendUsernameEmailToParticipant(participants, request);
                    return mv;
                }
            }
            else{
                mv = new ModelAndView("forgotUsername");
                mv.addObject("showConfirmation", true);
                sendUsernameEmail(clinicalStaffs, request);
                return mv;
            }
        }
        return null;
    }

    public void sendUsernameEmail(List<ClinicalStaff> clinicalStaffs, HttpServletRequest request) {
        try {

            String content = "";
            if (clinicalStaffs.size() == 1) {
                User user = clinicalStaffs.get(0).getUser();
                content += "Dear " + clinicalStaffs.get(0).getFirstName() + " " + clinicalStaffs.get(0).getLastName() + ", ";
                content += "\nFollowing is your username: " + user.getUsername();
            } else {
                content += "We have found multiple users with the email address that you provided. Below is the list of all the usernames : ";
                for (ClinicalStaff clinicalStaff : clinicalStaffs) {
                    content += "\n\n" + clinicalStaff.getUser().getUsername();
                }
            }
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("PRO-CTCAE Account Username");
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(clinicalStaffs.get(0).getEmailAddress());
            helper.setText(content, false);
            javaMailSender.send(message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // sending user names to participants
    public void sendUsernameEmailToParticipant(List<Participant> participants, HttpServletRequest request) {
        try {
        	Locale locale = LocaleContextHolder.getLocale();
        	String salutation = messageSource.getMessage("username.email.salutation", null, locale);
        	String msg1 = messageSource.getMessage("username.email.message.1", null, locale);
        	String msg2 = messageSource.getMessage("username.email.message.2", null, locale);
        	String subject = messageSource.getMessage("username.email.subject", null, locale);

            String content = "";
            if (participants.size() == 1) {
                User user = participants.get(0).getUser();
                content += salutation + participants.get(0).getFirstName() + " " + participants.get(0).getLastName() + ", ";
                content += "\n" + msg1 + user.getUsername();
            } else {
                content += msg2;
                for (Participant participant : participants) {
                    content += "\n\n" + participant.getUser().getUsername();
                }
            }
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(participants.get(0).getEmailAddress());
            helper.setText(content, false);
            javaMailSender.send(message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setProCtcAEProperties(Properties proCtcAEProperties) {
        this.proCtcAEProperties = proCtcAEProperties;
    }
}
