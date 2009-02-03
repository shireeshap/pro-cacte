package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class DeleteFormController.
 *
 * @author Mehul Gulati
 *         Date: Dec 4, 2008
 */
public class DeleteFormController extends CtcAeSimpleFormController {

    /**
     * The finder repository.
     */
    private FinderRepository finderRepository;

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * Instantiates a new delete form controller.
     */
    protected DeleteFormController() {
        super();
        setCommandClass(CRF.class);
        setFormView("form/deleteForm");
        setSessionForm(true);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String crfId = request.getParameter("crfId");
        CRF crf = finderRepository.findById(CRF.class, Integer.parseInt(crfId));
        return crf;
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        CRF crf = (CRF) command;
        Integer studyId = crf.getStudy().getId();

        if (crf.getParentVersionId() != null) {
            Integer parentId = crf.getParentVersionId();
            CRF parentCrf = finderRepository.findById(CRF.class, parentId);
            if (crf.getNextVersionId() != null) {
                parentCrf.setNextVersionId(crf.getNextVersionId());
            } else {
                parentCrf.setNextVersionId(null);
            }
            crfRepository.save(parentCrf);
        }

        if (crf.getNextVersionId() != null) {
            Integer nextVersionId = crf.getNextVersionId();
            CRF childCrf = finderRepository.findById(CRF.class, nextVersionId);
            if (crf.getParentVersionId() != null) {
                childCrf.setParentVersionId(crf.getParentVersionId());
            } else {
                childCrf.setParentVersionId(null);
            }
            crfRepository.save(childCrf);
        }
        crfRepository.delete(crf);
        RedirectView redirectView = new RedirectView("manageForm?studyId=" + studyId);

        return new ModelAndView(redirectView);
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.ctcae.web.CtcAeSimpleFormController#setFinderRepository(gov.nih.nci.ctcae.core.repository.FinderRepository)
     */
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
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
