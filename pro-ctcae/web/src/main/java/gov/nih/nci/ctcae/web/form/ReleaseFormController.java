package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vinay Kumar
 * @crated Nov 5, 2008
 */
public class ReleaseFormController extends CtcAeSimpleFormController {
    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    protected ReleaseFormController() {
        setCommandClass(StudyCrf.class);
        setFormView("form/releaseForm");
        setSessionForm(true);

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Integer studyCrfId = ServletRequestUtils.getIntParameter(request, "studyCrfId");
        StudyCrf studyCrf = finderRepository.findAndInitializeStudyCrf(studyCrfId);
        return studyCrf;


    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyCrf studyCrf = (StudyCrf) command;
        crfRepository.updateStatusToReleased(studyCrf.getCrf());
        RedirectView redirectView = new RedirectView("manageForm?studyCrfId=" + studyCrf.getId());
        return new ModelAndView(redirectView);
    }


    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    @Required

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
