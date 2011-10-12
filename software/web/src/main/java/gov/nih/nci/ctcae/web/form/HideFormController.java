package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul gulati
 *         Date: Sep 22, 2010
 */
public class HideFormController extends ToggleFormController {

    protected HideFormController(){
        super();
        setFormView("form/hideForm");
    }

    protected void updateStatusCRF(CRF crf) {
        crf.setHidden(true);
    }

}