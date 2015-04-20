package gov.nih.nci.ctcae.web.form;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

//
/**
 * The Class ShowVersionFormController.
 *
 * @author Mehul Gulati
 *         Date: Dec 31, 2008
 */
public class ShowVersionFormController extends AbstractController {

    /**
     * The crf repository.
     */
    private CRFRepository crfRepository;

    /**
     * Instantiates a new show version form controller.
     */
    public ShowVersionFormController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("form/showVersionForm");
        String crfId = request.getParameter("crfId");

        List<CRF> crfCollection = new ArrayList();

        CRF crf = crfRepository.findById(Integer.valueOf(crfId));
        CRF parentCrf = crf.getParentCrf();
        while (parentCrf != null) {
            crfCollection.add(parentCrf);
            parentCrf = parentCrf.getParentCrf();
        }

        modelAndView.getModel().put("crfs", crfCollection);
        modelAndView.getModel().put("parentCrfId", crfId);

        return modelAndView;
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

