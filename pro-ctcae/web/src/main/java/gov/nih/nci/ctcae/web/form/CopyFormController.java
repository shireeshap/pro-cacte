package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Dec 3, 2008
 */
public class CopyFormController extends AbstractController {

    private FinderRepository finderRepository;
    private CRFRepository crfRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        //  ModelAndView modelAndView = new ModelAndView("form/manageForm");


        String crfId = request.getParameter("crfId");
        CRF crf = finderRepository.findById(CRF.class, Integer.parseInt(crfId));
        CRF copiedCrf = crf.getCopy();
        copiedCrf.setCrfVersion("1.0");
        crfRepository.save(copiedCrf);
        RedirectView redirectView = new RedirectView("editForm?crfId=" + copiedCrf.getId() + "&copyForm=true");

        return new ModelAndView(redirectView);

    }

    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }

    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}