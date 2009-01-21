package gov.nih.nci.ctcae.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vinay Kumar
 * @crated Oct 21, 2008
 */
public class ConfirmationCheckController extends AbstractController {

    private static final String DELETE_CRF_CONFIRMATION_TYPE = "deleteCrf";
    private static final String DELETE_QUESTION_CONFIRMATION_TYPE = "deleteQuestion";

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = null;
        String confirmationType = request.getParameter("confirmationType");
        if (StringUtils.equals(confirmationType, DELETE_CRF_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteCrfConfirmationCheck");
            Map map = new HashMap();
            map.put("selectedCrfPageNumber", request.getParameter("selectedCrfPageNumber"));
            modelAndView.addAllObjects(map);
        } else if (StringUtils.equals(confirmationType, DELETE_QUESTION_CONFIRMATION_TYPE)) {
            modelAndView = new ModelAndView("form/ajax/deleteQuestionConfirmationCheck");
            Map map = new HashMap();
            map.put("questionId", request.getParameter("questionId"));
            map.put("proCtcTermId", request.getParameter("proCtcTermId"));
            modelAndView.addAllObjects(map);
        }


        return modelAndView;


    }


    public ConfirmationCheckController() {
        setSupportedMethods(new String[]{"GET"});

    }


}