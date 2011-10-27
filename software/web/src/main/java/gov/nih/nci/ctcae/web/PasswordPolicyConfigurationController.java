package gov.nih.nci.ctcae.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.ctcae.core.domain.security.passwordpolicy.PasswordPolicy;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.security.passwordpolicy.PasswordPolicyService;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.cabig.ctms.domain.CodedEnumHelper;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class PasswordPolicyConfigurationController extends SimpleFormController {
    private PasswordPolicyService passwordPolicyService;
    private GenericRepository genericRepository;

    public PasswordPolicyConfigurationController() {
        setFormView("password_policy_configure");
        setBindOnNewForm(true);
    }

    @Override
    protected Map referenceData(HttpServletRequest httpServletRequest) throws Exception {
        Map map = new HashMap();
        map.put("roles", Role.values());
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String roleName = request.getParameter("role");
        Role role = Role.getByDisplayName(roleName);
        return passwordPolicyService.getPasswordPolicy(role);
    }

    @Override
    protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getFormView(), errors.getModel());
        PasswordPolicy passwordPolicy = (PasswordPolicy) command;
        genericRepository.save(passwordPolicy);
        modelAndView.addObject("updated", true);
        modelAndView.addObject("roles", Role.values());
        return modelAndView;
    }

    @Required
    public void setPasswordPolicyService(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}
