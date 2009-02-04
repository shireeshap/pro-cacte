package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
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
 * @crated Dec 8, 2008
 */
public abstract class FormController<C extends CreateFormCommand> extends CtcAeTabbedFlowController<CreateFormCommand> {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * The unique title for crf validator.
     */
    private UniqueTitleForCrfValidator uniqueTitleForCrfValidator;

    /**
     * The not empty validator.
     */
    private NotEmptyValidator notEmptyValidator;

    /**
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
    private void layoutTabs(Flow<CreateFormCommand> flow) {
        flow.addTab(new SelectStudyForFormTab());
        flow.addTab(new FormDetailsTab());
        // flow.addTab(new CalendarTemplateTab());

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CreateFormCommand createFormCommand = (CreateFormCommand) command;
        createFormCommand.updateCrfItems(proCtcQuestionRepository);
        CRF crf = createFormCommand.getCrf();

        ModelAndView defaultModelAndView = showPage(request, errors, FORM_DETAILS_PAGE_NUMBER);
        if (!StringUtils.isBlank(request.getParameter("showForm"))) {
            if (shouldSave(request, command)) {
                save(createFormCommand);
            }
            return defaultModelAndView;
        } else {
            if (!notEmptyValidator.validate(crf.getTitle())) {
                errors.rejectValue("crf.title", "form.missing_title", "form.missing_title");
            } else if (!uniqueTitleForCrfValidator.validate(crf, crf.getTitle())) {
                errors.rejectValue("crf.title", "form.unique_title", "form.unique_title");
            }

            if (errors.hasErrors()) {
                return defaultModelAndView;
            }

            save(createFormCommand);

            Map model = new HashMap();
            model.put("crf", createFormCommand.getCrf());
            ModelAndView modelAndView = new ModelAndView("form/confirmForm", model);
            return modelAndView;
        }

    }

    protected boolean shouldSave(HttpServletRequest request, Object command) {
        return false;
    }

    /**
     * Save.
     *
     * @param createFormCommand the create form command
     */
    protected void save(final CreateFormCommand createFormCommand) {
        CRF crf = createFormCommand.getCrf();
        createFormCommand.setCrf(crf);
        CRF savedCrf = crfRepository.save(crf);
        createFormCommand.setCrf(savedCrf);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.form.CtcAeTabbedFlowController#validate()
     */
    @Override
    protected boolean validate() {
        return false;


    }

    /**
     * Sets the not empty validator.
     *
     * @param notEmptyValidator the new not empty validator
     */
    @Required
    public void setNotEmptyValidator(final NotEmptyValidator notEmptyValidator) {
        this.notEmptyValidator = notEmptyValidator;
    }

    /**
     * Sets the unique title for crf validator.
     *
     * @param uniqueTitleForCrfValidator the new unique title for crf validator
     */
    @Required
    public void setUniqueTitleForCrfValidator(final UniqueTitleForCrfValidator uniqueTitleForCrfValidator) {
        this.uniqueTitleForCrfValidator = uniqueTitleForCrfValidator;
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
}
