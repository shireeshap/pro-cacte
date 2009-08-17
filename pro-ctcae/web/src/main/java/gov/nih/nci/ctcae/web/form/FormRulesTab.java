package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.rules.ProCtcAEFactResolver;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//
/**
 * The Class CalendarTemplateTab.
 *
 * @author Vinay Kumar
 * @since Nov 3, 2008
 */
public class FormRulesTab extends SecuredTab<CreateFormCommand> {

    /**
     * Instantiates a new calendar template tab.
     */
    public FormRulesTab() {
        super("form.tab.rules", "form.tab.rules", "form/form_rules");
    }

    public String getRequiredPrivilege() {
        return Privilege.PRIVILEGE_ADD_FORM_RULES;
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.cabig.ctms.web.tabs.Tab#onDisplay(javax.servlet.http.HttpServletRequest, java.lang.Object)
     */
    @Override
    public void onDisplay(HttpServletRequest request, CreateFormCommand command) {
        command.initializeRulesForForm();
        command.setReadonlyview("false");
    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        map.put("questionTypes", ListValues.getQuestionTypes(command.getCrf()));
        map.put("comparisonOptions", ListValues.getComparisonOptions());
        map.put("comparisonValues", ProCtcAEFactResolver.getComparisonValues(command.getCrf()));
        map.put("notifications", ListValues.getNotificationOptions());
        map.put("isSite", "false");
        return map;
    }

    @Override
    public void onBind(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        super.onBind(request, command, errors);
    }

    @Override
    public void postProcess(HttpServletRequest request, CreateFormCommand command, Errors errors) {
        try {
            command.processRulesForForm(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.postProcess(request, command, errors);
    }
}