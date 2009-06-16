package gov.nih.nci.ctcae.web.setup;

import gov.nih.nci.ctcae.core.SetupStatus;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vinay Kumar
 * @since Mar 18, 2009
 */
public class SetupController extends CtcAeSimpleFormController {
    private UserRepository userRepository;
    private SetupStatus setupStatus;

    protected SetupController() {
        super();
        setFormView("setup/setup");
        setCommandClass(SetupCommand.class);
        setSuccessView("setup/confirmSetup");

    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        if (!setupStatus.isSetupNeeded()) {
            throw new CtcAeSystemException("can not run setup again because admin user already exists ");
        }
        SetupCommand setupCommand = (SetupCommand) command;
        userRepository.save(setupCommand.getUser());
        setupStatus.recheck();

    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBindAndValidate(request, command, errors);
        SetupCommand setupCommand = (SetupCommand) command;
        User user = setupCommand.getUser();
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            errors.rejectValue("user.confirmPassword", "clinicalStaff.user.confirm_password", "clinicalStaff.user.confirm_password");
        }


    }

    @Required
    public void setUserRepository(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Required
    public void setSetupStatus(SetupStatus setupStatus) {
        this.setupStatus = setupStatus;
    }
}
