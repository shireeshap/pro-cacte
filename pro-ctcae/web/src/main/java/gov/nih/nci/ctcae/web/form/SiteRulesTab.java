package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.rules.ProCtcAEFactResolver;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.semanticbits.rules.brxml.RuleSet;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class SiteRulesTab extends SecuredTab<CreateFormCommand> {


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
        StudyOrganization myOrg = null;

        List<StudyOrganizationClinicalStaff> siteLevelUsers = new ArrayList<StudyOrganizationClinicalStaff>();
        siteLevelUsers.addAll(command.getCrf().getStudy().getStudySiteLevelStudyOrganizationClinicalStaffsByRole(Role.SITE_PI));
        siteLevelUsers.addAll(command.getCrf().getStudy().getStudySiteLevelStudyOrganizationClinicalStaffsByRole(Role.SITE_CRA));
        for (StudyOrganizationClinicalStaff studyOrganizationClinicalStaff : siteLevelUsers) {
            if (studyOrganizationClinicalStaff.getOrganizationClinicalStaff().getClinicalStaff().getUser().equals(loggedInUser)) {
                myOrg = studyOrganizationClinicalStaff.getStudyOrganization();
            }
        }

        if (myOrg == null) {
            throw new CtcAeSystemException(String.format("Unable to locate study site for user - %s", loggedInUser.getUsername()));
        }
        command.setMyOrg(myOrg);
        command.initializeRulesForSite();
    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        map.put("questionTypes", ListValues.getQuestionTypes(command.getCrf()));
        map.put("comparisonOptions", ListValues.getComparisonOptions());
        map.put("comparisonValues", ProCtcAEFactResolver.getComparisonValues(command.getCrf()));
        map.put("notifications", ListValues.getNotificationOptions());
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
            if ("true".equals(command.getReadonlyview())) {
                RuleSet ruleSet = ProCtcAERulesService.getRuleSetForCrfAndSite(command.getCrf(), command.getMyOrg(), true);
                command.setRuleSet(ruleSet);
                command.setReadonlyview("false");
            } else {
                command.processRulesForSite(request);
                command.setReadonlyview("true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.reject(e.getMessage());
        }
        super.postProcess(request, command, errors);
    }

}