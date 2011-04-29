package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.CtcAeSimpleFormController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author Mehul Gulati
 *         Date: Jun 24, 2009
 */
public class EnterParticipantResponsesController extends CtcAeSimpleFormController {

    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    public EnterParticipantResponsesController() {
        super();
        setCommandClass(StudyParticipantCrfSchedule.class);
        setFormView("participant/inputParticipantResponses");
        setSuccessView("/participant/inputParticipantResponses");
        setBindOnNewForm(true);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = new StudyParticipantCrfSchedule();
        if (!StringUtils.isBlank(id)) {
            studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(id));
        }
        return studyParticipantCrfSchedule;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object oCommand, org.springframework.validation.BindException errors) throws Exception {
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = (StudyParticipantCrfSchedule) oCommand;
        String submitType = request.getParameter("submitType");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if ("save".equals(submitType)) {
            studyParticipantCrfSchedule.setStatus(CrfStatus.INPROGRESS);
        } else {
            studyParticipantCrfSchedule.setFormSubmissionMode(AppMode.CLINICWEB);
            studyParticipantCrfSchedule.setStatus(CrfStatus.COMPLETED);
            studyParticipantCrfSchedule.setCompletionDate(new Date());
            List<StudyParticipantCrfItem> spcItems = studyParticipantCrfSchedule.getStudyParticipantCrfItems();
            if (spcItems != null) {
                for (StudyParticipantCrfItem spcCrfItem : spcItems) {
                    spcCrfItem.setReponseDate(new Date());
                    spcCrfItem.setResponseMode(AppMode.CLINICWEB);
                    spcCrfItem.setUpdatedBy(user.getUsername());
                }
            }
            List<StudyParticipantCrfScheduleAddedQuestion> spcsAdded = studyParticipantCrfSchedule.getStudyParticipantCrfScheduleAddedQuestions();
            if (spcsAdded != null) {
                for (StudyParticipantCrfScheduleAddedQuestion spcsaq : spcsAdded) {
                    spcsaq.setReponseDate(new Date());
                    spcsaq.setResponseMode(AppMode.CLINICWEB);
                    spcsaq.setUpdatedBy(user.getUsername());
                }
            }
        }
        studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
        ModelAndView modelAndView = new ModelAndView(new RedirectView("enterResponses?id=" + studyParticipantCrfSchedule.getId()));
        modelAndView.addObject("successMessage", "true");
        return modelAndView;
    }


    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
