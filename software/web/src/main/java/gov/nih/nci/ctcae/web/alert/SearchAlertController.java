package gov.nih.nci.ctcae.web.alert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;


/**
 * @author Amey
 * SearchAlertController class
 */
public class SearchAlertController extends AbstractController {
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("alert/searchSystemAlert");
        String searchString = request.getParameter(FetchSystemAlertBannerController.ALERT_SEARCH_STRING);
        modelAndView.addObject(FetchSystemAlertBannerController.ALERT_SEARCH_STRING,searchString);
        request.getSession().setAttribute(FetchSystemAlertBannerController.ALERT_SEARCH_STRING, searchString);
        return modelAndView;
    }
}