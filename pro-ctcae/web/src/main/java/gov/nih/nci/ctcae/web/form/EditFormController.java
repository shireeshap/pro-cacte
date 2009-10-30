package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.participant.ParticipantCommand;
import gov.nih.nci.ctcae.web.participant.ParticipantReviewTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

//
/**
 * The Class EditFormController.
 *
 * @author Vinay Kumar
 * @since Oct 17, 2008
 */
public class EditFormController extends FormController {

    @Override
    protected void layoutTabs(Flow<CreateFormCommand> flow) {
        flow.addTab(new FormDetailsTab());
        flow.addTab(new CalendarTemplateTab());
        flow.addTab(new FormRulesTab());
        flow.addTab(new SiteRulesTab());

    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cabig.ctms.web.tabs.AbstractTabbedFlowFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
    */
    @Override
    protected Map referenceData(HttpServletRequest request, Object oCommand, Errors errors, int page) throws Exception {

        return super.referenceData(request, oCommand, errors, page);


    }


    @Override
    protected boolean shouldSave(final HttpServletRequest request, final CreateFormCommand command, final Tab tab) {
        if ("form/site_rules".equals(tab.getViewName())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected int getInitialPage(HttpServletRequest request) {
        return 0;
    }

    @Override
    public Flow<CreateFormCommand> getFlow(CreateFormCommand command) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) auth.getPrincipal();
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.PI);
        roles.add(Role.LEAD_CRA);
        StudyOrganization myOrgStudyLevel = command.getOrganizationForUser(loggedInUser, roles);

        roles = new ArrayList<Role>();
        roles.add(Role.SITE_CRA);
        roles.add(Role.SITE_PI);
        StudyOrganization myOrgSiteLevel = command.getOrganizationForUser(loggedInUser, roles);

        Flow flow = new Flow("Edit Form");
        if (command.getCrf().getStatus().equals(CrfStatus.DRAFT)) {
            flow.addTab(new FormDetailsTab());
            flow.addTab(new CalendarTemplateTab());
        }

        if (myOrgStudyLevel != null) {
            FormRulesTab formRulesTab = new FormRulesTab();
            formRulesTab.setCrfRepository(crfRepository);
            formRulesTab.setProCtcAERulesService(proCtcAERulesService);
            flow.addTab(formRulesTab);
        } else {
            if (myOrgSiteLevel != null) {
                SiteRulesTab siteRulesTab = new SiteRulesTab();
                siteRulesTab.setProCtcAERulesService(proCtcAERulesService);
                flow.addTab(siteRulesTab);

            }
        }
        return getSecuredFlow(flow);
    }


}