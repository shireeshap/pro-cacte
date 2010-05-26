package gov.nih.nci.ctcae.web.login;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.query.UserQuery;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.apache.commons.lang.StringUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.Date;
import java.sql.Timestamp;

/**
 * @author Harsh Agarwal
 * @since Nov 20, 2009
 */
public class ForgotPasswordController extends AbstractFormController {

    UserRepository userRepository;

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
        userQuery.filterByUserName(userName);
        user = userRepository.findSingle(userQuery);

        if (user == null) {
            mv = new ModelAndView("forgotPassword");
            mv.addObject("Message", "user.forgotpassword.usernotfound");
            return mv;
        }

        mv = new ModelAndView("passwordReset");
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(Role.PARTICIPANT)) {
                mv.addObject("Message", "user.forgotpassword.participant");
                return mv;
            }
        }

        ClinicalStaff clinicalStaff = userRepository.findClinicalStaffForUser(user);
        resetPasswordAndSendEmail(clinicalStaff, request);
        mv.addObject("Message", "user.forgotpassword.clinicalstaff");
        return mv;
    }

    public void resetPasswordAndSendEmail(ClinicalStaff clinicalStaff, HttpServletRequest request) {
        try {
            User user = clinicalStaff.getUser();
            user.setAccountNonLocked(true);
            user.setNumberOfAttempts(0);
            user.setToken(UUID.randomUUID().toString());
            user.setTokenTime(new Timestamp(new Date().getTime()));
            String token = user.getToken();
            userRepository.saveWithoutCheck(clinicalStaff.getUser());
            String content = "Dear " + clinicalStaff.getFirstName() + " " + clinicalStaff.getLastName() + ", ";
            content += "\nYou can reset your password by clicking on this link ";
            content += "\n\n" + StringUtils.replace(request.getRequestURL().toString(),"password","resetPassword") +"?token=" + token;
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("PRO-CTCAE Account Password");
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(clinicalStaff.getEmailAddress());
            helper.setText(content, false);
            javaMailSender.send(message);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}