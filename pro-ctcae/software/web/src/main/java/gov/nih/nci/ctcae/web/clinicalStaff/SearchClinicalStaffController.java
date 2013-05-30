package gov.nih.nci.ctcae.web.clinicalStaff;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class SearchParticipantController.
 *
 * @author Harsh Agarwal
 *         Date: Oct 23, 2008
 */
public class SearchClinicalStaffController extends AbstractController {
	private static String CLINICAL_STAFF_SEARCH_STRING = "clinicalStaffSearchString";
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");
        String searchString = request.getParameter("searchString");
        modelAndView.addObject("searchString",searchString);
        request.getSession().setAttribute(CLINICAL_STAFF_SEARCH_STRING, searchString);
        return modelAndView;
    }
}
