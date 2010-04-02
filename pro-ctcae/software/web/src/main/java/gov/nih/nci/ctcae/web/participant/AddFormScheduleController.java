package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrf;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

//

/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class AddFormScheduleController extends AbstractController {


    GenericRepository genericRepository;

    /* (non-Javadoc)
    * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));
        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("date")));

        ModelAndView mv = new ModelAndView("participant/addSchedule");
        List<CRF> crfs = new ArrayList<CRF>();
        for (StudyParticipantCrf studyParticipantCrf : studyParticipantCommand.getStudyParticipantAssignment().getStudyParticipantCrfs()) {
            CRF crf = studyParticipantCrf.getCrf();
            if (crf.getChildCrf() == null) {
                crfs.add(crf);
            }
        }

        mv.addObject("crfs", crfs);
        mv.addObject("day", request.getParameter("date"));
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("date", DateUtils.format(c.getTime()));
        mv.addObject("participant", studyParticipantCommand.getParticipant());
        return mv;
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddFormScheduleController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}