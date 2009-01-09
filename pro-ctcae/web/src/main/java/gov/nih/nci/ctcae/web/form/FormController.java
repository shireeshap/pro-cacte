package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.StaticFlowFactory;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.validation.annotation.NotEmptyValidator;
import gov.nih.nci.ctcae.core.validation.annotation.UniqueTitleForCrfValidator;
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

	public FormController() {
		setCommandClass(CreateFormCommand.class);
		Flow<CreateFormCommand> flow = new Flow<CreateFormCommand>("Build Form");
		layoutTabs(flow);
		setFlowFactory(new StaticFlowFactory<CreateFormCommand>(flow));
		setAllowDirtyBack(false);
		setAllowDirtyForward(false);
		setSessionForm(true);
	}


	protected void layoutTabs(Flow<CreateFormCommand> flow) {
		flow.addTab(new SelectStudyForFormTab());
		flow.addTab(new FormDetailsTab());

	}

	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CreateFormCommand createFormCommand = (CreateFormCommand) command;
		createFormCommand.updateCrfItems(finderRepository);
		CRF crf = createFormCommand.getStudyCrf().getCrf();
		if (!notEmptyValidator.validate(crf.getTitle()) || !uniqueTitleForCrfValidator.validate(crf, crf.getTitle())) {
			errors.rejectValue("studyCrf.crf.title", "form.missing_title", "form.missing_title");
		}

		if (errors.hasErrors()) {
			return showPage(request, errors, FORM_DETAILS_PAGE_NUMBER);
		}

		save(createFormCommand);

		Map model = new HashMap();
		model.put("studyCrf", createFormCommand.getStudyCrf());
		ModelAndView modelAndView = new ModelAndView("form/confirmForm", model);
		return modelAndView;


	}

	protected void save(final CreateFormCommand createFormCommand) {
		CRF crf = createFormCommand.getStudyCrf().getCrf();
		createFormCommand.setStudyCrf(crf.getStudyCrf());
		CRF savedCrf = crfRepository.save(crf);
		createFormCommand.setStudyCrf(savedCrf.getStudyCrf());
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
