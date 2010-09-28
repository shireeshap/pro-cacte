package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;

/**
 * @author mehul gulati
 *         Date: Sep 22, 2010
 */
public class ShowFormController extends ToggleFormController {

    protected ShowFormController(){
        super();
        setFormView("form/moveForm");
    }

    protected void updateStatusCRF(CRF crf) {
        crf.setHidden(false);
    }

}