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

/**
 * @author Harsh Agarwal
 * @crated Nov 5, 2008
 */
public class DisplayCalendarController extends AbstractController {

    FinderRepository finderRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("participant/ajax/displaycalendar");
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        String direction = request.getParameter("dir");
        if (StringUtils.isBlank(direction)) {
            participantSchedule.setDueDateAmount(Integer.parseInt(request.getParameter("duea")));
            participantSchedule.setDueDateUnit(request.getParameter("dueu"));
            participantSchedule.setRepeatUntilUnit(request.getParameter("repuu"));
            participantSchedule.setRepeatUntilValue(request.getParameter("repuv"));
            participantSchedule.setRepetitionPeriodAmount(Integer.parseInt(request.getParameter("reppa")));
            participantSchedule.setRepetitionPeriodUnit(request.getParameter("reppu"));
            participantSchedule.setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(request.getParameter("sdate")));
            studyParticipantCommand.setFinderRepository(finderRepository);
            studyParticipantCommand.createSchedules(index);
        } else {
            if (direction.equals("prev")) {
                participantSchedule.getCalendar().add(-1);
            }
            if (direction.equals("next")) {
                participantSchedule.getCalendar().add(1);
            }

        }

        modelAndView.addObject("participantSchedule", studyParticipantCommand.getParticipantSchedules().get(index));

        return modelAndView;
    }


    public DisplayCalendarController() {
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}