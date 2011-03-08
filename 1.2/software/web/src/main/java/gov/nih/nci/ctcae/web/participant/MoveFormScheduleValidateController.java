package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.CrfStatus;
import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//

/**
 * The Class MoveFormScheduleValidateController.
 *
 * @author Suneel Allareddy
 * @since Feb 3, 2010
 */
public class MoveFormScheduleValidateController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("participant/moveConfirmForm");
        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);

        Integer index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String date = request.getParameter("date");
        String fids = request.getParameter("fids");
        String[] strings = fids.split(",");
        List formIds = Arrays.asList(strings);

        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);

        Calendar c = new GregorianCalendar();
        c.setTime(participantSchedule.getProCtcAECalendar().getTime());
        String strNewdate = date.substring(0, date.indexOf(","));
        Date newDate = DateUtils.parseDate(strNewdate);
        String olddate = date.substring(date.indexOf(",") + 1);

        c.set(Calendar.DATE, Integer.parseInt(olddate));
        mv.addObject("olddate", DateUtils.format(c.getTime()));
        Calendar newCalendar = new GregorianCalendar();
        newCalendar.setTime(newDate);
        mv.addObject("newDate", DateUtils.format(newCalendar.getTime()));
        List<String> issueForms = participantSchedule.getReschedulePastDueForms(c, newCalendar, formIds);

        if (issueForms.size() == 0) {
            return null;
        }


        mv.addObject("day", request.getParameter("olddate"));
        mv.addObject("issueForms", issueForms);
        mv.addObject("date", date);
        mv.addObject("selectedForms", formIds);
        mv.addObject("action", action);
        mv.addObject("index", request.getParameter("index"));
        mv.addObject("participant", studyParticipantCommand.getParticipant());

        return mv;
    }

}