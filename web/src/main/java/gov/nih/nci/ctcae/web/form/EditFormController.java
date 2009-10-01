package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.web.participant.ParticipantCommand;
import gov.nih.nci.ctcae.web.participant.ParticipantReviewTab;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        if (command.getCrf().getStatus().equals(CrfStatus.RELEASED)) {
            Flow flow = new Flow("Edit Rules");
            flow.addTab(new FormRulesTab());
            flow.addTab(new SiteRulesTab());
            return getSecuredFlow(flow);
        } else {
            return super.getFlow(command);
        }
    }


}