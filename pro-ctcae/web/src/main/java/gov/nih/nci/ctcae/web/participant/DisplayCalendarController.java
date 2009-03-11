package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.repository.CRFRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

//
/**
 * The Class DisplayCalendarController.
 *
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class DisplayCalendarController extends AbstractController {

    private CRFRepository crfRepository;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaycalendar");
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        String direction = request.getParameter("dir");
        if (StringUtils.isBlank(direction)) {
            participantSchedule.getCalendar().setDueDateAmount(Integer.parseInt(request.getParameter("duea")));
            participantSchedule.getCalendar().setDueDateUnit(request.getParameter("dueu"));
            participantSchedule.getCalendar().setRepeatUntilUnit(request.getParameter("repuu"));
            participantSchedule.getCalendar().setRepeatUntilValue(request.getParameter("repuv"));
            participantSchedule.getCalendar().setRepetitionPeriodAmount(Integer.parseInt(request.getParameter("reppa")));
            participantSchedule.getCalendar().setRepetitionPeriodUnit(request.getParameter("reppu"));
            participantSchedule.getCalendar().setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(request.getParameter("sdate")));
            participantSchedule.setCrfRepository(crfRepository);
            participantSchedule.createSchedules(ParticipantSchedule.ScheduleType.GENERAL);
        } else {
            if (direction.equals("prev")) {
                participantSchedule.getCalendar().add(-1);
            }
            if (direction.equals("next")) {
                participantSchedule.getCalendar().add(1);
            }
            if (direction.equals("refresh")) {
                participantSchedule.getCalendar().add(0);
            }
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

    @Required
    public void setCrfRepository(CRFRepository crfRepository) {
        this.crfRepository = crfRepository;
    }
}