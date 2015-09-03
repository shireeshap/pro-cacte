package gov.nih.nci.ctcae.web.alert;

import gov.nih.nci.ctcae.core.domain.Alert;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.repository.AlertRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

public class CopySystemAlertController extends AbstractController{
	private AlertRepository alertRepository;
	private final static String SYSTEM_ALERT_ID = "systemAlertId";
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String systemAlertId = (String) request.getParameter(SYSTEM_ALERT_ID);
		Alert alert;
		if(!StringUtils.isEmpty(systemAlertId)) {
			alert = alertRepository.findById(Integer.parseInt(systemAlertId));
			Alert copiedAlert = alertRepository.copy(alert);
			return new ModelAndView(new RedirectView("../createSystemAlert?systemAlertId=" + copiedAlert.getId()));
		} else {
			throw new CtcAeSystemException("The system is not able to copy this alert message");
		}
	}
	
	public void setAlertRepository(AlertRepository alertRepository) {
		this.alertRepository = alertRepository;
	}
}