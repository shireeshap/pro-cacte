package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Dec 19, 2008
 */
public class VersionFormController extends CtcAeSimpleFormController {

    private CRFRepository crfRepository;

    protected VersionFormController() {
        setCommandClass(CRF.class);
        setFormView("form/versionForm");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String crfId = request.getParameter("crfId");
        CRF crf = crfRepository.findById(Integer.parseInt(crfId));
        return crf;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        CRF crf = (CRF) command;

        //FIXME: Mehul- remove it later
        crf = crfRepository.findById(crf.getId());
        Integer parentVersionId = crf.getId();
        String newVersion = "" + (new Float(crf.getCrfVersion()) + 1);
        CRF copiedCRF = crf.getCopy();
        copiedCRF.setTitle(crf.getTitle());
        copiedCRF.setCrfVersion(newVersion);
        copiedCRF.setParentVersionId(parentVersionId);

        crfRepository.save(copiedCRF);

        Integer nextVersionId = copiedCRF.getId();
        crf.setNextVersionId(nextVersionId);
        crfRepository.save(crf);

        RedirectView redirectView = new RedirectView("editForm?crfId=" + copiedCRF.getId() + "&showFormDetails=true");

        ModelAndView modelAndView = new ModelAndView(redirectView);
        return modelAndView;
    }


    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
