package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//
/**
 * The Class DisplayCalendarController.
 *
 * @author Harsh Agarwal
 * @since Nov 5, 2008
 */
public class DisplayCalendarController extends AbstractController {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaycalendar");
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        String direction = request.getParameter("dir");

        if (direction.equals("prev")) {
            participantSchedule.getCalendar().add(-1);
        }
        if (direction.equals("next")) {
            participantSchedule.getCalendar().add(1);
        }
        if (direction.equals("refresh")) {
            participantSchedule.getCalendar().add(0);
        }

        modelAndView.addObject("participantSchedule", studyParticipantCommand.getParticipantSchedules().get(index));
        modelAndView.addObject("index", index);

        return modelAndView;
    }


    /**
     * Instantiates a new display calendar controller.
     */
    public DisplayCalendarController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }
}