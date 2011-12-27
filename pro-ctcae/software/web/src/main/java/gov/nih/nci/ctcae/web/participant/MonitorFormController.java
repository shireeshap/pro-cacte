package gov.nih.nci.ctcae.web.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 * Date: Mar 9, 2009
 */
public class MonitorFormController extends AbstractController {

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        

        ModelAndView modelAndView = new ModelAndView("participant/monitorForm");
        return modelAndView;
    }

    public MonitorFormController(){
        super();
        setSupportedMethods(new String[]{"GET"});
    }
}
