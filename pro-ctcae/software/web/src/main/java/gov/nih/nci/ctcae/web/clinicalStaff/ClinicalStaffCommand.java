package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

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
    private boolean validUser = true;
    private String username = "";
    private String password = "";
    private String confirmPassword = "";

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
        if (clinicalStaff.getUser() != null) {
            username = clinicalStaff.getUser().getUsername();
            password = clinicalStaff.getUser().getPassword();
            confirmPassword = clinicalStaff.getUser().getPassword();
            if (clinicalStaff.getOrganizationsWithCCARole().size() > 0) {
                setCca(true);
                setAdmin(clinicalStaff.getUser().isAdmin());
            }
        }

    }

    private boolean userHasRole(User user, Role role) {
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public void apply() {
        if (getUserAccount()) {
            clearCasePassword = getClinicalStaff().getUser().getPassword();
            if (getCca()) {
                if (!userHasRole(getClinicalStaff().getUser(), Role.CCA)) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(Role.CCA);
                    getClinicalStaff().getUser().addUserRole(userRole);
                }
            }
            if (getAdmin()) {
                if (!userHasRole(getClinicalStaff().getUser(), Role.ADMIN)) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(Role.ADMIN);
                    getClinicalStaff().getUser().addUserRole(userRole);
                }
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

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
