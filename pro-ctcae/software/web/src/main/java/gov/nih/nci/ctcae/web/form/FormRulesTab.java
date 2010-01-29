package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.Privilege;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.security.SecuredTab;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Required;

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

    private CRFRepository crfRepository;
    private ProCtcTermRepository proCtcTermRepository;
    private GenericRepository genericRepository;

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
        if (command.getCrf().getTitle() == null) {
            command.getCrf().setTitle(command.getUniqueTitleForCrf());
        }
        if (!command.getCrf().isPersisted()) {
            crfRepository.save(command.getCrf());
        }
        command.setReadonlyview("false");
    }

    public Map<String, Object> referenceData(CreateFormCommand command) {
        Map<String, Object> map = super.referenceData(command);
        map.put("crfSymptoms", ListValues.getSymptomsForCRF(command.getCrf()));
        map.put("notifications", ListValues.getNotificationOptions());
        map.put("notificationRules", command.getFormRules(crfRepository));
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
            command.processRulesForForm(request, genericRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.postProcess(request, command, errors);
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}