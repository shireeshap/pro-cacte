package gov.nih.nci.ctcae.web.participant;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

//
/**
 * The Class DisplayCalendarController.
 *
 * @author Harsh Agarwal
 * @since Nov 5, 2008
 */
public class MatchSymptomsController extends AbstractController {

    ScheduleCrfAjaxFacade scheduleCrfAjaxFacade;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        List<String> symptoms = scheduleCrfAjaxFacade.matchSymptoms(request, request.getParameter("query"));
        PrintWriter writer = response.getWriter();
        StringBuilder builder = new StringBuilder();
        for (String symptom : symptoms) {
            builder.append(symptom + ";");
        }
        writer.write(builder.toString());
        writer.flush();

        return null;
    }


    /**
     * Instantiates a new display calendar controller.
     */
    public MatchSymptomsController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setScheduleCrfAjaxFacade(ScheduleCrfAjaxFacade scheduleCrfAjaxFacade) {
        this.scheduleCrfAjaxFacade = scheduleCrfAjaxFacade;
    }
}