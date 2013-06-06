package gov.nih.nci.ctcae.web.clinicalStaff;

import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.rules.JavaMailSender;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.opensymphony.module.sitemesh.taglib.decorator.UseHTMLPageTEI;

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
    private boolean validUser = true;
    private String username = "";
    private String clearCasePassword = "";

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
            setAdmin(clinicalStaff.getUser().isAdmin());
            if (clinicalStaff.getOrganizationsWithCCARole().size() > 0) {
                setCca(true);
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
    
    private void removeRole(User user, Role role) {
    	boolean isRolePresent = false;
    	UserRole userRoleToRemove = null;
        for (UserRole userRole : user.getUserRoles()) {
            if (userRole.getRole().equals(role)) {
                isRolePresent = true;
                userRoleToRemove = userRole;
            }
        }
        if(isRolePresent){
        	user.getUserRoles().remove(userRoleToRemove);
        }
    }

    public void apply() {
        if (getUserAccount()) {
        	
        	//If CCA checkBox is checked on UI then add Role.CCA to user's userRoles if this role not already present
            if (getCca()) {
                if (!userHasRole(getClinicalStaff().getUser(), Role.CCA)) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(Role.CCA);
                    getClinicalStaff().getUser().addUserRole(userRole);
                }
            } // If CCA checkBox is unchecked but Role.CCA was present in userRoles then remove this role from user's userRoles
            else {
            	removeRole(getClinicalStaff().getUser(), Role.CCA);
            }
            
            //If ADMIN checkBox is checked on UI then add Role.ADMIN to user's userRoles if this role not already present
            if (getAdmin()) {
                if (!userHasRole(getClinicalStaff().getUser(), Role.ADMIN)) {
                    UserRole userRole = new UserRole();
                    userRole.setRole(Role.ADMIN);
                    getClinicalStaff().getUser().addUserRole(userRole);
                }
            } // If ADMIN checkBox is unchecked but Role.ADMIN was present in userRoles then remove this role from user's userRoles
            else {
            	removeRole(getClinicalStaff().getUser(), Role.ADMIN);
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

    public void sendEmailWithUsernamePasswordDetails(UserRepository userRepository, HttpServletRequest request) {
        try {
            User user = clinicalStaff.getUser();
            user.setToken(UUID.randomUUID().toString());
            user.setTokenTime(new Timestamp(new Date().getTime()));
            String token = user.getToken();
            userRepository.saveWithoutCheck(clinicalStaff.getUser(), false);
            String content = "Dear " + clinicalStaff.getFirstName() + " " + clinicalStaff.getLastName() + ", ";
            content += "<br>You have been successfully registered as a clinical staff on PRO-CTCAE system.<br> Below are your login details:<br> Username: " + clinicalStaff.getUser().getUsername();
            content += "<br>You can reset your password by clicking on this link ";
            content += "<br>" + StringUtils.replace(request.getRequestURL().toString(), "pages/admin/createClinicalStaff", "public/resetPassword") + "?token=" + token;
            JavaMailSender javaMailSender = new JavaMailSender();
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setSubject("PRO-CTCAE Registration");
            message.setFrom(new InternetAddress(javaMailSender.getFromAddress()));
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(clinicalStaff.getEmailAddress());
            helper.setText(content, javaMailSender.isHtml());
            javaMailSender.send(message);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setClearCasePassword(String clearCasePassword) {
        this.clearCasePassword = clearCasePassword;
    }

    public String getClearCasePassword() {
        return clearCasePassword;
    }
}

