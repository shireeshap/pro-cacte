package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.domain.UserRole;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.ClinicalStaffRepository;
import gov.nih.nci.ctcae.core.validation.annotation.UserNameAndPasswordValidator;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffCommand;
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

    protected SetupController() {
        super();
        setFormView("setup/setup");
        setCommandClass(ClinicalStaffCommand.class);
        setSuccessView("setup/confirmSetup");

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
        setupCommand.sendEmailWithUsernamePasswordDetails();
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
        if (!userNameAndPasswordValidator.validate(user)) {
            errors.rejectValue("clinicalStaff.user.username", userNameAndPasswordValidator.message(), userNameAndPasswordValidator.message());
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
}
