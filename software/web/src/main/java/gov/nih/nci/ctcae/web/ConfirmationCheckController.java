package gov.nih.nci.ctcae.web;

import gov.nih.nci.ctcae.core.domain.ProCtcTerm;
import gov.nih.nci.ctcae.core.repository.ProCtcTermRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//

/**
 * The Class ConfirmationCheckController.
 *
 * @author Vinay Kumar
 * @since Oct 21, 2008
 */
public class ConfirmationCheckController extends AbstractController {

    protected ProCtcTermRepository proCtcTermRepository;

    /**
     * The Constant DELETE_CRF_CONFIRMATION_TYPE.
     */
    private static final String DELETE_CRF_CONFIRMATION_TYPE = "deleteCrf";

    /**
     * The Constant DELETE_QUESTION_CONFIRMATION_TYPE.
     */
    private static final String DELETE_QUESTION_CONFIRMATION_TYPE = "deleteQuestion";

    private static final String DELETE_CRF_CYCLE = "deleteCrfCycle";

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        String confirmationType = request.getParameter("confirmationType");
        Integer proCtcTermId = ServletRequestUtils.getIntParameter(request, "proTermId");
        String description = "";
        if (proCtcTermId != null){
        ProCtcTerm proCtcTerm = proCtcTermRepository.findById(proCtcTermId);
            description = proCtcTerm.getProCtcTermVocab().getTermEnglish();
        }
        if (StringUtils.equals(confirmationType, DELETE_CRF_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteCrfConfirmationCheck");
            Map map = new HashMap();
            map.put("selectedCrfPageNumber", request.getParameter("selectedCrfPageNumber"));
            map.put("crfPageDescription", description);
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_QUESTION_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteQuestionConfirmationCheck");
            Map map = new HashMap();
            map.put("questionId", request.getParameter("questionId"));
            map.put("proCtcTermId", request.getParameter("proCtcTermId"));
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_CRF_CYCLE)) {
            modelAndView = new ModelAndView("form/ajax/deleteCrfCycleConfirmationCheck");
            Map map = new HashMap();
            map.put("crfCycleIndex", request.getParameter("crfCycleIndex"));
            modelAndView.addAllObjects(map);
        }


        return modelAndView;


    }

    public void setProCtcTermRepository(ProCtcTermRepository proCtcTermRepository) {
        this.proCtcTermRepository = proCtcTermRepository;
    }

    /**
     * Instantiates a new confirmation check controller.
     */
    public ConfirmationCheckController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }


}