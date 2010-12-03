package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

/**
 * @author mehul gulati
 *         Date: Jun 2, 2010
 */

public class ForgotUsernameController extends AbstractFormController {

    GenericRepository genericRepository;
    protected Properties proCtcAEProperties;
    
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
        String lastName = request.getParameter("lastName");
        String participantIdentifier = request.getParameter("participantIdentifier");
        String participantEmail =request.getParameter("participantEmail");
        ModelAndView mv;
        if(lastName.length()>0 && email.length()>0){
            ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
            clinicalStaffQuery.filterByClinicalStaffLastName(lastName);
            clinicalStaffQuery.filterByEmail(email);
            List<ClinicalStaff> clinicalStaffs = genericRepository.find(clinicalStaffQuery);
            if (clinicalStaffs == null || clinicalStaffs.size() == 0) {
                mv = new ModelAndView("forgotUsername");
                mv.addObject("Message", "user.forgotusername.usernotfound");
                return mv;
            }
            mv = new ModelAndView("forgotUsername");
            mv.addObject("showConfirmation", true);
            sendUsernameEmail(clinicalStaffs, request);
            return mv;
        }
        else if(participantIdentifier.length()>0 && participantEmail.length()>0){
            ParticipantQuery participantQuery = new ParticipantQuery();
            participantQuery.filterByStudyParticipantIdentifier(participantIdentifier);
            participantQuery.filterByEmail(participantEmail);
            List<Participant> participants = genericRepository.find(participantQuery);
            if(participants == null || participants.size() ==0){
                mv = new ModelAndView("forgotUsername");
                mv.addObject("Message", "user.forgotusername.participantUsernotfound");
                return mv;
            }

            mv = new ModelAndView("forgotUsername");
            mv.addObject("showConfirmation", true);
            sendUsernameEmailToParticipant(participants, request);
            return mv;
        }
        return  null; //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendUsernameEmail(List<ClinicalStaff> clinicalStaffs, HttpServletRequest request) {
        try {

            String content = "";
            if (clinicalStaffs.size() == 1) {
                User user = clinicalStaffs.get(0).getUser();
                content += "Dear " + clinicalStaffs.get(0).getFirstName() + " " + clinicalStaffs.get(0).getLastName() + ", ";
                content += "\nFollowing is your username: " + user.getUsername();
            } else {
                content += "We have found multiple users with the last name and email address that you provided. Below is the list of all the usernames : ";
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

            String content = "";
            if (participants.size() == 1) {
                User user = participants.get(0).getUser();
                content += "Dear " + participants.get(0).getFirstName() + " " + participants.get(0).getLastName() + ", ";
                content += "\nFollowing is your username: " + user.getUsername();
            } else {
                content += "We have found multiple users with the participant study identifier and email address that you provided. Below is the list of all the usernames : ";
                for (Participant participant : participants) {
                    content += "\n\n" + participant.getUser().getUsername();
                }
            }
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("PRO-CTCAE Account Username");
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
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
