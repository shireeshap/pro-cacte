package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class VersionFormController.
 *
 * @author Mehul Gulati
 *         Date: Dec 19, 2008
 */
public class VersionFormController extends CtcAeSimpleFormController {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * Instantiates a new version form controller.
     */
    protected VersionFormController() {
        super();
        setCommandClass(CRF.class);
        setFormView("form/versionForm");
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String crfId = request.getParameter("crfId");
        CRF crf = crfRepository.findById(Integer.parseInt(crfId));
        return crf;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        CRF crf = (CRF) command;

        CRF versionedCrf = crfRepository.versionCrf(crf);

        RedirectView redirectView = new RedirectView("editForm?crfId=" + versionedCrf.getId() + "&showFormDetails=true");

        ModelAndView modelAndView = new ModelAndView(redirectView);
        return modelAndView;
    }


    /**
     * Sets the crf repository.
     *
     * @param crfRepository the new crf repository
     */
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
