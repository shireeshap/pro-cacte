package gov.nih.nci.ctcae.web.clinicalStaff;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchClinicalStaffController.
 * 
 * @autjor Mehul Gulati
 * Date: Oct 22, 2008
 * Time: 10:21:08 AM
 */
public class SearchClinicalStaffController extends AbstractController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("clinicalStaff/searchClinicalStaff");

        return modelAndView;
    }

    /**
     * Instantiates a new search clinical staff controller.
     */
    public SearchClinicalStaffController() {
        setSupportedMethods(new String[]{"GET"});

    }

}
