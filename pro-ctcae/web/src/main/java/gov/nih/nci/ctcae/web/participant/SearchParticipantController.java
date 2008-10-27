package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Harsh Agarwal
 * Date: Oct 23, 2008
 */
public class SearchParticipantController extends AbstractController {

   
    protected ModelAndView handleRequestInternal (final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/searchParticipant");

        return modelAndView;
    }
    
}
