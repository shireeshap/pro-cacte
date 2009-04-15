package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Mehul Gulati
 * Date: Apr 10, 2009
 */
public class ParticipantCareMonitorController extends AbstractController {

        protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

            ModelAndView modelAndView = new ModelAndView("reports/participantCareMonitor");
            return modelAndView;
        }

        public ParticipantCareMonitorController(){
            super();
            setSupportedMethods(new String[]{"GET"});
        }
    }
    

