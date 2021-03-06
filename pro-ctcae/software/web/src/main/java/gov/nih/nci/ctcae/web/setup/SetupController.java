package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.security.passwordpolicy.validators.PasswordCreationPolicyException;
import gov.nih.nci.ctcae.core.validation.ValidationError;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand;
import gov.nih.nci.ctcae.web.clinicalStaff.notifications.ClinicalStaffNotificationPublisher;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupController extends CtcAeSimpleFormController {
    private ClinicalStaffRepository clinicalStaffRepository;
    private UserRepository userRepository;
    private SetupStatus setupStatus;
    private UserNameAndPasswordValidator userNameAndPasswordValidator;
    private ClinicalStaffNotificationPublisher proctcaeEventPublisher;
    
    /*Used on the jsp to determine of the setup has already run. */
    public static final String SETUP_NEEDED = "setupNeeded";

    protected SetupController() {
        super();
        setFormView("setup/setup");
        setCommandClass(ClinicalStaffCommand.class);
        setSuccessView("setup/confirmSetup");

    }

    @Override
    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
    	if (userRepository.getByRole(Role.ADMIN).isEmpty()) {
    		httpServletRequest.setAttribute(SETUP_NEEDED, true);
    	} else {
    		httpServletRequest.setAttribute(SETUP_NEEDED, false);
    		updateSetupStatus();
    	}
        ClinicalStaffCommand clinicalStaffCommand = new ClinicalStaffCommand();
        clinicalStaffCommand.getClinicalStaff().setUser(new User());
        return clinicalStaffCommand;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        if (!setupStatus.isSetupNeeded()) {
            throw new CtcAeSystemException("can not run setup again because admin user already exists ");
        }
        ClinicalStaffCommand setupCommand = (ClinicalStaffCommand) command;
        setupCommand.getClinicalStaff().getOrganizationClinicalStaffs().clear();
        User user = saveAndLoadUserAsAdmin(setupCommand);
        setupCommand.getClinicalStaff().setUser(user);
        clinicalStaffRepository.save(setupCommand.getClinicalStaff());
        proctcaeEventPublisher.publishClinicalStaffNotificationOnSetup(user.getUsername(), setupCommand.getClearCasePassword(), setupCommand.getClinicalStaff().getEmailAddress());
        updateSetupStatus();
    }
    
    private void updateSetupStatus(){
    	setupStatus.assertAdminIsPresent();
        setupStatus.recheck();
    }

    private User saveAndLoadUserAsAdmin(ClinicalStaffCommand clinicalStaffCommand) {
        User user = clinicalStaffCommand.getClinicalStaff().getUser();
        String clearCasePassword = user.getPassword();
        clinicalStaffCommand.setClearCasePassword(clearCasePassword);
        UserRole userRole = new UserRole();
        userRole.setRole(Role.ADMIN);
        user.addUserRole(userRole);
        user = userRepository.save(user);
        user = userRepository.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, clearCasePassword, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return user;
    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);
        ClinicalStaffCommand setupCommand = (ClinicalStaffCommand) command;
        User user = setupCommand.getClinicalStaff().getUser();
        try {
            boolean validUser = userNameAndPasswordValidator.validate(user);
            if (!validUser) {
                if(userNameAndPasswordValidator.message().contains("Username")){
                    errors.rejectValue("clinicalStaff.user.username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
                }
                else if(userNameAndPasswordValidator.message().contains("Password")){
                    errors.rejectValue("clinicalStaff.user.password", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());     
                }

            }
        } catch (PasswordCreationPolicyException ex) {
            for (ValidationError ve : ex.getErrors().getErrors()) {
                errors.rejectValue("clinicalStaff.user.password", ve.getMessage(), ve.getMessage());
            }
        }
    }

    @Required
    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }

    @Required
    public void setSetupStatus(SetupStatus setupStatus) {
        this.setupStatus = setupStatus;
    }

    @Required
    public void setUserNameAndPasswordValidator(UserNameAndPasswordValidator userNameAndPasswordValidator) {
        this.userNameAndPasswordValidator = userNameAndPasswordValidator;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Required
    public void setProctcaeEventPublisher(ClinicalStaffNotificationPublisher proctcaeEventPublisher){
    	this.proctcaeEventPublisher = proctcaeEventPublisher;
    }
}
