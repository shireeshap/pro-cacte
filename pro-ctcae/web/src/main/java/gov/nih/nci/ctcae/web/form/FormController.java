package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//
/**
 * The Class FormController.
 *
 * @author Vinay Kumar
 * @since Dec 8, 2008
 */
public abstract class FormController extends CtcAeSecuredTabbedFlowController<CreateFormCommand> {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * /**
     * The Constant FORM_DETAILS_PAGE_NUMBER.
     */
    protected static final Integer FORM_DETAILS_PAGE_NUMBER = 1;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getInitialPage(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected int getInitialPage(HttpServletRequest request) {
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            return FORM_DETAILS_PAGE_NUMBER;
        } else if (!StringUtils.isBlank(request.getParameter("crfId"))) {
            return FORM_DETAILS_PAGE_NUMBER;
        }
        return super.getInitialPage(request);


    }

    /**
     * Instantiates a new form controller.
     */
    public FormController() {
        super();
        setCommandClass(CreateFormCommand.class);
        Flow<CreateFormCommand> flow = new Flow<CreateFormCommand>("Build Form");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<CreateFormCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#getFormSessionAttributeName()
     */
    protected String getFormSessionAttributeName() {
        return FormController.class.getName() + ".FORM." + getCommandName();
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getPageSessionAttributeName()
     */
    @Override
    protected String getPageSessionAttributeName() {
        return FormController.class.getName() + ".PAGE." + getCommandName();


    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = new CreateFormCommand();

        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            command.getCrf().setStudy(studyRepository.findById(Integer.parseInt(request.getParameter("studyId"))));
        } else if (!StringUtils.isBlank(request.getParameter("crfId"))) {
            CRF crf = crfRepository.findById(ServletRequestUtils.getRequiredIntParameter(request, "crfId"));
            command.setCrf(crf);
        }
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        return command;


    }

    /**
     * Layout tabs.
     *
     * @param flow the flow
     */
    protected void layoutTabs(Flow<CreateFormCommand> flow) {
        flow.addTab(new SelectStudyForFormTab());
        flow.addTab(new FormDetailsTab());
        flow.addTab(new CalendarTemplateTab());
        flow.addTab(new FormRulesTab());

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CreateFormCommand createFormCommand = (CreateFormCommand) command;
        createFormCommand.processRulesForForm(request);
        save(createFormCommand);

        Map model = new HashMap();
        model.put("crf", createFormCommand.getCrf());
        ModelAndView modelAndView = new ModelAndView("form/confirmForm", model);
        return modelAndView;


    }


    /**
     * Save.
     *
     * @param createFormCommand the create form command
     */
    @Override
    protected void save(final CreateFormCommand createFormCommand) {
        CRF crf = createFormCommand.getCrf();
        if (crf != null) {
            createFormCommand.setCrf(crf);
            CRF savedCrf = crfRepository.save(crf);
            createFormCommand.setCrf(savedCrf);
        }
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.form.CtcAeTabbedFlowController#validate()
     */
    @Override
    protected boolean validate() {
        return false;


    }

    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }


    @Override
    protected boolean shouldSave(HttpServletRequest request, CreateFormCommand command, Tab tab) {

        if (tab instanceof FormDetailsTab) {
            return true;
        }
        if (tab instanceof CalendarTemplateTab) {
            return true;
        }
        return false;
    }
}
