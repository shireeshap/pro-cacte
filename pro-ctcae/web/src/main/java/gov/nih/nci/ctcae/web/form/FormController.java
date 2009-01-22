package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfCreationMode;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
import gov.nih.nci.ctcae.web.ControllersUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Dec 8, 2008
 */
public abstract class FormController<C extends CreateFormCommand> extends CtcAeTabbedFlowController<CreateFormCommand> {
    private CRFRepository crfRepository;
    private UniqueTitleForCrfValidator uniqueTitleForCrfValidator;
    private NotEmptyValidator notEmptyValidator;

    protected static final Integer FORM_DETAILS_PAGE_NUMBER = 1;

    @Override
    protected int getInitialPage(HttpServletRequest request) {
        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            return FORM_DETAILS_PAGE_NUMBER;
        }
        return super.getInitialPage(request);


    }

    public FormController() {
        setCommandClass(CreateFormCommand.class);
        Flow<CreateFormCommand> flow = new Flow<CreateFormCommand>("Build Form");
        layoutTabs(flow);
        setFlowFactory(new StaticFlowFactory<CreateFormCommand>(flow));
        setAllowDirtyBack(false);
        setAllowDirtyForward(false);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        CreateFormCommand command = (CreateFormCommand) ControllersUtils.getFormCommand(request, this);
        if (command == null) {
            command = new CreateFormCommand();

        }
////        else {
////            request.setAttribute("flashMessage", "You were already updating one form. Do you want to  resume it or discard it.");
////        }


        if (!StringUtils.isBlank(request.getParameter("studyId"))) {
            command.getCrf().setStudy(studyRepository.findById(Integer.parseInt(request.getParameter("studyId"))));
        }
        command.getCrf().setCrfCreationMode(CrfCreationMode.BASIC);
        return command;


    }

    protected void layoutTabs(Flow<CreateFormCommand> flow) {
        flow.addTab(new SelectStudyForFormTab());
        flow.addTab(new FormDetailsTab());

    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CreateFormCommand createFormCommand = (CreateFormCommand) command;
        createFormCommand.updateCrfItems(finderRepository);
        CRF crf = createFormCommand.getCrf();

        if (!StringUtils.isBlank(request.getParameter("switchToAdvance"))) {
            createFormCommand.getCrf().setCrfCreationMode(CrfCreationMode.ADVANCE);
            return showPage(request, errors, FORM_DETAILS_PAGE_NUMBER);
        } else if (!StringUtils.isBlank(request.getParameter("showForm"))) {

            return showPage(request, errors, FORM_DETAILS_PAGE_NUMBER);
        } else {
            if (!notEmptyValidator.validate(crf.getTitle())) {
                errors.rejectValue("crf.title", "form.missing_title", "form.missing_title");
            } else if (!uniqueTitleForCrfValidator.validate(crf, crf.getTitle())) {
                errors.rejectValue("crf.title", "form.unique_title", "form.unique_title");
            }

            if (errors.hasErrors()) {
                return showPage(request, errors, FORM_DETAILS_PAGE_NUMBER);
            }

            save(createFormCommand);

            Map model = new HashMap();
            model.put("crf", createFormCommand.getCrf());
            ModelAndView modelAndView = new ModelAndView("form/confirmForm", model);
            return modelAndView;
        }

    }

    protected void save(final CreateFormCommand createFormCommand) {
        CRF crf = createFormCommand.getCrf();
        createFormCommand.setCrf(crf);
        CRF savedCrf = crfRepository.save(crf);
        createFormCommand.setCrf(savedCrf);
    }

    @Override
    protected boolean validate() {
        return false;


    }

    @Required
    public void setNotEmptyValidator(final NotEmptyValidator notEmptyValidator) {
        this.notEmptyValidator = notEmptyValidator;
    }

    @Required
    public void setUniqueTitleForCrfValidator(final UniqueTitleForCrfValidator uniqueTitleForCrfValidator) {
        this.uniqueTitleForCrfValidator = uniqueTitleForCrfValidator;
    }

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
