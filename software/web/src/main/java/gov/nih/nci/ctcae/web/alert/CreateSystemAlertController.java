package gov.nih.nci.ctcae.web.alert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.repository.AlertRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

public class CreateSystemAlertController extends CtcAeSimpleFormController {
	AlertRepository alertRepository;

	private final static String SYSTEM_ALERT_ID = "systemAlertId";
	private final static String CREATE_SYSTEM_ALERT_COMMAND = "createSystemAlertCommand";
	private final static String VIEW_NAME = "alert/createSystemAlert";
	private final static String CONFIRM_VIEW_NAME = "alert/confirmSystemAlert";
	
	
	public CreateSystemAlertController() {
		super();
		setCommandClass(CreateSystemAlertCommand.class);
		setCommandName(CREATE_SYSTEM_ALERT_COMMAND);
		setFormView(VIEW_NAME);
		setBindOnNewForm(true);
		setSessionForm(true);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CreateSystemAlertCommand command = new CreateSystemAlertCommand();
		String systemAlertId = request.getParameter(SYSTEM_ALERT_ID);
		
		if(systemAlertId != null) {
			Alert alert = alertRepository.findById(Integer.parseInt(systemAlertId));
			if(alert != null) {
				command.setAlert(alert);
			}
		}
		return command;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object o, BindException e) throws Exception {
		CreateSystemAlertCommand command = (CreateSystemAlertCommand) o;
		super.onBindAndValidate(request, command, e);		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, 
			Object o, BindException errors) throws Exception {
		CreateSystemAlertCommand command = (CreateSystemAlertCommand) o;
		
		Alert alert = alertRepository.saveOrUpdate(command.getAlert());
		command.setAlert(alert);
		
		String searchString = (String) request.getSession().getAttribute(FetchSystemAlertBannerController.ALERT_SEARCH_STRING);
		request.setAttribute(FetchSystemAlertBannerController.ALERT_SEARCH_STRING, searchString);
		request.setAttribute(CREATE_SYSTEM_ALERT_COMMAND, command);
		return showForm(request, errors, CONFIRM_VIEW_NAME);
	}
	
	public void setAlertRepository(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}

}
