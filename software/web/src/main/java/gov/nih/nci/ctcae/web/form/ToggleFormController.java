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
public abstract class ToggleFormController extends CtcAeSimpleFormController {

    private CRFRepository crfRepository;
    private StudyRepository studyRepository;

    protected ToggleFormController() {
        super();
        setCommandClass(CRF.class);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String crfId = request.getParameter("crfId");
        CRF crf = crfRepository.findById(Integer.parseInt(crfId));
        return crf;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        CRF crf = (CRF) command;
        Integer studyId = crf.getStudy().getId();
        updateStatusCRF(crf);
        crfRepository.save(crf);
        RedirectView redirectView = new RedirectView("manageForm?studyId=" + studyId);
        return new ModelAndView(redirectView);
    }

    abstract protected void updateStatusCRF(CRF crf);

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
