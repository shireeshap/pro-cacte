package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DelegatingMessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

/**
 * @author Harsh Agarwal
 * @since Nov 20, 2009
 */
public class ForgotPasswordController extends AbstractFormController {

    UserRepository userRepository;
    private DelegatingMessageSource messageSource;
    protected static final Log logger = LogFactory.getLog(PasswordExpiredController.class);

	public void setMessageSource(DelegatingMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ForgotPasswordController() {
        setCommandClass(ClinicalStaff.class);
    }

    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        return new ModelAndView("forgotPassword");
    }

    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
        String userName = request.getParameter("username");
        User user;
        ModelAndView mv;
        UserQuery userQuery = new UserQuery();
        userQuery.filterByUserName(userName.trim());
        user = userRepository.findSingle(userQuery);

        if (user == null) {
            mv = new ModelAndView("forgotPassword");
            mv.addObject("Message", "user.forgotpassword.usernotfound");
            return mv;
        }
        
        mv = new ModelAndView("passwordReset");
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
              Participant participant = userRepository.findParticipantForUser(user);
              String email=participant.getEmailAddress();
              if(email==null){
                  mv.addObject("Message", "user.forgotpassword.participant");
              }
              else{
                  resetPasswordAndSendEmailToParticipant(participant, request);
	              mv.addObject("Message", "user.forgotpassword.participantWithEmail");
              }
              return mv;
            }
        }

        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        if(clinicalStaff !=null){
        	if(clinicalStaff.getEmailAddress() != null){
        		resetPasswordAndSendEmail(clinicalStaff, request);
        		mv.addObject("Message", "user.forgotpassword.clinicalstaff");
        	}else{
        		mv.addObject("Message", "user.forgotpassword.participant");
        	}
        } else {
        	mv.addObject("Message", "user.forgotusername.inactiveUser");
        }
        return mv;
    }

    public void resetPasswordAndSendEmail(ClinicalStaff clinicalStaff, HttpServletRequest request) {
        try {
            User user = clinicalStaff.getUser();
            user.setToken(UUID.randomUUID().toString());
            user.setTokenTime(new Timestamp(new Date().getTime()));
            String token = user.getToken();
            userRepository.saveWithoutCheck(clinicalStaff.getUser(), false);
            String content = "Dear " + clinicalStaff.getFirstName() + " " + clinicalStaff.getLastName() + ", ";
            content += "\nYou must reset your password by clicking on this link ";
            content += "\n\n" + StringUtils.replace(request.getRequestURL().toString(),"password","resetPassword") +"?token=" + token;
            String lang = LocaleContextHolder.getLocale().getLanguage();
            String specifyLang = "&lang=en";
            if(!StringUtils.isBlank(lang)){
            	specifyLang = "&lang="+lang;
            }
            content += specifyLang;
            
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("PRO-CTCAE Account Password");
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(clinicalStaff.getEmailAddress());
            helper.setText(content, false);
            javaMailSender.send(message);
            logger.error("ForgotPasswordController: Sent forgot password email to ClinicalStaff " +
            		"with email address " + clinicalStaff.getEmailAddress());
        } catch (Exception ex) {
        	logger.error("ForgotPasswordController: Exception in sending out forgot password email to ClinicalStaff " +
            		"with email address " + clinicalStaff.getEmailAddress());
            ex.printStackTrace();
        }
    }

    // sending email to participants
    public void resetPasswordAndSendEmailToParticipant(Participant participant, HttpServletRequest request) {
    	Locale locale = LocaleContextHolder.getLocale();
    	String salutation = messageSource.getMessage("username.email.salutation", null, locale);
    	String msg1 = messageSource.getMessage("password.email.message.1", null, locale);
    	String subject = messageSource.getMessage("password.email.subject", null, locale);
    	
    	try {
            User user = participant.getUser();
            user.setToken(UUID.randomUUID().toString());
            user.setTokenTime(new Timestamp(new Date().getTime()));
            String token = user.getToken();
            userRepository.saveWithoutCheck(participant.getUser(), false);
            
            String content = salutation + participant.getFirstName() + " " + participant.getLastName() + ", ";
            content += "\n" + msg1;
            content += "\n\n" + StringUtils.replace(request.getRequestURL().toString(),"password","resetPassword") +"?token=" + token;
            String lang = LocaleContextHolder.getLocale().getLanguage();
            String specifyLang = "&lang=en";
            if(!StringUtils.isBlank(lang)){
            	specifyLang = "&lang="+lang;
            }
            content += specifyLang;
            
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject(subject);
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(participant.getEmailAddress());
            helper.setText(content, false);
            javaMailSender.send(message);
            logger.error("ForgotPasswordController: Sent forgot password email to participant " +
            		"with email address " + participant.getEmailAddress());
        } catch (Exception ex) {
            logger.error("ForgotPasswordController: Exception in sending out forgot password email to participant " +
            		"with email address " + participant.getEmailAddress());
            ex.printStackTrace();
        }
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}