package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//
/**
 * The Class ReleaseFormController.
 *
 * @author Vinay Kumar
 * @since Nov 5, 2008
 */
public class MoveFormScheduleController extends AbstractController {

    GenericRepository genericRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("participant/moveForm");

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));

        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("newdate")));
        mv.addObject("newdate", DateUtils.format(c.getTime()));

        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("olddate")));
        mv.addObject("olddate", DateUtils.format(c.getTime()));
        mv.addObject("day", request.getParameter("olddate"));

        Set<CRF> crfs = new HashSet<CRF>();
        String sids = request.getParameter("sids");
        String[] sidArr = sids.split("_");
        for (String sid : sidArr) {
            if (!StringUtils.isBlank(sid)) {
                crfs.add(genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(sid)).getStudyParticipantCrf().getCrf());
            }
        }
        mv.addObject("crfs", new ArrayList<CRF>(crfs));
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("participant", studyParticipantCommand.getParticipant());

        return mv;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}