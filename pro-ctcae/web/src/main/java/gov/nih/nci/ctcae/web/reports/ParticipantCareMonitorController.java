package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mehul Gulati
 * Date: Apr 10, 2009
 */
public class ParticipantCareMonitorController extends AbstractController {

        protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

            ModelAndView modelAndView = new ModelAndView("reports/participantCareMonitor");
            modelAndView.addObject("visits", 0);
            return modelAndView;
        }

        public ParticipantCareMonitorController(){
            super();
            setSupportedMethods(new String[]{"GET"});
        }
    }
    

