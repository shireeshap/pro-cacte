package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;

public class PasswordPolicyConfigurationController extends SimpleFormController {
     private PasswordPolicyService passwordPolicyService;

    public PasswordPolicyConfigurationController() {
        setFormView("password_policy_configure");
        setBindOnNewForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return passwordPolicyService.getPasswordPolicy();
    }

    @Override
    protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getFormView(), errors.getModel());
        passwordPolicyService.setPasswordPolicy((PasswordPolicy) command);
        return modelAndView.addObject("updated", true);
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }
}
