package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.StudyCrf;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

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
        setFormView("form/relaseForm");
        setSessionForm(true);

    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Integer studyCrfId = ServletRequestUtils.getIntParameter(request, "studyCrfId");
        StudyCrf studyCrf = finderRepository.findById(StudyCrf.class, studyCrfId);
        return studyCrf;


    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        StudyCrf studyCrf = (StudyCrf) command;
        studyCrf.getCrf().setStatus(CrfStatus.RELEASEED);
        crfRepository.save(studyCrf.getCrf());
        ModelAndView modelAndView = new ModelAndView("forward:viewForm", errors.getModel());
        return modelAndView;

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
