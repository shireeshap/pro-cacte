package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

/**
 * @author Harsh Agarwal
 * @crated Nov 6, 2008
 */
public class AddCrfScheduleController extends AbstractController {

    FinderRepository finderRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String date = request.getParameter("date");

        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        participantSchedule.setFinderRepository(finderRepository);
        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getCalendar().getTime());
        int duedate = 24 * 60 * 60 * 1000;

        if ("add,del".equals(action)) {
            String newdate = date.substring(0, date.indexOf(","));
            String olddate = date.substring(date.indexOf(",") + 1);

            c.set(Calendar.DATE, Integer.parseInt(newdate));
            participantSchedule.createSchedule(c, duedate);

            c.set(Calendar.DATE, Integer.parseInt(olddate));
            participantSchedule.removeSchedule(c);
        } else {
            c.set(Calendar.DATE, Integer.parseInt(date));
            if ("add".equals(action)) {
                participantSchedule.createSchedule(c, duedate);
            }
            if ("del".equals(action)) {
                participantSchedule.removeSchedule(c);
            }
        }

        return null;
    }


    public AddCrfScheduleController() {
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}