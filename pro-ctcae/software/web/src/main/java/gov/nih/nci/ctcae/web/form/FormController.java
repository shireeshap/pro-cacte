package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.constants.ItemBank;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CRFPage;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.domain.CrfPageItem;
import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
    protected CRFRepository crfRepository;
    protected ProCtcTermRepository proCtcTermRepository;
    protected UniqueTitleForCrfValidator uniqueTitleForCrfValidator;
    protected NotEmptyValidator notEmptyValidator;
    protected GenericRepository genericRepository;
    protected UserRepository userRepository;
    public static String CRF_ID = "crfId"; 

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
    
    public static CreateFormCommand getCreateFormCommand(HttpServletRequest request) {
    	String crfId = request.getParameter(CRF_ID);
    	if(StringUtils.isNotEmpty(crfId)) {
    		return (CreateFormCommand) 
						request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command" + "." + Integer.parseInt(crfId)) ;
    	}
    	return (CreateFormCommand)
                 request.getSession().getAttribute(FormController.class.getName() + ".FORM." + "command");
         
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#getFormSessionAttributeName()
     */
    @Override
    protected String getFormSessionAttributeName() {
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    	String crfId = attr.getRequest().getParameter(CRF_ID);
    	if(StringUtils.isNotEmpty(crfId)) {
    		return FormController.class.getName() + ".FORM." + getCommandName() + "." + Integer.parseInt(crfId);
    	}
    	
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
        for (CRFPage crfPage : command.getCrf().getCrfPagesSortedByPageNumber()) {
            for (CrfPageItem crfPageItem : crfPage.getCrfPageItems()) {
           	 ProCtcTerm proCtcTerm = crfPageItem.getProCtcQuestion().getProCtcTerm();
           	 proCtcTerm.getCtcTerm().getCategoryTermSets().size();
            }
        }
        command.setSelectedItemBank(command.getCrf().isEq5d() ? command.getCrf().isEq5d5L() ? ItemBank.EQ5D5L.getCode() : ItemBank.EQ5D3L.getCode() : ItemBank.PROCTCAE.getCode());
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
        flow.addTab(new EmptyFormTab("form.tab.overview", "form.tab.overview", "form/confirmForm"));
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CreateFormCommand createFormCommand = (CreateFormCommand) command;
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


    @Required
    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    @Required
    public void setUniqueTitleForCrfValidator(UniqueTitleForCrfValidator uniqueTitleForCrfValidator) {
        this.uniqueTitleForCrfValidator = uniqueTitleForCrfValidator;
    }

    @Required
    public void setNotEmptyValidator(NotEmptyValidator notEmptyValidator) {
        this.notEmptyValidator = notEmptyValidator;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldSave(HttpServletRequest request, CreateFormCommand command, Tab tab) {

        if (tab instanceof FormDetailsTab) {
            if (StringUtils.isBlank(request.getParameter("crfPageNumberToRemove"))
                    && StringUtils.isBlank(request.getParameter("questionIdToRemove"))) {
                return true;
            }
        }
        if (tab instanceof CalendarTemplateTab) {
            return true;
        }
        return false;
    }
}
