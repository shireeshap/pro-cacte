package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

//
/**
 * The Class ReleaseFormController.
 *
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class ReleaseFormController extends CtcAeSimpleFormController {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * Instantiates a new release form controller.
     */
    protected ReleaseFormController() {
        super();
        setCommandClass(CRF.class);
        setFormView("form/releaseForm");
        setSessionForm(true);

    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Integer crfId = ServletRequestUtils.getIntParameter(request, "crfId");
        CRF crf = crfRepository.findById(crfId);
        crf.setEffectiveStartDate(new Date());
        return crf;


    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        CRF crf = (CRF) command;
        Date effectiveStarDate = crf.getEffectiveStartDate();
        crf = crfRepository.findById(crf.getId());
        crf.setEffectiveStartDate(effectiveStarDate);
        crfRepository.updateStatusToReleased(crf);

        RedirectView redirectView = new RedirectView("manageForm?studyId=" + crf.getStudy().getId());
        return new ModelAndView(redirectView);
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
}
