package gov.nih.nci.ctcae.web.form;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 * Date: Dec 4, 2008
 */
public class DeleteFormController extends CtcAeSimpleFormController {

    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    protected DeleteFormController() {
        setCommandClass(StudyCrf.class);
        setFormView("form/deleteForm");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String studyCrfId = request.getParameter("studyCrfId");
        StudyCrf studyCrf = finderRepository.findById(StudyCrf.class, Integer.parseInt(studyCrfId));
        return studyCrf;
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyCrf studyCrf = (StudyCrf) command;
        Integer studyId = studyCrf.getStudy().getId();
        crfRepository.delete(studyCrf.getCrf());
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
