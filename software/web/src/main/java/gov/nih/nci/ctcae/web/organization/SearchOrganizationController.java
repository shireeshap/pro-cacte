package gov.nih.nci.ctcae.web.organization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * @author Amey
 * SearchOrganizationController class
 */
public class SearchOrganizationController extends AbstractController {
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("organization/searchOrganization");
        String searchString = request.getParameter("searchString");
        modelAndView.addObject("searchString",searchString);
        request.getSession().setAttribute(FetchOrganizationController.ORGANIZATION_SEARCH_STRING, searchString);
        return modelAndView;
    }
}
