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

/**
 * @author Mehul Gulati
 *         Date: Dec 4, 2008
 */
public class DeleteFormController extends CtcAeSimpleFormController {

    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    protected DeleteFormController() {
        setCommandClass(CRF.class);
        setFormView("form/deleteForm");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String crfId = request.getParameter("crfId");
        CRF crf = finderRepository.findById(CRF.class, Integer.parseInt(crfId));
        return crf;
    }


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

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
