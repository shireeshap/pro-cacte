package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.mail.MessagingException;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.springframework.mail.javamail.MimeMessageHelper;

//
/**
 * The Class ClinicalStaffCommand.
 *
 * @author Mehul Gulati
 *         Date: Oct 30, 2008
 */
public class ClinicalStaffCommand {

    /**
     * The clinical staff.
     */
    private ClinicalStaff clinicalStaff;

    private Boolean cca = false;
    private Boolean admin = false;
    private List<Integer> indexesToRemove = new ArrayList<Integer>();
    private Boolean email = false;
    private Boolean userAccount = false;
    private String clearCasePassword;

    public Boolean isEmail() {
        return email;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean isUserAccount() {
        return userAccount;
    }

    public Boolean getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(Boolean userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * Instantiates a new clinical staff command.
     */
    public ClinicalStaffCommand() {
        super();
        clinicalStaff = new ClinicalStaff();
        OrganizationClinicalStaff organizationClinicalStaff = new OrganizationClinicalStaff();
        clinicalStaff.addOrganizationClinicalStaff(organizationClinicalStaff);
        clinicalStaff.setUser(new User());
    }

    /**
     * Gets the clinical staff.
     *
     * @return the clinical staff
     */
    public ClinicalStaff getClinicalStaff() {
        return clinicalStaff;
    }

    /**
     * Sets the clinical staff.
     *
     * @param clinicalStaff the new clinical staff
     */
    public void setClinicalStaff(ClinicalStaff clinicalStaff) {
        this.clinicalStaff = clinicalStaff;
    }

    public void apply() {
        if (getUserAccount()) {
            clearCasePassword = getClinicalStaff().getUser().getPassword();
            if (getCca()) {
                UserRole userRole = new UserRole();
                userRole.setRole(Role.CCA);
                getClinicalStaff().getUser().addUserRole(userRole);
            }
        } else {
            getClinicalStaff().setUser(null);
        }
        indexesToRemove.clear();
    }

    public Boolean getCca() {
        return cca;
    }

    public void setCca(Boolean cca) {
        this.cca = cca;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<Integer> getIndexesToRemove() {
        return indexesToRemove;
    }

    public void sendEmailWithUsernamePasswordDetails() {
        try {
            if (getEmail()) {
                String content = "You have been successfully registered as a clinical staff on PRO-CTCAE system.<br> Below are your login details:<br> Username: " + clinicalStaff.getUser().getUsername() + "<br> Password: " + clearCasePassword;
                JavaMailSender javaMailSender = new JavaMailSender();
                MimeMessage message = javaMailSender.createMimeMessage();
                message.setSubject("PRO-CTCAE Registration");
                message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(clinicalStaff.getEmailAddress());
                helper.setText(content, javaMailSender.isHtml());
                javaMailSender.send(message);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setClearCasePassword(String clearCasePassword) {
        this.clearCasePassword = clearCasePassword;
    }
}
