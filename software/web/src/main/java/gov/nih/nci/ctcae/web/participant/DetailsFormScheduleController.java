package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

//

/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class DetailsFormScheduleController extends AbstractController {


    GenericRepository genericRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ParticipantCommand participantCommand = ParticipantControllerUtils.getParticipantCommand(request);
        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));
        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("date")));

        ModelAndView mv = new ModelAndView("participant/detailsSchedule");
        Set<StudyParticipantCrfSchedule> schedules = new HashSet<StudyParticipantCrfSchedule>();
        String sids = request.getParameter("sids");
        String[] sidArr = sids.split("_");
        for (String sid : sidArr) {
            if (!StringUtils.isBlank(sid)) {
                schedules.add(genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(sid)));
            }
        }
        mv.addObject("schedules", new ArrayList<StudyParticipantCrfSchedule>(schedules));
        mv.addObject("day", request.getParameter("date"));
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("date", DateUtils.format(c.getTime()));
        mv.addObject("participant", participantCommand.getParticipant());
        mv.addObject("pid", participantCommand.getSelectedStudyParticipantAssignmentId());
        return mv;
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public DetailsFormScheduleController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}