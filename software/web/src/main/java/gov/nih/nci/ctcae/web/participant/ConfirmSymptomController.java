package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 *         Date: Mar 16, 2010
 */
public class ConfirmSymptomController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView modelAndView = new ModelAndView("participant/confirmSymptom");
        modelAndView.addObject("values", request.getParameter("values"));
        modelAndView.addObject("selectedChoice", request.getParameter("selectedChoice"));
        return modelAndView;
    }

}
