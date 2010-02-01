package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.rules.ProCtcAEFactResolver;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import com.semanticbits.rules.brxml.RuleSet;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class SiteRulesTab extends SecuredTab<CreateFormCommand> {
    private GenericRepository genericRepository;

    /**
     * Instantiates a new calendar template tab.
     */
    public SiteRulesTab() {
        super("form.tab.site_rules", "form.tab.site_rules", "form/form_rules");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_SITE_RULES;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#onDisplay(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    @Override
    public void onDisplay(HttpServletRequest request, CreateFormCommand command) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) auth.getPrincipal();
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.SITE_CRA);
        roles.add(Role.SITE_PI);
        StudyOrganization myOrg = command.getOrganizationForUser(loggedInUser, roles);
        if (myOrg == null) {
            throw new CtcAeSystemException(String.format("Unable to locate study site for user - %s", loggedInUser.getUsername()));
        }
        command.setMyOrg(myOrg);
    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        map.put("notifications", ListValues.getNotificationOptions());
        map.put("notificationRules", command.getSiteRules(genericRepository));
        map.put("allQuestionTypes", Arrays.asList(ProCtcQuestionType.values()));
        map.put("isSite", "true");
        return map;
    }

    @Override
    public void onBind(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        super.onBind(request, command, errors);
    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        try {
            command.processRulesForSite(request, genericRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.postProcess(request, command, errors);
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}