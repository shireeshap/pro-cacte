package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.repository.FinderRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayCalendarController.
 * 
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class DisplayCalendarController extends AbstractController {

    /** The finder repository. */
    FinderRepository finderRepository;

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
            participantSchedule.setFinderRepository(finderRepository);
            participantSchedule.createSchedules();
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
        setSupportedMethods(new String[]{"GET"});

    }

    /**
     * Sets the finder repository.
     * 
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}