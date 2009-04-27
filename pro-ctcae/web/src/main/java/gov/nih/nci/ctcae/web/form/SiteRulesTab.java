package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.repository.ClinicalStaffRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.rules.ProCtcAERulesService;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @crated Nov 3, 2008
 */
public class SiteRulesTab extends SecuredTab<CreateFormCommand> {

    private ProCtcAERulesService proCtcAERulesService;
    private ClinicalStaffRepository clinicalStaffRepository;

    /**
     * Instantiates a new calendar template tab.
     */
    public SiteRulesTab() {
        super("form.tab.site_rules", "form.tab.site_rules", "form/site_rules");
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
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserId(loggedInUser.getId());
        ClinicalStaff clinicalStaff = (ClinicalStaff) clinicalStaffRepository.findSingle(clinicalStaffQuery);

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
        command.initializeRulesForSite(proCtcAERulesService);
    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        map.put("questionTypes", ListValues.getQuestionTypes(command.getCrf()));
        map.put("comparisonOptions", ListValues.getComparisonOptions());
        map.put("comparisonValues", ListValues.getComparisonValues(command.getCrf()));
        map.put("notifications", ListValues.getNotificationOptions());
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
                command.setReadonlyview("false");
            } else {
                command.processRulesForSite(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.reject(e.getMessage());
        }
        super.postProcess(request, command, errors);
    }

    public void setProCtcAERulesService(ProCtcAERulesService proCtcAERulesService) {
        this.proCtcAERulesService = proCtcAERulesService;
    }

    public void setClinicalStaffRepository(ClinicalStaffRepository clinicalStaffRepository) {
        this.clinicalStaffRepository = clinicalStaffRepository;
    }
}