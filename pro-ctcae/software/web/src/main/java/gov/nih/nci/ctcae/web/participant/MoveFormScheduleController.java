package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

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

        ParticipantCommand participantCommand = ParticipantControllerUtils.getParticipantCommand(request);
        ParticipantSchedule participantSchedule = participantCommand.getParticipantSchedules().get(Integer.parseInt(request.getParameter("index")));

        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("newdate")));
        mv.addObject("newdate", DateUtils.format(c.getTime()));

        c.set(Calendar.DATE, Integer.parseInt(request.getParameter("olddate")));
        mv.addObject("olddate", DateUtils.format(c.getTime()));
        mv.addObject("day", request.getParameter("olddate"));

       // Set<CRF> crfs = new HashSet<CRF>();
        LinkedHashMap<CRF,Boolean> crfListMap = new LinkedHashMap<CRF,Boolean>();
        String sids = request.getParameter("sids");
        String[] sidArr = sids.split("_");
        for (String sid : sidArr) {
            if (!StringUtils.isBlank(sid)) {
                StudyParticipantCrfSchedule schedule =   genericRepository.findById(StudyParticipantCrfSchedule.class, Integer.parseInt(sid));
                //crfs.add(schedule.getStudyParticipantCrf().getCrf());
                crfListMap.put(schedule.getStudyParticipantCrf().getCrf(),schedule.getStatus().equals(CrfStatus.COMPLETED));
            }
        }


        mv.addObject("crfsList", crfListMap);
        mv.addObject("firstCrf", (CRF)crfListMap.keySet().iterator().next());
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("participant", participantCommand.getParticipant());
        mv.addObject("pid", participantCommand.getSelectedStudyParticipantAssignmentId());
        return mv;
    }

    @Required
    public void setGenericRepository(GenericRepository genericRepository) {
        this.genericRepository = genericRepository;
    }
}