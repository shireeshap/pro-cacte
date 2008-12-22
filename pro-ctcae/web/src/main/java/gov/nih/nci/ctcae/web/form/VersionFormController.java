package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.domain.StudyCrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.BindException;

/**
 * @author Mehul Gulati
 * Date: Dec 19, 2008
 */
public class VersionFormController extends CtcAeSimpleFormController {

    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    protected VersionFormController() {
        setCommandClass(StudyCrf.class);
        setFormView("form/versionForm");
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String studyCrfId = request.getParameter("studyCrfId");
        StudyCrf studyCrf = finderRepository.findById(StudyCrf.class,  Integer.parseInt(studyCrfId));
        return studyCrf;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyCrf studyCrf = (StudyCrf) command;
        Integer parentVersionId = studyCrf.getCrf().getId();
        String newVersion =  "" +(new Float(studyCrf.getCrf().getCrfVersion()) + 1);
        StudyCrf copiedStudyCrf = studyCrf.getCopy();
        copiedStudyCrf.getCrf().setTitle(studyCrf.getCrf().getTitle() + " ver " + newVersion + "_" + System.currentTimeMillis());
        copiedStudyCrf.getCrf().setCrfVersion(newVersion);
        copiedStudyCrf.getCrf().setParentVersionId(parentVersionId);

        crfRepository.save(copiedStudyCrf.getCrf());

        Integer nextVersionId = copiedStudyCrf.getCrf().getId();
        studyCrf.getCrf().setNextVersionId(nextVersionId);
        crfRepository.save(studyCrf.getCrf());

        RedirectView redirectView = new RedirectView("editForm?studyCrfId=" + copiedStudyCrf.getId());

        return new ModelAndView(redirectView);
    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}
