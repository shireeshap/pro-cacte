package gov.nih.nci.ctcae.web.alert;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.domain.AlertStatus;
import gov.nih.nci.ctcae.core.repository.AlertRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ToggleSystemAlertStatusController extends CtcAeSimpleFormController {
	private AlertRepository alertRepository;
	private final static String SYSTEM_ALERT_ID = "systemAlertId";
	
	public ToggleSystemAlertStatusController() {
		setFormView("alert/toggleSystemAlertStatus");
		setCommandClass(CreateSystemAlertCommand.class);
		setCommandName("command");
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CreateSystemAlertCommand command = new CreateSystemAlertCommand();
		String systemAlertId = (String) request.getParameter(SYSTEM_ALERT_ID);
		
		if(!StringUtils.isEmpty(systemAlertId)) {
			Alert alert = alertRepository.findById(Integer.parseInt(systemAlertId));
			command.setAlert(alert);
		}
		
		return command;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, 
					BindException errors) throws Exception {
		CreateSystemAlertCommand command = (CreateSystemAlertCommand) o;
		
		Alert alert = command.getAlert();
		if(AlertStatus.ACTIVE.equals(alert.getAlertStatus())) {
			command.getAlert().setAlertStatus(AlertStatus.IN_ACTIVE);
		} else {
			command.getAlert().setAlertStatus(AlertStatus.ACTIVE);
		}
		
		alert = alertRepository.save(command.getAlert());
		String searchString = (String) request.getSession().getAttribute(FetchSystemAlertBannerController.ALERT_SEARCH_STRING);
		
		if(!StringUtils.isEmpty(searchString)){
			return new ModelAndView(new RedirectView("../searchAlert?searchString" + searchString));
		} 
		
		return new ModelAndView(new RedirectView("../searchAlert"));
	}
	
	public void setAlertRepository(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}

}
