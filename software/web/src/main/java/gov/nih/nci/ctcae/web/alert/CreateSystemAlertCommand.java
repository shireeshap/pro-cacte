package gov.nih.nci.ctcae.web.alert;

import gov.nih.nci.ctcae.core.domain.Alert;

public class CreateSystemAlertCommand {
	Alert alert;
	
	public CreateSystemAlertCommand() {
		this.alert = new Alert();
	}
	
	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

}